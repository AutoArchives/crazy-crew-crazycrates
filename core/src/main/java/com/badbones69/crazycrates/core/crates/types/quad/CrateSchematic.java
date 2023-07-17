package com.badbones69.crazycrates.core.crates.types.quad;

import java.io.File;

public record CrateSchematic(String schematicName, File schematicFile) {

    @Override
    public String schematicName() {
        return schematicName;
    }

    @Override
    public File schematicFile() {
        return schematicFile;
    }
}