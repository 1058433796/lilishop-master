package cn.lili.modules.item.service.serviceImpl;


import cn.lili.modules.item.entity.OrderBean;
import cn.lili.modules.item.entity.enums.*;
import cn.lili.modules.item.mapper.IbankMapper;
import cn.lili.modules.item.service.IbankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.util.logging.Logger;
@Service
public class IbankServiceImpl extends ServiceImpl<IbankMapper, OrderBean> implements IbankService {
    @Resource
    private IbankMapper ibankMapper;

    private StringHandler stringHandler;
//    public OrderBean createOrder(){
//        //生成订单号
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String datestr =sdf.format(date).replace("-","");// DateUtil.getCurrentDateToString("yyMMdd");可能年份的位数有区分
//        String seqstr=String.valueOf((Math.random() * 9 + 1) * 1000);
//        String orderid = datestr+seqstr;
//        //生成校验码
//        double authkey=Math.random();
//        Double valueD = authkey * 1000000;
//        String jym = valueD.intValue() + "";
//        OrderBean orderBean=new OrderBean();
//        orderBean.setOrderid(orderid);
//        //还是先查询
//        OrderBean res=ibankMapper.findData(orderid);
//        if(res!=null){
//            //有订单
//            orderBean.setInDB(true);
//            orderBean.setJym(res.getJym());
//            orderBean.setAppid(res.getAppid());
//            orderBean.setOstate(res.getOstate());
//            orderBean.setAstate(res.getAstate());
//            orderBean.setDsforderid(res.getDsforderid());
//            orderBean.setJgid(res.getJgid());
//            orderBean.setYwlxid(res.getYwlxid());
//            orderBean.setJfje(res.getJfje());
//            orderBean.setCustomid(res.getCustomid());
//            orderBean.setDescription(res.getDescription());
//            orderBean.setCustomid(res.getCustomid());
//            orderBean.setYhid(res.getYhid());
//            orderBean.setShid(res.getShid());
//            orderBean.setBstate(res.getBstate());
//            orderBean.setNstate(res.getNstate());
//            orderBean.setCstate(res.getCstate());
//            orderBean.setTradingid(res.getTradingid());
//            orderBean.setJfsj(res.getJfsj());
//            orderBean.setIstate(res.getIstate());
//            orderBean.setIactkey(res.getIactkey());
//            orderBean.setCustomname(res.getCustomname());
//            orderBean.setExtension(res.getExtension());
//            orderBean.setRefundingredid(res.getRefundingredid());
//            orderBean.setRefundjebyredids(String.valueOf(res.getRefundjebyredids()));
//            orderBean.setOrdertype(res.getOrdertype());
//            orderBean.setShbh(res.getShbh());
//            orderBean.setZdbh(res.getZdbh());
//            orderBean.setPch(res.getPch());
//            orderBean.setPzh(res.getPzh());
//            orderBean.setCkh(res.getCkh());
//            orderBean.setClr(res.getClr());
//            orderBean.setClrq(res.getClrq());
//            orderBean.setClzy(res.getClzy());
//            orderBean.setYhkh(res.getYhkh());
//            orderBean.setOperid(res.getOperid());
//            orderBean.setTstate(res.getTstate());
//            orderBean.setExtensionforbank(res.getExtensionforbank());
//
//        }else{
//            orderBean.setInDB(false);
//            orderBean.setJym("");
//            orderBean.setAppid("");
//            orderBean.setOstate("");
//            orderBean.setAstate("");
//            orderBean.setDsforderid("");
//            orderBean.setJgid("");
//            orderBean.setYwlxid("");
//            orderBean.setJfje("0");
//            orderBean.setCustomid("");
//            orderBean.setDescription("");
//            orderBean.setCustomid("");
//            orderBean.setYhid("");
//            orderBean.setShid("");
//            orderBean.setBstate("");
//            orderBean.setNstate("");
//            orderBean.setCstate("");
//            orderBean.setTradingid("");
//            orderBean.setJfsj("");
//            orderBean.setIstate("");
//            orderBean.setIactkey("");
//            orderBean.setCustomname("");
//            orderBean.setExtension("");
//            orderBean.setRefundingredid("");
//            orderBean.setRefundjebyredids("0");
//            orderBean.setOrdertype("");
//            orderBean.setShbh("");
//            orderBean.setZdbh("");
//            orderBean.setPch("");
//            orderBean.setPzh("");
//            orderBean.setCkh("");
//            orderBean.setClr("");
//            orderBean.setClrq("");
//            orderBean.setClzy("");
//            orderBean.setYhkh("");
//            orderBean.setOperid("");
//            orderBean.setTstate("");
//            orderBean.setExtensionforbank("");
//        }
//        this.copyOrderValueToOrig(orderBean);
//        orderBean.setOstate(OState.CREATED.getName());
//        orderBean.setAstate(AState.NA.getName());
//        orderBean.setNstate(NState.NA.getName());
//        orderBean.setCstate(CState.NA.getName());
//        orderBean.setBstate(BState.NA.getName());
//        orderBean.setIstate(IState.NA.getName());
//        orderBean.setTstate(TState.NA.getName());
//        this.copyOrderValueToOrig(orderBean);
//        this.saveOrderBean(orderBean);
//        return orderBean;
//
//    }
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveOrderBean(OrderBean orderBean){
            this.save(orderBean);
    }
    private static final Map<String, KeyStore> certKeyStoreMap = new ConcurrentHashMap();
    private Logger loga = Logger.getLogger(String.valueOf(this.getClass()));
//    @Override
//    @Transactional(rollbackFor = {Exception.class})
//    public void copyOrderValueToOrig(OrderBean orderBean){
//        orderBean.setOperid_orig(orderBean.getOrderid());
//        orderBean.setJym_orig(orderBean.getJym());
//        orderBean.setAppid_orig(orderBean.getAppid());
//        orderBean.ostate_orig = new String(stringHandler.handleString(orderBean.getOstate(), ""));
//        orderBean.astate_orig = new String(stringHandler.handleString(orderBean.getAstate(), ""));
//        orderBean.dsforderid_orig = new String(stringHandler.handleString(orderBean.getDsforderid(), ""));
//        orderBean.jgid_orig = new String(stringHandler.handleString(orderBean.getJgid(), ""));
//        orderBean.ywlxid_orig = new String(stringHandler.handleString(ywlxid, ""));
//        orderBean.customid_orig = new String(stringHandler.handleString(customid, ""));
//        orderBean.description_orig = new String(stringHandler.handleString(description, ""));
//        orderBean.yhid_orig = new String(stringHandler.handleString(yhid, ""));
//        orderBean.shid_orig = new String(stringHandler.handleString(shid, ""));
//        orderBean.bstate_orig = new String(stringHandler.handleString(bstate, ""));
//        orderBean.nstate_orig = new String(stringHandler.handleString(nstate, ""));
//        orderBean.cstate_orig = new String(stringHandler.handleString(cstate, ""));
//        orderBean.tradingid_orig = new String(stringHandler.handleString(tradingid, ""));
//        orderBean.jfsj_orig = new String(stringHandler.handleString(jfsj, ""));
//        orderBean.qssj_orig = new String(stringHandler.handleString(qssj, ""));
//        orderBean.istate_orig = new String(stringHandler.handleString(istate, ""));
//        orderBean.iactkey_orig = new String(stringHandler.handleString(iactkey, ""));
//        orderBean.jfje_orig = new String(stringHandler.handleString(jfje, "0"));
//        orderBean.customname_orig = new String(stringHandler
//                .handleString(this.customname, ""));
//        orderBean.customtype_orig = new String(stringHandler
//                .handleString(this.customtype, ""));
//        // 扩展字段
//        orderBean.extension_orig = new String(stringHandler.handleString(this.extension, ""));
//        orderBean.refundingredid_orig = new String(stringHandler.handleString(
//                this.refundingredid, ""));
//        orderBean.refundjebyredids_orig = new String(stringHandler.handleString(
//                this.refundjebyredids, ""));
//        orderBean.ordertype_orig = new String(stringHandler.handleString(this.ordertype, ""));
//        orderBean.shbh_orig = new String(stringHandler.handleString(this.shbh, ""));
//        orderBean.zdbh_orig = new String(stringHandler.handleString(this.zdbh, ""));
//        orderBean.pch_orig = new String(stringHandler.handleString(this.pch, ""));
//        orderBean.pzh_orig = new String(stringHandler.handleString(this.pzh, ""));
//        orderBean.ckh_orig = new String(stringHandler.handleString(this.ckh, ""));
//        orderBean.clr_orig = new String(stringHandler.handleString(this.clr, ""));
//        orderBean.clrq_orig = new String(stringHandler.handleString(this.clrq, ""));
//        orderBean.clzy_orig = new String(stringHandler.handleString(this.clzy, ""));
//        orderBean.yhkh_orig = new String(stringHandler.handleString(this.yhkh, ""));
//        orderBean.operid_orig = new String(stringHandler.handleString(this.operid, ""));
//        orderBean.extensionforbank_orig = new String(stringHandler.handleString(this.extensionforbank, ""));
//        orderBean.tstate_orig = new String(stringHandler.handleString(this.tstate, ""));
//
//
//    }
//    @Override
//    public void getPayUrl(String orderId,String txnAmt,String txnTime) throws IOException {
//        Map<String, String> data = new HashMap<String, String>();
//        data.put("version", "5.0.0");
//        data.put("encoding", "UTF-8");
//        // 签名方法 01 RSA
//        data.put("signMethod","01");
//        // 交易类型 01-消费
//        data.put("txnType", "01");
//        // 交易子类型 01:自助消费 02:订购 03:分期付款
//        data.put("txnSubType", "01");
//        // 业务类型
//        data.put("bizType", "000202");
//        // 渠道类型，07-PC，08-手机
//        data.put("channelType","07");
//        // 前台通知地址 ，控件接入方式无作用
//        data.put("frontUrl", "");
//        // 后台通知地址
//        data.put("backUrl", "http://www.specialUrl.com");
//        // 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
//        data.put("accessType", "0");
//        // 商户号码，请改成自己的商户号
//        data.put("merId", "37050161667600000374");
//        // 商户订单号，8-40位数字字母
//        data.put("orderId", orderId);
//        // 订单发送时间，取系统时间
//        data.put("txnTime", txnTime);
//        // 交易金额，单位分
//        data.put("txnAmt",txnAmt);
//        // 交易币种
//        data.put("currencyCode",  "156");
//        Map<String, String> submitFromData = CUPACPUtil.signData(data,
//                "C\\:\\\\cupacp\\\\b2b\\\\PRO_700000000000001_acp.pfx", "123456");
//        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();
//
//        String html = CUPACPUtil.createHtml(requestFrontUrl, submitFromData);
//
//        loga.info("银联全渠道ACP-B2B支付请求的报文串：" + html);
////        OrderLogUtil.savePayLog(this.orderid, "1", "银联全渠道ACP-B2B支付请求的报文串：" + html);
//
//
//    }


}
