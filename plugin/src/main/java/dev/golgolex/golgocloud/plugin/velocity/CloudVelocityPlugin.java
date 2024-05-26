package dev.golgolex.golgocloud.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;

@Getter
@Plugin(
        id = "cloudvelocityplugin",
        authors = "GolgolexTV",
        name = "CloudPlugin",
        version = "1.0",
        url = "https://mario-kurz.de/"
)
public class CloudVelocityPlugin {

    private ProxyServer proxyServer;

    @Inject
    private CloudVelocityPlugin(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

}