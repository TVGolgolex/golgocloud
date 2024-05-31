package dev.golgolex.golgocloud.base.instance;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.base.configuration.InstanceConfiguration;
import dev.golgolex.quala.event.EventTarget;
import dev.golgolex.quala.netty5.event.ServerChannelAuthorizieEvent;
import dev.golgolex.quala.netty5.event.ServerChannelInactiveEvent;
import lombok.AllArgsConstructor;

import java.net.InetSocketAddress;
import java.util.logging.Level;

@AllArgsConstructor
public final class InstanceEventHandler {

    private final CloudInstanceProviderImpl instanceService;

    @EventTarget
    public void onClientConnect(ServerChannelAuthorizieEvent event) {
        var clientAddress = ((InetSocketAddress) event.ctx().channel().remoteAddress()).getAddress().getHostAddress();

        if (CloudBase.instance().serviceProvider().cloudService(event.networkChannel().channelIdentity().namespace()).isPresent()) {
            return;
        }

        CloudBase.instance().configurationService().configurationOptional("instance").ifPresentOrElse(configurationClass -> {
            var instanceConfiguration = (InstanceConfiguration) configurationClass;
            instanceConfiguration.ipWhitelist().stream().filter(s -> s.equalsIgnoreCase(clientAddress)).findFirst()
                    .ifPresentOrElse(s -> this.instanceService.allowConnection(clientAddress),
                            () -> CloudBase.instance().logger().log(Level.WARNING, "A Netty connection was requested and the IP: '" + clientAddress + "' is not whitelisted."));
        }, () -> {
            throw new IllegalStateException("Instance configuration not found");
        });
    }

    @EventTarget
    public void onClientShutdown(ServerChannelInactiveEvent event) {
        this.instanceService.cloudInstances().stream().filter(cloudInstance -> cloudInstance.uuid().equals(event.networkChannel().channelIdentity().uniqueId()))
                .findFirst()
                .ifPresentOrElse(this.instanceService::disconnect,
                        () -> CloudBase.instance().logger().log(Level.WARNING, "A Netty connection was cancelled although it was not connected. (" + event.networkChannel().toString() + ")"));
    }

}
