package dev.golgolex.golgocloud.common.group.platform;

import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public final class PlatformVersion implements BufferClass {
    private String version;
    private boolean proxy;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(version);
        codecBuffer.writeBoolean(proxy);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        version = codecBuffer.readString();
        proxy = codecBuffer.readBoolean();
    }
}
