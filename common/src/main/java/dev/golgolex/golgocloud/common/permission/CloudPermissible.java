package dev.golgolex.golgocloud.common.permission;

import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public abstract class CloudPermissible implements BufferClass {
    
    private UUID id;
    private List<Permission> permissions = new ArrayList<>();

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeUniqueId(id);
        codecBuffer.writeList(permissions, (it, permission) -> permission.writeBuffer(it));
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        this.id = codecBuffer.readUniqueId();
        this.permissions = codecBuffer.readList(new ArrayList<>(), () -> {
            var permission = new Permission();
            permission.readBuffer(codecBuffer);
            return permission;
        });
    }
}