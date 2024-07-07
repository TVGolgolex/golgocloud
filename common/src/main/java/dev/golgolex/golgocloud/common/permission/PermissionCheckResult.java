package dev.golgolex.golgocloud.common.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@AllArgsConstructor
public enum PermissionCheckResult {

    ALLOWED(true),
    DENY(false);

    private final boolean asBoolean;

    public static PermissionCheckResult fromBoolean(boolean var1) {
        if (var1) return ALLOWED;
        else return DENY;
    }
}