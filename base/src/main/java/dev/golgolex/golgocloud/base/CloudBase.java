package dev.golgolex.golgocloud.base;

import dev.golgolex.golgocloud.base.commands.ClearCommand;
import dev.golgolex.golgocloud.base.commands.ReloadCommand;
import dev.golgolex.golgocloud.base.commands.StopCommand;
import dev.golgolex.golgocloud.base.configuration.*;
import dev.golgolex.golgocloud.base.group.CloudGroupProviderImpl;
import dev.golgolex.golgocloud.base.instance.CloudInstanceProviderImpl;
import dev.golgolex.golgocloud.base.network.CloudNetworkProviderImpl;
import dev.golgolex.golgocloud.base.permission.CloudPermissionServiceImpl;
import dev.golgolex.golgocloud.base.serverbranding.ServerBrandingServiceImpl;
import dev.golgolex.golgocloud.base.service.CloudServiceCommand;
import dev.golgolex.golgocloud.base.service.CloudServiceProviderImpl;
import dev.golgolex.golgocloud.base.service.CloudServiceWorkerThread;
import dev.golgolex.golgocloud.base.template.CloudTemplateProviderImpl;
import dev.golgolex.golgocloud.base.user.CloudPlayerProviderImpl;
import dev.golgolex.golgocloud.common.CloudShutdownExecutor;
import dev.golgolex.golgocloud.common.configuration.ConfigurationService;
import dev.golgolex.golgocloud.base.configuration.DatabaseConfiguration;
import dev.golgolex.golgocloud.common.threading.Scheduler;
import dev.golgolex.quala.common.utils.color.ConsoleColor;
import dev.golgolex.quala.logger.Logger;
import dev.golgolex.quala.logger.LoggerFactory;
import dev.golgolex.quala.logger.handler.FileLoggerHandler;
import dev.golgolex.quala.logger.handler.LoggerOutPutStream;
import dev.golgolex.quala.netty5.basic.InactiveAction;
import dev.golgolex.quala.netty5.basic.NetworkCodec;
import dev.golgolex.quala.netty5.basic.NetworkUtils;
import dev.golgolex.quala.netty5.basic.server.NettyServer;
import dev.golgolex.quala.terminal.QualaTerminal;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public final class CloudBase {

    @Getter
    private static CloudBase instance;
    private final File baseDirectory;

    private final QualaTerminal cloudTerminal;
    private final LoggerFactory loggerFactory = new LoggerFactory();
    private final Logger logger = new Logger(loggerFactory, false);

    private final ConfigurationService configurationService;
    private final NettyServer nettyServer;
    private final CloudNetworkProviderImpl networkProvider;
    private final CloudInstanceProviderImpl instanceService;
    private final CloudGroupProviderImpl groupProvider;
    private final CloudServiceProviderImpl serviceProvider;
    private final CloudTemplateProviderImpl templateProvider;
    private final CloudPlayerProviderImpl playerProvider;
    private final ServerBrandingServiceImpl serverBrandingService;
    private final CloudPermissionServiceImpl cloudPermissionService;
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

        this.cloudTerminal = new QualaTerminal("&3base&2: &1");
        this.loggerFactory.registerLoggers(new FileLoggerHandler(), this.cloudTerminal);
        System.setErr(new PrintStream(new LoggerOutPutStream(this.logger, true), true, StandardCharsets.UTF_8));
        System.setOut(new PrintStream(new LoggerOutPutStream(this.logger, false), true, StandardCharsets.UTF_8));
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> e.printStackTrace());
        CloudShutdownExecutor.task(() -> shutdown(false));

        this.configurationService = new ConfigurationService(this.baseDirectory);
        this.nettyServer = new NettyServer(false, InactiveAction.SHUTDOWN, NetworkCodec.OSGAN, future -> {
            if (future.isSuccess()) {
                this.logger.success("Communication handler is connected.");
            } else {
                this.logger.error("Communication handler failed to connect. ", future.cause());
            }
        });
        this.networkProvider = new CloudNetworkProviderImpl();
        this.instanceService = new CloudInstanceProviderImpl();
        this.groupProvider = new CloudGroupProviderImpl();
        this.serviceProvider = new CloudServiceProviderImpl();
        this.templateProvider = new CloudTemplateProviderImpl();
        this.playerProvider = new CloudPlayerProviderImpl(this.baseDirectory);
        this.serverBrandingService = new ServerBrandingServiceImpl();
        this.cloudPermissionService = new CloudPermissionServiceImpl();

        this.configurationService.addConfiguration(
                new BaseConfiguration(this.configurationService.configurationDirectory()),
                new NetworkConfiguration(this.configurationService.configurationDirectory()),
                new InstanceConfiguration(this.configurationService.configurationDirectory()),
                new GroupsConfiguration(this.configurationService.configurationDirectory()),
                new TemplateConfiguration(this.configurationService.configurationDirectory()),
                new DatabaseConfiguration(this.configurationService.configurationDirectory()),
                new ServerBrandingConfiguration(this.configurationService.configurationDirectory()),
                new PermissionsConfiguration(this.configurationService.configurationDirectory())
        );
        this.networkProvider.initPacketReceivers(this.nettyServer.serverChannelTransmitter().packetReceiverManager());

        this.cloudTerminal.spacer();
        this.cloudTerminal.spacer("  &3Cloud &1for ClayMC.net &1version&2: &3" + CloudBase.class.getPackage().getImplementationVersion());
        this.cloudTerminal.spacer("  &1Java&2: &3" + System.getProperty("java.version") + " &2- &1User&2: &3" + System.getProperty("user.name") + " &2- &1OS &2: &3" + System.getProperty("os.name"));
        this.cloudTerminal.spacer();

        this.cloudTerminal.terminalCommandService().registerCommand(new ReloadCommand());
        this.cloudTerminal.terminalCommandService().registerCommand(new StopCommand());
        this.cloudTerminal.terminalCommandService().registerCommand(new ClearCommand());
        this.cloudTerminal.terminalCommandService().registerCommand(new CloudServiceCommand());

        this.bootstrap();
        this.cloudTerminal.start();
    }

    public void bootstrap() {
        this.configurationService.configurationOptional("network").ifPresentOrElse(configurationClass -> {
            var networkConfiguration = (NetworkConfiguration) configurationClass;
            this.nettyServer.connect(networkConfiguration.nettyServerHostname(), networkConfiguration.nettyServerPort());
        }, () -> this.logger.warn("No network configuration found."));

        this.configurationService.configurationOptional("base").ifPresentOrElse(configurationClass -> {
            var baseConfiguration = (BaseConfiguration) configurationClass;
            this.logger.debugMode(baseConfiguration.consoleDebug());
            NetworkUtils.DEV_MODE = baseConfiguration.nettyDebug();
            this.logger.info("Handling is based on &2'&3" + baseConfiguration.cloudSyncFunction().name() + ConsoleColor.DARK_GRAY.ansiCode() + "&2'");
        }, () -> this.logger.warn("No base configuration found."));

        this.groupProvider.reloadGroups();
        this.templateProvider.reloadTemplates();
        this.playerProvider.reloadPlayers();
        this.serverBrandingService.reloadStyles();
        this.cloudPermissionService.reload();

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
        this.serverBrandingService.reloadStyles();
        this.cloudPermissionService.reload();
    }

    public void shutdown(boolean shutdownCycle) {
        this.loggerFactory.close();
        this.scheduler.cancelAllTasks();
        try {
            this.nettyServer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!shutdownCycle) {
            System.exit(0);
        }
    }
}
