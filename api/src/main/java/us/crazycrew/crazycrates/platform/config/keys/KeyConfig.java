package us.crazycrew.crazycrates.platform.config.keys;

import org.simpleyaml.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class KeyConfig extends YamlConfiguration {

    private final File file;

    public KeyConfig(File file) {
        this.file = file;
    }

    public void load() throws IOException {
        load(this.file);
    }

    public File getFile() {
        return this.file;
    }
}