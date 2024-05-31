package dev.golgolex.golgocloud.common.configuration.packets;

import dev.golgolex.quala.json.document.JsonDocument;
import dev.golgolex.quala.netty5.protocol.Packet;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CloudConfigurationReplyPacket extends Packet {

    private final JsonDocument configuration;

    public CloudConfigurationReplyPacket(JsonDocument configuration) {
        this.configuration = configuration;
        this.buffer.writeBoolean(this.configuration != null);
        if (this.configuration != null) {
            this.buffer.writeDocument(this.configuration);
        }
    }

    public CloudConfigurationReplyPacket(CodecBuffer buffer) {
        super(buffer);
        if (this.buffer.readBoolean()) {
            this.configuration = buffer.readDocument();
        } else {
            this.configuration = null;
        }
    }
}
