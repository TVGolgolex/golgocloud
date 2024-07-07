

package dev.golgolex.golgocloud.base.permission.defaults;

import dev.golgolex.golgocloud.common.permission.CloudPermissibleGroup;
import dev.golgolex.quala.common.json.JsonDocument;

import java.util.ArrayList;

public final class DefaultUserCloudPermissibleGroup extends CloudPermissibleGroup {

    public DefaultUserCloudPermissibleGroup() {
        super(new JsonDocument(), new ArrayList<>(), "user", new ArrayList<>(), true);
        properties().write("prefix$", "<color:#9097a0>User</color> <dark_gray>|</dark_gray>");
        properties().write("suffix$", "");
        properties().write("color$", "#9097a0");
        properties().write("color$$", "gray");
    }
}
