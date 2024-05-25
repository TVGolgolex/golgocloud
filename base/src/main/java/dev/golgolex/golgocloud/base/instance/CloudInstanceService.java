package dev.golgolex.golgocloud.base.instance;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.base.configuration.InstanceConfiguration;
import dev.golgolex.golgocloud.common.instance.CloudInstance;
import dev.golgolex.golgocloud.common.instance.packet.InstanceAuthPacket;
import dev.golgolex.golgocloud.common.instance.packet.InstanceAuthReplyPacket;
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
public final class CloudInstanceService {

    private final List<CloudInstance> connectedCloudInstances = new ArrayList<>();
    private final List<String> allowed = new ArrayList<>();

    public CloudInstanceService() {
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

    public void connect(@NotNull InstanceAuthPacket packet, @NotNull NetworkChannel networkChannel) {
        var hostname = ((InetSocketAddress) networkChannel.channel().remoteAddress()).getAddress().getHostAddress();
        CloudBase.instance().configurationService().configurationOptional("instance").ifPresentOrElse(configurationClass -> {
            var instanceConfiguration = (InstanceConfiguration) configurationClass;

            if (!instanceConfiguration.authKey().equals(packet.authKey())) {
                CloudBase.instance().logger().log(Level.WARNING, "Instance '" + networkChannel.channelIdentity().namespace() + "' has tried to connect with an incorrect Auth-Key.");
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
                        "your-domain.de"
                );
                var instances = instanceConfiguration.instances();
                instances.add(instance);
                instanceConfiguration.instances(instances);
                instanceConfiguration.save();
            }

            this.connectedCloudInstances.add(instance);
            networkChannel.sendPacket(new InstanceAuthReplyPacket(instance));
            CloudBase.instance().logger().log(Level.INFO, "Instance '" + instance.id() + "' successfully connected");
        }, () -> {
            throw new IllegalStateException("Instance configuration not found");
        });
    }

    public void disconnect(@NotNull CloudInstance cloudInstance) {
        this.connectedCloudInstances.removeIf(it -> it.uuid().equals(cloudInstance.uuid()));
        CloudBase.instance().logger().log(Level.INFO, "Instance '" + cloudInstance.id() + "' disconnected");
    }

}
