package dev.golgolex.golgocloud.base.configuration;

import dev.golgolex.golgocloud.common.configuration.ConfigurationClass;
import dev.golgolex.quala.common.json.JsonDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DatabaseConfiguration extends ConfigurationClass {

    public DatabaseConfiguration(@NotNull File configurationDirectory) {
        super("database", configurationDirectory);
    }

    @Override
    public JsonDocument defaultConfiguration() {
        return new JsonDocument()
                .write("mongodb", new JsonDocument()
                        .write("enabled", true)
                        .write("host", "127.0.0.1")
                        .write("port", 27017)
                        .write("user", "admin")
                        .write("password", "**********")
                        .write("database", "GolgoCloud")
                        .write("userDatabaseName", "admin")
                        .write("collections", new JsonDocument()
                                .write("translation", "cloud-translations")))
                .write("redis", new JsonDocument()
                        .write("enabled", false)
                        .write("host", "127.0.0.1")
                        .write("user", "admin")
                        .write("password", "**********"));
    }
}