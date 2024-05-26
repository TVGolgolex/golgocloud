package dev.golgolex.golgocloud.base.group;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.base.configuration.GroupsConfiguration;
import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.golgocloud.common.group.CloudGroupProvider;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupCreatePacket;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupDeletePacket;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupUpdatePacket;
import dev.golgolex.golgocloud.common.service.ServiceEnvironment;
import dev.golgolex.quala.json.document.JsonDocument;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public final class CloudGroupProviderImpl implements CloudGroupProvider {

    private final List<CloudGroup> cloudGroups = new ArrayList<>();

    @Override
    public void reloadGroups() {
        this.cloudGroups.clear();
        CloudBase.instance().configurationService().configurationOptional("groups").ifPresentOrElse(configurationClass -> {
            var groupsConfiguration = (GroupsConfiguration) configurationClass;
            this.cloudGroups.addAll(groupsConfiguration.groups());

            if (this.cloudGroups.isEmpty()) {
                var defaultGroup = new CloudGroup(
                        "lobby",
                        "-",
                        "",
                        1,
                        -1,
                        25565,
                        new ArrayList<>(),
                        false,
                        false,
                        ServiceEnvironment.SERVER,
                        "PAPER",
                        "1.20.5",
                        950,
                        true,
                        true,
                        true,
                        true,
                        new JsonDocument()
                );
                this.createGroup(defaultGroup);
            }

            CloudBase.instance().logger().log(Level.INFO, "Loaded following groups: " + this.cloudGroups.stream()
                    .map(CloudGroup::name)
                    .collect(Collectors.joining(", ")));
        }, () -> CloudBase.instance().logger().log(Level.SEVERE, "No groups configuration found."));
    }

    @Override
    public void updateGroup(@NotNull CloudGroup cloudGroup) {
        CloudBase.instance().configurationService().configurationOptional("groups").ifPresentOrElse(configurationClass -> {
            var groupsConfiguration = (GroupsConfiguration) configurationClass;
            var list = groupsConfiguration.groups();
            list.removeIf(it -> it.name().equalsIgnoreCase(cloudGroup.name()));
            list.add(cloudGroup);
            groupsConfiguration.groups(list);
            groupsConfiguration.save();
            CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudGroupUpdatePacket(cloudGroup), null);
        }, () -> CloudBase.instance().logger().log(Level.SEVERE, "No groups configuration found."));
    }

    @Override
    public void deleteGroup(@NotNull CloudGroup cloudGroup) {
        CloudBase.instance().logger().log(Level.INFO, "Server group " + cloudGroup.name() + " deleted.");
        CloudBase.instance().configurationService().configurationOptional("groups").ifPresentOrElse(configurationClass -> {
            var groupsConfiguration = (GroupsConfiguration) configurationClass;
            var list = groupsConfiguration.groups();
            list.removeIf(it -> it.name().equalsIgnoreCase(cloudGroup.name()));
            groupsConfiguration.groups(list);
            groupsConfiguration.save();
            CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudGroupDeletePacket(cloudGroup), null);
        }, () -> CloudBase.instance().logger().log(Level.SEVERE, "No groups configuration found."));
    }

    @Override
    public void createGroup(@NotNull CloudGroup cloudGroup) {
        CloudBase.instance().logger().log(Level.INFO, "Server group " + cloudGroup.name() + " successfully created.");
        CloudBase.instance().configurationService().configurationOptional("groups").ifPresentOrElse(configurationClass -> {
            var groupsConfiguration = (GroupsConfiguration) configurationClass;
            var list = groupsConfiguration.groups();
            list.add(cloudGroup);
            groupsConfiguration.groups(list);
            groupsConfiguration.save();
            CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudGroupCreatePacket(cloudGroup), null);
        }, () -> CloudBase.instance().logger().log(Level.SEVERE, "No groups configuration found."));
    }
}
