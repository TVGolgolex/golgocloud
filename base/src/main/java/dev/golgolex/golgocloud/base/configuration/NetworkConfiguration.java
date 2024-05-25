package dev.golgolex.golgocloud.base.configuration;

import dev.golgolex.golgocloud.common.configuration.ConfigurationClass;
import dev.golgolex.quala.json.document.JsonDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class NetworkConfiguration extends ConfigurationClass {

    public NetworkConfiguration(@NotNull File configurationDirectory) {
        super("network", configurationDirectory);
    }

    @Override
    public JsonDocument defaultConfiguration() {
        return new JsonDocument()
                .write("netty-server-hostname", "127.0.0.1")
                .write("netty-server-port", 8087)
                ;
    }

    public String nettyServerHostname() {
        return this.configuration().readString("netty-server-hostname");
    }

    public int nettyServerPort() {
        return this.configuration().readInteger("netty-server-port");
    }
}
