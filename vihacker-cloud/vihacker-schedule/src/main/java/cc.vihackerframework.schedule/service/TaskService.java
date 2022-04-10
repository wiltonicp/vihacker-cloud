package cc.vihackerframework.schedule.service;


import cc.vihackerframework.schedule.entity.dtos.Task;



/**
 * @author jiangshanchen
 * @title: TaskService
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/2上午9:36
 */

public interface TaskService {


    /**
     * 添加延迟任务
     * @param task
     * @return
     */
    public long addTask(Task task);

    /**
     * 取消任务
     * @param taskId
     * @return
     */
    public boolean cancelTask(long taskId);

    /**
     * 按照类型和优先级拉取任务
     * @param type
     * @param priority
     * @return
     */
    public Task poll(int type,int priority);


}
