package com.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 8:05 下午
 * description：
 */
@Data
public class SayHelloParam implements Serializable {

    private static final long serialVersionUID = -7963604075112264875L;

    /**
     * 名字
     */
    private String name;

}
