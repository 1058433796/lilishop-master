package cn.lili.modules.contract.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.vo.PageVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContractSearchParams extends PageVO {

    @ApiModelProperty(value = "采购方ID")
    private String buyerId;

    @ApiModelProperty(value = "合同号")
    private String contractId;

    private String storeId;
    @ApiModelProperty(value = "供应商店铺名")
    private String storeName;

    @ApiModelProperty(value = "供应商签署状态")
    private String providerState;


    @ApiModelProperty(value = "采购方签署状态")
    private String buyerState;

    private String startDate;
    private String endDate;

    private String schemeId;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotEmpty(buyerId)) {
            queryWrapper.eq("buyer_id", buyerId);
        }

        if (CharSequenceUtil.isNotEmpty(contractId)) {
            queryWrapper.like("id", contractId);
        }

        if (CharSequenceUtil.isNotEmpty(storeName)) {
            queryWrapper.like("store_name", storeName);
        }

        if (CharSequenceUtil.isNotEmpty(providerState)) {
            queryWrapper.like("provider_state", providerState);
        }

        if (CharSequenceUtil.isNotEmpty(buyerState)) {
            queryWrapper.like("buyer_state", buyerState);
        }

        if (CharSequenceUtil.isNotEmpty(startDate) && CharSequenceUtil.isNotEmpty(endDate)) {
            queryWrapper.ge("time_start", startDate);
            endDate += " 23:59:59";
            queryWrapper.le("time_start", endDate);
        }

        if (CharSequenceUtil.isNotEmpty(schemeId)) {
            queryWrapper.eq("o.scheme_id", schemeId);
        }

        if (CharSequenceUtil.isNotEmpty(storeId)) {
            queryWrapper.eq("store_id", storeId);
        }


        return queryWrapper;
    }
}