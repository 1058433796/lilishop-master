package cn.lili.modules.statistics.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.utils.CurrencyUtil;
import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.contract.service.ContractService;
import cn.lili.modules.item.service.ItemGuarantyService;
import cn.lili.modules.item.service.ItemService;
import cn.lili.modules.itemOrder.entity.dos.ItemOrder;
import cn.lili.modules.itemOrder.entity.vo.ItemOrderSimpleVO;
import cn.lili.modules.order.order.entity.enums.FlowTypeEnum;
import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
import cn.lili.modules.statistics.entity.dto.StatisticsQueryParam;
import cn.lili.modules.statistics.entity.vo.HomeStatisticDataVO;
import cn.lili.modules.statistics.entity.vo.OrderOverviewVO;
import cn.lili.modules.statistics.entity.vo.OrderStatisticsDataVO;
import cn.lili.modules.statistics.mapper.ItemOrderStatisticsMapperZy;
import cn.lili.modules.statistics.service.ItemOrderStatisticsServiceZy;
import cn.lili.modules.statistics.service.PlatformViewService;
import cn.lili.modules.statistics.service.StoreFlowStatisticsService;
import cn.lili.modules.statistics.util.StatisticsDateUtil;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 订单统计业务层实现
 *
 * @author Bulbasaur
 * @since 2020/12/9 17:16
 */
@Service
public class ItemOrderStatisticsServiceZyImpl extends ServiceImpl<ItemOrderStatisticsMapperZy, ItemOrder> implements ItemOrderStatisticsServiceZy {

    /**
     * 平台PV统计
     */
    @Autowired
    private PlatformViewService platformViewService;

    @Autowired
    private StoreFlowStatisticsService storeFlowStatisticsService;

    @Autowired
    private ItemGuarantyService itemGuarantyService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ItemService itemService;
    @Override
    public OrderOverviewVO overview(StatisticsQueryParam statisticsQueryParam) {
        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);

        OrderOverviewVO orderOverviewVO = new OrderOverviewVO();

        /**
         * 组织统计初始化
         */
        //storeFlowStatisticsService.overview(dates, orderOverviewVO, statisticsQueryParam);
        //付款订单数，付款金额
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.between("create_time", dates[0], dates[1]);
        //如果有店铺id传入，则查询店铺
        if (StringUtils.isNotEmpty(statisticsQueryParam.getStoreId())) {
            queryWrapper.eq("buyer_id", statisticsQueryParam.getStoreId());
        }
        queryWrapper.select("SUM(order_amount) AS trade_amount , COUNT(0) AS num");
        queryWrapper.eq("pay_status", "已付款");
        Map payment = this.getMap(queryWrapper);
        // 已付款订单数
        orderOverviewVO.setPaymentOrderNum(payment != null && payment.containsKey("num") ? (Long) payment.get("num") : 0L);
        // 已付款总金额
        orderOverviewVO.setPaymentAmount(payment != null && payment.containsKey("trade_amount") ? Double.parseDouble(payment.get("trade_amount").toString()) : 0D);


        queryWrapper = Wrappers.query();
        queryWrapper.between("create_time", dates[0], dates[1]);
        //如果有店铺id传入，则查询店铺
        if (StringUtils.isNotEmpty(statisticsQueryParam.getStoreId())) {
            queryWrapper.eq("buyer_id", statisticsQueryParam.getStoreId());
        }
        queryWrapper.select("SUM(order_amount) AS total_order_amount , COUNT(0) AS total_orders");
        queryWrapper.groupBy("buyer_id");
        Map totalMemberNum = this.getMap(queryWrapper);
        // 所有订单数（包括已付款的和未付款的）
        orderOverviewVO.setTotalOrders(totalMemberNum != null && totalMemberNum.containsKey("total_orders") ? (Long) totalMemberNum.get("total_orders") : 0L);
        // 所有订单总金额（包括已付款的和未付款的）
        orderOverviewVO.setOrderAmount(totalMemberNum != null && totalMemberNum.containsKey("total_order_amount") ? Double.parseDouble(totalMemberNum.get("total_order_amount").toString()) : 0D);

