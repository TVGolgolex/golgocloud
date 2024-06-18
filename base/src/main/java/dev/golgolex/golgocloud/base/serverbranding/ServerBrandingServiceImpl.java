package dev.golgolex.golgocloud.base.serverbranding;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.base.configuration.ServerBrandingConfiguration;
import dev.golgolex.golgocloud.common.serverbranding.ServerBrandStyle;
import dev.golgolex.golgocloud.common.serverbranding.ServerBrandingService;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        CloudBase.instance().configurationService().configurationOptional("server-branding").ifPresentOrElse(configurationClass -> {
            var serverBrandingConfiguration = (ServerBrandingConfiguration) configurationClass;
            this.loadedBrands.addAll(serverBrandingConfiguration.serverBrandStyles());
            CloudBase.instance().logger().info("Loaded following groups: &3" + this.loadedBrands.stream()
                    .map(ServerBrandStyle::name)
                    .collect(Collectors.joining("&2, &3")));
        }, () -> CloudBase.instance().logger().error("No styles configuration found.", null));
    }

    @Override
    public void updateStyle(@NotNull ServerBrandStyle serverBrandStyle) {

    }
}
