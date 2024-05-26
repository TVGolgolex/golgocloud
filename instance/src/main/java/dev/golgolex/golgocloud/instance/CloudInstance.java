package dev.golgolex.golgocloud.instance;

import dev.golgolex.golgocloud.common.FileHelper;
import dev.golgolex.golgocloud.common.configuration.ConfigurationClass;
import dev.golgolex.golgocloud.common.configuration.ConfigurationService;
import dev.golgolex.golgocloud.common.instance.packet.InstanceAuthPacket;
import dev.golgolex.golgocloud.common.instance.packet.InstanceUpdatePacket;
import dev.golgolex.golgocloud.common.service.CloudServiceFactory;
import dev.golgolex.golgocloud.instance.configuration.InstanceConfiguration;
import dev.golgolex.golgocloud.instance.configuration.NetworkConfiguration;
import dev.golgolex.golgocloud.instance.group.CloudGroupProviderImpl;
import dev.golgolex.golgocloud.instance.network.CloudNetworkProviderImpl;
import dev.golgolex.golgocloud.instance.service.CloudServiceProviderImpl;
import dev.golgolex.golgocloud.instance.version.CloudServiceVersionProvider;
import dev.golgolex.golgocloud.logger.Logger;
import dev.golgolex.quala.Quala;
import dev.golgolex.quala.netty5.ChannelIdentity;
import dev.golgolex.quala.netty5.InactiveAction;
import dev.golgolex.quala.netty5.NetworkCodec;
import dev.golgolex.quala.netty5.client.NettyClient;
import dev.golgolex.quala.utils.color.ConsoleColor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public class CloudInstance {

    @Getter
    private static CloudInstance instance;
    private final File instanceDirectory;
    private final Logger logger;
    private final ConfigurationService configurationService;
    private final CloudNetworkProviderImpl networkProvider;
    private final CloudGroupProviderImpl groupProvider;
    private final CloudServiceVersionProvider serviceVersionProvider;
    private final CloudServiceProviderImpl serviceProvider;
    private UUID instanceId;
    private NettyClient nettyClient;
    private boolean osLinux;
    @Setter
    private dev.golgolex.golgocloud.common.instance.CloudInstance cloudInstance = null;
    private int connectionTry;

    public CloudInstance() throws IOException, NoSuchFieldException, IllegalAccessException {
        instance = this;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> this.shutdown(true)));

        // init base directory
        this.instanceDirectory = new File("cloud-instance");
        if (!instanceDirectory.exists()) {
            var ignore = instanceDirectory.mkdirs();
        }

        // init log directory
        var logDirectory = new File(this.instanceDirectory, "logs");
        if (!logDirectory.exists()) {
            boolean ignore = logDirectory.mkdirs();
        }

        this.logger = new Logger(logDirectory);
        this.configurationService = new ConfigurationService(this.instanceDirectory);
        this.networkProvider = new CloudNetworkProviderImpl();
        this.groupProvider = new CloudGroupProviderImpl();
        this.serviceVersionProvider = new CloudServiceVersionProvider(this.instanceDirectory);
        this.serviceProvider = new CloudServiceProviderImpl(this.instanceDirectory);

        this.configurationService.addConfiguration(
                new InstanceConfiguration(this.configurationService.configurationDirectory()),
                new NetworkConfiguration(this.configurationService.configurationDirectory())
        );

        this.configurationService.configurationOptional("instance").ifPresentOrElse(configurationClass -> {
            var instanceConfiguration = (InstanceConfiguration) configurationClass;
            this.instanceId = instanceConfiguration.instanceId();
            this.nettyClient = new NettyClient(
                    new ChannelIdentity("Instance-" + instanceConfiguration.instanceId(), instanceConfiguration.instanceId()),
                    InactiveAction.RETRY,
                    NetworkCodec.OSGAN,
                    future -> {
                        if (future.isSuccess()) {
                            this.logger.log(Level.INFO, "Connection handler successfully connected.");
                        } else {
                            this.logger.log(Level.SEVERE, "Connection handler failed while connection.");
                        }
                    },
                    false
            );
        }, () -> {
            this.logger.log(Level.SEVERE, "No instance configuration found.");
            this.nettyClient = null;
        });
        this.networkProvider.initPacketReceivers(this.nettyClient.packetReceiverManager());

        this.logger.log(Level.INFO, "");
        this.logger.log(Level.INFO, "    "
                + ConsoleColor.WHITE.ansiCode()
                + "GolgoCloud "
                + ConsoleColor.DARK_GRAY
                + "|"
                + ConsoleColor.DEFAULT.ansiCode()
                + " modern network environment"
                + " ["
                + ConsoleColor.WHITE.ansiCode()
                + CloudInstance.class.getPackage().getImplementationVersion()
                + "]");
        this.logger.log(Level.INFO, "    "
                + "Java » "
                + ConsoleColor.WHITE.ansiCode()
                + System.getProperty("java.version")
                + ConsoleColor.DEFAULT.ansiCode()
                + " | User » " + ConsoleColor.WHITE.ansiCode()
                + System.getProperty("user.name")
                + ConsoleColor.DEFAULT.ansiCode()
                + " | OS » " + ConsoleColor.WHITE.ansiCode()
                + System.getProperty("os.name")
                + ConsoleColor.DEFAULT.ansiCode());
        this.logger.log(Level.INFO, "    "
                + "Instance » "
                + ConsoleColor.WHITE.ansiCode()
                + this.instanceId
                + ConsoleColor.DEFAULT.ansiCode());
        this.logger.log(Level.INFO, "");

        var os = System.getProperty("os.name").toLowerCase();
        this.osLinux = os.contains("nux") || os.contains("nix");

        this.bootstrap();
    }

    public void bootstrap() {
        this.configurationService.configurationOptional("network").ifPresentOrElse(configurationClass -> {
            var networkConfiguration = (NetworkConfiguration) configurationClass;
            this.nettyClient.connect(networkConfiguration.nettyServerHostname(), networkConfiguration.nettyServerPort());
        }, () -> this.logger.log(Level.SEVERE, "No network configuration found."));

        this.configurationService.configurationOptional("instance").ifPresentOrElse(configurationClass -> {
            var instanceConfiguration = (InstanceConfiguration) configurationClass;
            this.nettyClient.thisNetworkChannel().sendPacket(new InstanceAuthPacket(this.instanceId, instanceConfiguration.authKey()));
        }, () -> this.logger.log(Level.SEVERE, "No instance configuration found."));

        while (this.cloudInstance == null) {
            if (this.connectionTry > 9) {
                this.shutdown(false);
                return;
            }
            Quala.sleepUninterruptedly(1000);
            this.connectionTry++;
        }

        this.groupProvider.reloadGroups();

        Quala.sleepUninterruptedly(1000);

        this.cloudInstance.ready(true);
        this.nettyClient.thisNetworkChannel().sendPacket(new InstanceUpdatePacket(this.cloudInstance));
    }

    public void reload() {
        for (var configuration : this.configurationService.configurations()) {
            configuration.reload();
        }
        this.groupProvider.reloadGroups();
    }

    public void shutdown(boolean shutdownCycle) {
        for (var serviceFactory : this.serviceProvider.serviceFactories()) {
            serviceFactory.terminate();
        }

        FileHelper.deleteDirectory(new File(this.instanceDirectory, "running/dynamic"));

        try {
            this.nettyClient.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.logger.shutdownAll();
        if (!shutdownCycle) {
            System.exit(0);
        }
    }
}