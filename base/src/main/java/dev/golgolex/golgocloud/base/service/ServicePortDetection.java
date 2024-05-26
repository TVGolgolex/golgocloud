package dev.golgolex.golgocloud.base.service;

import dev.golgolex.golgocloud.common.group.CloudGroup;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public final class ServicePortDetection {

    public static int validPort(@NotNull CloudGroup cloudGroup) {
        var startPort = cloudGroup.startPort();

        while (inUse(startPort)) {
            startPort++;
        }

        return startPort;
    }

    public static boolean inUse(int servicePort) {
        try(var socket = new ServerSocket()) {
            socket.bind(new InetSocketAddress(servicePort));
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
