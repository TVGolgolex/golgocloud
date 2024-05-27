package dev.golgolex.golgocloud.common.service.environment;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.ServerState;
import dev.golgolex.golgocloud.common.service.ServiceEnvironment;
import dev.golgolex.golgocloud.common.service.ServiceLifeCycle;
import dev.golgolex.golgocloud.common.user.ServicePlayer;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Accessors(fluent = true)
public class CloudServerService extends CloudService {

    private ServerState serverState;

    public CloudServerService(String id, int serviceNumber, UUID uuid, String gameId, ServiceEnvironment environment, ServiceLifeCycle lifeCycle, String group, UUID instance, String template, String path, String host, int port, boolean ready, long memory, int maxPlayers, List<ServicePlayer> online, List<ServicePlayer> connected, ServerState serverState) {
        super(id, serviceNumber, uuid, gameId, environment, lifeCycle, group, instance, template, path, host, port, ready, memory, maxPlayers, online, connected);
        this.serverState = serverState;
    }

    public CloudServerService() {
    }

    @Override
    public void writeBufferService(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeEnum(serverState);
    }

    @Override
    public void readBufferService(@NotNull CodecBuffer codecBuffer) {
        serverState = codecBuffer.readEnum(ServerState.class);
    }
}
