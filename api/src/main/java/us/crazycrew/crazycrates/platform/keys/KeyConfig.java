package us.crazycrew.crazycrates.platform.keys;

import org.simpleyaml.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class KeyConfig extends YamlConfiguration {

    private final File file;

    public KeyConfig(File file) {
        this.file = file;
    }

    public void load() throws IOException {
        load(this.file);
    }

    public String getFileName() {
        return getFile().getName().replaceAll(".yml", "");
    }

    public File getFile() {
        return this.file;
    }

    public String getMaterial() {
        return getString("item.material", "TRIPWIRE_HOOK");
    }

    public String getName() {
        return getString("item.name", "");
    }

    public List<String> getLore() {
        return getStringList("item.lore");
    }

    public List<String> getItemFlags() {
        return getStringList("item.flags");
    }

    public boolean isUnbreakable() {
        return getBoolean("item.unbreakable", false);
    }

    public boolean isGlowing() {
        return getBoolean("item.glowing", true);
    }
}