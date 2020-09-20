package com.moxi.mougblog.base.result;

/**
 * 统一整个项目中Ajax请求返回的结果（未来也可以用于分布式架构各个模块间调用时返回统一类型）
 *
 * @author 陌溪
 * @date 2020年9月9日18:39:20
 */
public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    /**
     * 用来封装当前请求处理的结果是成功还是失败
     */
    private String result;

    /**
     * 请求处理失败时返回的错误消息
     */
    private String message;

    /**
     * 要返回的数据
     */
    private T data;

    public ResultEntity() {

    }

    public ResultEntity(String result, String message, T data) {
        super();
        this.result = result;
        this.message = message;
        this.data = data;
    }

    /**
     * 请求处理成功且不需要返回数据时使用的工具方法
     *
     * @return
     */
    public static <Type> ResultEntity<Type> successWithoutData() {
        return new ResultEntity<Type>(SUCCESS, null, null);
    }

    /**
     * 请求处理成功且需要返回数据时使用的工具方法
     *
     * @param data 要返回的数据
     * @return
     */
    public static <Type> ResultEntity<Type> successWithData(Type data) {
        return new ResultEntity<Type>(SUCCESS, null, data);
    }

    /**
     * 请求处理失败后使用的工具方法
     *
     * @param message 失败的错误消息
     * @return
     */
    public static <Type> ResultEntity<Type> failed(String message) {
        return new ResultEntity<Type>(FAILED, message, null);
    }

    @Override
    public String toString() {
        return "ResultEntity [result=" + result + ", message=" + message + ", data=" + data + "]";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
