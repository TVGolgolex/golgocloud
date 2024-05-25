package dev.golgolex.golgocloud.base;

import java.io.IOException;

public final class CloudBaseBootstrap {
    public static void main(String[] args) {
        if (Runtime.version().feature() < 21) {
            System.out.println("Cloud needs Java 21");
            return;
        }

        try {
            new CloudBase();
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
