package dev.golgolex.golgocloud.base.configuration;

import com.google.common.reflect.TypeToken;
import dev.golgolex.golgocloud.base.permission.defaults.DefaultCloudPermissionPool;
import dev.golgolex.golgocloud.common.configuration.ConfigurationClass;
import dev.golgolex.golgocloud.common.permission.CloudPermissionPool;
import dev.golgolex.quala.common.json.JsonDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PermissionsConfiguration extends ConfigurationClass {

    public PermissionsConfiguration(@NotNull File configurationDirectory) {
        super("permissions", configurationDirectory);
    }

    @Override
    public JsonDocument defaultConfiguration() {
        return new JsonDocument().write("pools", new ArrayList<>(List.of(
                new DefaultCloudPermissionPool()
        )));
    }

    public List<CloudPermissionPool> permissionPools() {
        return this.configuration().readObject("pools", new TypeToken<List<CloudPermissionPool>>() {
        }.getType());
    }

    public void permissionPools(List<CloudPermissionPool> permissionPools) {
        this.configuration().write("pools", permissionPools);
    }
}
