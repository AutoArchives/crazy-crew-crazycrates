package com.ryderbelserion.crazycrates.core.storage.types.file;

public interface FileLoader {

    void load();

    void save();

    String getImplName();

}