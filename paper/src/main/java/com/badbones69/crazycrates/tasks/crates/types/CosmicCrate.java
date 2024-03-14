package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.objects.Crate;
import org.bukkit.entity.Player;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import com.badbones69.crazycrates.api.builders.CrateBuilder;

public class CosmicCrate extends CrateBuilder {

    public CosmicCrate(Crate crate, Player player, int size) {
        super(crate, player, size, crate.getSection().getString("Crate.CrateName") + " - Choose");
    }

    @Override
    public void open(KeyType type, boolean checkHand) {
        /*// If the crate event failed.
        if (isCrateEventValid(type, checkHand)) {
            return;
        }

        CosmicCrateManager manager = (CosmicCrateManager) getCrate().getManager();
        int slot = 1;

        for (int index = 0; index < getSize(); index++) {
            ItemStack stack = manager.getMysteryCrate().setTarget(getPlayer()).setAmount(slot).addNamePlaceholder("%Slot%", String.valueOf(slot)).addLorePlaceholder("%Slot%", String.valueOf(slot)).build();

            ItemMeta itemMeta = stack.getItemMeta();

            Tier tier = PrizeManager.getTier(getCrate());

            if (tier != null) {
                itemMeta.getPersistentDataContainer().set(PersistentKeys.crate_tier.getNamespacedKey(), PersistentDataType.STRING, tier.getName());
                stack.setItemMeta(itemMeta);

                setItem(index, stack);
                slot++;
            }
        }

        this.crateManager.addPlayerKeyType(getPlayer(), type);
        this.crateManager.addHands(getPlayer(), checkHand);

        getPlayer().openInventory(getInventory());*/
    }

    @Override
    public void run() {

    }
}