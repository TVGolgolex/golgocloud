package dev.golgolex.golgocloud.common.user.packets;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.ServiceEnvironment;
import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class CloudPlayerTransferredPacket extends AbstractCloudPlayerPacket{

    private final CloudService transferredService;

    public CloudPlayerTransferredPacket(@NotNull CloudPlayer cloudPlayer, @NotNull CloudService transferredService) {
        super(cloudPlayer);
        this.transferredService = transferredService;
        buffer.writeEnum(transferredService.environment());
        this.transferredService.writeBuffer(buffer);
    }

    public CloudPlayerTransferredPacket(CodecBuffer buffer) {
        super(buffer);
        this.transferredService = CloudService.constructEnrvionment(buffer.readEnum(ServiceEnvironment.class));
        if (this.transferredService != null) {
            this.transferredService.readBuffer(buffer);
        }
    }
}
