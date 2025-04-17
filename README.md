# SpawnProtect

SpawnProtect is a Minecraft plugin designed to protect a configurable spawn area from unauthorized block breaking and command usage. It allows server administrators to define a protected zone and manage permissions for players.

## Features

- Protect a configurable spawn area from block breaking.
- Restrict specific commands within the spawn area.
- Commands to set the spawn protection zone positions.
- Permission-based access for bypassing protections.

## Installation

1. Download the plugin `.jar` file.
2. Place the `.jar` file in the `plugins` folder of your Minecraft server.
3. Start or restart the server to generate the default configuration files.
4. Edit the `config.yml` file to configure the spawn zone as needed.
5. Use `/setpos1` and `/setpos2` in-game to set the spawn zone positions.

## Usage

- Define the spawn protection zone using `/setpos1` and `/setpos2`.
- Players without the `spawnprotect.bypass.break` permission will be unable to break blocks in the spawn zone.
- Commands starting with `/cpvpbot` are restricted within the spawn zone.

## Commands

| Command    | Description                           | Permission             |
|------------|---------------------------------------|------------------------|
| `/setpos1` | Set spawn protection zone position 1  | `spawnprotect.setpos` |
| `/setpos2` | Set spawn protection zone position 2  | `spawnprotect.setpos` |

## Permissions

| Permission                  | Description                                      | Default |
|-----------------------------|--------------------------------------------------|---------|
| `spawnprotect.setpos`       | Allows player to set spawn zone positions        | `op`    |
| `spawnprotect.bypass.break` | Allows player to break blocks in the spawn zone  | `op`    |

## Configuration

The plugin uses a `config.yml` file to define the spawn zone. Below is an example configuration:

```yaml
spawnzone:
  world: world
  pos1:
    x: 0
    y: 64
    z: 0
  pos2:
    x: 10
    y: 70
    z: 10
