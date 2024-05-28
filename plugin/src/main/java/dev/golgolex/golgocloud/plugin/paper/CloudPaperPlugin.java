package dev.golgolex.golgocloud.plugin.paper;

import com.google.common.reflect.TypeToken;
import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.cloudapi.configuration.NetworkConfiguration;
import dev.golgolex.golgocloud.common.SyncMode;
import dev.golgolex.golgocloud.common.configuration.ConfigurationService;
import dev.golgolex.golgocloud.common.service.ServiceLifeCycle;
import dev.golgolex.golgocloud.common.service.environment.CloudServerService;
import dev.golgolex.golgocloud.plugin.connection.ServerToServerConnection;
import dev.golgolex.quala.json.document.JsonDocument;
import dev.golgolex.quala.netty5.ChannelIdentity;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.management.OperatingSystemMXBean;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public class CloudPaperPlugin extends JavaPlugin {

    @Getter
    private static CloudPaperPlugin instance;
    private SyncMode syncMode;
    private ConfigurationService localConfigurationService;
    private ConfigurationService instanceConfigurationService;
    private UUID thisServiceUUID;
    private String thisServiceId;
    private String thisGroupName;
    private ServerToServerConnection playerConnectionHandler;

    @Override
    public void onLoad() {
        instance = this;

        this.localConfigurationService = new ConfigurationService(new File("./"), ".cloud-configuration");
        this.localConfigurationService.addConfiguration(
                new NetworkConfiguration(this.localConfigurationService.configurationDirectory())
        );
        var cloudServiceConfig = JsonDocument.fromPath(new File(this.localConfigurationService.configurationDirectory(), "cloud-service.json").toPath());
        CloudServerService cloudService = cloudServiceConfig.readObject("cloudService", new TypeToken<CloudServerService>() {
        }.getType());

        this.syncMode = SyncMode.valueOf(cloudServiceConfig.readString("cloud-player-handling"));
        this.thisServiceUUID = cloudService.uuid();
        this.thisServiceId = cloudService.id();
        this.thisGroupName = cloudService.group();
        this.playerConnectionHandler = switch (syncMode) {
            case SERVER_TO_SERVER -> new ServerToServerConnection();
            case PROXY -> null;
        };

        new CloudAPI(
                new ChannelIdentity(cloudService.id(), cloudService.uuid()),
                this.localConfigurationService,
                this.getLogger()
        );
    }

    @Override
    public void onEnable() {
        CloudAPI.instance().initialize();

        var cloudService = CloudAPI.instance().cloudServiceProvider().cloudService(thisServiceId).orElse(null);
        if (cloudService == null) {
            this.getLogger().log(Level.SEVERE, "No CloudService for id: " + thisServiceId + " found. Stopped initialization!");
            return;
        }
        var cloudInstance = CloudAPI.instance().cloudInstanceProvider().cloudInstance(cloudService.instance());
        this.instanceConfigurationService = new ConfigurationService(new File(cloudInstance.path()));

        cloudService.lifeCycle(ServiceLifeCycle.READY);
        CloudAPI.instance().cloudServiceProvider().updateService(cloudService);
    }

    @Override
    public void onDisable() {
        CloudAPI.instance().terminate(true);
    }
}
