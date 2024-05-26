package dev.golgolex.golgocloud.common.service.environment;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.ServiceEnvironment;
import dev.golgolex.golgocloud.common.user.ServicePlayer;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class CloudServerService extends CloudService {

    public CloudServerService(
            String id,
            int serviceNumber,
            UUID uuid,
            String gameId,
            String group,
            ServiceEnvironment environment,
            String host,
            int port,
            boolean ready,
            long memory,
            int maxPlayers,
            List<ServicePlayer> online,
            List<ServicePlayer> connected
    ) {
        super(
                id,
                serviceNumber,
                uuid,
                gameId,
                group,
                environment,
                host,
                port,
                ready,
                memory,
                maxPlayers,
                online,
                connected
        );
    }

    public CloudServerService() {
    }

    @Override
    public void writeBufferService(@NotNull CodecBuffer codecBuffer) {
    }

    @Override
    public void readBufferService(@NotNull CodecBuffer codecBuffer) {
    }
}
