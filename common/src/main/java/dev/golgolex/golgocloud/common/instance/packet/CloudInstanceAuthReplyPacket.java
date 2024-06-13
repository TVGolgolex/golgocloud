package dev.golgolex.golgocloud.common.instance.packet;

import dev.golgolex.golgocloud.common.instance.CloudInstance;
import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public final class CloudInstanceAuthReplyPacket extends Packet {

    private final CloudInstance cloudInstance;

    /**
     * Constructs a new InstanceAuthReplyPacket with the given instance.
     *
     * @param cloudInstance the instance for the reply packet
     */
    public CloudInstanceAuthReplyPacket(CloudInstance cloudInstance) {
        this.cloudInstance = cloudInstance;
        buffer.writeNullable(this.cloudInstance, this.cloudInstance::writeBuffer);
    }

    /**
     * Constructs an InstanceAuthReplyPacket object using the provided CodecBuffer.
     * It reads the instance data from the buffer and initializes the 'instance' field.
     *
     * @param buffer The CodecBuffer containing the instance data.
     */
    public CloudInstanceAuthReplyPacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudInstance = buffer.readNullable(CloudInstance.class, () -> {
            var instance = new CloudInstance();
            instance.readBuffer(buffer);
            return instance;
        });
    }
}
