package dev.golgolex.golgocloud.base;

import dev.golgolex.golgocloud.base.configuration.*;
import dev.golgolex.golgocloud.base.group.CloudGroupProviderImpl;
import dev.golgolex.golgocloud.base.instance.CloudInstanceProviderImpl;
import dev.golgolex.golgocloud.base.network.CloudNetworkProviderImpl;
import dev.golgolex.golgocloud.base.service.CloudServiceProviderImpl;
import dev.golgolex.golgocloud.base.service.CloudServiceWorkerThread;
import dev.golgolex.golgocloud.base.template.CloudTemplateProviderImpl;
import dev.golgolex.golgocloud.base.user.CloudPlayerProviderImpl;
import dev.golgolex.golgocloud.common.configuration.ConfigurationService;
import dev.golgolex.golgocloud.common.threading.Scheduler;
import dev.golgolex.golgocloud.logger.Logger;
import dev.golgolex.quala.netty5.InactiveAction;
import dev.golgolex.quala.netty5.NetworkCodec;
import dev.golgolex.quala.netty5.NetworkUtils;
import dev.golgolex.quala.netty5.server.NettyServer;
import dev.golgolex.quala.utils.color.ConsoleColor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public final class CloudBase {

    @Getter
    private static CloudBase instance;
    private final File baseDirectory;
    private final Logger logger;
    private final ConfigurationService configurationService;
    private final NettyServer nettyServer;
    private final CloudNetworkProviderImpl networkProvider;
    private final CloudInstanceProviderImpl instanceService;
    private final CloudGroupProviderImpl groupProvider;
    private final CloudServiceProviderImpl serviceProvider;
    private final CloudTemplateProviderImpl templateProvider;
    private final CloudPlayerProviderImpl playerProvider;
    private final Scheduler scheduler = new Scheduler(50);

    public CloudBase() throws IOException, NoSuchFieldException, IllegalAccessException {
        instance = this;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> this.shutdown(true)));

        // init base directory
        this.baseDirectory = new File("cloud-base");
        if (!baseDirectory.exists()) {
            var ignore = baseDirectory.mkdirs();
        }

        // init log directory
        var logDirectory = new File(this.baseDirectory, "logs");
        if (!logDirectory.exists()) {
            boolean ignore = logDirectory.mkdirs();
        }

        this.logger = new Logger(logDirectory);
        this.configurationService = new ConfigurationService(this.baseDirectory);
        this.nettyServer = new NettyServer(false, InactiveAction.SHUTDOWN, NetworkCodec.OSGAN, future -> {
            if (future.isSuccess()) {
                this.logger.log(Level.INFO, "Communication handler is connected.");
            } else {
                this.logger.log(Level.SEVERE, "Communication handler failed to connect.", future.cause());
            }
        });
        this.networkProvider = new CloudNetworkProviderImpl();
        this.instanceService = new CloudInstanceProviderImpl();
        this.groupProvider = new CloudGroupProviderImpl();
        this.serviceProvider = new CloudServiceProviderImpl();
        this.templateProvider = new CloudTemplateProviderImpl();
        this.playerProvider = new CloudPlayerProviderImpl(this.baseDirectory);

        this.configurationService.addConfiguration(
                new BaseConfiguration(this.configurationService.configurationDirectory()),
                new NetworkConfiguration(this.configurationService.configurationDirectory()),
                new InstanceConfiguration(this.configurationService.configurationDirectory()),
                new GroupsConfiguration(this.configurationService.configurationDirectory()),
                new TemplateConfiguration(this.configurationService.configurationDirectory())
        );
        this.networkProvider.initPacketReceivers(this.nettyServer.serverChannelTransmitter().packetReceiverManager());

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
                + CloudBase.class.getPackage().getImplementationVersion()
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
        this.logger.log(Level.INFO, "");

        this.bootstrap();
    }

    public void bootstrap() {
        this.configurationService.configurationOptional("network").ifPresentOrElse(configurationClass -> {
            var networkConfiguration = (NetworkConfiguration) configurationClass;
            this.nettyServer.connect(networkConfiguration.nettyServerHostname(), networkConfiguration.nettyServerPort());
        }, () -> this.logger.log(Level.SEVERE, "No network configuration found."));

        this.configurationService.configurationOptional("base").ifPresentOrElse(configurationClass -> {
            var baseConfiguration = (BaseConfiguration) configurationClass;
            this.logger.setDebugging(baseConfiguration.consoleDebug());
            NetworkUtils.DEV_MODE = baseConfiguration.nettyDebug();
            this.logger.log(Level.INFO, "Handling is based on " + ConsoleColor.DARK_GRAY.ansiCode() + "'" + ConsoleColor.AQUA.ansiCode() + baseConfiguration.cloudSyncFunction().name() + ConsoleColor.DARK_GRAY.ansiCode() + "'");
        }, () -> this.logger.log(Level.SEVERE, "No base configuration found."));

        this.groupProvider.reloadGroups();
        this.templateProvider.reloadTemplates();
        this.playerProvider.reloadPlayers();

        var schedulerThread = new Thread(this.scheduler);
        schedulerThread.setDaemon(true);
        schedulerThread.start();

        new CloudServiceWorkerThread(this).init();
    }

    public void reload() {
        for (var configuration : this.configurationService.configurations()) {
            configuration.reload();
        }
        this.groupProvider.reloadGroups();
        this.templateProvider.reloadTemplates();
        this.playerProvider.reloadPlayers();
    }

    public void shutdown(boolean shutdownCycle) {
        try {
            this.nettyServer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.logger.shutdownAll();
        if (!shutdownCycle) {
            System.exit(0);
        }
    }
}
