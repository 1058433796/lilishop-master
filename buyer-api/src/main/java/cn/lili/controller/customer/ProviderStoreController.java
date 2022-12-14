package cn.lili.controller.customer;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.store.entity.dos.StoreDetail;
import cn.lili.modules.store.entity.vos.CustomerStoreVO;
import cn.lili.modules.store.entity.vos.StoreSearchParamsZy;
import cn.lili.modules.store.service.StoreDetailService;
import cn.lili.modules.store.service.StoreServiceZy;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/store/customer/customer")
@Api(tags = "店铺端,客户接口")
public class ProviderStoreController {


    /**
     * 企业（店铺）
     */
    @Autowired
    private StoreServiceZy storeServiceZy;

    /**
     * 企业（店铺）详细信息
     */
    @Autowired
    private StoreDetailService storeDetailService;

    @ApiOperation(value = "查询采购方(客户)列表")
    @GetMapping
    public ResultMessage<IPage<CustomerStoreVO>> queryMineCustomer(StoreSearchParamsZy storeSearchParamsZy) {
        return ResultUtil.data(storeServiceZy.queryByParams(storeSearchParamsZy));
    }

    @ApiOperation(value = "客户明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buyerId", value = "采购方(客户)编号", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping(value = "/{buyerId}")
    public ResultMessage<StoreDetail> detail(@NotNull @PathVariable String buyerId) {
        return ResultUtil.data(storeDetailService.getStoreDetail(buyerId));
    }


}
