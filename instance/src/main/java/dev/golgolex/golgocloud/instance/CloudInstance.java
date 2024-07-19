package dev.golgolex.golgocloud.instance;

import dev.golgolex.golgocloud.common.CloudShutdownExecutor;
import dev.golgolex.golgocloud.common.FileHelper;
import dev.golgolex.golgocloud.common.configuration.ConfigurationService;
import dev.golgolex.golgocloud.common.instance.packets.CloudInstanceAuthPacket;
import dev.golgolex.golgocloud.common.instance.packets.CloudInstanceUpdatePacket;
import dev.golgolex.golgocloud.common.threading.Scheduler;
import dev.golgolex.golgocloud.instance.commands.StopCommand;
import dev.golgolex.golgocloud.instance.configuration.InstanceConfiguration;
import dev.golgolex.golgocloud.instance.configuration.NetworkConfiguration;
import dev.golgolex.golgocloud.instance.group.CloudGroupProviderImpl;
import dev.golgolex.golgocloud.instance.network.CloudNetworkProviderImpl;
import dev.golgolex.golgocloud.instance.service.CloudServiceProcessQueue;
import dev.golgolex.golgocloud.instance.service.CloudServiceProviderImpl;
import dev.golgolex.golgocloud.instance.service.CloudServiceWorkerThread;
import dev.golgolex.golgocloud.instance.service.version.CloudServiceVersionProvider;
import dev.golgolex.golgocloud.instance.template.CloudTemplateProviderImpl;
import dev.golgolex.quala.common.Quala;
import dev.golgolex.quala.logger.Logger;
import dev.golgolex.quala.logger.LoggerFactory;
import dev.golgolex.quala.logger.handler.FileLoggerHandler;
import dev.golgolex.quala.logger.handler.LoggerOutPutStream;
import dev.golgolex.quala.module.ModuleInjector;
import dev.golgolex.quala.netty5.basic.ChannelIdentity;
import dev.golgolex.quala.netty5.basic.InactiveAction;
import dev.golgolex.quala.netty5.basic.NetworkCodec;
import dev.golgolex.quala.netty5.basic.client.NettyClient;
import dev.golgolex.quala.terminal.QualaTerminal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public final class CloudInstance {

    @Getter
    private static CloudInstance instance;
    private final File instanceDirectory;

    private final QualaTerminal cloudTerminal;
    private final LoggerFactory loggerFactory = new LoggerFactory();
    private final Logger logger = new Logger(loggerFactory, false);

    private final ConfigurationService configurationService;
    private final CloudNetworkProviderImpl networkProvider;
    private final CloudGroupProviderImpl groupProvider;
    private final CloudServiceVersionProvider serviceVersionProvider;
    private final CloudServiceProviderImpl serviceProvider;
    private final CloudTemplateProviderImpl templateProvider;
    private final Scheduler scheduler = new Scheduler(40);
    private final boolean osLinux;
    private final ModuleInjector moduleInjector;
    private CloudServiceProcessQueue serverProcessQueue;
    private UUID instanceId;
    private NettyClient nettyClient;
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
        /*var logDirectory = new File(this.instanceDirectory, "logs");
        if (!logDirectory.exists()) {
            boolean ignore = logDirectory.mkdirs();
        }*/

        var moduleDirectory = new File(this.instanceDirectory, "module");
        if (!moduleDirectory.exists()) {
            boolean ignore = moduleDirectory.mkdirs();
        }

        this.cloudTerminal = new QualaTerminal("&3instance&2: &1");
        this.loggerFactory.registerLoggers(new FileLoggerHandler(), this.cloudTerminal);
        System.setErr(new PrintStream(new LoggerOutPutStream(this.logger, true), true, StandardCharsets.UTF_8));
        System.setOut(new PrintStream(new LoggerOutPutStream(this.logger, false), true, StandardCharsets.UTF_8));
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> e.printStackTrace());
        CloudShutdownExecutor.task(() -> shutdown(false));

        this.configurationService = new ConfigurationService(this.instanceDirectory);
        this.networkProvider = new CloudNetworkProviderImpl();
        this.groupProvider = new CloudGroupProviderImpl();
        this.serviceVersionProvider = new CloudServiceVersionProvider(this.instanceDirectory);
        this.serviceProvider = new CloudServiceProviderImpl(this.instanceDirectory);
        this.templateProvider = new CloudTemplateProviderImpl(this.instanceDirectory);
        this.moduleInjector = new ModuleInjector(moduleDirectory,
                module -> this.logger.info("&1Module &2'&3" + module.moduleProperties().name() + "&2' &1by &2'&3" + module.moduleProperties().author() + "&2' &1initialized"),
                module -> this.logger.success("&1Module &2'&3" + module.moduleProperties().name() + "&2' &1by &2'&3" + module.moduleProperties().author() + "&2' &1activated"),
                module -> this.logger.warn("&1Module &2'&3" + module.moduleProperties().name() + "&2' &1by &2'&3" + module.moduleProperties().author() + "&2' &1deactivated"));

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
                            this.logger.success("Communication handler is connected.");
                        } else {
                            this.logger.error("Communication handler failed to connect. ", future.cause());
                        }
                    },
                    false
            );
        }, () -> {
            this.logger.error("No instance configuration found.", null);
            this.nettyClient = null;
        });
        this.networkProvider.initPacketReceivers(this.nettyClient.packetReceiverManager());

        this.cloudTerminal.message();
        this.cloudTerminal.message("    &3GolgoCloud &2| &1modern network environment &2| &3" + CloudInstance.class.getPackage().getImplementationVersion());
        this.cloudTerminal.message("    &1Java&2: &3" + System.getProperty("java.version") + " &2- &1User&2: &3" + System.getProperty("user.name") + " &2- &1OS &2: &3" + System.getProperty("os.name"));
        this.cloudTerminal.message("    &1Instance ID&2: &3" + this.instanceId.toString());
        this.cloudTerminal.message();

        this.cloudTerminal.terminalCommandService().registerCommand(new StopCommand());

        var os = System.getProperty("os.name").toLowerCase();
        this.osLinux = os.contains("nux") || os.contains("nix");

        this.bootstrap();
        this.cloudTerminal.start();
    }

    public void bootstrap() {
        this.configurationService.configurationOptional("network").ifPresentOrElse(configurationClass -> {
            var networkConfiguration = (NetworkConfiguration) configurationClass;
            this.nettyClient.connect(networkConfiguration.nettyServerHostname(), networkConfiguration.nettyServerPort());
        }, () -> this.logger.error("No network configuration found.", null));

        this.configurationService.configurationOptional("instance").ifPresentOrElse(configurationClass -> {
            var instanceConfiguration = (InstanceConfiguration) configurationClass;
            this.nettyClient.thisNetworkChannel().sendPacket(new CloudInstanceAuthPacket(this.instanceId, instanceConfiguration.authKey()));
            instanceConfiguration.configuration().write("maximalMemory", (Quala.systemMemory() / 1048576) - 2048);
            instanceConfiguration.save();

            if (instanceConfiguration.maximalMemory() < 1024) {
                System.err.println("WARNING: YOU CAN'T USE THE CLOUD NETWORK SOFTWARE WITH SUCH A SMALL MEMORY SIZE!");
            }

            this.serverProcessQueue = new CloudServiceProcessQueue(instanceConfiguration.processQueueSize());
        }, () -> this.logger.error("No instance configuration found.", null));

        while (this.cloudInstance == null) {
            if (this.connectionTry > 9) {
                this.shutdown(false);
                return;
            }
            Quala.sleepUninterruptedly(1000);
            this.connectionTry++;
        }

        this.cloudInstance.path(this.instanceDirectory.getAbsolutePath());

        var schedulerThread = new Thread(this.scheduler);
        schedulerThread.setPriority(Thread.MIN_PRIORITY);
        schedulerThread.setDaemon(true);
        schedulerThread.start();

        var processQueueThread = new Thread(this.serverProcessQueue);
        processQueueThread.setPriority(Thread.MIN_PRIORITY);
        processQueueThread.setDaemon(true);
        processQueueThread.start();

        this.groupProvider.reloadGroups();
        this.templateProvider.reloadTemplates();

        new CloudServiceWorkerThread().init(scheduler);

        for (var module : this.moduleInjector.modulesInFolder()) {
            this.moduleInjector.initialize(module);
            this.moduleInjector.activate(module);
        }

        Quala.sleepUninterruptedly(1000);

        this.cloudInstance.ready(true);
        this.nettyClient.thisNetworkChannel().sendPacket(new CloudInstanceUpdatePacket(this.cloudInstance));
    }

    public void reload() {
        for (var configuration : this.configurationService.configurations()) {
            configuration.reload();
        }

        for (var module : this.moduleInjector.modulesInFolder()) {
            module.refresh();
        }

        this.groupProvider.reloadGroups();
        this.templateProvider.reloadTemplates();
    }

    public void shutdown(boolean shutdownCycle) {
        this.cloudInstance.ready(false);
        this.nettyClient.thisNetworkChannel().sendPacket(new CloudInstanceUpdatePacket(this.cloudInstance));

        for (var serviceFactory : this.serviceProvider.serviceFactories()) {
            serviceFactory.terminate();
        }

        this.loggerFactory.close();
        this.scheduler.cancelAllTasks();

        for (var module : this.moduleInjector.modulesInFolder()) {
            this.moduleInjector.deactivate(module);
        }

        FileHelper.deleteDirectory(new File(this.instanceDirectory, "running/dynamic"));

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