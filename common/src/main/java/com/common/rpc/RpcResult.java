package com.common.rpc;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 7:55 下午
 * description：PRC返回结果基类
 */
@Data
public class RpcResult<T> implements Serializable {

    private static final long serialVersionUID = -539152367760093770L;

    private T data;

    private int code;

    private String msg;

    private Map<String, String> attachments = new HashMap();

}

