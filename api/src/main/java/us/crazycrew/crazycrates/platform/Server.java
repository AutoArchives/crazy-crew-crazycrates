package us.crazycrew.crazycrates.platform;

import us.crazycrew.crazycrates.api.users.UserManager;
import us.crazycrew.crazycrates.platform.keys.KeyManager;
import java.io.File;
import java.util.logging.Logger;

public interface Server {

    File getFolder();

    File getKeyFolder();

    File[] getKeyFiles();

    Logger getLogger();

    KeyManager getKeyManager();

    UserManager getUserManager();

    //CrateManager getCrateManager();

}