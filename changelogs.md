# Version 2.11.16-rc1
<p>
The next major update of CrazyCrates, Please thoroughly read this from `start` to `finish`.<br>
You should take a pretty portion of what you know of the previous versions of CrazyCrates and just drop it in the bin.

**This is a BETA TEST and is not the final product, Do not use this on a production server.**<br>
**I am not responsible for any data loss, You've been warned**
</p>

## Changes:
 ### Major Changes:
  * Spigot is genuinely no longer supported, The plugin will not even boot. I've removed the plugin.yml in favor of paper-plugin.yml
  * All internal placeholders not related to PlaceholderAPI have been changed as to not be confused with PlaceholderAPI

| Old         | New         |
|-------------|-------------|
| %command%   | {command}   |
| %crate%     | {crate}     |
| %key%       | {key}       |
| %usage%     | {usage}     |
| %prize%     | {prize}     |
| %player%    | {player}    |
| %amount%    | {amount}    |
| %world%     | {world}     |
| %id%        | {id}        |
| %prefix%    | {prefix}    |
| %number%    | {number}    |
| %cratetype% | {cratetype} |
| %keys%      | {keys}      |

 * All old placeholders should be automatically replaced however it's easy for you to do it manually in advance!
   * Find & Replace in your file editors.
  * Color Codes such as &7, &c or &e are no longer supported. We fully support MiniMessage across the entire project.
    * https://docs.advntr.dev/minimessage/format.html

 ### Dev API Changes:
  * N/A

 ### Bug Fixes:
  * N/A

 ### Permissions Changed:

<details><summary>/crazycrates schematic set1/set2 | /crazycrates/schematic save</summary>

| Old                                        | New                                         |
|--------------------------------------------|---------------------------------------------|
| crazycrates.command.admin.schematic.save   | crazycrates.command.admin.schematic-save    |
| crazycrates.command.admin.schematic.set    | crazycrates.command.admin.schematic-set     |
</details>

<details><summary>/crazycrates open-others</summary>

| Old                                        | New                                         |
|--------------------------------------------|---------------------------------------------|
| crazycrates.command.admin.open.others      | crazycrates.command.admin.open-others       |
</details>

<details><summary>/crazycrates mass-open</summary>

| Old                                        | New                                         |
|--------------------------------------------|---------------------------------------------|
| crazycrates.command.admin.mass.open        | crazycrates.command.admin.mass-open         |
</details>

<details><summary>/crazycrates force-open</summary>

| Old                                        | New                                         |
|--------------------------------------------|---------------------------------------------|
| crazycrates.command.admin.forceopen        | crazycrates.command.admin.force-open        |
</details>

<details><summary>/crazycrates give</summary>

| Old                                        | New                                         |
|--------------------------------------------|---------------------------------------------|
| crazycrates.command.admin.givekey          | crazycrates.command.admin.give-key          |
</details>

<details><summary>/crazycrates give-random</summary>

| Old                                        | New                                         |
|--------------------------------------------|---------------------------------------------|
| crazycrates.command.admin.giverandomkey    | crazycrates.command.admin.give-random-key   |
</details>

<details><summary>/crazycrates give-all</summary>

| Old                                        | New                                         |
|--------------------------------------------|---------------------------------------------|
| crazycrates.command.admin.giveall          | crazycrates.command.admin.give-all          |
| crazycrates.command.exclude.player.giveall | crazycrates.command.exclude.player.give-all |
</details>

<details><summary>/crazycrates set-crate</summary>

| Old                                        | New                                         |
|--------------------------------------------|---------------------------------------------|
| crazycrates.command.admin.set              | crazycrates.command.admin.set-crate         |
</details>


<details><summary>/crazycrates set-menu</summary>

| Old                               | New                                |
|-----------------------------------|------------------------------------|
| crazycrates.command.admin.setmenu | crazycrates.command.admin.set-menu |
</details>

<details><summary>/crazycrates key {player}</summary>

| Old                                   | New                                   |
|---------------------------------------|---------------------------------------|
| crazycrates.command.player.key.others | crazycrates.command.player.key-others |
</details>

<details><summary>/crazycrates gui</summary>

| Old                             | New                                   |
|---------------------------------|---------------------------------------|
| crazycrates.command.player.menu | crazycrates.command.player.crate-menu |
</details>