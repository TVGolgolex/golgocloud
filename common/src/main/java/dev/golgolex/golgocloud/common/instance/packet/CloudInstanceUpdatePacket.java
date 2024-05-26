package dev.golgolex.golgocloud.common.instance.packet;

import dev.golgolex.golgocloud.common.instance.CloudInstance;
import dev.golgolex.quala.netty5.protocol.Packet;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class CloudInstanceUpdatePacket extends Packet {

    private final CloudInstance cloudInstance;

    /**
     * Represents a packet used to update an instance of CloudInstance.
     */
    public CloudInstanceUpdatePacket(CloudInstance cloudInstance) {
        this.cloudInstance = cloudInstance;
        buffer.writeNullable(this.cloudInstance, this.cloudInstance::writeBuffer);
    }

    /**
     * Represents a packet used for updating a specific instance in a cloud.
     *
     * @param buffer The CodecBuffer containing the data to initialize the packet.
     *
     * @see Packet
     */
    public CloudInstanceUpdatePacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudInstance = buffer.readNullable(CloudInstance.class, () -> {
            var instance = new CloudInstance();
            instance.readBuffer(buffer);
            return instance;
        });
    }
}
