package com.ryderbelserion.crazycrates.core.storage.types.file.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryderbelserion.crazycrates.core.storage.FileExtension;
import com.ryderbelserion.crazycrates.core.storage.enums.StorageType;
import com.ryderbelserion.crazycrates.core.storage.types.file.FileLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;

public class JsonLoader implements FileLoader {

    private final FileExtension fileExtension;

    private final File file;

    private final Gson gson;

    public JsonLoader(FileExtension fileExtension) {
        this.fileExtension = fileExtension;

        this.file = this.fileExtension.getFile();

        if (this.fileExtension.getBuilder() != null) {
            this.gson = this.fileExtension.getBuilder().create();
            return;
        }

        GsonBuilder builder = new GsonBuilder().disableHtmlEscaping()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .excludeFieldsWithoutExposeAnnotation();

        this.gson = builder.create();
    }

    @Override
    public void load() {
        try {
            if (this.file.createNewFile()) {
                save();

                return;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(this.file), StandardCharsets.UTF_8)) {
            this.gson.fromJson(reader, this.fileExtension.getClass());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            if (!this.file.exists()) this.file.createNewFile();

            write();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String getImplName() {
        return StorageType.JSON.getName();
    }

    private void write() throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8)) {
            String values = this.gson.toJson(this.fileExtension, this.fileExtension.getClass());

            writer.write(values);
        }
    }
}