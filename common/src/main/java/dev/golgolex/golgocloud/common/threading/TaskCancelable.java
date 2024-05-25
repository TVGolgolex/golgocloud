package dev.golgolex.golgocloud.common.threading;

/**
 * Created by Tareko on 24.05.2017.
 */
public interface TaskCancelable {

    void cancelTask(Long id);

    void cancelAllTasks();

}
