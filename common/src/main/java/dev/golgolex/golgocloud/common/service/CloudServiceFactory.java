package dev.golgolex.golgocloud.common.service;

import java.io.File;

public interface CloudServiceFactory<T extends  CloudService> {

    Process process();

    File dir();

    long startupTimeStamp();

    T cloudService();

    void cloudService(T cloudService);

    void prepare();

    void start();

    void terminate();

    boolean isAlive();

}