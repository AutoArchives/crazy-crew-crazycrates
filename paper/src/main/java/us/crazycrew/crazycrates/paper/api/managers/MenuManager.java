package us.crazycrew.crazycrates.paper.api.managers;

import ch.jalu.configme.SettingsManager;
import us.crazycrew.crazycrates.common.config.MainConfig;
import us.crazycrew.crazycrates.common.config.menus.CrateMainMenu;
import us.crazycrew.crazycrates.common.config.menus.CratePreviewMenu;
import us.crazycrew.crazycrates.paper.api.CrazyManager;
import us.crazycrew.crazycrates.paper.api.plugin.frame.BukkitUserManager;
import us.crazycrew.crazycrates.paper.api.objects.Crate;
import us.crazycrew.crazycrates.api.enums.types.CrateType;
import us.crazycrew.crazycrates.paper.CrazyCrates;
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
    private final @NotNull BukkitUserManager userManager = this.cratesLoader.getUserManager();

    private final @NotNull SettingsManager config = this.cratesLoader.getConfigManager().getConfig();
    private final @NotNull SettingsManager mainMenuConfig = this.cratesLoader.getConfigManager().getMainMenuConfig();
    private final @NotNull SettingsManager previewMenuConfig = this.cratesLoader.getConfigManager().getPreviewMenuConfig();

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
        this.menuButton = new ItemBuilder()
                .setMaterial(this.previewMenuConfig.getProperty(CratePreviewMenu.CRATE_PREVIEW_MENU_BUTTON_MATERIAL))
                .setName(this.previewMenuConfig.getProperty(CratePreviewMenu.CRATE_PREVIEW_MENU_BUTTON_NAME))
                .setLore(this.previewMenuConfig.getProperty(CratePreviewMenu.CRATE_PREVIEW_MENU_BUTTON_LORE))
                .build();
        this.nextButton = new ItemBuilder()
                .setMaterial(this.previewMenuConfig.getProperty(CratePreviewMenu.CRATE_PREVIEW_NEXT_BUTTON_MATERIAL))
                .setName(this.previewMenuConfig.getProperty(CratePreviewMenu.CRATE_PREVIEW_NEXT_BUTTON_NAME))
                .setLore(this.previewMenuConfig.getProperty(CratePreviewMenu.CRATE_PREVIEW_NEXT_BUTTON_LORE));
        this.backButton = new ItemBuilder()
                .setMaterial(this.previewMenuConfig.getProperty(CratePreviewMenu.CRATE_PREVIEW_BACK_BUTTON_MATERIAL))
                .setName(this.previewMenuConfig.getProperty(CratePreviewMenu.CRATE_PREVIEW_BACK_BUTTON_NAME))
                .setLore(this.previewMenuConfig.getProperty(CratePreviewMenu.CRATE_PREVIEW_BACK_BUTTON_LORE));
    }

    public void openNewPreview(Player player, Crate crate) {
        this.playerCrate.put(player.getUniqueId(), crate);
        setPage(player.getUniqueId(), 1);

        player.openInventory(crate.getPreview(player));
    }

    public void openPreview(Player player, Crate crate) {
        UUID uuid = player.getUniqueId();

        this.playerCrate.put(uuid, crate);

        player.openInventory(crate.getPreview(player));
    }

    public int getPage(UUID uuid) {
        return this.playerPage.getOrDefault(uuid, 1);
    }

    public void setPage(UUID uuid, int pageNumber) {
        int max = this.playerCrate.get(uuid).getMaxPage();

        if (pageNumber < 1) {
            pageNumber = 1;
        } else if (pageNumber >= max) {
            pageNumber = max;
        }

        this.playerPage.put(uuid, pageNumber);
    }

    public ItemStack getMenuButton() {
        return this.menuButton;
    }

    public ItemStack getNextButton() {
        return getNextButton(null);
    }

    public ItemStack getNextButton(UUID uuid) {
        ItemBuilder button = new ItemBuilder(this.nextButton);

        button.addLorePlaceholder("%Page%", (getPage(uuid) + 1) + "");

        return button.build();
    }

    public ItemStack getBackButton() {
        return getBackButton(null);
    }

    public ItemStack getBackButton(UUID uuid) {
        ItemBuilder button = new ItemBuilder(this.backButton);
        
        button.addLorePlaceholder("%Page%", (getPage(uuid) - 1) + "");

        return button.build();
    }

    public boolean playerInMenu(Player player) {
        return this.playerInMenu.getOrDefault(player.getUniqueId(), false);
    }

    public void setPlayerInMenu(Player player, boolean inMenu) {
        this.playerInMenu.put(player.getUniqueId(), inMenu);
    }

    public void openMainMenu(Player player) {
        int size = this.mainMenuConfig.getProperty(CrateMainMenu.CRATE_MENU_SIZE);
        Inventory inv = this.plugin.getServer().createInventory(null, size, this.mainMenuConfig.getProperty(CrateMainMenu.CRATE_MENU_TITLE));

        if (!this.mainMenuConfig.getProperty(CrateMainMenu.CRATE_MENU_FILLER_TOGGLE)) {
            String id = this.mainMenuConfig.getProperty(CrateMainMenu.CRATE_MENU_FILLER_ITEM);
            String name = this.mainMenuConfig.getProperty(CrateMainMenu.CRATE_MENU_FILLER_NAME);
            List<String> lore = this.mainMenuConfig.getProperty(CrateMainMenu.CRATE_MENU_FILLER_LORE);
            ItemStack item = new ItemBuilder().setMaterial(id).setName(name).setLore(lore).build();

            for (int i = 0; i < size; i++) {
                inv.setItem(i, item.clone());
            }
        }

        UUID uuid = player.getUniqueId();

        if (this.config.getProperty(MainConfig.CUSTOMIZER_TOGGLE)) {
            for (String custom : this.config.getProperty(MainConfig.CUSTOMIZER)) {
                int slot = 0;
                if (this.config.getProperty(MainConfig.CUSTOMIZER_TOGGLE)) {
                    ItemBuilder item = new ItemBuilder();
                    String[] split = custom.split(", ");

                    for (String option : split) {

                        if (option.contains("Item:")) item.setMaterial(option.replace("Item:", ""));

                        if (option.contains("Name:")) {
                            option = option.replace("Name:", "");

                            option = getCrates(player, option);

                            item.setName(option.replaceAll("%player%", player.getName()));
                        }

                        if (option.contains("Lore:")) {
                            option = option.replace("Lore:", "");
                            String[] d = option.split(",");

                            for (String ignored : d) {
                                option = getCrates(player, option);

                                item.addLore(option.replaceAll("%player%", player.getName()));
                            }
                        }

                        if (option.contains("Glowing:")) item.setGlow(Boolean.parseBoolean(option.replace("Glowing:", "")));

                        if (option.contains("Player:")) item.setPlayerName(option.replaceAll("%player%", player.getName()));

                        if (option.contains("Slot:")) slot = Integer.parseInt(option.replace("Slot:", ""));

                        if (option.contains("Unbreakable-Item"))
                            item.setUnbreakable(Boolean.parseBoolean(option.replace("Unbreakable-Item:", "")));

                        if (option.contains("Hide-Item-Flags"))
                            item.hideItemFlags(Boolean.parseBoolean(option.replace("Hide-Item-Flags:", "")));
                    }

                    if (slot > size) continue;

                    slot--;
                    inv.setItem(slot, item.build());
                }
            }
        }

        for (Crate crate : this.crazyManager.getCrates()) {
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
                            .addLorePlaceholder("%Keys%", NumberFormat.getNumberInstance().format(this.userManager.getVirtualKeys(uuid, crate.getName())))
                            .addLorePlaceholder("%Keys_Physical%", NumberFormat.getNumberInstance().format(this.userManager.getPhysicalKeys(uuid, crate.getName())))
                            .addLorePlaceholder("%Keys_Total%", NumberFormat.getNumberInstance().format(this.userManager.getTotalKeys(uuid, crate.getName())))
                            .addLorePlaceholder("%Player%", player.getName())
                            .build());
                }
            }
        }

        player.openInventory(inv);
    }

    private String getCrates(Player player, String option) {
        UUID uuid = player.getUniqueId();

        for (Crate crate : this.crazyManager.getCrates()) {
            if (crate.getCrateType() != CrateType.MENU) {
                option = option.replaceAll("%" + crate.getName().toLowerCase() + "%", this.userManager.getVirtualKeys(uuid, crate.getName()) + "")
                        .replaceAll("%" + crate.getName().toLowerCase() + "_physical%", this.userManager.getPhysicalKeys(uuid, crate.getName()) + "")
                        .replaceAll("%" + crate.getName().toLowerCase() + "_total%", this.userManager.getTotalKeys(uuid, crate.getName()) + "");
            }
        }

        return option;
    }
}