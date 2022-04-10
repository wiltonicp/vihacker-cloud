package cc.vihackerframework.schedule.entity.constants;

/**
 * @author jiangshanchen
 * @title: FileConstant
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/2上午9:36
 */
public class ScheduleConstants {

    //task状态
    public static final int SCHEDULED=0;   //初始化状态

    public static final int EXECUTED=1;       //已执行状态

    public static final int CANCELLED=2;   //已取消状态

    public static String FUTURE="future_";   //未来数据key前缀

    public static String TOPIC="topic_";     //当前数据key前缀
}