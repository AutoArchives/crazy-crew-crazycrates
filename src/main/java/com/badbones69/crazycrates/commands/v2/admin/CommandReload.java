package com.badbones69.crazycrates.commands.v2.admin;

/*
import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v2.builders.requirements.CommandRequirementsBuilder;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.api.support.InternalPlaceholderSupport;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandReload extends CommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final InternalPlaceholderSupport placeholderSupport = this.plugin.getApiManager().getPlaceholderSupport();

    private final SettingsManager pluginConfig = this.plugin.getApiManager().getPluginConfig();
    private final SettingsManager locale = this.plugin.getApiManager().getLocale();

    public CommandReload() {
        addAlias("reload");

        //setCommandEntryData(new CommandDataEntry());

        //getCommandDataEntry().setDescription(Permissions.ADMIN_RELOAD.getDescription());

        this.requirements = new CommandRequirementsBuilder()
                .withRawPermission(Permissions.ADMIN_RELOAD.getBuiltPermission())
                .asPlayer(false)
                .build();
    }

    @Override
    protected void perform() {
        this.plugin.getApiManager().reload(true);

        //if (!this.pluginConfig.getProperty(PluginConfig.AUTO_SAVE_TOGGLE)) {
        //    if (this.plugin.getTimer() != null) this.plugin.getTimer().cancel();
        //} else {
        //    if (this.plugin.getTimer() == null) this.plugin.setTimer(new Timer());

        //    this.plugin.getTimer().schedule(new AutoSaveTask(), 0, 20 * 60 * 1000);
        //}

        String message = this.locale.getProperty(Locale.RELOAD_PLUGIN);

        //context.reply(this.placeholderSupport.setPlaceholders(message));
    }

    //if (this.apiManager.getPluginConfig().getProperty(PluginConfig.AUTO_SAVE_TOGGLE)) {
    //    this.timer = new Timer();

    //    this.timer.schedule(new AutoSaveTask(), 0, 20 * 60 * 1000);
    //}

        /*private Timer timer;

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Timer getTimer() {
        return this.timer;
    }

    //if (this.timer != null) this.timer.cancel();
}
*/