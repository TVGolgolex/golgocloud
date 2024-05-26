package dev.golgolex.golgocloud.plugin.paper;

import com.google.common.reflect.TypeToken;
import dev.golgolex.golgocloud.api.CloudAPI;
import dev.golgolex.golgocloud.api.configuration.NetworkConfiguration;
import dev.golgolex.golgocloud.common.configuration.ConfigurationService;
import dev.golgolex.golgocloud.common.service.environment.CloudServerService;
import dev.golgolex.quala.json.document.JsonDocument;
import dev.golgolex.quala.netty5.ChannelIdentity;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public class CloudPaperPlugin extends JavaPlugin {

    @Getter
    private static CloudPaperPlugin instance;
    private ConfigurationService configurationService;

    @Override
    public void onLoad() {
        instance = this;

        this.configurationService = new ConfigurationService(new File("./", ".cloud-configuration"));
        this.configurationService.addConfiguration(
                new NetworkConfiguration(this.configurationService.configurationDirectory())
        );

        CloudServerService cloudService = JsonDocument.fromPath(new File(this.configurationService.configurationDirectory(), "cloud-service.json").toPath()).readObject("cloudService", new TypeToken<CloudServerService>() {
        }.getType());

        new CloudAPI(
                new ChannelIdentity(cloudService.id(), cloudService.uuid()),
                this.configurationService,
                this.getLogger()
        );
    }

    @Override
    public void onEnable() {
        CloudAPI.instance().initialize();
    }

    @Override
    public void onDisable() {
        CloudAPI.instance().terminate(true);
    }
}
