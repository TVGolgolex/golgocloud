package dev.golgolex.golgocloud.instance.service;

import dev.golgolex.golgocloud.common.FileHelper;
import dev.golgolex.golgocloud.common.SyncMode;
import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.golgocloud.common.service.CloudServiceFactory;
import dev.golgolex.golgocloud.common.service.ServiceLifeCycle;
import dev.golgolex.golgocloud.common.service.environment.CloudServerService;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceStartedPacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.golgocloud.instance.configuration.NetworkConfiguration;
import dev.golgolex.quala.Quala;
import dev.golgolex.quala.json.document.JsonDocument;
import dev.golgolex.quala.utils.color.ConsoleColor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public final class CloudServerServiceFactory implements CloudServiceFactory<CloudServerService> {

    private CloudServerService cloudService;
    private CloudGroup cloudGroup;
    private Process process;
    private File dir;
    private long startupTimeStamp;

    public CloudServerServiceFactory(CloudServerService cloudService) {
        this.cloudService = cloudService;
    }

    @Override
    public void cloudService(CloudServerService cloudService) {
        this.cloudService = cloudService;
    }

    @Override
    public void prepare() {
        CloudInstance.instance().serviceProvider().serviceFactories().add(this);
        CloudInstance.instance().groupProvider().cloudGroup(cloudService.group()).ifPresentOrElse(cloudGroup -> {
            this.cloudGroup = cloudGroup;

            if (cloudGroup.staticService()) {
                this.dir = new File(CloudInstance.instance().serviceProvider().staticServiceDir().toFile(), "/" + cloudService.id());
            } else {
                this.dir = new File(CloudInstance.instance().serviceProvider().runningDynamicServiceDir().toFile(), "/" + cloudService.id() + "#" + cloudService.uuid().toString());
            }

            if (!Files.exists(dir.toPath())) {
                try {
                    Files.createDirectories(dir.toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            this.cloudService.host(Quala.hostName());
            this.cloudService.path(this.dir.getAbsolutePath());
            CloudInstance.instance().logger().info("Service &2'&3" + cloudService.id() + "&2' &1was prepared");
            this.start();
        }, () -> {
            System.err.println("No CloudGroup for " + cloudService.group() + " loaded.");
            // todo cancel
        });
    }

    @Override
    public void start() {
        this.startupTimeStamp = System.currentTimeMillis();

        var pluginsDirectory = Paths.get(this.dir + "/plugins");
        if (!Files.exists(pluginsDirectory)) {
            try {
                Files.createDirectory(pluginsDirectory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Files.deleteIfExists(Paths.get(this.dir.toPath() + "/plugins/CloudPlugin.jar"));
            FileHelper.insertData("server-files/cloudapi/CloudPlugin.jar", this.dir.toPath() + "/plugins/CloudPlugin.jar");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var versionPath = new File(CloudInstance.instance().serviceVersionProvider().paperVersionsDirectory(), "paper-" + cloudGroup.version() + ".jar");
        if (!Files.exists(versionPath.toPath())) {
            System.err.println("No Paper version for " + cloudGroup.name() + " (" + cloudGroup.version() + ") found.");
            return;
        }

        if (!Files.exists(Paths.get(dir + "/paper-" + cloudGroup.version() + ".jar"))) {
            try {
                FileHelper.copyFileToDirectory(versionPath, dir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        CloudInstance.instance().templateProvider().copyFiles(this.cloudService);

        this.cloudFileConfiguration();
        this.fileConfigration();
        this.serverPropertiesConfiguration();

        var commandBuilder = new StringBuilder();

        if (CloudInstance.instance().osLinux()) {
            commandBuilder.append("screen -dmS ").append(cloudService.id()).append("#").append(cloudService.uuid().toString()).append(" ");
        }

        if (!cloudGroup.javaCommand().isEmpty()) {
            commandBuilder.append(cloudGroup.javaCommand());
        } else {
            commandBuilder
                    .append("java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:MaxMetaspaceSize=256M -XX:-UseAdaptiveSizePolicy -XX:CICompilerCount=2 -Dcom.mojang.eula.agree=true -Dio.netty.recycler.maxCapacity=0 -Dio.netty.recycler.maxCapacity.default=0 -Djline.terminal=jline.UnsupportedTerminal -Xmx")
                    .append(cloudService.memory()).append("M -jar ").append("paper-").append(cloudGroup.version()).append(".jar");
        }

        try {
            this.process = Runtime.getRuntime().exec(commandBuilder.toString().split(" "), null, dir);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        this.cloudService.lifeCycle(ServiceLifeCycle.EXECUTED);
        CloudInstance.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudServiceStartedPacket(this.cloudService));
        CloudInstance.instance().logger().success("Service &2'&3" + cloudService.id() + "&2' &1started");
    }

    /**
     * Configures the cloud file system for the server.
     * This method checks if certain files exist in the specified directory, and if they don't, it inserts them.
     * The method is responsible for setting up the*/
    public void cloudFileConfiguration() {
        var configurationDirectory = Paths.get(this.dir + "/.cloud-configuration");
        if (Files.exists(configurationDirectory)) {
            for (var file : Objects.requireNonNull(configurationDirectory.toFile().listFiles())) {
                var ignored = file.delete();
            }
        }

        Quala.sleepUninterruptedly(200);

        try {
            Files.deleteIfExists(configurationDirectory);
            Files.createDirectory(configurationDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CloudInstance.instance().configurationService().configurationOptional("network").ifPresent(configurationClass -> {
            var networkConfiguration = (NetworkConfiguration) configurationClass;
            networkConfiguration.configuration().saveAsConfig(new File(configurationDirectory.toFile(), networkConfiguration.id() + ".json").toPath());
        });

        new JsonDocument()
                .write("cloudService", cloudService)
                .write("cloud-player-handling", SyncMode.SERVER_TO_SERVER.name())
                .saveAsConfig(new File(configurationDirectory.toFile(), "cloud-service.json").toPath());
    }

    /**
     * Configures the file system for the server.
     * This method checks if certain files exist in the specified directory, and if they don't, it inserts them.
     * The files that are checked and inserted are 'bukkit.yml' and 'server-icon.png'.
     * This method is called to set up the initial file configuration for the server.
     */
    public void fileConfigration() {
        if (!Files.exists(Paths.get(dir + "/bukkit.yml"))) {
            FileHelper.insertData("server-files/spigot/bukkit.yml", dir + "/bukkit.yml");
        }

        if (!Files.exists(Paths.get(dir + "/server-icon.png"))) {
            FileHelper.insertData("server-files/server-icon.png", dir + "/server-icon.png");
        }
    }

    /**
     * Configures the server.properties file for the server.
     * This method checks if the server.properties file exists in the specified directory,
     * and if it doesn't, it inserts it from a template file.
     * The method then updates certain properties in the server.properties file,
     * such as the server IP, server port, and online mode.
     * Finally, it saves the updated server.properties file.
     * This method is called to configure the server properties.
     */
    public void serverPropertiesConfiguration() {
        var serverPropertiesDir = Paths.get(dir + "/server.properties");
        if (!Files.exists(serverPropertiesDir)) {
            FileHelper.insertData("server-files/spigot/server.properties", serverPropertiesDir.toString());
        }

        var properties = new Properties();
        try (var inputStreamReader = new InputStreamReader(Files.newInputStream(serverPropertiesDir))) {
            properties.load(inputStreamReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        properties.setProperty("server-ip", Quala.hostName());
        properties.setProperty("server-port", String.valueOf(this.cloudService.port()));
        properties.setProperty("online-mode", String.valueOf(this.cloudGroup.onlineMode()));

        try (var outputStream = Files.newOutputStream(serverPropertiesDir)) {
            properties.store(outputStream, "GolgoCloud-Edit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void terminate() {
        if (isAlive()) {
            if (this.cloudGroup.staticService()) {
                this.runCommand("save-all");
                Quala.sleepUninterruptedly(500);
            }
            this.runCommand("stop");
            Quala.sleepUninterruptedly(500);
        }

        if (process != null) {
            this.process.destroyForcibly();
        }

        Quala.sleepUninterruptedly(500);
        if (!this.cloudGroup.staticService()) {
            FileHelper.deleteDirectory(this.dir);
        }

        CloudInstance.instance().serviceProvider().shutdownService(cloudService);
        CloudInstance.instance().logger().info("Service &2'&3" + cloudService.id() + "&2' &1was terminated");
    }

    @Override
    public boolean isAlive() {
        if (process == null) {
            return false;
        }

        try {
            return CloudInstance.instance().osLinux() ? CloudServiceScreenHelper.isScreenExisting(cloudService.id() + "#" + cloudService.uuid().toString()) : Objects.requireNonNull(process).isAlive() && process.getInputStream().available() != -1;
        } catch (IOException e) {
            return false;
        }
    }

    public void runCommand(@NotNull String commandLine) {
        if (process == null || !isAlive()) {
            CloudInstance.instance().logger().warn("Service process &2'&3" + cloudService.id() + "&2' &1is not alive.");
            return;
        }

        if (CloudInstance.instance().osLinux()) {
            CloudServiceScreenHelper.executeScreenCommand(cloudService.id() + "#" + cloudService.uuid().toString(), commandLine);
        } else {
            try {
                process.getOutputStream().write((commandLine + '\n').getBytes());
                process.getOutputStream().flush();
            } catch (Exception ignored) {
            }
        }
    }
}
