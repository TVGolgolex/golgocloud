package dev.golgolex.golgocloud.instance.service;

import dev.golgolex.golgocloud.common.service.CloudServiceFactory;
import dev.golgolex.golgocloud.common.threading.Scheduler;
import dev.golgolex.golgocloud.instance.CloudInstance;

import java.util.ArrayList;

public final class CloudServiceWorkerThread implements Runnable {

    public void init(Scheduler scheduler) {
        scheduler.runTaskRepeatSync(this, 0, 20);
    }

    @Override
    public void run() {
        for (var cloudServiceFactory : new ArrayList<>(CloudInstance.instance().serviceProvider().serviceFactories())) {
            if (!cloudServiceFactory.isAlive() && cloudServiceFactory.startupTimeStamp() != 0
                    && (System.currentTimeMillis() > (cloudServiceFactory.startupTimeStamp() + 10000))) {
                cloudServiceFactory.terminate();
            }
        }
    }
}
