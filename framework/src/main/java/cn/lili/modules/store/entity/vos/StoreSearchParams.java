package cn.lili.modules.store.entity.vos;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.utils.StringUtils;
import cn.lili.common.vo.PageVO;
import cn.lili.modules.store.entity.enums.StoreStatusEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺搜索参数VO
 *
 * @author pikachu
 * @since 2020-03-07 17:02:05
 */
@Data
public class StoreSearchParams extends PageVO implements Serializable {

    private static final long serialVersionUID = 6916054310764833369L;

    @ApiModelProperty(value = "会员名称")
    private String memberName;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    /**
     * @see StoreStatusEnum
     */
    @ApiModelProperty(value = "店铺状态")
    private String storeDisable;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

    @ApiModelProperty(value = "采购方Id")
    private String buyerId;

    @ApiModelProperty(value = "采购方名称")
    private String buyerName;

    @ApiModelProperty(value = "采购方联系方式")
    private String buyerPhone;

    @ApiModelProperty(value = "供货商Id")
    private String storeId;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser != null) {
            //按卖家查询
            queryWrapper.eq(CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.STORE.name()), "o.store_id", currentUser.getStoreId());

            //店铺查询
            queryWrapper.eq(CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.MANAGER.name())
                    && CharSequenceUtil.isNotEmpty(storeId), "o.store_id", storeId);

            //按买家查询
            queryWrapper.eq(CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.MEMBER.name()) && buyerId == null, "o.buyer_id", currentUser.getId());

        }
        if (StringUtils.isNotEmpty(storeName)) {
            queryWrapper.like("store_name", storeName);
        }
//        if (StringUtils.isNotEmpty(storeId)) {
//            queryWrapper.like("store_id", storeId);
//        }

        if (StringUtils.isNotEmpty(memberName)) {
            queryWrapper.like("member_name", memberName);
        }
        //按时间查询
        if (StringUtils.isNotEmpty(startDate)) {
            queryWrapper.ge("create_time", DateUtil.parse(startDate));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            queryWrapper.le("create_time", DateUtil.parse(endDate));
        }

        if (StringUtils.isNotEmpty(buyerName)) {
            queryWrapper.like("buyer_name",buyerName);
        }
        if (StringUtils.isNotEmpty(buyerPhone)) {
            queryWrapper.like("buyer_phone", buyerPhone);
        }
        return queryWrapper;
    }

    public <T> QueryWrapper<T> queryWrapper2() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        AuthUser currentUser = UserContext.getCurrentUser();
        if (currentUser != null) {
            //按卖家查询
            queryWrapper.eq(CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.STORE.name()), "o.store_id", currentUser.getStoreId());

            //店铺查询
            queryWrapper.eq(CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.MANAGER.name())
                    && CharSequenceUtil.isNotEmpty(storeId), "o.store_id", storeId);

            //按买家查询
            queryWrapper.eq(CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.MEMBER.name()) && buyerId == null, "o.buyer_id", currentUser.getId());

        }
        if (StringUtils.isNotEmpty(storeName)) {
            queryWrapper.like("a.store_name", storeName);
        }
        if (StringUtils.isNotEmpty(memberName)) {
            queryWrapper.like("member_name", memberName);
        }
//        if (StringUtils.isNotEmpty(storeDisable)) {
//            queryWrapper.eq("store_disable", storeDisable);
//        } else {
//            queryWrapper.and(Wrapper -> Wrapper.eq("store_disable", StoreStatusEnum.OPEN.name()).or().eq("store_disable", StoreStatusEnum.CLOSED.name()));
//        }
        //按时间查询
        if (StringUtils.isNotEmpty(startDate)) {
            queryWrapper.ge("create_time", DateUtil.parse(startDate));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            queryWrapper.le("create_time", DateUtil.parse(endDate));
        }

        if (StringUtils.isNotEmpty(buyerName)) {
            queryWrapper.like("buyer_name",buyerName);
        }
        if (StringUtils.isNotEmpty(buyerPhone)) {
            queryWrapper.like("link_phone", buyerPhone);
        }
        return queryWrapper;
    }
}
