package dev.golgolex.golgocloud.common.service.packets;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.ServiceEnvironment;
import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class AbstractCloudServicePacket extends Packet {

    private final CloudService cloudService;

    /**
     * Represents an abstract class for a packet related to a cloud service.
     */
    public AbstractCloudServicePacket(@NotNull CloudService cloudService) {
        this.cloudService = cloudService;
        buffer.writeEnum(cloudService.environment());
        cloudService.writeBuffer(buffer);
    }

    /**
     * This method creates a new instance of AbstractCloudServicePacket.
     * It initializes the superclass using the given CodecBuffer.
     * It constructs the CloudService based on the provided ServiceEnvironment.
     * If the CloudService is not null, it reads the buffer data for the CloudService.
     *
     * @param buffer the CodecBuffer used to initialize the superclass and read the buffer data
     */
    public AbstractCloudServicePacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudService = CloudService.constructEnrvionment(buffer.readEnum(ServiceEnvironment.class));
        if (this.cloudService != null) {
            this.cloudService.readBuffer(buffer);
        }
    }
}
