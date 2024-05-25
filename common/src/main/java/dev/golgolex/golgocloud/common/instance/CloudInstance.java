package dev.golgolex.golgocloud.common.instance;

import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
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
public final class CloudInstance implements BufferClass {

    private String id;
    private UUID uuid;
    private String host;
    private String domain;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(id);
        codecBuffer.writeUniqueId(uuid);
        codecBuffer.writeString(host);
        codecBuffer.writeString(domain);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        id = codecBuffer.readString();
        uuid = codecBuffer.readUniqueId();
        host = codecBuffer.readString();
        domain = codecBuffer.readString();
    }
}