        return orderOverviewVO;
    }

    @Override
    public HomeStatisticDataVO homeStatistic(String storeId) {
        HomeStatisticDataVO homeStatisticData = new HomeStatisticDataVO();
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.eq("buyer_id", storeId);
        queryWrapper.select("SUM(order_amount) AS trade_amount , COUNT(0) AS num");
        queryWrapper.eq("pay_status", "已付款");

        Map payment = this.getMap(queryWrapper);
        // 已付款订单数
        homeStatisticData.setPayedOrderNum(payment != null && payment.containsKey("num") ? (Long) payment.get("num") : 0L);
        // 已付款总金额
        homeStatisticData.setPayedOrderAmount(payment != null && payment.containsKey("trade_amount") ? Double.parseDouble(payment.get("trade_amount").toString()) : 0D);
        queryWrapper = Wrappers.query();
        queryWrapper.eq("buyer_id", storeId);
        queryWrapper.select("SUM(order_amount) AS total_order_amount , COUNT(0) AS total_orders");
        queryWrapper.groupBy("buyer_id");
        Map totalMemberNum = this.getMap(queryWrapper);
        // 所有订单数（包括已付款的和未付款的）
        homeStatisticData.setTotalOrderNum(totalMemberNum != null && totalMemberNum.containsKey("total_orders") ? (Long) totalMemberNum.get("total_orders") : 0L);
        // 所有订单总金额（包括已付款的和未付款的）
        homeStatisticData.setTotalOrderAmount(totalMemberNum != null && totalMemberNum.containsKey("total_order_amount") ? Double.parseDouble(totalMemberNum.get("total_order_amount").toString()) : 0D);
        homeStatisticData.setProductNum(itemService.getStoreProductNum(storeId));
        // 查询待响应订单数
        queryWrapper = Wrappers.query();
        queryWrapper.eq("buyer_id", storeId);
        queryWrapper.eq("buyer_reply", "未响应");
        queryWrapper.select("count(*) AS to_res_order");
        Map waitToSignOrder = this.getMap(queryWrapper);
        homeStatisticData.setWaitToSignOrder(waitToSignOrder != null && waitToSignOrder.containsKey("to_res_order") ? (Long) waitToSignOrder.get("to_res_order") : 0L);

        homeStatisticData.setWaitToSignGuaranty(itemGuarantyService.waitToSignGuaranty(storeId));
        homeStatisticData.setWaitToSignContract(contractService.waitToSignContract(storeId));

        return homeStatisticData;
    }
    /**
     * 运算转换率
     *
     * @param orderOverviewVO 订单统计视图
     */
    private void conversionRateOperation(OrderOverviewVO orderOverviewVO) {

        //下单转换率 订单数/UV
        Double orderConversionRate = CurrencyUtil.div(orderOverviewVO.getOrderNum(), orderOverviewVO.getUvNum(), 4);
        if (orderConversionRate > 1) {
            orderConversionRate = 1d;
        }
        orderOverviewVO.setOrderConversionRate(CurrencyUtil.mul(orderConversionRate, 100) + "%");
        //付款转换率 付款订单数/订单数
        Double paymentsConversionRate = CurrencyUtil.div(orderOverviewVO.getPaymentOrderNum(), orderOverviewVO.getOrderNum(), 4);
        if (paymentsConversionRate > 1) {
            paymentsConversionRate = 1d;
        }
        orderOverviewVO.setPaymentsConversionRate(CurrencyUtil.mul(paymentsConversionRate, 100) + "%");
        //整体转换率 付款数/UV
        Double overallConversionRate = CurrencyUtil.div(orderOverviewVO.getPaymentOrderNum(), orderOverviewVO.getUvNum(), 4);
        if (overallConversionRate > 1) {
            overallConversionRate = 1d;
        }
        orderOverviewVO.setOverallConversionRate(CurrencyUtil.mul(overallConversionRate, 100) + "%");
    }

    @Override
    public long orderNum(String orderStatus) {
        LambdaQueryWrapper<ItemOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CharSequenceUtil.isNotEmpty(orderStatus), ItemOrder::getOrderStatus, orderStatus);
        queryWrapper.eq(CharSequenceUtil.equals(Objects.requireNonNull(UserContext.getCurrentUser()).getRole().name(), UserEnums.STORE.name()),
                ItemOrder::getStoreId, UserContext.getCurrentUser().getStoreId());
        return this.count(queryWrapper);
    }

    @Override
    public List<OrderStatisticsDataVO> statisticsChart(StatisticsQueryParam statisticsQueryParam) {
        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);
        QueryWrapper queryWrapper = new QueryWrapper();
        //已支付
        queryWrapper.eq("pay_status", "已付款");
        //选择商家判定
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsQueryParam.getStoreId()), "buyer_id", statisticsQueryParam.getStoreId());
//      查询时间区间
        queryWrapper.between("create_time", dates[0], dates[1]);
