package dev.golgolex.golgocloud.common.permission;

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
public final class Permission implements BufferClass {

    private String value;
    private boolean state;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(value);
        codecBuffer.writeBoolean(state);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        value = codecBuffer.readString();
        state = codecBuffer.readBoolean();
    }
}