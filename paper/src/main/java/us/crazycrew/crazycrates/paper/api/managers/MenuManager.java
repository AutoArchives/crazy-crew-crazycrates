package us.crazycrew.crazycrates.paper.api.managers;

import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.FileManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import us.crazycrew.crazycrates.paper.Methods;
import us.crazycrew.crazycrates.paper.api.objects.ItemBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.api.plugin.CrazyCratesLoader;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MenuManager {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final @NotNull CrazyCratesLoader cratesLoader = this.plugin.getCratesLoader();
    private final @NotNull CrazyManager crazyManager = this.cratesLoader.getCrazyManager();
    private final @NotNull Methods methods = this.cratesLoader.getMethods();

    private final HashMap<UUID, Integer> playerPage = new HashMap<>();
    private final HashMap<UUID, Crate> playerCrate = new HashMap<>();
    private final HashMap<UUID, Boolean> playerInMenu = new HashMap<>();
    private ItemStack menuButton;
    private ItemBuilder nextButton;
    private ItemBuilder backButton;

    public Map<UUID, Boolean> getPlayerInMenu() {
        return Collections.unmodifiableMap(this.playerInMenu);
    }

    public Map<UUID, Crate> getPlayerCrate() {
        return Collections.unmodifiableMap(this.playerCrate);
    }

    public Map<UUID, Integer> getPlayerPage() {
        return Collections.unmodifiableMap(this.playerPage);
    }

    public void loadButtons() {
        FileConfiguration config = FileManager.Files.CONFIG.getFile();
        String path = "Settings.Preview.Buttons.";
        menuButton = new ItemBuilder()
                .setMaterial(config.getString(path + "Menu.Item", "COMPASS"))
                .setName(config.getString(path + "Menu.Name", "&7&l>> &c&lMenu &7&l<<"))
                .setLore(config.contains(path + "Menu.Lore") ? config.getStringList(path + "Menu.Lore") : Collections.singletonList("&7Return to the menu."))
                .build();
        nextButton = new ItemBuilder()
                .setMaterial(config.getString(path + "Next.Item", "FEATHER"))
                .setName(config.getString(path + "Next.Name", "&6&lNext >>"))
                .setLore(config.contains(path + "Next.Lore") ? config.getStringList(path + "Next.Lore") : Collections.singletonList("&7&lPage: &b%page%"));
        backButton = new ItemBuilder()
                .setMaterial(config.getString(path + "Back.Item", "FEATHER"))
                .setName(config.getString(path + "Back.Name", "&6&l<< Back"))
                .setLore(config.contains(path + "Back.Lore") ? config.getStringList(path + "Back.Lore") : Collections.singletonList("&7&lPage: &b%page%"));
    }

    public void openNewPreview(UUID uuid, Crate crate) {
        playerCrate.put(uuid, crate);
        setPage(uuid, 1);

        Player player = this.plugin.getServer().getPlayer(uuid);
        if (player != null) player.openInventory(crate.getPreview(uuid));
    }

    public void openPreview(UUID uuid) {
        Player player = this.plugin.getServer().getPlayer(uuid);
        if (player != null) player.openInventory(playerCrate.get(uuid).getPreview(uuid));
    }

    public void openPreview(UUID uuid, Crate crate) {
        playerCrate.put(uuid, crate);

        Player player = this.plugin.getServer().getPlayer(uuid);

        if (player != null) player.openInventory(crate.getPreview(uuid));
    }

    public int getPage(UUID uuid) {
        return playerPage.getOrDefault(uuid, 1);
    }

    public void setPage(UUID uuid, int pageNumber) {
        int max = playerCrate.get(uuid).getMaxPage();

        if (pageNumber < 1) {
            pageNumber = 1;
        } else if (pageNumber >= max) {
            pageNumber = max;
        }

        playerPage.put(uuid, pageNumber);
    }

    public ItemStack getMenuButton() {
        return menuButton;
    }

    public ItemStack getNextButton() {
        return getNextButton(null);
    }

    public ItemStack getNextButton(UUID uuid) {
        ItemBuilder button = new ItemBuilder(nextButton);

        button.addLorePlaceholder("%Page%", (getPage(uuid) + 1) + "");

        return button.build();
    }

    public ItemStack getBackButton() {
        return getBackButton(null);
    }

    public ItemStack getBackButton(UUID uuid) {
        ItemBuilder button = new ItemBuilder(backButton);
        
        button.addLorePlaceholder("%Page%", (getPage(uuid) - 1) + "");

        return button.build();
    }

    public boolean playerInMenu(UUID uuid) {
        return playerInMenu.getOrDefault(uuid, false);
    }

    public void setPlayerInMenu(UUID uuid, boolean inMenu) {
        playerInMenu.put(uuid, inMenu);
    }

    public void openMainMenu(UUID uuid) {
        int size = FileManager.Files.CONFIG.getFile().getInt("Settings.InventorySize");
        Inventory inv = this.plugin.getServer().createInventory(null, size, methods.sanitizeColor(FileManager.Files.CONFIG.getFile().getString("Settings.InventoryName")));

        if (FileManager.Files.CONFIG.getFile().contains("Settings.Filler.Toggle")) {
            if (FileManager.Files.CONFIG.getFile().getBoolean("Settings.Filler.Toggle")) {
                String id = FileManager.Files.CONFIG.getFile().getString("Settings.Filler.Item");
                String name = FileManager.Files.CONFIG.getFile().getString("Settings.Filler.Name");
                List<String> lore = FileManager.Files.CONFIG.getFile().getStringList("Settings.Filler.Lore");
                ItemStack item = new ItemBuilder().setMaterial(id).setName(name).setLore(lore).build();

                for (int i = 0; i < size; i++) {
                    inv.setItem(i, item.clone());
                }
            }
        }

        Player player = this.plugin.getServer().getPlayer(uuid);

        if (player != null) {
            if (FileManager.Files.CONFIG.getFile().contains("Settings.GUI-Customizer")) {
                for (String custom : FileManager.Files.CONFIG.getFile().getStringList("Settings.GUI-Customizer")) {
                    int slot = 0;
                    ItemBuilder item = new ItemBuilder();
                    String[] split = custom.split(", ");

                    for (String option : split) {

                        if (option.contains("Item:")) item.setMaterial(option.replace("Item:", ""));

                        if (option.contains("Name:")) {
                            option = option.replace("Name:", "");

                            option = getCrates(uuid, option);

                            item.setName(option.replaceAll("%player%", player.getName()));
                        }

                        if (option.contains("Lore:")) {
                            option = option.replace("Lore:", "");
                            String[] d = option.split(",");

                            for (String l : d) {
                                option = getCrates(uuid, option);

                                item.addLore(option.replaceAll("%player%", player.getName()));
                            }
                        }

                        if (option.contains("Glowing:")) item.setGlow(Boolean.parseBoolean(option.replace("Glowing:", "")));

                        if (option.contains("Player:")) item.setPlayerName(option.replaceAll("%player%", player.getName()));

                        if (option.contains("Slot:")) slot = Integer.parseInt(option.replace("Slot:", ""));

                        if (option.contains("Unbreakable-Item")) item.setUnbreakable(Boolean.parseBoolean(option.replace("Unbreakable-Item:", "")));

                        if (option.contains("Hide-Item-Flags")) item.hideItemFlags(Boolean.parseBoolean(option.replace("Hide-Item-Flags:", "")));
                    }

                    if (slot > size) continue;

                    slot--;
                    inv.setItem(slot, item.build());
                }
            }

            for (Crate crate : crazyManager.getCrates()) {
                FileConfiguration file = crate.getFile();

                if (file != null) {
                    if (file.getBoolean("Crate.InGUI")) {
                        String path = "Crate.";
                        int slot = file.getInt(path + "Slot");

                        if (slot > size) continue;

                        slot--;
                        inv.setItem(slot, new ItemBuilder()
                                .setMaterial(file.getString(path + "Item"))
                                .setName(file.getString(path + "Name"))
                                .setLore(file.getStringList(path + "Lore"))
                                .setCrateName(crate.getName())
                                .setPlayerName(file.getString(path + "Player"))
                                .setGlow(file.getBoolean(path + "Glowing"))
                                .addLorePlaceholder("%Keys%", NumberFormat.getNumberInstance().format(crazyManager.getVirtualKeys(uuid, crate)))
                                .addLorePlaceholder("%Keys_Physical%", NumberFormat.getNumberInstance().format(crazyManager.getPhysicalKeys(uuid, crate)))
                                .addLorePlaceholder("%Keys_Total%", NumberFormat.getNumberInstance().format(crazyManager.getTotalKeys(uuid, crate)))
                                .addLorePlaceholder("%Player%", player.getName())
                                .build());
                    }
                }
            }

            player.openInventory(inv);
        }
    }

    private String getCrates(UUID uuid, String option) {
        for (Crate crate : crazyManager.getCrates()) {
            if (crate.getCrateType() != CrateType.MENU) {
                option = option.replaceAll("%" + crate.getName().toLowerCase() + "%", crazyManager.getVirtualKeys(uuid, crate) + "")
                        .replaceAll("%" + crate.getName().toLowerCase() + "_physical%", crazyManager.getPhysicalKeys(uuid, crate) + "")
                        .replaceAll("%" + crate.getName().toLowerCase() + "_total%", crazyManager.getTotalKeys(uuid, crate) + "");
            }
        }

        return option;
    }
}