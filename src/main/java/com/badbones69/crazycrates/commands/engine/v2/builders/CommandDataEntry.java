package com.badbones69.crazycrates.commands.engine.v2.builders;

public class CommandDataEntry {

    private boolean isHidden = false;

    public void setHidden(boolean value) {
        this.isHidden = value;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    /*
    private final LinkedList<String> aliases = new LinkedList<>();

    public boolean hasAlias(String alias) {
        return this.aliases.contains(alias);
    }

    public void addAlias(String alias) {
        if (hasAlias(alias)) return;

        this.aliases.add(alias);
    }

    public void removeAlias(String alias) {
        if (!hasAlias(alias)) return;

        this.aliases.remove(alias);
    }

    public List<String> getAliases() {
        return Collections.unmodifiableList(this.aliases);
    }
     */
}