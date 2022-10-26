package cn.lili.modules.item.service;

import cn.lili.modules.item.entity.OrderBean;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Map;

@Service
public interface IbankService {
//    OrderBean createOrder();

    void saveOrderBean(OrderBean orderBean);

//    void copyOrderValueToOrig(OrderBean orderBean);

//    void getPayUrl(String orderId,String txnAmt,String txnTime) throws IOException;




}