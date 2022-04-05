package cc.vihackerframework.flow.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 任务意见
 * Created by Ranger on 2022/04/05.
 */
@Data
@Builder
public class FlowCommentDto implements Serializable {

    /**
     * 意见类别 0 正常意见  1 退回意见 2 驳回意见
     */
    private String type;

    /**
     * 意见内容
     */
    private String comment;
}
