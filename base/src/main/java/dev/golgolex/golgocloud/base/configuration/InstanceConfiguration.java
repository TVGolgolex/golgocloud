package dev.golgolex.golgocloud.base.configuration;

import com.google.common.reflect.TypeToken;
import dev.golgolex.golgocloud.common.configuration.ConfigurationClass;
import dev.golgolex.golgocloud.common.instance.CloudInstance;
import dev.golgolex.quala.json.document.JsonDocument;
import dev.golgolex.quala.utils.string.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InstanceConfiguration extends ConfigurationClass {

    public InstanceConfiguration(@NotNull File configurationDirectory) {
        super("instance", configurationDirectory);
    }

    @Override
    public JsonDocument defaultConfiguration() {
        return new JsonDocument()
                .write("authKey", StringUtils.generateRandomString(65))
                .write("authenticated", new ArrayList<UUID>())
                .write("ip-whitelist", new ArrayList<>())
                .write("instances", new ArrayList<>())
                ;
    }

    public String authKey() {
        return this.configuration().readString("authKey");
    }

    public List<String> authenticated() {
        return this.configuration().readObject("authenticated", new TypeToken<List<UUID>>() {
        }.getType());
    }

    public List<String> ipWhitelist() {
        return this.configuration().readObject("ip-whitelist", new TypeToken<List<String>>() {
        }.getType());
    }

    public List<CloudInstance> instances() {
        return this.configuration().readObject("instances", new TypeToken<List<CloudInstance>>() {
        }.getType());
    }

    public void authenticated(List<String> list) {
        this.configuration().write("authenticated", list);
    }

    public void instances(List<CloudInstance> list) {
        this.configuration().write("instances", list);
    }

    public void ipWhitelist(List<String> list) {
        this.configuration().write("ip-whitelist", list);
    }
}
