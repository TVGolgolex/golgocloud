package dev.golgolex.golgocloud.cloudapi.configuration;

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
                .write("base-netty-server-hostname", "127.0.0.1")
                .write("base-netty-server-port", 8087);
    }

    public String nettyServerHostname() {
        return this.configuration().readString("base-netty-server-hostname");
    }

    public int nettyServerPort() {
        return this.configuration().readInteger("base-netty-server-port");
    }
}
