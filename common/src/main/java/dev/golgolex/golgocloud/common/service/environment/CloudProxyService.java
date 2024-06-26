package dev.golgolex.golgocloud.common.service.environment;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.ServiceEnvironment;
import dev.golgolex.golgocloud.common.service.ServiceLifeCycle;
import dev.golgolex.golgocloud.common.user.ServicePlayer;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class CloudProxyService extends CloudService {

    public CloudProxyService(String id, int serviceNumber, UUID uuid, String gameId, ServiceEnvironment environment, ServiceLifeCycle lifeCycle, String group, UUID instance, String template, String path, String host, int port, boolean ready, long memory, int maxPlayers, List<ServicePlayer> online, List<ServicePlayer> connected) {
        super(id, serviceNumber, uuid, gameId, environment, lifeCycle, group, instance, template, path, host, port, ready, memory, maxPlayers, online, connected);
    }

    public CloudProxyService() {
    }

    @Override
    public void writeBufferService(@NotNull CodecBuffer codecBuffer) {
    }

    @Override
    public void readBufferService(@NotNull CodecBuffer codecBuffer) {
    }
}
