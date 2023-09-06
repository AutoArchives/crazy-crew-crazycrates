package us.crazycrew.crazycrates.api.platforms;

import org.jetbrains.annotations.NotNull;

public interface Platform {

    Platform.@NotNull Type getType();

    enum Type {

        PAPER("Paper"),
        FOLIA("Folia"),

        FABRIC("Fabric");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }
}