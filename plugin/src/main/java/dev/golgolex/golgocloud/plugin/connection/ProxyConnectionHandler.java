package dev.golgolex.golgocloud.plugin.connection;

import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;

public class ProxyConnectionHandler implements PlayerConnectionHandler<Object, Object, Object, Player> {
    @Override
    public void login(@NotNull Object event, @NotNull Player player, Object... sub) {

    }

    @Override
    public void finalJoin(@NotNull Object event, @NotNull Player player, Object... sub) {

    }

    @Override
    public void logout(@NotNull Object event, @NotNull Player player, Object... sub) {

    }
}
