package com.badbones69.crazycrates.commands.engine.builder;

public class CommandDataEntry {

    private String description;
    private boolean isVisible;
    private boolean isExcludeValidation = false;

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void setExcludeValidation(boolean excludeValidation) {
        this.isExcludeValidation = excludeValidation;
    }

    public boolean isExcludeValidation() {
        return this.isExcludeValidation;
    }
}