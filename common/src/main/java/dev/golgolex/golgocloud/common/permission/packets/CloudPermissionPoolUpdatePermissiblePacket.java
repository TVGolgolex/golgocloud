package dev.golgolex.golgocloud.common.permission.packets;

import dev.golgolex.golgocloud.common.permission.CloudPermissible;
import dev.golgolex.golgocloud.common.permission.CloudPermissibleEntity;
import dev.golgolex.golgocloud.common.permission.CloudPermissibleGroup;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class CloudPermissionPoolUpdatePermissiblePacket extends AbstractCloudPermissionPoolPacket{

    private final CloudPermissible cloudPermissible;

    public CloudPermissionPoolUpdatePermissiblePacket(@NotNull String id, @NotNull UUID uuid, CloudPermissible cloudPermissible) {
        super(id, uuid);
        this.cloudPermissible = cloudPermissible;

        if (cloudPermissible instanceof CloudPermissibleGroup cloudPermissibleGroup) {
            buffer.writeInt(1);
            buffer.writeBufferClass(cloudPermissibleGroup);
        }

        if (cloudPermissible instanceof CloudPermissibleEntity cloudPermissibleEntity) {
            buffer.writeInt(2);
            buffer.writeBufferClass(cloudPermissibleEntity);
        }
    }

    public CloudPermissionPoolUpdatePermissiblePacket(CodecBuffer buffer) {
        super(buffer);

        CloudPermissible permissible = null;
        var id = buffer.readInt();

        if (id == 1) {
            permissible = buffer.readBufferClass(new CloudPermissibleGroup());
        }

        if (id == 2) {
            permissible = buffer.readBufferClass(new CloudPermissibleEntity());
        }

        cloudPermissible = permissible;
    }
}
