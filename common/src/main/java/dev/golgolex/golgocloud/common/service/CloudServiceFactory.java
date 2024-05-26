package dev.golgolex.golgocloud.common.service;

import java.io.File;

public interface CloudServiceFactory<T extends  CloudService> {

    /**
     * Executes the process.
     *
     * @return the result of the process.
     */
    Process process();

    /**
     * Returns the directory associated with the CloudServiceFactory.
     *
     * @return the directory
     */
    File dir();

    /**
     * Returns the timestamp when the CloudService was started.
     *
     * @return the startup timestamp
     */
    long startupTimeStamp();

    /**
     * Retrieves the CloudService associated with this CloudServiceFactory.
     *
     * @return the CloudService associated with this CloudServiceFactory
     */
    T cloudService();

    /**
     * This method sets the cloud service for the CloudServiceFactory.
     * The cloud service represents either a CloudProxyService or a CloudServerService.
     *
     * @param cloudService the cloud service to set
     */
    void cloudService(T cloudService);

    /**
     * Prepares the cloud service for starting.
     */
    void prepare();

    /**
     * Starts the cloud service.
     *
     * <p>This method performs the necessary steps to start the cloud service. It configures the file system for the server,
     * inserts required files, creates necessary directories, sets up server properties, and executes the startup command.
     * After starting the cloud service, it sends a packet to notify the network channel and logs a message with the service ID.</p>
     *
     * @throws RuntimeException if an error occurs during the startup process
     */
    void start();

    /**
     * Terminates the cloud service.
     */
    void terminate();

    /**
     * Checks if the cloud service is alive.
     *
     * @return true if the cloud service is alive, false otherwise.
     */
    boolean isAlive();

}