package com.badbones69.crazycrates.tasks.crates.types;

import com.badbones69.crazycrates.api.objects.Crate;
import org.bukkit.entity.Player;
import us.crazycrew.crazycrates.api.enums.types.KeyType;
import com.badbones69.crazycrates.api.builders.CrateBuilder;

public class CrateOnTheGo extends CrateBuilder {

    public CrateOnTheGo(Crate crate, Player player) {
        super(crate, player);
    }

    @Override
    public void open(KeyType type, boolean checkHand) {
        // Crate event failed so we return.
        /*if (isCrateEventValid(KeyType.physical_key, checkHand)) {
            return;
        }

        boolean keyCheck = this.userManager.takeKeys(1, getPlayer().getUniqueId(), getCrate().getName(), KeyType.physical_key, true);

        if (!keyCheck) {
            // Send the message about failing to take the key.
            MiscUtils.failedToTakeKey(getPlayer(), getCrate().getName());

            // Remove from opening list.
            this.crateManager.removePlayerFromOpeningList(getPlayer());

            return;
        }

        Prize prize = getCrate().pickPrize(getPlayer());
        PrizeManager.givePrize(getPlayer(), prize, getCrate());

        if (prize.useFireworks()) MiscUtils.spawnFirework(getPlayer().getLocation().add(0, 1, 0), null);

        this.crateManager.removePlayerKeyType(getPlayer());*/
    }

    @Override
    public void run() {

    }
}