package cn.lili.controller.payment;

import cn.lili.modules.item.entity.OrderBean;
import cn.lili.modules.item.entity.OrderData;
import cn.lili.modules.item.service.IbankService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/store/iBank/iBank")
public class IbankController {


    @Resource
    private IbankService ibankService;


    @ResponseBody
    @RequestMapping("/pay/{zongji}/{ordersn}")
    public String generateOrder(OrderData para){
        //para是生成订单前端传入的数据
        //先生成订单
        OrderBean orderbean=ibankService.createOrder();
        String orderid = orderbean.getOrderid();
        String jym = orderbean.getJym();

    }
}
