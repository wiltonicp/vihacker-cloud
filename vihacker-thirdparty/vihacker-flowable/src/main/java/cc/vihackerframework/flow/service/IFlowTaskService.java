package cc.vihackerframework.flow.service;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.flow.entity.FlowTaskVo;
import org.flowable.task.api.Task;

import java.io.InputStream;

/**
 * Created by Ranger on 2022/04/05.
 */
public interface IFlowTaskService {

    /**
     * 审批任务
     *
     * @param task 请求实体参数
     */
    ViHackerApiResult complete(FlowTaskVo task);

    /**
     * 驳回任务
     *
     * @param flowTaskVo
     */
    void taskReject(FlowTaskVo flowTaskVo);


    /**
     * 退回任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void taskReturn(FlowTaskVo flowTaskVo);

    /**
     * 获取所有可回退的节点
     *
     * @param flowTaskVo
     * @return
     */
    ViHackerApiResult findReturnTaskList(FlowTaskVo flowTaskVo);

    /**
     * 删除任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void deleteTask(FlowTaskVo flowTaskVo);

    /**
     * 认领/签收任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void claim(FlowTaskVo flowTaskVo);

    /**
     * 取消认领/签收任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void unClaim(FlowTaskVo flowTaskVo);

    /**
     * 委派任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void delegateTask(FlowTaskVo flowTaskVo);


    /**
     * 转办任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void assignTask(FlowTaskVo flowTaskVo);

    /**
     * 我发起的流程
     * @param pageNum
     * @param pageSize
     * @return
     */
    ViHackerApiResult myProcess(Integer pageNum, Integer pageSize);

    /**
     * 取消申请
     * @param flowTaskVo
     * @return
     */
    ViHackerApiResult stopProcess(FlowTaskVo flowTaskVo);

    /**
     * 撤回流程
     * @param flowTaskVo
     * @return
     */
    ViHackerApiResult revokeProcess(FlowTaskVo flowTaskVo);


    /**
     * 代办任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return
     */
    ViHackerApiResult todoList(Integer pageNum, Integer pageSize);


    /**
     * 已办任务列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return
     */
    ViHackerApiResult finishedList(Integer pageNum, Integer pageSize);

    /**
     * 根据任务ID查询挂载的表单信息
     *
     * @param taskId 任务Id
     * @return
     */
    Task getTaskForm(String taskId);

    /**
     * 获取流程执行过程
     * @param procInsId
     * @return
     */
    ViHackerApiResult getFlowViewer(String procInsId);

    /**
     * 获取流程变量
     * @param taskId
     * @return
     */
    ViHackerApiResult processVariables(String taskId);
}
