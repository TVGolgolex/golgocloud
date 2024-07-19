package dev.golgolex.golgocloud.common.permission;

import dev.golgolex.quala.common.json.JsonDocument;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public  class CloudPermissibleGroup extends CloudPermissible {

    private String name;
    private List<String> implementedGroups;
    private boolean fallbackOption;

    public CloudPermissibleGroup(JsonDocument properties,
                                 List<CloudPermission> activePermissions,
                                 String name,
                                 List<String> implementedGroups,
                                 boolean fallbackOption) {
        super(properties, activePermissions);
        this.name = name;
        this.implementedGroups = implementedGroups;
        this.fallbackOption = fallbackOption;
    }

    public CloudPermissibleGroup() {
    }

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        super.writeBuffer(codecBuffer);
        codecBuffer.writeString(this.name);
        codecBuffer.writeList(this.implementedGroups, CodecBuffer::writeString);
        codecBuffer.writeBoolean(this.fallbackOption);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        super.readBuffer(codecBuffer);
        this.name = codecBuffer.readString();
        this.implementedGroups = codecBuffer.readList(new ArrayList<>(), codecBuffer::readString);
        this.fallbackOption = codecBuffer.readBoolean();
    }

    public PermissionCheckResult hasPermission(@NotNull CloudPermissionPool permissionPool, @NotNull String permission) {
        if (this.permissionCheckResult("*").equals(PermissionCheckResult.ALLOWED)) {
            return PermissionCheckResult.ALLOWED;
        }

        for (var permissibleGroup : permissionPool.permissibleGroups()) {
            for (var implementedGroup : this.implementedGroups) {
                if (implementedGroup.equalsIgnoreCase(permissibleGroup.name())) {
                    if (permissibleGroup.hasPermission(permissionPool, permission).equals(PermissionCheckResult.ALLOWED)) {
                        return PermissionCheckResult.ALLOWED;
                    }
                }
            }
        }
        return this.permissionCheckResult(permission);
    }
}
