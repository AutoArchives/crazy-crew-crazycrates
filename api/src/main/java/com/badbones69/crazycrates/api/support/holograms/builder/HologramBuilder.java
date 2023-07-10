package com.badbones69.crazycrates.api.support.holograms.builder;

import net.minecraft.world.entity.Display;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;

public class HologramBuilder {

    private TextDisplay display;

    private String name;
    private int width;
    private Color color;
    private byte opacity;
    private boolean hasShadow;
    private boolean isTransparent;
    private boolean hasBackground;
    private TextDisplay.TextAlignment alignment;

    public HologramBuilder setName(String name) {
        this.name = name;

        Display.TextDisplay.setViewScale(5);

        return this;
    }

    public HologramBuilder setWidth(int width) {
        this.width = width;

        return this;
    }

    public HologramBuilder setColor(Color color) {
        this.color = color;

        return this;
    }

    public HologramBuilder setOpacity(byte opacity) {
        this.opacity = opacity;

        return this;
    }

    public HologramBuilder setShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;

        return this;
    }

    public HologramBuilder setTransparent(boolean isTransparent) {
        this.isTransparent = isTransparent;

        return this;
    }

    public HologramBuilder setHasBackground(boolean hasBackground) {
        this.hasBackground = hasBackground;

        return this;
    }

    public HologramBuilder setAlignment(TextDisplay.TextAlignment alignment) {
        this.alignment = alignment;

        return this;
    }

    public TextDisplay build(TextDisplay display) {
        display.setText(this.name);

        return this.display = display;
    }
}