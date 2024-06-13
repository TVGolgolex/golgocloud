package dev.golgolex.golgocloud.common.user;

import dev.golgolex.quala.netty5.basic.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class ServicePlayer implements BufferClass {
    private String name;
    private UUID uuid;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(name);
        codecBuffer.writeNullable(uuid, codecBuffer1 -> codecBuffer1.writeUniqueId(uuid));
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        name = codecBuffer.readString();
        uuid = codecBuffer.readNullable(UUID.class, codecBuffer::readUniqueId);
    }
}
