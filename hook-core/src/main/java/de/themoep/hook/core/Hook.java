package de.themoep.hook.core;

/*
 * hook
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

/**
 * Represents a hook
 * @param <P> The plugin class which this hook is for
 */
public interface Hook<P> {
    /**
     * The parent plugin of this hook
     * @return The parent plugin
     */
    P getHooked();

    /**
     * Get the name of this hook
     * @return The name of this hook; usually the name of the hooked plugin
     */
    String getName();

    /**
     * The version of this hook. This might be useful for some.
     * @return The version of this hook; defaults to <tt>0</tt>
     */
    default int getHookVersion() {
        return 0;
    }

    /**
     * Do some extra registration which this hook might require to work.
     * E.g. registering event listeners
     */
    default void register() {}

    /**
     * Do some extra unregistration which this hook might require to be properly cleaned up.
     * E.g. unregistering event listeners
     */
    default void unregister() {}
}
