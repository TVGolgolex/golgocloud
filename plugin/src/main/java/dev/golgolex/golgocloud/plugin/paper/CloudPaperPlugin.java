package dev.golgolex.golgocloud.plugin.paper;

import com.google.common.reflect.TypeToken;
import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.cloudapi.configuration.NetworkConfiguration;
import dev.golgolex.golgocloud.common.configuration.ConfigurationService;
import dev.golgolex.golgocloud.common.service.ServiceLifeCycle;
import dev.golgolex.golgocloud.common.service.environment.CloudServerService;
import dev.golgolex.quala.json.document.JsonDocument;
import dev.golgolex.quala.netty5.ChannelIdentity;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;

@Getter
public class CloudPaperPlugin extends JavaPlugin {

    @Getter
    private static CloudPaperPlugin instance;
    private ConfigurationService localConfigurationService;
    private ConfigurationService instanceConfigurationService;
    private UUID thisServiceUUID;
    private String thisServiceId;

    @Override
    public void onLoad() {
        instance = this;

        this.localConfigurationService = new ConfigurationService(new File("./"), ".cloud-configuration");
        this.localConfigurationService.addConfiguration(
                new NetworkConfiguration(this.localConfigurationService.configurationDirectory())
        );
        CloudServerService cloudService = JsonDocument.fromPath(new File(this.localConfigurationService.configurationDirectory(), "cloud-service.json").toPath()).readObject("cloudService", new TypeToken<CloudServerService>() {
        }.getType());

        this.thisServiceUUID = cloudService.uuid();
        this.thisServiceId = cloudService.id();

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
