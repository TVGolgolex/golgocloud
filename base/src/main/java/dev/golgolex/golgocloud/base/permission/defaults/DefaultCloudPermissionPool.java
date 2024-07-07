package dev.golgolex.golgocloud.base.permission.defaults;

import dev.golgolex.golgocloud.common.permission.CloudPermissionPool;

import java.util.ArrayList;
import java.util.UUID;

public final class DefaultCloudPermissionPool extends CloudPermissionPool {

    public DefaultCloudPermissionPool() {
        super("cloud", UUID.randomUUID(), new ArrayList<>(), new ArrayList<>());
        createPermissible(new DefaultAdminCloudPermissibleGroup());
        createPermissible(new DefaultUserCloudPermissibleGroup());
    }
}