//       格式化时间
        queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m-%d')");
        List<OrderStatisticsDataVO> orderStatisticsDataVOS = this.baseMapper.getOrderStatisticsData(queryWrapper);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dates[0]);

        List<OrderStatisticsDataVO> result = new ArrayList<>();
        //时间判定，将数据填充好
        //如果当前的时间，在结束时间之前
        while (calendar.getTime().before(dates[1])) {
            OrderStatisticsDataVO item = null;
            //判定是否已经有这一天的数据
            for (OrderStatisticsDataVO orderStatisticsDataVO : orderStatisticsDataVOS) {
                if (orderStatisticsDataVO.getCreateTime().equals(calendar.getTime())) {
                    item = orderStatisticsDataVO;
                }
            }
            //如果数据不存在，则进行数据填充
            if (item == null) {
                item = new OrderStatisticsDataVO();
                item.setPrice(0d);
                item.setCreateTime(calendar.getTime());
            }
            result.add(item);
            //增加时间
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }
        return result;
    }

    @Override
    public IPage<ItemOrderSimpleVO> getStatistics(StatisticsQueryParam statisticsQueryParam, PageVO pageVO) {

        QueryWrapper<OrderSimpleVO> queryWrapper = new QueryWrapper<>();
        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);
        System.out.println("时间"+dates[0]+dates[1]);
        queryWrapper.between("o.create_time", dates[0], dates[1]);
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsQueryParam.getStoreId()),
                "o.buyer_id", statisticsQueryParam.getStoreId());
        System.out.println("现在的ID"+statisticsQueryParam.getStoreId());

        queryWrapper.groupBy("o.order_id");
        queryWrapper.orderByDesc("o.order_id");
        return this.baseMapper.queryByParams(PageUtil.initPage(pageVO), queryWrapper);
    }

    private QueryWrapper getQueryWrapper(StatisticsQueryParam statisticsQueryParam) {

        QueryWrapper queryWrapper = Wrappers.query();

        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);
        queryWrapper.between("create_time", dates[0], dates[1]);

        //设置店铺ID
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsQueryParam.getStoreId()), "buyer_id", statisticsQueryParam.getStoreId());

        //设置为付款查询
        queryWrapper.eq("flow_type", FlowTypeEnum.PAY.name());

        return queryWrapper;
    }

}

