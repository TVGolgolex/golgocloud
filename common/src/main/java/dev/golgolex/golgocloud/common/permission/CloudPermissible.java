package dev.golgolex.golgocloud.common.permission;

import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
public abstract class CloudPermissible implements BufferClass {
    
    private UUID id;
    private Lis

}