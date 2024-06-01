package dev.golgolex.golgocloud.base.instance;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.base.configuration.InstanceConfiguration;
import dev.golgolex.golgocloud.common.instance.CloudInstance;
import dev.golgolex.golgocloud.common.instance.CloudInstanceProvider;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstanceAuthPacket;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstanceAuthReplyPacket;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstanceUpdatePacket;
import dev.golgolex.quala.event.EventRegistry;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public final class CloudInstanceProviderImpl implements CloudInstanceProvider {

    private final List<CloudInstance> cloudInstances = new ArrayList<>();
    private final List<String> allowed = new ArrayList<>();

    public CloudInstanceProviderImpl() {
        EventRegistry.registerListener(new InstanceEventHandler(this));
    }

    /**
     * Allows a connection for the specified IP address.
     *
     * @param str the IP address to allow connection for
     */
    public void allowConnection(String str) {
        if (this.allowed.contains(str)) return;
        this.allowed.add(str);
    }

    /**
     * Connects to the network channel with the specified authentication packet.
     *
     * @param packet         the authentication packet containing instance ID and authentication key
     * @param networkChannel the network channel to connect to
     */
    public void connect(@NotNull CloudInstanceAuthPacket packet, @NotNull NetworkChannel networkChannel) {
        var hostname = ((InetSocketAddress) networkChannel.channel().remoteAddress()).getAddress().getHostAddress();
        CloudBase.instance().configurationService().configurationOptional("instance").ifPresentOrElse(configurationClass -> {
            var instanceConfiguration = (InstanceConfiguration) configurationClass;

            if (!instanceConfiguration.authKey().equals(packet.authKey())) {
                CloudBase.instance().logger().warn( "Instance '" + networkChannel.channelIdentity().namespace() + "' has tried to connect with an incorrect Auth-Key.");
                return;
            }

            if (!instanceConfiguration.ipWhitelist().isEmpty()) {
                if (!this.allowed.contains(hostname)) {
                    return;
                }
            } else {
                var whitelist = instanceConfiguration.ipWhitelist();
                whitelist.add(hostname);
                instanceConfiguration.ipWhitelist(whitelist);
                instanceConfiguration.save();
            }

            var instance = instanceConfiguration.instances().stream()
                    .filter(it -> it.uuid().equals(packet.instanceId()))
                    .findFirst()
                    .orElse(null);

            if (instance == null) {
                instance = new CloudInstance(
                        networkChannel.channelIdentity().namespace(),
                        packet.instanceId(),
                        hostname,
                        "your-domain.de",
                        false,
                        ""
                );
                var instances = instanceConfiguration.instances();
                instances.add(instance);
                instanceConfiguration.instances(instances);
                instanceConfiguration.save();
            }

            this.cloudInstances.add(instance);
            networkChannel.sendPacket(new CloudInstanceAuthReplyPacket(instance));
            CloudBase.instance().logger().info( "Instance &2'&3" + instance.id() + "&2' &1successfully connected");
        }, () -> {
            throw new IllegalStateException("Instance configuration not found");
        });
    }

    /**
     * Disconnects the specified cloud instance from the service.
     *
     * @param cloudInstance the cloud instance to disconnect
     */
    public void disconnect(@NotNull CloudInstance cloudInstance) {
        this.cloudInstances.removeIf(it -> it.uuid().equals(cloudInstance.uuid()));
        CloudBase.instance().logger().info( "Instance &2'&3" + cloudInstance.id() + "&2' &1disconnected");
        for (var cloudService : CloudBase.instance().serviceProvider().cloudServices().stream().filter(cloudService -> cloudService.instance().equals(cloudInstance.uuid())).toList()) {
            CloudBase.instance().serviceProvider().shutdownService(cloudService);
        }
        CloudBase.instance().configurationService().configurationOptional("instance").ifPresentOrElse(configurationClass -> {
            var instanceConfiguration = (InstanceConfiguration) configurationClass;
            var instances = instanceConfiguration.instances();
            instances.removeIf(it -> it.uuid().equals(cloudInstance.uuid()));
            cloudInstance.ready(false);
            instances.add(cloudInstance);
            instanceConfiguration.instances(instances);
            instanceConfiguration.save();
        }, () -> {
            throw new IllegalStateException("Instance configuration not found");
        });
    }

    @Override
    public void reloadInstances() {
        // no implementation
    }

    @Override
    public void updateInstance(@NotNull CloudInstance cloudInstance) {
        this.cloudInstances.removeIf(it -> it.uuid().equals(cloudInstance.uuid()));
        this.cloudInstances.add(cloudInstance);
        CloudBase.instance().configurationService().configurationOptional("instance").ifPresentOrElse(configurationClass -> {
            var instanceConfiguration = (InstanceConfiguration) configurationClass;
            var instances = instanceConfiguration.instances();
            instances.removeIf(it -> it.uuid().equals(cloudInstance.uuid()));
            instances.add(cloudInstance);
            instanceConfiguration.instances(instances);
            instanceConfiguration.save();
        }, () -> {
            throw new IllegalStateException("Instance configuration not found");
        });
        CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudInstanceUpdatePacket(cloudInstance), null);
    }

    @Override
    public void shutdownInstance(@NotNull CloudInstance cloudInstance) {
        // todo
    }

}
