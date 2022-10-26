package cn.lili.controller.payment;

import cn.lili.modules.demo.UnionPaymentService;
import cn.lili.modules.item.entity.OrderData;
import cn.lili.modules.item.service.IbankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/store/iBank/iBank")
public class IbankController {


    @Resource
    private IbankService ibankService;


    @ResponseBody
    @RequestMapping("/pay/{zongji}/{ordersn}")
    public String generateOrder(OrderData para){
            return "";
    }

    @Autowired
    private UnionPaymentService service;

    /**
     * 支付
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/payNew", method = {RequestMethod.POST, RequestMethod.GET})
    public void pay(HttpServletRequest request, HttpServletResponse response) throws IOException {

        service.pay(request, response);

//        return res;

    }
}
