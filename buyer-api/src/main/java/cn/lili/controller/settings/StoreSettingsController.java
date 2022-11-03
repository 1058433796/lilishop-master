package cn.lili.controller.settings;


import cn.lili.common.enums.ResultUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.vo.MemberVO;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.store.entity.dto.StoreAfterSaleAddressDTO;
import cn.lili.modules.store.entity.dto.StoreSettingDTO;
import cn.lili.modules.store.entity.vos.StoreVO;
import cn.lili.modules.store.service.StoreDetailService;
import cn.lili.modules.store.service.StoreServiceZy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 店铺端,店铺设置接口
 *
 * @author Bulbasaur
 * @since 2020/11/22 14:23
 */
@RestController
@Api(tags = "店铺端,店铺设置接口")
@RequestMapping("/store/settings/storeSettings")
public class StoreSettingsController {

    /**
     * 店铺
     */
    @Autowired
    private StoreServiceZy storeServiceZy;

    @Autowired
    private MemberService memberService;
    /**
     * 店铺详情
     */
    @Autowired
    private StoreDetailService storeDetailService;

    @ApiOperation(value = "获取商家设置")
    @GetMapping
    public ResultMessage<MemberVO> get() {
        // 获取当前登录用户内容
        AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
        String id = currentUser.getId();
        return ResultUtil.data(memberService.getMember(id));
    }


    @ApiOperation(value = "修改商家设置")
    @PutMapping
    public ResultMessage<Object> edit(@Valid StoreSettingDTO storeSettingDTO) {
        //修改商家设置
        Boolean result = storeDetailService.editStoreSetting(storeSettingDTO);
        return ResultUtil.data(result);
    }

    @ApiOperation(value = "修改商家设置")
    @PutMapping("/merchantEuid")
    public ResultMessage<Object> edit(String merchantEuid) {
        //修改UDESK设置
        Boolean result = storeDetailService.editMerchantEuid(merchantEuid);
        return ResultUtil.data(result);
    }

    @ApiOperation(value = "修改店铺库存预警数量")
    @ApiImplicitParam(name = "stockWarning", value = "库存预警数量", required = true, dataType = "Integer", paramType = "query")
    @PutMapping("/updateStockWarning")
    public ResultMessage<Object> updateStockWarning(Integer stockWarning) {
        //修改商家设置
        boolean result = storeDetailService.updateStockWarning(stockWarning);
        return ResultUtil.data(result);
    }

    @ApiOperation(value = "获取商家退货收件地址")
    @GetMapping("/storeAfterSaleAddress")
    public ResultMessage<StoreAfterSaleAddressDTO> getStoreAfterSaleAddress() {
        //获取当前登录商家内容
        return ResultUtil.data(storeDetailService.getStoreAfterSaleAddressDTO());
    }

    @ApiOperation(value = "修改商家退货收件地址")
    @PutMapping("/storeAfterSaleAddress")
    public ResultMessage<Object> editStoreAfterSaleAddress(@Valid StoreAfterSaleAddressDTO storeAfterSaleAddressDTO) {
        //修改商家退货收件地址
        boolean result = storeDetailService.editStoreAfterSaleAddressDTO(storeAfterSaleAddressDTO);
        return ResultUtil.data(result);
    }

}
