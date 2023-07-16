package com.badbones69.crazycrates.paper.api.v2.enums.support;

import com.badbones69.crazycrates.paper.api.v2.storage.interfaces.UserManager;
import com.badbones69.crazycrates.paper.api.v2.storage.types.file.yaml.users.YamlUserManager;

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