package dev.golgolex.golgocloud.common.group.platform;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class CloudGroupPlatform implements BufferClass {

    private boolean proxyPlattform;
    private Set<PlatformVersion> possibleVersions = new HashSet<>();

    public abstract void download(String version);

    public abstract void prepare(CloudService cloudService);

    public String[] platformsEnvironment() {
        return new String[0];
    }

    public String[] platformsArguments() {
        return new String[0];
    }

}
