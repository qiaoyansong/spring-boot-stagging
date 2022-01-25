package result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 8:05 下午
 * description：
 */
@Data
public class SayHelloResult implements Serializable {

    private static final long serialVersionUID = -2698243142977067627L;

    /**
     * 返回的信息
     */
    private String message;

}
