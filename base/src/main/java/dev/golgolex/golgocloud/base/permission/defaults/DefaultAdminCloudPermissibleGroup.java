package dev.golgolex.golgocloud.base.permission.defaults;

import dev.golgolex.golgocloud.common.permission.CloudPermissibleGroup;
import dev.golgolex.golgocloud.common.permission.CloudPermission;
import dev.golgolex.quala.common.json.JsonDocument;

import java.util.ArrayList;

public final class DefaultAdminCloudPermissibleGroup extends CloudPermissibleGroup {

    public DefaultAdminCloudPermissibleGroup() {
        super(new JsonDocument(), new ArrayList<>(), "admin", new ArrayList<>(), true);
        properties().write("prefix$", "<color:#980c23>Admin</color> <dark_gray>|</dark_gray>");
        properties().write("suffix$", "");
        properties().write("color$", "#980c23");
        properties().write("color$$", "gray");
        addPermission(new CloudPermission("*", System.currentTimeMillis(), -1L));
    }
}
