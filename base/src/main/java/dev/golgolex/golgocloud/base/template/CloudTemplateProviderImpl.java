package dev.golgolex.golgocloud.base.template;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.base.configuration.TemplateConfiguration;
import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.golgocloud.common.template.CloudTemplateProvider;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplateCreatePacket;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplateDeletePacket;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplateUpdatePacket;
import io.netty5.buffer.internal.LifecycleTracer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public class CloudTemplateProviderImpl implements CloudTemplateProvider {

    private final List<CloudServiceTemplate> cloudServiceTemplates = new ArrayList<>();

    @Override
    public void reloadTemplates() {
        CloudBase.instance().configurationService().configurationOptional("template").ifPresentOrElse(configurationClass -> {
            var templateConfiguration = (TemplateConfiguration) configurationClass;
            cloudServiceTemplates.addAll(templateConfiguration.serviceTemplates());
        }, () -> CloudBase.instance().logger().error( "No template configuration found.", null));
    }

    @Override
    public void checkExistTemplates(@NotNull CloudService cloudService) {
        var template = this.cloudServiceTemplate(cloudService.id(), cloudService.group());
        if (template != null) {
            return;
        }
        this.createCloudServiceTemplate(new CloudServiceTemplate(
                cloudService.group(),
                cloudService.template(),
                true,
                new ArrayList<>(List.of(cloudService.instance()))
        ));
    }

    @Override
    public void updateCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate) {
        CloudBase.instance().configurationService().configurationOptional("template").ifPresentOrElse(configurationClass -> {
            var templateConfiguration = (TemplateConfiguration) configurationClass;
            var list = templateConfiguration.serviceTemplates();
            list.removeIf(it -> it.id().equalsIgnoreCase(cloudServiceTemplate.id()) && it.groupName().equalsIgnoreCase(cloudServiceTemplate.groupName()));
            list.add(cloudServiceTemplate);
            templateConfiguration.serviceTemplates(list);
            templateConfiguration.save();
            CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudServiceTemplateUpdatePacket(cloudServiceTemplate), null);
        }, () -> CloudBase.instance().logger().error("No template configuration found.", null));
        this.reloadTemplates();
    }

    @Override
    public void deleteCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate) {
        CloudBase.instance().configurationService().configurationOptional("template").ifPresentOrElse(configurationClass -> {
            var templateConfiguration = (TemplateConfiguration) configurationClass;
            var list = templateConfiguration.serviceTemplates();
            list.removeIf(it -> it.id().equalsIgnoreCase(cloudServiceTemplate.id()) && it.groupName().equalsIgnoreCase(cloudServiceTemplate.groupName()));
            templateConfiguration.serviceTemplates(list);
            templateConfiguration.save();
            CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudServiceTemplateDeletePacket(cloudServiceTemplate), null);
        }, () -> CloudBase.instance().logger().error("No template configuration found.", null));
        this.reloadTemplates();
    }

    @Override
    public void createCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate) {
        CloudBase.instance().configurationService().configurationOptional("template").ifPresentOrElse(configurationClass -> {
            var templateConfiguration = (TemplateConfiguration) configurationClass;
            var list = templateConfiguration.serviceTemplates();
            list.add(cloudServiceTemplate);
            templateConfiguration.serviceTemplates(list);
            templateConfiguration.save();
            CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudServiceTemplateCreatePacket(cloudServiceTemplate), null);
        }, () -> CloudBase.instance().logger().error("No template configuration found.", null));
        this.reloadTemplates();
    }
}
