package dev.golgolex.golgocloud.common.permission;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Permission {
    private String value;
    private boolean state;
}