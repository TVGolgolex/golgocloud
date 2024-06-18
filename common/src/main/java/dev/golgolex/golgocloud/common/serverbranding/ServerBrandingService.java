package dev.golgolex.golgocloud.common.serverbranding;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface ServerBrandingService {

    List<ServerBrandStyle> loadedBrands();

    ServerBrandStyle brandStyle(@NotNull String name);

    ServerBrandStyle brandStyle(@NotNull UUID uniqueId);

    ServerBrandStyle anyDefault();

    void reloadStyles();

    void updateStyle(@NotNull ServerBrandStyle serverBrandStyle);

}
