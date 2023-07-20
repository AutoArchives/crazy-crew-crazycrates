package com.badbones69.crazycrates.paper.api.enums.support;

import com.badbones69.crazycrates.paper.api.storage.types.file.yaml.users.YamlUserManager;
import com.badbones69.crazycrates.paper.api.storage.interfaces.UserManager;

public enum DataSupport {

    //json(JsonUserManager.class),
    yaml(YamlUserManager.class);

    private final Class<? extends UserManager> classObject;

    DataSupport(Class<? extends UserManager> classObject) {
        this.classObject = classObject;
    }

    public Class<? extends UserManager> getClassObject() {
        return this.classObject;
    }
}