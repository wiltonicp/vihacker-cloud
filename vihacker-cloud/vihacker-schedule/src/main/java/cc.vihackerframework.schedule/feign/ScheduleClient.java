package cc.vihackerframework.schedule.feign;


import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.schedule.entity.dtos.Task;
import cc.vihackerframework.schedule.service.TaskService;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * @author jiangshanchen
 * @title: ScheduleClient
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/2上午9:36
 */

@RestController
@AllArgsConstructor
@Api(tags = "任务计划调用")
@Slf4j
public class ScheduleClient  {

    @Autowired
    private TaskService taskService;
    /**
     * 添加延迟任务
     *
     * @param task
     * @return
     */
    @PostMapping("/api/v1/task/add")
    public ViHackerApiResult addTask(@RequestBody Task task) {
        log.info("addtask========");

        return ViHackerApiResult.data(taskService.addTask(task));
    }

    /**
     * 取消任务
     *
     * @param taskId
     * @return
     */
    @GetMapping("/api/v1/task/{taskId}")
    public ViHackerApiResult cancelTask(@PathVariable("taskId") long taskId){
        return ViHackerApiResult.data(taskService.cancelTask(taskId));
    }

    /**
     * 按照类型和优先级拉取任务
     *
     * @param type
     * @param priority
     * @return
     */
    @GetMapping("/api/v1/task/{type}/{priority}")
    public ViHackerApiResult poll(@PathVariable("type") int type,@PathVariable("priority") int priority) {


        log.info("consumetask========");


        return ViHackerApiResult.data(taskService.poll(type,priority));
    }
}
