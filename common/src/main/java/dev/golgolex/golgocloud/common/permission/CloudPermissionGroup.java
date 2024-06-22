package dev.golgolex.golgocloud.common.permission;

import dev.golgolex.quala.common.json.JsonDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class CloudPermissionGroup extends CloudPermissible {

    private String name;

    public CloudPermissionGroup(UUID id, List<Permission> permissions, JsonDocument meta, String name) {
        super(id, permissions, meta);
        this.name = name;
    }


    @Override
    public PermissionCheckResult hasPermission(@NotNull String permission) {
        return null;
    }

    @Override
    public void addPermission(@NotNull String permission) {

    }

    @Override
    public void removePermission(@NotNull String permission) {

    }
}
