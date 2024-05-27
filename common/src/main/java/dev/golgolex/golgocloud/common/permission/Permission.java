package dev.golgolex.golgocloud.common.permission;

import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class Permission implements BufferClass {
    private String value;
    private boolean state;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {

    }
}