package dev.golgolex.golgocloud.common;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CloudShutdownExecutor {

    @Getter
    @Setter
    private static Runnable task;

}
