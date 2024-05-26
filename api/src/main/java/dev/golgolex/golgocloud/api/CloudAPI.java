package dev.golgolex.golgocloud.api;

import dev.golgolex.golgocloud.api.configuration.NetworkConfiguration;
import dev.golgolex.golgocloud.api.group.CloudGroupProviderImpl;
import dev.golgolex.golgocloud.api.network.CloudNetworkProviderImpl;
import dev.golgolex.golgocloud.api.service.CloudServiceProviderImpl;
import dev.golgolex.golgocloud.api.template.CloudTemplateProviderImpl;
import dev.golgolex.golgocloud.common.configuration.ConfigurationService;
import dev.golgolex.golgocloud.common.group.CloudGroupProvider;
import dev.golgolex.golgocloud.common.network.CloudNetworkProvider;
import dev.golgolex.golgocloud.common.service.CloudServiceProvider;
import dev.golgolex.golgocloud.common.template.CloudTemplateProvider;
import dev.golgolex.quala.netty5.ChannelIdentity;
import dev.golgolex.quala.netty5.InactiveAction;
import dev.golgolex.quala.netty5.NetworkCodec;
import dev.golgolex.quala.netty5.client.NettyClient;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The CloudAPI class is the entry point for interacting with the cloud system.
 * It provides access to various components and functionalities of the cloud system.
 */
@Getter
@Accessors(fluent = true)
public class CloudAPI {

    @Getter
    private static volatile CloudAPI instance;

    private final Logger logger;
    private final ConfigurationService configurationService;
    private final CloudNetworkProvider cloudNetworkProvider;
    private final CloudGroupProvider cloudGroupProvider;
    private final CloudServiceProvider cloudServiceProvider;
    private final CloudTemplateProvider cloudTemplateProvider;
    private final NettyClient nettyClient;

    /**
     * The CloudAPI class is the entry point for interacting with the cloud system.
     * It provides access to various components and functionalities of the cloud system.
     */
    public CloudAPI(@NotNull ChannelIdentity identity, @NotNull ConfigurationService configurationService, @NotNull Logger logger) {
        instance = this;

        this.logger = logger;
        this.configurationService = configurationService;
        this.cloudNetworkProvider = new CloudNetworkProviderImpl();
        this.cloudGroupProvider = new CloudGroupProviderImpl();
        this.cloudServiceProvider = new CloudServiceProviderImpl();
        this.cloudTemplateProvider = new CloudTemplateProviderImpl();

        this.nettyClient = new NettyClient(identity, InactiveAction.RETRY, NetworkCodec.OSGAN, future -> {
            if (future.isSuccess()) {
                this.logger.log(Level.INFO, "Connection handler successfully connected.");
            } else {
                this.logger.log(Level.SEVERE, "Connection handler failed while connection.");
            }
        }, false);

        this.cloudNetworkProvider.initPacketReceivers(this.nettyClient.packetReceiverManager());
    }

    public void initialize() {
        this.configurationService.configurationOptional("network").ifPresentOrElse(configurationClass -> {
            var networkConfiguration = (NetworkConfiguration) configurationClass;
            this.nettyClient.connect(networkConfiguration.nettyServerHostname(), networkConfiguration.nettyServerPort());
        }, () -> this.logger.log(Level.SEVERE, "No network configuration found."));
    }

    public void terminate(boolean shutdownCycle) {

        try {
            this.nettyClient.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!shutdownCycle) {
            System.exit(0);
        }
    }
}
