package dev.golgolex.golgocloud.instance;

import java.io.IOException;

public final class CloudInstanceBootstrap {
    public static void main(String[] args) {
        if (Runtime.version().feature() < 21) {
            System.out.println("Cloud needs Java 21");
            return;
        }

        try {
            new CloudInstance();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
