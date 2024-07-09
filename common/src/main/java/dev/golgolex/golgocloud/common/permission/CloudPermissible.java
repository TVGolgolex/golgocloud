package dev.golgolex.golgocloud.common.permission;

import dev.golgolex.quala.common.json.JsonDocument;
import dev.golgolex.quala.netty5.basic.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public abstract class CloudPermissible implements BufferClass {

    private JsonDocument properties;
    private List<CloudPermission> activePermissions;

    public CloudPermissible(JsonDocument properties, List<CloudPermission> activePermissions) {
        this.properties = properties;
        this.activePermissions = activePermissions;
    }

    public CloudPermissible() {
        this.activePermissions = new ArrayList<>();
    }

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeDocument(this.properties);
        codecBuffer.writeList(this.activePermissions, CodecBuffer::writeBufferClass);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        this.properties = codecBuffer.readDocument();
        this.activePermissions = codecBuffer.readList(new ArrayList<>(), () -> codecBuffer.readBufferClass(new CloudPermission()));
    }

    public CloudPermission permission(@NotNull String permissionKey) {
        return this.activePermissions.stream().filter(cloudPermission -> cloudPermission.permissionKey().equalsIgnoreCase(permissionKey)).findFirst().orElse(null);
    }

    public void addPermission(@NotNull CloudPermission permission) {
        if (this.activePermissions.stream().anyMatch(cloudPermission -> cloudPermission.equals(permission))) {
            this.activePermissions.removeIf(cloudPermission -> cloudPermission.equals(permission));
        }
        this.activePermissions.add(permission);
    }

    public void removePermission(@NotNull CloudPermission permission) {
        this.activePermissions.removeIf(cloudPermission -> cloudPermission.equals(permission));
    }

    @ApiStatus.Internal
    public PermissionCheckResult permissionCheckResult(@NotNull CloudPermission permission) {
        return this.permissionCheckResult(permission.permissionKey());
    }

    @ApiStatus.Internal
    public PermissionCheckResult permissionCheckResult(@NotNull String permission) {
        for (var activePermission : this.activePermissions) {
            if (activePermission.permissionKey().equalsIgnoreCase(permission)) {
                return PermissionCheckResult.ALLOWED;
            }
        }
        return PermissionCheckResult.DENY;
    }
}