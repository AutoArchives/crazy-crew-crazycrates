# Version 2.11.16-rc1
<p>
The next major update of CrazyCrates, Please thoroughly read this from `start` to `finish`.<br>
You should take a pretty portion of what you know of the previous versions of CrazyCrates and just drop it in the bin.

**This is a BETA TEST and is not the final product, Do not use this on a production server.**<br>
**I am not responsible for any data loss, You've been warned**
</p>

## Changes:
 ### Major Changes:
  * Spigot is genuinely no longer supported, The plugin will disable on Spigot based servers.
  * Color Codes such as &7, &c or &e are no longer supported. We fully support MiniMessage across the entire project.
    * https://docs.advntr.dev/minimessage/format.html

 ### Dev API Changes:
  * N/A

 ### Bug Fixes:
  * N/A

 ### Permissions Changed:

<details><summary>/crazycrates schematic set1/set2 | /crazycrates/schematic save</summary>

  * crazycrates.command.admin.schematic.set -> crazycrates.command.admin.schematic-set
  * crazycrates.command.admin.schematic.save -> crazycrates.command.admin.schematic-save
</details>

<details><summary>/crazycrates open-others</summary>

  * crazycrates.command.admin.open.others -> crazycrates.command.admin.open-others
</details>

<details><summary>/crazycrates mass-open</summary>

  * crazycrates.command.admin.mass.open -> crazycrates.command.admin.mass-open
</details>

<details><summary>/crazycrates force-open</summary>

  * crazycrates.command.admin.forceopen -> crazycrates.command.admin.force-open
</details>

<details><summary>/crazycrates give</summary>

  * crazycrates.command.admin.givekey -> crazycrates.command.admin.give-key
</details>

<details><summary>/crazycrates give-random</summary>

  * crazycrates.command.admin.giverandomkey -> crazycrates.command.admin.give-random-key
</details>

<details><summary>/crazycrates give-all</summary>

  * crazycrates.command.admin.giveall -> crazycrates.command.admin.give-all
  #### Note: The permission used to exclude players from getting keys from give-all has changed!
  * crazycrates.command.exclude.player.giveall -> crazycrates.command.excludes.player.give-all
</details>

<details><summary>/crazycrates set-crate</summary>

  * crazycrates.command.admin.set -> crazycrates.command.admin.set-crate
</details>


<details><summary>/crazycrates set-menu</summary>

  * crazycrates.command.admin.setmenu -> crazycrates.command.admin.set-menu
</details>

<details><summary>/crazycrates key {player}</summary>

  * crazycrates.command.player.key.others -> crazycrates.command.player.key-others
</details>

<details><summary>/crazycrates gui</summary>

  * crazycrates.command.player.menu -> crazycrates.command.player.crate-menu
</details>