package dev.golgolex.golgocloud.base.service;

import dev.golgolex.golgocloud.base.CloudBase;
import lombok.AllArgsConstructor;

import java.util.logging.Level;

@AllArgsConstructor
public final class CloudServiceWorkerThread implements Runnable {

    private final CloudBase cloudBase;

    public void init() {
        cloudBase.scheduler().runTaskRepeatSync(this, 0, 60);
    }

    /**
     * The run method checks and prepares services for each cloud group.
     * If the maximal or minimal service count is reached for a cloud group, the method returns.
     * If the service preparation fails for a group, an error message is logged.
     */
    @Override
    public void run() {
        for (var cloudGroup : cloudBase.groupProvider().cloudGroups()) {
            var services = cloudBase.serviceProvider().runningAndWaiting(cloudGroup.name());

            if (cloudGroup.maximalServiceCount() < services.size() && cloudGroup.maximalServiceCount() != -1) {
                return;
            }

            if (cloudGroup.minimalServiceCount() <= services.size()) {
                return;
            }

            var prepare = this.cloudBase.serviceProvider().constructService(cloudGroup);
            if (prepare == null) {
                CloudBase.instance().logger().log(Level.SEVERE, "Cannot prepare service for group: " + cloudGroup.name());
                return;
            }
            this.cloudBase.serviceProvider().prepareService(prepare);
        }
    }
}