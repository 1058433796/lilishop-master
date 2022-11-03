package cn.lili.common.enums;

import cn.lili.common.vo.ResultMessage;
import cn.lili.common.vo.ResultMessageG;
import org.apache.poi.ss.formula.functions.T;

public class ResultUtilG<T> {
    private final ResultMessageG<T> resultMessage;

    private static final Integer SUCCESS = 200;

    public ResultUtilG() {
        resultMessage = new ResultMessageG<>();
        resultMessage.setSuccess(true);
        resultMessage.setMessage("success");
        resultMessage.setCode(SUCCESS);
    }
    public ResultMessageG<T> setData(T t) {
        this.resultMessage.setData(t);
        return this.resultMessage;
    }
    public static <T> ResultMessageG<T> data(T t) {
        return new ResultUtilG<T>().setData(t);
    }

    /**
     * 返回成功
     *
     * @param resultCode 返回状态码
     * @return 消息
     */
    public static <T> ResultMessageG<T> success(ResultCode resultCode) {
        return new ResultUtilG<T>().setSuccessMsg(resultCode);
    }

    public ResultMessageG<T> setSuccessMsg(ResultCode resultCode) {
        this.resultMessage.setSuccess(true);
        this.resultMessage.setMessage(resultCode.message());
        this.resultMessage.setCode(resultCode.code());
        return this.resultMessage;

    }


    /**
     * 返回成功
     * @return 消息
     */
    public static <T> ResultMessageG<T> success() {
        return new ResultUtilG<T>().setSuccessMsg(ResultCode.SUCCESS);
    }

    /**
     * 返回失败
     *
     * @param resultCode 返回状态码
     * @return 消息
     */
    public static <T> ResultMessageG<T> error(ResultCode resultCode) {
        return new ResultUtilG<T>().setErrorMsg(resultCode);
    }

    /**
     * 返回失败
     *
     * @param code 状态码
     * @param msg  返回消息
     * @return 消息
     */
    public static <T> ResultMessageG<T> error(Integer code, String msg) {
        return new ResultUtilG<T>().setErrorMsg(code, msg);
    }

    /**
     * 服务器异常 追加状态码
     * @param resultCode 返回码
     * @return 消息
     */
    public ResultMessageG<T> setErrorMsg(ResultCode resultCode) {
        this.resultMessage.setSuccess(false);
        this.resultMessage.setMessage(resultCode.message());
        this.resultMessage.setCode(resultCode.code());
        return this.resultMessage;
    }

    /**
     * 服务器异常 追加状态码
     *
     * @param code 状态码
     * @param msg  返回消息
     * @return 消息
     */
    public ResultMessageG<T> setErrorMsg(Integer code, String msg) {
        this.resultMessage.setSuccess(false);
        this.resultMessage.setMessage(msg);
        this.resultMessage.setCode(code);
        return this.resultMessage;
    }


}
