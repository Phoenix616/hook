# Hook

Library to simplify hooking into other plugins.

Currently (directly) supports Bukkit, Bungee and Velocity, adding your own is easily
possible though by simply implementing the AbstractHookManager.

## How to use it

Depend on one of the adapters or implement the AbstrackHookManager for your platform. 
Then shade it in and (ideally) relocate it in your own package.

Now just create classes extending `de.themoep.hook.core.Hook` named after your plugin
in the package you defined in the constructor of your HookManager.
If you specified that each hook should have its own package then the manager will
search for hook classes in packages named after the plugin.

Besides simple plugin names the manager will also search for hook classes matching the
plugin's version. That way hooking into multiple versions of the same plugin becomes
possible.

See the [Javadocs](https://docs.phoenix616.dev/hook/) for code specifications.

### Examples

Example classes that are checked starting from the provided hookPackage root 
without specifying per-hook package mode and the default `"Hook"` suffix:

| Plugin     | Version                 | Checked class paths                      |
|------------|-------------------------|------------------------------------------|
| Factions   | 1.6.9.5-U0.2.1-SNAPSHOT | Factions1_6_9_U0_2_1_SNAPSHOTHook.class  |
|            |                         | Factions1_6_9_U0_2Hook.class             |
|            |                         | Factions1_6_9Hook.class                  |
|            |                         | Factions1_6Hook.class                    |
|            |                         | Factions1Hook.class                      |
|            |                         | FactionsHook.class                       |
| Factions   | 2.12.0                  | FactionsHook2_12_0.class                 |
|            |                         | FactionsHook2_12.class                   |
|            |                         | FactionsHook2.class                      |
|            |                         | FactionsHook.class                       |
| WorldGuard | 6.1.1-SNAPSHOT          | WorldGuard6_1Hook.class                  |
|            |                         | WorldGuard6Hook.class                    |
| WorldGuard | 7.2.0-SNAPSHOT          | WorldGuard7_2Hook.class                  |
|            |                         | WorldGuard7Hook.class                    |
|            |                         | WorldGuardHook.class                     |

It will basically replace all non-word characters (`\W` regex) with underscores to make
legal path and package names. Of course the table does not list all possible combinations.

The same logic will be used in *per-hook package mode*. In that mode the package name will
have the dynamic path including the version string (plugin name and version will be
lowercase in the path). The actual class name should there be `PluginNameHook.class` 
(with the default `"Hook"` suffix).

E.g. for WorldGuard 7.2.0-SNAPSHOT a possible hook path would be `your.hook.package.root.worldguard7_2.WorldGuardHook.class`.

### Maven information

#### Repository:

```xml
<repository>
    <id>hook-repo</id>
    <url>https://repo.minebench.de/</url>
</repository>
```

#### Bukkit adapter:

```xml
<dependency>
    <groupId>de.themoep.hook</groupId>
    <artifactId>hook-bukkit</artifactId>
    <version>1.0-SNAPSHOT<version>
    <scope>compile</scope>
</dependency>
```

#### Bungee adapter:

```xml
<dependency>
    <groupId>de.themoep.hook</groupId>
    <artifactId>hook-bungee</artifactId>
    <version>1.0-SNAPSHOT<version>
    <scope>compile</scope>
</dependency>
```

#### Velocity adapter:

```xml
<dependency>
    <groupId>de.themoep.hook</groupId>
    <artifactId>hook-velocity</artifactId>
    <version>1.0-SNAPSHOT<version>
    <scope>compile</scope>
</dependency>
```

#### Core for implementing AbstractHookManager:

```xml
<dependency>
    <groupId>de.themoep.hook</groupId>
    <artifactId>hook-core</artifactId>
    <version>1.0-SNAPSHOT<version>
    <scope>compile</scope>
</dependency>
```

## License

```
hook - library to manage hooking into other plugins
Copyright (c) 2020 Max Lee aka Phoenix616 (mail@moep.tv)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```