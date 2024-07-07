package dev.golgolex.golgocloud.plugin.paper.permission;

/*
 * MIT License
 *
 * Copyright (c) 2024 ClayCloud contributors
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

import dev.golgolex.golgocloud.common.permission.CloudPermissionPool;
import dev.golgolex.quala.reflections.advanced.FieldAccessor;
import dev.golgolex.quala.reflections.advanced.MethodAccessor;
import dev.golgolex.quala.reflections.advanced.Reflexion;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import static dev.golgolex.quala.reflections.advanced.matcher.FieldMatcher.newMatcher;

@UtilityClass
public class PaperPermissionHelper {

    private final Pattern PACKAGE_VERSION_PATTERN = Pattern
            .compile("^org\\.bukkit\\.craftbukkit\\.(\\w+)\\.CraftServer$");
    private final String SERVER_PACKAGE_VERSION;
    private final FieldAccessor PERMISSIBLE_ACCESSOR;
    private final MethodAccessor<?> UPDATE_COMMAND_TREE_ACCESSOR;

    static {
        // Extracts the server package version from the Bukkit server class name using a regex pattern.
        var matcher = PACKAGE_VERSION_PATTERN.matcher(Bukkit.getServer().getClass().getName());
        // If a match is found, sets the server package version accordingly.
        if (matcher.matches()) {
            SERVER_PACKAGE_VERSION = '.' + matcher.group(1) + '.';
        } else {
            SERVER_PACKAGE_VERSION = ".";
        }

        // Finds the permissible accessor field in the CraftHumanEntity class.
        PERMISSIBLE_ACCESSOR = Reflexion.findAny(
                        "org.bukkit.craftbukkit" + SERVER_PACKAGE_VERSION + "entity.CraftHumanEntity",
                        "net.glowstone.entity.GlowHumanEntity"
                )
                .flatMap(reflexion -> reflexion.findField(newMatcher().denyModifier(Modifier.STATIC).derivedType(Field::getType, Permissible.class)))
                .orElseThrow(() -> new NoSuchElementException("Unable to resolve permissible field of player"));

        // Finds the method responsible for updating commands in the Player class.
        UPDATE_COMMAND_TREE_ACCESSOR = Reflexion.on(Player.class).findMethod("updateCommands").orElse(null);
    }

    public void injectPlayer(@NotNull Player player, @NotNull CloudPermissionPool permissionPool) {
        PERMISSIBLE_ACCESSOR.setValue(player, new PaperPermissionInjector(player, permissionPool));
    }

    public void resendCommandTree(@NotNull Player player) {
        if (UPDATE_COMMAND_TREE_ACCESSOR != null) {
            UPDATE_COMMAND_TREE_ACCESSOR.invoke(player);
        }
    }

    public boolean canUpdateCommandTree() {
        return UPDATE_COMMAND_TREE_ACCESSOR != null;
    }

}
