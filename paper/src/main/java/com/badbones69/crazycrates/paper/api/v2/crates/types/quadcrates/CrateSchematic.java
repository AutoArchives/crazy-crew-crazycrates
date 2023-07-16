package com.badbones69.crazycrates.paper.api.v2.crates.types.quadcrates;

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