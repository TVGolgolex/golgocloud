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
public class CloudPlayerLogoutPacket extends AbstractCloudPlayerPacket {

    private final CloudService cloudService;

    public CloudPlayerLogoutPacket(@NotNull CloudPlayer cloudPlayer, CloudService cloudService) {
        super(cloudPlayer);
        this.cloudService = cloudService;
        buffer.writeEnum(this.cloudService.environment());
        this.cloudService.writeBuffer(buffer);
    }

    public CloudPlayerLogoutPacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudService = CloudService.constructEnrvionment(buffer.readEnum(ServiceEnvironment.class));
        this.cloudService.readBuffer(buffer);
    }
}
