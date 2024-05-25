package dev.golgolex.golgocloud.common;

/*
 * MIT License
 *
 * Copyright (c) 2024 21:14 LumeCloud contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum MinecraftVersion {

    MINECRAFT_1_7(3, "1.7"),
    MINECRAFT_1_7_2(4, "1.7.2"),
    MINECRAFT_1_7_4(4, "1.7.4"), // Same protocol as 1_7_2
    MINECRAFT_1_7_5(4, "1.7.5"), // Same protocol as 1_7_2
    MINECRAFT_1_7_6(5, "1.7.6"),
    MINECRAFT_1_7_7(5, "1.7.7"), // Same protocol as 1_7_6
    MINECRAFT_1_7_8(5, "1.7.8"), // Same protocol as 1_7_7
    MINECRAFT_1_7_9(5, "1.7.9"), // Same protocol as 1_7_8
    MINECRAFT_1_7_10(5, "1.7.10"), // Same protocol as 1_7_9

    MINECRAFT_1_8(47, "1.8"),
    MINECRAFT_1_8_1(47, "1.8.1"), // Same protocol as 1_8
    MINECRAFT_1_8_2(47, "1.8.2"), // Same protocol as 1_8
    MINECRAFT_1_8_3(47, "1.8.3"), // Same protocol as 1_8
    MINECRAFT_1_8_4(47, "1.8.4"), // Same protocol as 1_8
    MINECRAFT_1_8_5(47, "1.8.5"), // Same protocol as 1_8
    MINECRAFT_1_8_6(47, "1.8.6"), // Same protocol as 1_8
    MINECRAFT_1_8_7(47, "1.8.7"), // Same protocol as 1_8
    MINECRAFT_1_8_8(47, "1.8.8"), // Same protocol as 1_8
    MINECRAFT_1_8_9(47, "1.8.9"), // Same protocol as 1_8

    MINECRAFT_1_9(107, "1.9"),
    MINECRAFT_1_9_1(108, "1.9.1"),
    MINECRAFT_1_9_2(109, "1.9.2"),
    MINECRAFT_1_9_3(110, "1.9.3"),

    MINECRAFT_1_10(210, "1.10"),

    MINECRAFT_1_11(315, "1.11"),
    MINECRAFT_1_11_1(316, "1.11.1"),

    MINECRAFT_1_12(335, "1.12"),
    MINECRAFT_1_12_1(338, "1.12.1"),
    MINECRAFT_1_12_2(340, "1.12.2"),

    MINECRAFT_1_13(393, "1.13"),
    MINECRAFT_1_13_1(401, "1.13.1"),
    MINECRAFT_1_13_2(404, "1.13.2"),

    MINECRAFT_1_14(477, "1.14"),
    MINECRAFT_1_14_1(480, "1.14.1"),
    MINECRAFT_1_14_2(485, "1.14.2"),
    MINECRAFT_1_14_3(490, "1.14.3"),
    MINECRAFT_1_14_4(498, "1.14.4"),

    MINECRAFT_1_15(573, "1.15"),
    MINECRAFT_1_15_1(575, "1.15.1"),
    MINECRAFT_1_15_2(578, "1.15.2"),

    MINECRAFT_1_16(735, "1.16"),
    MINECRAFT_1_16_1(736, "1.16.1"),
    MINECRAFT_1_16_2(751, "1.16.2"),
    MINECRAFT_1_16_3(753, "1.16.3"),
    MINECRAFT_1_16_4(754, "1.16.4"), // Same protocol as 1_16_5
    MINECRAFT_1_16_5(754, "1.16.5"),

    MINECRAFT_1_17(755, "1.17"),
    MINECRAFT_1_17_1(756, "1.17.1"),

    MINECRAFT_1_18(757, "1.18"), // Same protocol as 1_18_1
    MINECRAFT_1_18_1(757, "1.18.1"),
    MINECRAFT_1_18_2(758, "1.18.2"),

    MINECRAFT_1_19(759, "1.19"),
    MINECRAFT_1_19_1(760, "1.19.1"), // Same protocol as 1_19_2
    MINECRAFT_1_19_2(760, "1.19.2"),
    MINECRAFT_1_19_3(761, "1.19.3"),
    MINECRAFT_1_19_4(762, "1.19.4"),

    MINECRAFT_1_20(763, "1.20"), // Same protocol as 1_20_1
    MINECRAFT_1_20_1(763, "1.20.1"),
    MINECRAFT_1_20_2(764, "1.20.2"),
    MINECRAFT_1_20_3(765, "1.20.3"),
    MINECRAFT_1_20_4(765, "1.20.4"), // Same protocol as 1_20_3
    MINECRAFT_1_20_5(766, "1.20.5"),
    MINECRAFT_1_20_6(766, "1.20.6"), // Same protocol as 1_20_5
    ;

    private final int protocolId;
    private final String display;

    /**
     * Constructor for creating a MinecraftVersion object.
     *
     * @param protocolId the protocol ID of the Minecraft version
     * @param display    the display name of the Minecraft version
     */
    MinecraftVersion(int protocolId, String display) {
        this.protocolId = protocolId;
        this.display = display;
    }

    /**
     * Retrieves the MinecraftVersion based on the provided protocol ID.
     *
     * @param protocolId The protocol ID of the Minecraft version.
     * @return The MinecraftVersion object that corresponds to the given protocol ID, or null if no match is found.
     */
    public static MinecraftVersion fromProtocolId(int protocolId) {
        for (var value : values()) {
            if (value.protocolId == protocolId) {
                return value;
            }
        }
        return null;
    }

    /**
     * Returns the MinecraftVersion based on the given name.
     *
     * @param name the name of the MinecraftVersion
     * @return the MinecraftVersion object matching the given name, or null if no match is found
     */
    public static MinecraftVersion fromName(String name) {
        for (var value : values()) {
            if (value.display.equals(name)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Returns the latest Minecraft version.
     *
     * @return the latest Minecraft version
     */
    public static MinecraftVersion latest() {
        MinecraftVersion latestVersion = null;
        for (var version : MinecraftVersion.values()) {
            if (latestVersion == null || version.protocolId > latestVersion.protocolId) {
                latestVersion = version;
            }
        }
        return latestVersion;
    }

}