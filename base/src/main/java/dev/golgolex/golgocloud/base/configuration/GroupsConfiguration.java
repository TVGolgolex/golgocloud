package dev.golgolex.golgocloud.base.configuration;

import com.google.gson.reflect.TypeToken;
import dev.golgolex.golgocloud.common.configuration.ConfigurationClass;
import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.quala.common.json.JsonDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupsConfiguration extends ConfigurationClass {

    public GroupsConfiguration(@NotNull File configurationDirectory) {
        super("groups", configurationDirectory);
    }

    @Override
    public JsonDocument defaultConfiguration() {
        return new JsonDocument().write("groups", new ArrayList<CloudGroup>());
    }

    public List<CloudGroup> groups() {
        return this.configuration().readObject("groups", new TypeToken<List<CloudGroup>>() {
        }.getType());
    }

    public void groups(List<CloudGroup> list) {
        this.configuration().write("groups", list);
    }
}
