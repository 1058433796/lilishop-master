package cn.lili.controller.payment;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.modules.item.service.ItemGuarantyService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/store/payMent/payMent")
public class PaymentController {
    @Resource
    private ItemGuarantyService itemGuarantyService;

    //appid
    private final String APP_ID = "2021000121671686";
    //应用私钥
    private final String APP_PRIVATE_KEY ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCbI3sHb9LnCFmLM9tjgMWQT2oWenIBnLItGPHcgIbN6RVxmrD5diJ3zFII8hULAOC/d2b0Rl/mgp0zGgGxcfOizBp8vOl+leUCk55O5+Yp+WDqOUoTuCK4WtO+w5tXmcoJNHD/xWUYQlNSnNdBBq3ciK6dCHa05h8G+/KLCiXqGhfNIxksoXmbyQ4S/WWyjUyjiG7/uN8p2LAQEz2aX0qMc4lmPeOt1D6xBbfKV1iVHDxVGurY1hnyr4A+CCGX+J32EPXjcdyNv4SHWDG5YjgydLZ0asP2ecIDr3OdxiQSv7oSjX9s2LVa+hA0S0utVKHBarnPADKgvdunzMY7TnVjAgMBAAECggEAG+ssrs/u8mjMRILWjneUFj5UZyst7SBX6Em+7OGsYM37b/BYvOwHk+uPL9Dw/IPx7kL8oiUwCPmkdJfPijFpmPiGI8CTfJZZGjeam7Sb9R3N1RjuPdI7XVXrGT92azNf6nwngb/yS4VUHV/gVPFwxGlMkzH8ut1W5BjfMypmXFSCCX+cX6dKMiVXMPtFOFjmWL6U95ZYSFWqM//xY9cadXfrUHiCze1WTPkvNiOqgowSaJal7Tr09rPehbMnfYTGiL3aJ1gPEeofcLN3YFlGzk4d3Fzg/02jdaiWLUmRoQIf5pCrFrvgKtrmtgbgtA+bvVspogIdPw0TpmyYkTsn0QKBgQDLhaMNY96Jvc/2P3c8zmWnNRWIp5CqQhpZ3de2IaejcKwTBSWtj7Vp0y/ovwavehEl8yAbiz2WZLJCaxDKBMwohdDxK56uz98ZTlT3IqphX3hMgytg48tVh/e65TJ0wRqxxEdc0o/5RSvh3JKrMtl0sIMH5CVXOuOgIlTrQ24PTQKBgQDDJBVt3iCfhh4NDZljBy+IORNQIrBDLErDNfZ5Xg1BKjojwgmCr87GZ1T1TgUnNmxwiPDQWP5w2fxChZz54J1fONkc315ZJ3jPAkAiU1Qj7ssB6sNfgl3KknbpQhx/0FNxFk29YQvaBgYPwrJyT6uVqvROb3C7KTLzgwgijumfbwKBgHi8o9lKuPdf+qJsTjFthFKopEi4l5LZukY73D95QbktG3gBIrQPQCEynwZlZPLu6INp809D0FqLDFFosMIYDIGxuR2Jsg6k1QZvOOqPzyILABPr8Oz+1l68VdOzKram5E43UdqYM78+MOVB9GTJusWF/YXN6/UOw4D5PQWAsCc1AoGBAIwYObI4yF7+YFCqtLYdkIhRwYXEgZqYD8x5z0tutEFWWYsWYNGL+f0CbkH2E0kFj3BzP7+0RGcseCgh2IKkORcFrwlFa2zCI78qNHTwMfXiF6h2rHDDHSazY98ucVtpap5Djugo5eNOc8eY1ZevjtXSd7Zf1yTm/QdJyOtWgm7NAoGBAKk/DGK7AGgo0p77au9APAeC7vNSkITz10N0h6X7KHvaqV7pYhJByeSxjjNphdwnLnTGC6INIGcVWaA/0EsUW+JevbTwyWbc9eVVZYdtpTO38TDm/BPuvG1XrD7wOtPH8/S9NPOvsJ99xIbforyE/oq/egu4P4tA7sLHPMK+G5Eg";
    private final String CHARSET = "UTF-8";
    // 支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApNWPyq3T/3HG6NM7wC593HF3Q/TFv4+4T/+CruXfTrcEcj8cyAw7d3J6SpfdzCoxlv1KcJey7OdfaQb0IsyS9jGT6e40GQDzfo4sOomrslDj6b2RvVZOhqKXj3s48P6LcGDe4TBs/VRECl053N8xn4zvNPtHnYSK2wvtdp3aZD1QSRtKhwrK50sydDA2IJ7YZmu+KVrAKVRdnXNvFCFV1ylOV4RTpEhgjYelwa7cayPSa+qmKLAlxMSbo245hUcph41arR/4PspooWVkbFrTE77WuH4C1Jwoxwq2huyFR7YAGH08I6tF7lWFIOopShWhTyrZCFJHFyvfv4uxhaW8AwIDAQAB";
    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
    private final String GATEWAY_URL ="https://openapi.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    //签名方式
    private final String SIGN_TYPE = "RSA2";
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private final String NOTIFY_URL = "http://localhost:8889/store/payMent/payMent/returnUrl123";
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private final String RETURN_URL = "http://172.19.64.1:8889/store/payMent/payMent/returnUrl";

    @ResponseBody
    @RequestMapping("/pay/{zongji}/{ordersn}")
    public String alipay(HttpSession session,
                         @PathVariable("zongji") String dona_money,
                         @PathVariable("ordersn") String dona_id) throws AlipayApiException {
        //money是float
        //id是Int
        //把dona_id项目id 放在session中
        System.out.println("zhifubao zhifu");
        session.setAttribute("dona_id",Integer.parseInt(dona_id.substring(10)));

        //生成订单号（支付宝的要求？）
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String user = UUID.randomUUID().toString().replace("-","").toUpperCase();

        String OrderNum = time+user;

        //调用封装好的方法（给支付宝接口发送请求）
        return sendRequestToAlipay(OrderNum,Float.parseFloat(dona_money),"ghjk");
    }
    private String sendRequestToAlipay(String outTradeNo,Float totalAmount,String subject) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,APP_ID,APP_PRIVATE_KEY,FORMAT,CHARSET,ALIPAY_PUBLIC_KEY,SIGN_TYPE);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(RETURN_URL);
        alipayRequest.setNotifyUrl(NOTIFY_URL);

        //商品描述（可空）
        String body="";
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\","
                + "\"total_amount\":\"" + totalAmount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        return result;
    }}


