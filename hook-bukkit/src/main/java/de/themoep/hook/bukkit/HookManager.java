package de.themoep.hook.bukkit;

/*
 * hook - Bukkit adapter
 * Copyright (c) 2020 Max Lee aka Phoenix616 (mail@moep.tv)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import de.themoep.hook.core.AbstractHookManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Collection;

public class HookManager extends AbstractHookManager<Plugin> implements Listener {
    private final Plugin parent;

    /**
     * Construct the manager
     * @param parent The parent plugin
     * @param hookPackage The package the hooks are in
     */
    public HookManager(Plugin parent, String hookPackage) {
        this(parent,  hookPackage, false);
    }

    /**
     * Construct the manager
     * @param parent The parent plugin
     * @param hookPackage The package the hooks are in
     * @param onePackagePerHook Whether each hook should have its own package named after the hook or not
     *                          Defaults to <tt>false</tt>
     */
    public HookManager(Plugin parent,String hookPackage, boolean onePackagePerHook) {
        super(parent.getLogger(), hookPackage, onePackagePerHook);
        this.parent = parent;
        registerExistingHookables();
        parent.getServer().getPluginManager().registerEvents(this, parent);
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        onHookableEnable(event.getPlugin());
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        onHookableDisable(event.getPlugin());
    }

    @Override
    protected String getName(Plugin plugin) {
        return plugin.getName();
    }

    @Override
    protected String getVersion(Plugin plugin) {
        return plugin.getDescription().getVersion();
    }

    @Override
    protected boolean isEnabled(Plugin plugin) {
        return plugin.isEnabled();
    }

    @Override
    protected Collection<Plugin> getHookables() {
        return Arrays.asList(parent.getServer().getPluginManager().getPlugins());
    }
}
