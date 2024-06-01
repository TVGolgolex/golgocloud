package dev.golgolex.golgocloud.common.permission;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The PermissionCheckResult class represents the result of a permission check.
 * It is an enum that provides different permission check results.
 */
@Getter
@AllArgsConstructor
public enum PermissionCheckResult {

    /**
     * Represents the permission check result.
     *
     * <p>
     * The ALLOWED variable is an instance of the {@link PermissionCheckResult} enum. It indicates
     * that the permission check was successful and access is allowed.
     * </p>
     */
    ALLOWED(true),
    /**
     * Represents a permission check result.
     *
     * <p>
     * The DENIED variable is an instance of the {@link PermissionCheckResult} enum. It indicates
     * that the permission check was unsuccessful and access is denied.
     * </p>
     */
    DENIED(false),
    /**
     * Represents a wildcard permission in the permission check result.
     *
     * <p>
     * The WILDCARD_PERMISSION variable is an instance of the {@link PermissionCheckResult} enum.
     * It indicates that the permission check was successful and access is allowed, but
     * it represents a wildcard permission.
     * </p>
     */
    WILDCARD_PERMISSION(true),
    /**
     * The FORBIDDEN variable represents a permission check result indicating that access is forbidden.
     * It is a constant in the {@link PermissionCheckResult} enum and has a boolean value of false.
     *
     * <p>
     * Use this constant when checking if access is forbidden in a permission check result.
     * </p>
     */
    FORBIDDEN(false);

    /**
     * Represents the boolean value associated with a specific permission check result.
     */
    private final boolean asBoolean;

    /**
     * Converts the nullable boolean into a permission check result. If the given boolean is null the result is {@code
     * DENIED}, if the boolean is true then {@code ALLOWED}, {@code FORBIDDEN} otherwise.
     *
     * @param value the boolean to convert
     * @return the converted check result.
     */
    public static @NotNull PermissionCheckResult fromBoolean(@Nullable Boolean value) {
        return value == null ? DENIED : value ? ALLOWED : FORBIDDEN;
    }

    /**
     * Converts the given permission into a permission check result. If the permission is null the result is {@code
     * DENIED}, a potency that is not negative results in a {@code ALLOWED}, {@code FORBIDDEN} otherwise.
     *
     * @param permission the permission to convert.
     * @return the converted check result.
     */
    public static @NotNull PermissionCheckResult fromPermission(@Nullable Permission permission) {
        return fromBoolean(permission == null);
    }

}