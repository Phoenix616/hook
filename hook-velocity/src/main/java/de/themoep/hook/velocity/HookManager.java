package de.themoep.hook.velocity;

/*
 * hook - hook-velocity - $project.description
 * Copyright (C) 2021 Max Lee (max@themoep.de)
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

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import de.themoep.hook.core.AbstractHookManager;

import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HookManager extends AbstractHookManager<Hookable> {
    private final Hookable parent;
    private ProxyServer proxy;

    /**
     * Construct the manager
     * @param parent The parent plugin
     * @param proxy The proxy server instance
     * @param hookPackage The package the hooks are in
     */
    public HookManager(Hookable parent, ProxyServer proxy, String hookPackage) {
        this(parent, proxy, hookPackage, false);
    }

    /**
     * Construct the manager
     * @param parent The parent plugin
     * @param proxy The proxy server instance
     * @param hookPackage The package the hooks are in
     * @param onePackagePerHook Whether each hook should have its own package named after the hook or not
     *                          Defaults to <code>false</code>
     */
    public HookManager(Hookable parent, ProxyServer proxy, String hookPackage, boolean onePackagePerHook) {
        super(Logger.getLogger(parent.getDescription().getId()), hookPackage, onePackagePerHook);
        this.parent = parent;
        this.proxy = proxy;
        registerExistingHookables();
        proxy.getEventManager().register(parent, this);
    }

    @Subscribe
    public void onReload(ProxyReloadEvent event) {
        getHookables().forEach(this::onHookableDisable);
        registerExistingHookables();
    }

    @Override
    protected String getName(Hookable plugin) {
        return plugin.getDescription().getName().orElse(plugin.getDescription().getId());
    }

    @Override
    protected String getVersion(Hookable plugin) {
        return plugin.getDescription().getVersion().orElse("");
    }

    @Override
    protected boolean isEnabled(Hookable plugin) {
        return proxy.getPluginManager().isLoaded(plugin.getDescription().getId());
    }

    @Override
    protected Collection<Hookable> getHookables() {
        return proxy.getPluginManager().getPlugins().stream().map(HookableWrapper::new).collect(Collectors.toList());
    }
}
