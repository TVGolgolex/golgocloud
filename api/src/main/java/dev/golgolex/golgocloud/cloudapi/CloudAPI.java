package dev.golgolex.golgocloud.cloudapi;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.golgolex.golgocloud.cloudapi.configuration.NetworkConfiguration;
import dev.golgolex.golgocloud.cloudapi.group.CloudGroupProviderImpl;
import dev.golgolex.golgocloud.cloudapi.instance.CloudInstanceProviderIpl;
import dev.golgolex.golgocloud.cloudapi.network.CloudNetworkProviderImpl;
import dev.golgolex.golgocloud.cloudapi.serverbranding.ServerBrandingServiceImpl;
import dev.golgolex.golgocloud.cloudapi.service.CloudServiceProviderImpl;
import dev.golgolex.golgocloud.cloudapi.template.CloudTemplateProviderImpl;
import dev.golgolex.golgocloud.cloudapi.user.CloudPlayerProviderImpl;
import dev.golgolex.golgocloud.common.MongoConnectionUtil;
import dev.golgolex.golgocloud.common.configuration.ConfigurationService;
import dev.golgolex.golgocloud.common.configuration.packets.CloudConfigurationReplyPacket;
import dev.golgolex.golgocloud.common.configuration.packets.CloudConfigurationRequestPacket;
import dev.golgolex.golgocloud.common.group.CloudGroupProvider;
import dev.golgolex.golgocloud.common.instance.CloudInstanceProvider;
import dev.golgolex.golgocloud.common.network.CloudNetworkProvider;
import dev.golgolex.golgocloud.common.serverbranding.ServerBrandingService;
import dev.golgolex.golgocloud.common.service.CloudServiceProvider;
import dev.golgolex.golgocloud.common.template.CloudTemplateProvider;
import dev.golgolex.golgocloud.common.user.CloudPlayerProvider;
import dev.golgolex.quala.netty5.basic.ChannelIdentity;
import dev.golgolex.quala.netty5.basic.InactiveAction;
import dev.golgolex.quala.netty5.basic.NetworkCodec;
import dev.golgolex.quala.netty5.basic.client.NettyClient;
import dev.golgolex.quala.translation.basic.DefaultTranslationAPI;
import dev.golgolex.quala.translation.basic.MessageRepository;
import dev.golgolex.quala.translation.basic.TranslationAPI;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * The CloudAPI class is the entry point for interacting with the cloud system.
 * It provides access to various components and functionalities of the cloud system.
 */
@Getter
@Accessors(fluent = true)
public class CloudAPI {

    @Getter
    private static volatile CloudAPI instance;
    public static final Pattern IP_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    private final Logger logger;
    private final ConfigurationService configurationService;
    private final CloudNetworkProvider cloudNetworkProvider;
    private final CloudGroupProvider cloudGroupProvider;
    private final CloudServiceProvider cloudServiceProvider;
    private final CloudTemplateProvider cloudTemplateProvider;
    private final CloudInstanceProvider cloudInstanceProvider;
    private final CloudPlayerProvider cloudPlayerProvider;
    private final ServerBrandingService serverBrandingService;
    private final NettyClient nettyClient;
    @ApiStatus.Internal
    private MongoDatabase mongoDatabase;
    private TranslationAPI translationAPI;

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
        this.cloudInstanceProvider = new CloudInstanceProviderIpl();
        this.cloudPlayerProvider = new CloudPlayerProviderImpl();
        this.serverBrandingService = new ServerBrandingServiceImpl();

        this.nettyClient = new NettyClient(identity, InactiveAction.RETRY, NetworkCodec.OSGAN, future -> {
            if (future.isSuccess()) {
                this.logger.log(Level.INFO, "Connection handler successfully connected.");
            } else {
                this.logger.log(Level.SEVERE, "Connection handler failed while connection.");
            }
        }, false);

        this.cloudNetworkProvider.initPacketReceivers(this.nettyClient.packetReceiverManager());
    }

    @ApiStatus.Internal
    public void initialize() {
        this.configurationService.configurationOptional("network").ifPresentOrElse(configurationClass -> {
            var networkConfiguration = (NetworkConfiguration) configurationClass;
            this.nettyClient.connect(networkConfiguration.nettyServerHostname(), networkConfiguration.nettyServerPort());
        }, () -> this.logger.log(Level.SEVERE, "No network configuration found."));

        this.cloudGroupProvider.reloadGroups();
        this.cloudServiceProvider.reloadServices();
        this.cloudTemplateProvider.reloadTemplates();
        this.cloudInstanceProvider.reloadInstances();
        this.cloudPlayerProvider.reloadPlayers();
        this.serverBrandingService.reloadStyles();

        CloudConfigurationReplyPacket replyPacket = this.nettyClient.thisNetworkChannel().sendQuery(new CloudConfigurationRequestPacket("database"));

        var mongoConfiguration = replyPacket.configuration().readJsonDocument("mongodb");
        var collectionsConfiguration = mongoConfiguration.readJsonDocument("collections");

        this.mongoDatabase = MongoConnectionUtil.open("cloud-root",
                mongoConfiguration.readString("user"),
                mongoConfiguration.readString("password"),
                mongoConfiguration.readString("host"),
                mongoConfiguration.readInteger("port"),
                mongoConfiguration.readString("database"),
                mongoConfiguration.readString("userDatabaseName"));

        this.translationAPI = new DefaultTranslationAPI(this.mongoDatabase.getCollection(collectionsConfiguration.readString("translation")));
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
