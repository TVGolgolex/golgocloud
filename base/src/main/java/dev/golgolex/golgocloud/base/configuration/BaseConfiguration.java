package dev.golgolex.golgocloud.base.configuration;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.configuration.ConfigurationClass;
import dev.golgolex.golgocloud.common.SyncMode;
import dev.golgolex.quala.common.json.JsonDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BaseConfiguration extends ConfigurationClass {

    public BaseConfiguration(@NotNull File configurationDirectory) {
        super("base", configurationDirectory);
    }

    @Override
    public JsonDocument defaultConfiguration() {
        return new JsonDocument()
                .write("base-directory-path", CloudBase.instance().baseDirectory().getAbsolutePath())
                .write("cloud-sync-function", SyncMode.SERVER_TO_SERVER)
                .write("haste-bin-server", new ArrayList<>(List.of("http://127.0.0.1/")))
                .write("consoleDebug", false)
                .write("nettyDebug", false);
    }

    public Path baseDirectoryPath() {
        return Path.of(this.configuration().readString("base-directory-path"));
    }

    public SyncMode cloudSyncFunction() {
        return this.configuration().readObject("cloud-sync-function", SyncMode.class);
    }

    public boolean consoleDebug() {
        return this.configuration().readBoolean("consoleDebug");
    }

    public boolean nettyDebug() {
        return this.configuration().readBoolean("nettyDebug");
    }
}
