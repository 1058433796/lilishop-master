package cn.lili.modules.item.service;

import cn.lili.modules.item.entity.OrderBean;

import org.springframework.stereotype.Service;

@Service
public interface IbankService {
    OrderBean createOrder();

    void saveOrderBean(OrderBean orderBean);

    void copyOrderValueToOrig(OrderBean orderBean);


}