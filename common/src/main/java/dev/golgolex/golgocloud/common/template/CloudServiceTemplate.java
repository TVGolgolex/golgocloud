package dev.golgolex.golgocloud.common.template;

import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class CloudServiceTemplate implements BufferClass {

    private String groupName;
    private String id;
    private boolean enabled;
    private List<UUID> instances;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(groupName);
        codecBuffer.writeString(id);
        codecBuffer.writeBoolean(enabled);
        codecBuffer.writeList(instances, CodecBuffer::writeUniqueId);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        groupName = codecBuffer.readString();
        id = codecBuffer.readString();
        enabled = codecBuffer.readBoolean();
        instances = codecBuffer.readList(new ArrayList<>(), codecBuffer::readUniqueId);
    }
}
