<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<%@ page import="com.spring.util.*" %>
<%@ page import="com.alipay.api.AlipayClient" %>
<%@ page import="com.alipay.api.request.AlipayTradePagePayRequest" %>


<%
    //获得初始化的AlipayClient
    AlipayClient alipayClient = AlipayConfig.getAlipayClient();

    //设置请求参数
    AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
    alipayRequest.setReturnUrl(AlipayConfig.return_url);
    alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

    //商户订单号，商户网站订单系统中唯一订单号
    String out_trade_no = Info.getID()+"-"+ Request.get("biao")+"-"+Request.get("id");
    //付款金额
    String total_amount = request.getParameter("zongji");
    //订单名称
    String subject =request.getParameter("ordersn");
    //商品描述
    String body = "";//new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");

    alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
            + "\"total_amount\":\""+ total_amount +"\","
            + "\"subject\":\""+ subject +"\","
            + "\"body\":\""+ body +"\","
            + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
    //请求 向支付宝发送一个下单请求
    String result = alipayClient.pageExecute(alipayRequest).getBody();

    //输出
    out.println(result);
%>