//
//package cn.lili.modules.statistics.serviceimpl;
//
//import cn.hutool.core.text.CharSequenceUtil;
//import cn.lili.common.security.context.UserContext;
//import cn.lili.common.security.enums.UserEnums;
//import cn.lili.common.utils.CurrencyUtil;
//import cn.lili.common.utils.StringUtils;
//import cn.lili.common.vo.PageVO;
//import cn.lili.modules.itemOrder.entity.ItemOrder;
//import cn.lili.modules.itemOrder.entity.ItemOrderSimpleVO;
//import cn.lili.modules.order.order.entity.enums.FlowTypeEnum;
//import cn.lili.modules.order.order.entity.vo.OrderSimpleVO;
//import cn.lili.modules.statistics.entity.dto.StatisticsQueryParam;
//import cn.lili.modules.statistics.entity.vo.OrderOverviewVO;
//import cn.lili.modules.statistics.entity.vo.OrderStatisticsDataVO;
//import cn.lili.modules.statistics.mapper.ItemOrderStatisticsMapper;
//import cn.lili.modules.statistics.service.ItemOrderStatisticsService;
//import cn.lili.modules.statistics.service.PlatformViewService;
//import cn.lili.modules.statistics.service.StoreFlowStatisticsService;
//import cn.lili.modules.statistics.util.StatisticsDateUtil;
//import cn.lili.mybatis.util.PageUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
///**
// * 订单统计业务层实现
// *
// * @author Bulbasaur
// * @since 2020/12/9 17:16
// */
//@Service
//public class ItemOrderStatisticsServiceImpl extends ServiceImpl<ItemOrderStatisticsMapper, ItemOrder> implements ItemOrderStatisticsService {
//
//    /**
//     * 平台PV统计
//     */
//    @Autowired
//    private PlatformViewService platformViewService;
//
//    @Autowired
//    private StoreFlowStatisticsService storeFlowStatisticsService;
//
//    @Override
//    public OrderOverviewVO overview(StatisticsQueryParam statisticsQueryParam) {
//        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);
//
//        OrderOverviewVO orderOverviewVO = new OrderOverviewVO();
//
//        /**
//         * 组织统计初始化
//         */
//        //storeFlowStatisticsService.overview(dates, orderOverviewVO, statisticsQueryParam);
//        //付款订单数，付款金额
//        QueryWrapper queryWrapper = Wrappers.query();
//        queryWrapper.between("create_time", dates[0], dates[1]);
//        //如果有店铺id传入，则查询店铺
//        if (StringUtils.isNotEmpty(statisticsQueryParam.getStoreId())) {
//            queryWrapper.eq("store_id", statisticsQueryParam.getStoreId());
//        }
//        queryWrapper.select("SUM(order_amount) AS trade_amount , COUNT(0) AS num");
//        queryWrapper.eq("pay_status", "已付款");
//        Map payment = this.getMap(queryWrapper);
//
//        orderOverviewVO.setPaymentOrderNum(payment != null && payment.containsKey("num") ? (Long) payment.get("num") : 0L);
//        orderOverviewVO.setPaymentAmount(payment != null && payment.containsKey("trade_amount") ? Double.parseDouble(payment.get("trade_amount").toString()) : 0D);
//
//        //付款人数
//        queryWrapper = Wrappers.query();
//        queryWrapper.between("create_time", dates[0], dates[1]);
//        //如果有店铺id传入，则查询店铺
//        if (StringUtils.isNotEmpty(statisticsQueryParam.getStoreId())) {
//            queryWrapper.eq("store_id", statisticsQueryParam.getStoreId());
//        }
//        queryWrapper.select("COUNT(0) AS num");
//        queryWrapper.groupBy("buyer_id");
//        Map paymentMemberNum = this.getMap(queryWrapper);
//
//        orderOverviewVO.setPaymentsNum(paymentMemberNum != null && paymentMemberNum.containsKey("num") ? (Long) paymentMemberNum.get("num") : 0L);
//        return orderOverviewVO;
//    }
//
//    /**
//     * 运算转换率
//     *
//     * @param orderOverviewVO 订单统计视图
//     */
//    private void conversionRateOperation(OrderOverviewVO orderOverviewVO) {
//
//        //下单转换率 订单数/UV
//        Double orderConversionRate = CurrencyUtil.div(orderOverviewVO.getOrderNum(), orderOverviewVO.getUvNum(), 4);
//        if (orderConversionRate > 1) {
//            orderConversionRate = 1d;
//        }
//        orderOverviewVO.setOrderConversionRate(CurrencyUtil.mul(orderConversionRate, 100) + "%");
//        //付款转换率 付款订单数/订单数
//        Double paymentsConversionRate = CurrencyUtil.div(orderOverviewVO.getPaymentOrderNum(), orderOverviewVO.getOrderNum(), 4);
//        if (paymentsConversionRate > 1) {
//            paymentsConversionRate = 1d;
//        }
//        orderOverviewVO.setPaymentsConversionRate(CurrencyUtil.mul(paymentsConversionRate, 100) + "%");
//        //整体转换率 付款数/UV
//        Double overallConversionRate = CurrencyUtil.div(orderOverviewVO.getPaymentOrderNum(), orderOverviewVO.getUvNum(), 4);
//        if (overallConversionRate > 1) {
//            overallConversionRate = 1d;
//        }
//        orderOverviewVO.setOverallConversionRate(CurrencyUtil.mul(overallConversionRate, 100) + "%");
//    }
//
//    @Override
//    public long orderNum(String orderStatus) {
//        LambdaQueryWrapper<ItemOrder> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(CharSequenceUtil.isNotEmpty(orderStatus),ItemOrder::getOrderStatus, orderStatus);
//        queryWrapper.eq(CharSequenceUtil.equals(Objects.requireNonNull(UserContext.getCurrentUser()).getRole().name(), UserEnums.STORE.name()),
//                ItemOrder::getStoreId, UserContext.getCurrentUser().getStoreId());
//        return this.count(queryWrapper);
//    }
//
//    @Override
//    public List<OrderStatisticsDataVO> statisticsChart(StatisticsQueryParam statisticsQueryParam) {
//        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);
//        QueryWrapper queryWrapper = new QueryWrapper();
//        //已支付
//        queryWrapper.eq("pay_status", "已付款");
//        //选择商家判定
//        queryWrapper.eq(StringUtils.isNotEmpty(statisticsQueryParam.getStoreId()), "store_id", statisticsQueryParam.getStoreId());
////      查询时间区间
//        queryWrapper.between("create_time", dates[0], dates[1]);
////       格式化时间
//        queryWrapper.groupBy("DATE_FORMAT(create_time,'%Y-%m-%d')");
//        List<OrderStatisticsDataVO> orderStatisticsDataVOS = this.baseMapper.getOrderStatisticsData(queryWrapper);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(dates[0]);
//
//        List<OrderStatisticsDataVO> result = new ArrayList<>();
//        //时间判定，将数据填充好
//        //如果当前的时间，在结束时间之前
//        while (calendar.getTime().before(dates[1])) {
//            OrderStatisticsDataVO item = null;
//            //判定是否已经有这一天的数据
//            for (OrderStatisticsDataVO orderStatisticsDataVO : orderStatisticsDataVOS) {
//                if (orderStatisticsDataVO.getCreateTime().equals(calendar.getTime())) {
//                    item = orderStatisticsDataVO;
//                }
//            }
//            //如果数据不存在，则进行数据填充
//            if (item == null) {
//                item = new OrderStatisticsDataVO();
//                item.setPrice(0d);
//                item.setCreateTime(calendar.getTime());
//            }
//            result.add(item);
//            //增加时间
//            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
//        }
//        return result;
//    }
//
//    @Override
//    public IPage<ItemOrderSimpleVO> getStatistics(StatisticsQueryParam statisticsQueryParam, PageVO pageVO) {
//
//        QueryWrapper<OrderSimpleVO> queryWrapper = new QueryWrapper<>();
//        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);
//        queryWrapper.between("o.create_time", dates[0], dates[1]);
//        queryWrapper.eq(StringUtils.isNotEmpty(statisticsQueryParam.getStoreId()),
//                "o.store_id", statisticsQueryParam.getStoreId());
//
//        queryWrapper.groupBy("o.order_id");
//        queryWrapper.orderByDesc("o.order_id");
//        return this.baseMapper.queryByParams(PageUtil.initPage(pageVO), queryWrapper);
//    }
//
//    private QueryWrapper getQueryWrapper(StatisticsQueryParam statisticsQueryParam) {
//
//        QueryWrapper queryWrapper = Wrappers.query();
//
//        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);
//        queryWrapper.between("create_time", dates[0], dates[1]);
//
//        //设置店铺ID
//        queryWrapper.eq(StringUtils.isNotEmpty(statisticsQueryParam.getStoreId()), "store_id", statisticsQueryParam.getStoreId());
//
//
//        //设置为付款查询
//        queryWrapper.eq("flow_type", FlowTypeEnum.PAY.name());
//
//        return queryWrapper;
//    }
//
//}
