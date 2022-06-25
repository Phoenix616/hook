package de.themoep.hook.core;

/*
 * hook - library to manage hooking into other plugins
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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public abstract class AbstractHookManager<H> {
    private static final Pattern NON_WORD_PATTERN = Pattern.compile("\\W");

    private final Logger logger;
    private final String hookPackage;

    private String suffix = "Hook";
    private final boolean onePackagePerHook;
    private final Map<String, Hook<H>> hookMap = new LinkedHashMap<>();

    /**
     * Construct the manager
     * @param hookPackage The package the hooks are in
     */
    public AbstractHookManager(String hookPackage) {
        this(Logger.getLogger("HookManager"), hookPackage, false);
    }

    /**
     * Construct the manager
     * @param logger The custom logger that should be used for errors
     * @param hookPackage The package the hooks are in
     */
    public AbstractHookManager(Logger logger, String hookPackage) {
        this(logger, hookPackage, false);
    }

    /**
     * Construct the manager
     * @param logger The custom logger that should be used for errors
     * @param hookPackage The package the hooks are in
     * @param onePackagePerHook Whether each hook should have its own package named after the hook or not
     *                          Defaults to <code>false</code>
     */
    public AbstractHookManager(Logger logger, String hookPackage, boolean onePackagePerHook) {
        this.logger = logger;
        this.hookPackage = hookPackage;
        this.onePackagePerHook = onePackagePerHook;
    }

    protected void registerExistingHookables() {
        for (H hookable : getHookables()) {
            if (isEnabled(hookable)) {
                registerHook(hookable);
            }
        }
    }

    /**
     * Get the Hook from a Hookable
     * @param hookable The hookable to get the hook from
     * @return The hook or null
     */
    public Hook<H> getHook(H hookable) {
        return hookMap.get(getName(hookable));
    }

    /**
     * Get the suffix to append to the class name when searching for a hook.
     * @return The hook class suffix; defaults to <code>"Hook"</code>
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Set the suffix to append to the class name when searching for a hook.
     * @param suffix  The hook class suffix; defaults to <code>"Hook"</code>
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    private void registerHook(H hookable) {
        for (String name : getNames(hookable)) {
            if (registerHook(name, hookable)) {
                break;
            }
        }
    }

    private boolean registerHook(String name, H hookable) {
        String adjustedName = NON_WORD_PATTERN.matcher(name).replaceAll("_");
        String version = NON_WORD_PATTERN.matcher(getVersion(hookable)).replaceAll("_");
        String path = hookPackage + "." + (onePackagePerHook ? name.toLowerCase(): name);
        Class<?> hookClass = null;
        do {
            try {
                hookClass = Class.forName(path +  (onePackagePerHook ? version.toLowerCase() + "." + adjustedName : version) + suffix);
                if (!Hook.class.isAssignableFrom(hookClass)) {
                    hookClass = null;
                }
            } catch (ClassNotFoundException ignored) {}
            if (version.contains("_")) {
                version = version.substring(0, version.lastIndexOf('_'));
            } else {
                try {
                    hookClass = Class.forName(path + (onePackagePerHook ? adjustedName : "") + suffix);
                    if (!Hook.class.isAssignableFrom(hookClass)) {
                        hookClass = null;
                    }
                } catch (ClassNotFoundException ignored) {}
                break;
            }
        } while (hookClass == null);

        if (hookClass != null) {
            try {
                Hook<H> hook;
                try {
                    hook = (Hook<H>) hookClass.getConstructor(hookable.getClass()).newInstance(hookable);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    hook = (Hook<H>) hookClass.getConstructor().newInstance();
                }
                hook.register();
                hookMap.put(getName(hookable), hook);
                return true;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                logger.log(Level.SEVERE, "Could not hook into " + getName(hookable) + " " + getVersion(hookable) + "!", e);
            }
        }
        return false;
    }

    protected void onHookableEnable(H hookable) {
        registerHook(hookable);
    }

    protected void onHookableDisable(H hookable) {
        Hook<H> hook = hookMap.remove(getName(hookable));
        if (hook != null) {
            hook.unregister();
        }
    }

    protected String[] getNames(H hookable) {
        return new String[] { getName(hookable) };
    }

    protected abstract String getName(H hookable);

    protected abstract String getVersion(H hookable);

    protected abstract boolean isEnabled(H hookable);

    protected abstract Collection<H> getHookables();

}
