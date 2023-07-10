package com.badbones69.crazycrates.commands.engine.sender;

public class CommandData {

    private String description;
    private boolean isVisible;

    public CommandData(String description) {
        this.description = description;
    }

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
}