package cc.vihackerframework.schedule.entity.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangshanchen
 * @title: Task
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/2上午9:36
 */
@Data
public class Task implements Serializable {

    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 类型
     */
    private Integer taskType;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 执行id
     */
    private long executeTime;

    /**
     * task参数
     */
    private byte[] parameters;
    
}