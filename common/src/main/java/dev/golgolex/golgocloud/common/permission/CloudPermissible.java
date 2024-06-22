package dev.golgolex.golgocloud.common.permission;

import dev.golgolex.quala.common.json.JsonDocument;
import dev.golgolex.quala.netty5.basic.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class CloudPermissible implements BufferClass {

    private UUID id;
    private List<Permission> permissions;
    private JsonDocument meta;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeUniqueId(id);
        codecBuffer.writeList(permissions, (it, permission) -> permission.writeBuffer(it));
        codecBuffer.writeDocument(meta);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        id = codecBuffer.readUniqueId();
        permissions = codecBuffer.readList(new ArrayList<>(), () -> {
            var permission = new Permission();
            permission.readBuffer(codecBuffer);
            return permission;
        });
        meta = codecBuffer.readDocument();
    }

    public boolean containsPermission(@NotNull String permission) {
        return this.permissions.stream().anyMatch(it -> it.value().equalsIgnoreCase(permission));
    }

    public abstract PermissionCheckResult hasPermission(@NotNull String permission);

    public abstract void addPermission(@NotNull String permission);

    public abstract void removePermission(@NotNull String permission);
}