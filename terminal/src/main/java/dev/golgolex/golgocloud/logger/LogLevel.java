package dev.golgolex.golgocloud.logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum LogLevel {

    OFF(""),
    INFO("&4"),
    SUCCESS("&8"),
    WARN("&5"),
    ERROR("&6"),
    DEBUG("");

    private final String colorCode;

}