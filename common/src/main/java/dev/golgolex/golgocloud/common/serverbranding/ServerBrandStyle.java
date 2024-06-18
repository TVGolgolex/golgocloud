package dev.golgolex.golgocloud.common.serverbranding;

import dev.golgolex.quala.common.json.JsonDocument;
import dev.golgolex.quala.netty5.basic.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Accessors(fluent = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerBrandStyle implements BufferClass {

    private String name;
    private UUID uuid;
    private JsonDocument properties;
    private long pings;
    private boolean fallback;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(this.name);
        codecBuffer.writeUniqueId(this.uuid);
        codecBuffer.writeDocument(this.properties);
        codecBuffer.writeLong(this.pings);
        codecBuffer.writeBoolean(this.fallback);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        this.name = codecBuffer.readString();
        this.uuid = codecBuffer.readUniqueId();
        this.properties = codecBuffer.readDocument();
        this.pings = codecBuffer.readLong();
        this.fallback = codecBuffer.readBoolean();
    }
}