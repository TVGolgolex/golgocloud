package dev.golgolex.golgocloud.base.configuration;

import com.google.common.reflect.TypeToken;
import dev.golgolex.golgocloud.common.configuration.ConfigurationClass;
import dev.golgolex.golgocloud.common.serverbranding.ServerBrandStyle;
import dev.golgolex.quala.common.json.JsonDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class ServerBrandingConfiguration extends ConfigurationClass {

    public ServerBrandingConfiguration(@NotNull File configurationDirectory) {
        super("server-branding", configurationDirectory);
    }

    @Override
    public JsonDocument defaultConfiguration() {
        return new JsonDocument().write("styles", List.of(new ServerBrandStyle("default", "your-domain.de", UUID.randomUUID(), new JsonDocument().write("custom$", "#f5b72f").write("alternativ$", "&6"), 0L, true)));
    }

    public List<ServerBrandStyle> serverBrandStyles() {
        return this.configuration().readObject("styles", new TypeToken<List<ServerBrandStyle>>() {
        }.getType());
    }

    public void serverBrandStyles(List<ServerBrandStyle> serverBrandStyles) {
        this.configuration().write("styles", serverBrandStyles);
    }
}
