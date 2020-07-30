package de.themoep.hook.bungee;

/*
 * hook - Bungee adapter
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
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

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
     *                          Defaults to <code>false</code>
     */
    public HookManager(Plugin parent,String hookPackage, boolean onePackagePerHook) {
        super(parent.getLogger(), hookPackage, onePackagePerHook);
        this.parent = parent;
        registerExistingHookables();
        parent.getProxy().getPluginManager().registerListener(parent, this);
    }

    @EventHandler
    public void onPluginEnable(ProxyReloadEvent event) {
        getHookables().forEach(this::onHookableDisable);
        registerExistingHookables();
    }

    @Override
    protected String getName(Plugin plugin) {
        return plugin.getDescription().getName();
    }

    @Override
    protected String getVersion(Plugin plugin) {
        return plugin.getDescription().getVersion();
    }

    @Override
    protected boolean isEnabled(Plugin plugin) {
        return true;
    }

    @Override
    protected Collection<Plugin> getHookables() {
        return parent.getProxy().getPluginManager().getPlugins();
    }
}
