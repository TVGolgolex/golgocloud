package dev.golgolex.golgocloud.common.service.environment;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

public class CloudServerService extends CloudService {

    @Override
    public void writeBufferService(@NotNull CodecBuffer codecBuffer) {
    }

    @Override
    public void readBufferService(@NotNull CodecBuffer codecBuffer) {
    }
}
