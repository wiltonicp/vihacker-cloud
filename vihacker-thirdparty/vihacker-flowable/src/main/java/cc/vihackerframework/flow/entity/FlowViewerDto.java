package cc.vihackerframework.flow.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Ranger on 2022/04/05.
 */
@Data
public class FlowViewerDto implements Serializable {

    private String key;
    private boolean completed;
}
