package dev.golgolex.golgocloud.common.template;

import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

public class CloudTemplate implements BufferClass {

    private String groupName;
    private String id;

    private boolean enabled;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(groupName);
        codecBuffer.writeString(id);
        codecBuffer.writeBoolean(enabled);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        groupName = codecBuffer.readString();
        id = codecBuffer.readString();
        enabled = codecBuffer.readBoolean();
    }
}
