package dev.golgolex.golgocloud.cloudapi.serverbranding;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.serverbranding.ServerBrandStyle;
import dev.golgolex.golgocloud.common.serverbranding.ServerBrandingService;
import dev.golgolex.golgocloud.common.serverbranding.packets.ServerBrandRequestPacket;
import dev.golgolex.golgocloud.common.serverbranding.packets.ServerBrandsReplyPacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class ServerBrandingServiceImpl implements ServerBrandingService {

    private final List<ServerBrandStyle> loadedBrands = new ArrayList<>();

    @Override
    public ServerBrandStyle brandStyle(@NotNull String name) {
        return this.loadedBrands.stream().filter(serverBrandStyle -> serverBrandStyle.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public ServerBrandStyle brandStyle(@NotNull UUID uniqueId) {
        return this.loadedBrands.stream().filter(serverBrandStyle -> serverBrandStyle.uuid().equals(uniqueId)).findFirst().orElse(null);
    }

    @Override
    public ServerBrandStyle anyDefault() {
        return this.loadedBrands.stream().filter(ServerBrandStyle::fallback).findAny().orElse(null);
    }

    @Override
    public void reloadStyles() {
        this.loadedBrands.clear();
        ServerBrandsReplyPacket replyPacket = CloudAPI.instance().nettyClient().thisNetworkChannel().sendQuery(new ServerBrandRequestPacket());
        this.loadedBrands.addAll(replyPacket.serverBrandStyles());
    }

    @Override
    public void updateStyle(@NotNull ServerBrandStyle serverBrandStyle) {

    }
}
