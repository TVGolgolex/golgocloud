package dev.golgolex.golgocloud.instance.service;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.environment.CloudServerService;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.golgocloud.instance.configuration.InstanceConfiguration;
import dev.golgolex.quala.Quala;
import dev.golgolex.quala.utils.color.ConsoleColor;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public final class CloudServiceProcessQueue implements Runnable {

    private final Queue<CloudService> processQueue = new ConcurrentLinkedDeque<>();
    private final boolean running = true;

    // not used
    public CloudServiceProcessQueue(int processQueueSize) {
    }

    public void put(@NotNull CloudService cloudService) {
        this.processQueue.offer(cloudService);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Quala.sleepUninterruptedly(500);
            var i = new AtomicInteger();

            CloudInstance.instance().configurationService().configurationOptional("instance").ifPresent(configurationClass -> {
                var instanceConfiguration = (InstanceConfiguration) configurationClass;
                while (running && !processQueue.isEmpty() && (instanceConfiguration.percentOfCPUForANewServer() == 0D
                        || Quala.cpuUsage() <= instanceConfiguration.percentOfCPUForANewServer())) {
                    i.getAndIncrement();
                    if (i.get() == 3) {
                        break;
                    }

                    var memory = CloudInstance.instance().serviceProvider().usedMemory();
                    var cloudService = processQueue.poll();

                    if (cloudService == null) return;
                    if (memory + cloudService.memory() < instanceConfiguration.maximalMemory()) {
                        switch (cloudService.environment()) {
                            case SERVER -> {
                                var factory = new CloudServerServiceFactory((CloudServerService) cloudService);
                                factory.prepare();
                            }
                            case PROXY -> {
                                // todo
                            }
                        }
                    } else {
                        this.processQueue.add(cloudService);
                    }
                }
            });
        }
    }
}
