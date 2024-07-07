package dev.golgolex.golgocloud.common.permission;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface CloudPermissionService {

    List<CloudPermissionPool> permissionPools();

    default CloudPermissionPool permissionPool(@NotNull String id) {
        return this.permissionPools()
                .stream()
                .filter(permissionPool -> permissionPool.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    default CloudPermissionPool permissionPool(@NotNull UUID uniqueId) {
        return this.permissionPools()
                .stream()
                .filter(permissionPool -> permissionPool.uuid().equals(uniqueId))
                .findFirst()
                .orElse(null);
    }

    void reload();

    void reload(@NotNull CloudPermissionPool permissionPool);

    void createPermissionPool(@NotNull CloudPermissionPool permissionPool);

    void deletePermissionPool(@NotNull CloudPermissionPool permissionPool);

    void updatePermissionPool(@NotNull CloudPermissionPool permissionPool);

    void updatePermissible(@NotNull CloudPermissionPool permissionPool, @NotNull CloudPermissibleGroup permissibleGroup);

    void updatePermissible(@NotNull CloudPermissionPool permissionPool, @NotNull CloudPermissibleEntity permissibleEntity);

}
