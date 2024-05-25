package dev.golgolex.golgocloud.instance.configuration;

import dev.golgolex.golgocloud.common.configuration.ConfigurationClass;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.json.document.JsonDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

public class InstanceConfiguration extends ConfigurationClass {

    public InstanceConfiguration(@NotNull File configurationDirectory) {
        super("instance", configurationDirectory);
    }

    @Override
    public JsonDocument defaultConfiguration() {
        return new JsonDocument()
                .write("auth-key", "")
                .write("instance-id", UUID.randomUUID().toString())
                .write("base-directory-path", CloudInstance.instance().instanceDirectory().getAbsolutePath())
                .write("consoleDebug", false)
                .write("nettyDebug", false);
    }

    public String authKey() {
        return this.configuration().readString("auth-key");
    }

    public UUID instanceId() {
        return UUID.fromString(this.configuration().readString("instance-id"));
    }

    public Path baseDirectoryPath() {
        return Path.of(this.configuration().readString("base-directory-path"));
    }

    public boolean consoleDebug() {
        return this.configuration().readBoolean("consoleDebug");
    }

    public boolean nettyDebug() {
        return this.configuration().readBoolean("nettyDebug");
    }
}
