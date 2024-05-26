package dev.golgolex.golgocloud.instance.service;

import dev.golgolex.golgocloud.common.FileHelper;
import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.golgocloud.common.service.CloudServiceFactory;
import dev.golgolex.golgocloud.common.service.environment.CloudServerService;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.Quala;
import dev.golgolex.quala.utils.color.ConsoleColor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
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
                this.dir = new File(CloudInstance.instance().serviceProvider().staticServiceDir().toFile(), "/" + cloudService.id() + "#" + cloudService.uuid().toString());
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

            CloudInstance.instance().logger().log(Level.INFO, "Service '" + ConsoleColor.WHITE.ansiCode() + cloudService.id() + ConsoleColor.DEFAULT.ansiCode() + "' prepared.");
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
            Files.deleteIfExists(Paths.get(this.dir.toPath() + "/plugins/GolgoCloudAPI.jar"));
            FileHelper.insertData("server-files/cloudapi/GolgoCloudAPI.jar", this.dir.toPath() + "/plugins/GolgoCloudAPI.jar");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var configurationDirectory = Paths.get(this.dir + "/.cloud-configuration");
        if (Files.exists(configurationDirectory)) {
            for (var file : Objects.requireNonNull(configurationDirectory.toFile().listFiles())) {
                var ignored = file.delete();
            }
        }
        try {
            Files.deleteIfExists(configurationDirectory);
            Files.createDirectory(configurationDirectory);
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

        var commandBuilder = new StringBuilder();

        if (CloudInstance.instance().osLinux()) {
            commandBuilder.append("screen -dmS ").append(cloudService.id()).append("#").append(cloudService.uuid().toString()).append(" ");
        }

        if (!cloudGroup.javaCommand().isEmpty()) {
            commandBuilder.append(cloudGroup.javaCommand());
        } else {
            commandBuilder
                    .append("java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:MaxMetaspaceSize=256M -XX:-UseAdaptiveSizePolicy -XX:CICompilerCount=2 -Dcom.mojang.eula.agree=true -Dio.netty.recycler.maxCapacity=0 -Dio.netty.recycler.maxCapacity.default=0 -Djline.terminal=jline.UnsupportedTerminal -Xmx")
                    .append(cloudService.memory())
                    .append("M -jar ")
                    .append("paper-")
                    .append(cloudGroup.version())
                    .append(".jar");
        }

        var command = commandBuilder.toString();
        try {
            this.process = Runtime.getRuntime().exec(command.split(" "), null, dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CloudInstance.instance().logger().log(Level.INFO, "Service '" + ConsoleColor.WHITE.ansiCode() + cloudService.id() + ConsoleColor.DEFAULT.ansiCode() + "' started.");
    }

    @Override
    public void terminate() {
        if (isAlive()) {
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

        CloudInstance.instance().logger().log(Level.INFO, "Service '" + ConsoleColor.WHITE.ansiCode() + cloudService.id() + ConsoleColor.DEFAULT.ansiCode() + "' terminated.");
    }

    @Override
    public boolean isAlive() {
        try {
            return process != null
                    && CloudInstance.instance().osLinux()
                    ? CloudServiceScreenHelper.isScreenExisting(cloudService.id() + "#" + cloudService.uuid().toString()) : Objects.requireNonNull(process).isAlive()
                    && process.getInputStream().available() != -1;
        } catch (IOException e) {
            return false;
        }
    }

    public void runCommand(@NotNull String commandLine) {
        if (process == null || !isAlive()) {
            CloudInstance.instance().logger().log(Level.SEVERE, "Service process " + cloudService.id() + " is not alive.");
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
