package dev.golgolex.golgocloud.cloudapi.template;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.instance.CloudInstance;
import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.golgocloud.common.template.CloudTemplateProvider;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplatesReplyPacket;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplatesRequestPacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public class CloudTemplateProviderImpl implements CloudTemplateProvider {

    private final List<CloudServiceTemplate> cloudServiceTemplates = new ArrayList<>();

    @Override
    public void checkExistTemplates(@NotNull CloudService cloudService) {

    }

    @Override
    public void updateCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate) {

    }

    @Override
    public void deleteCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate) {

    }

    @Override
    public void createCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate) {

    }

    @Override
    public void reloadTemplates() {
        this.cloudServiceTemplates.clear();
        CloudServiceTemplatesReplyPacket reply = CloudAPI.instance().nettyClient().thisNetworkChannel().sendQuery(new CloudServiceTemplatesRequestPacket());
        this.cloudServiceTemplates.addAll(reply.cloudServiceTemplates());
        if (!this.cloudServiceTemplates.isEmpty()) {
            CloudAPI.instance().logger().log(Level.INFO, "Loaded following service templates: " + this.cloudServiceTemplates.stream()
                    .map(CloudServiceTemplate::id)
                    .collect(Collectors.joining(", ")));
        }
    }
}
