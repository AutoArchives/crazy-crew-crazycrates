package us.crazycrew.crazycrates.api.platforms;

import org.jetbrains.annotations.NotNull;

public interface Platform {

    @NotNull Type getType();

    enum Type {

        PAPER("paper");

        private final String platformType;

        Type(String platformType) {
            this.platformType = platformType;
        }

        public @NotNull String getPlatformType() {
            return this.platformType;
        }
    }
}