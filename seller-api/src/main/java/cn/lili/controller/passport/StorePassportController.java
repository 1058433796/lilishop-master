package cn.lili.controller.passport;


import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.security.token.Token;
import cn.lili.common.utils.BeanUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.permission.service.AdminUserService;
import cn.lili.modules.store.entity.dos.Store;
import cn.lili.modules.store.entity.dos.StoreDetail;
import cn.lili.modules.store.entity.dos.StoreMaterial;
import cn.lili.modules.store.entity.enums.StoreMaterialEnum;
import cn.lili.modules.store.entity.enums.StoreStatusEnum;
import cn.lili.modules.store.entity.vos.CompanySecondVo;
import cn.lili.modules.store.entity.vos.CompanyVo;
import cn.lili.modules.store.service.StoreDetailService;
import cn.lili.modules.store.service.StoreMaterialService;
import cn.lili.modules.store.service.StoreService;
import cn.lili.modules.verification.entity.enums.VerificationEnums;
import cn.lili.modules.verification.service.VerificationService;
import com.aliyuncs.policy.retry.RetryUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.reflection.wrapper.BaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 店铺端,商家登录接口
 *
 * @author Chopper
 * @since 2020/12/22 15:02
 */

@RestController
@Api(tags = "店铺端,商家登录接口 ")
@RequestMapping("/store/passport/login")
public class StorePassportController {

    /**
     * 会员
     */
    @Autowired
    private MemberService memberService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private StoreDetailService storeDetailService;

    @Autowired
    private StoreMaterialService storeMaterialService;


    @ApiOperation(value = "商家登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query")
    })
    @PostMapping("/storeLogin")
    public ResultMessage<Object> storeLogin(@NotNull(message = "用户名不能为空") @RequestParam String username,
                                            @NotNull(message = "密码不能为空") @RequestParam String password, @RequestHeader String uuid) {
        try {
            Token token = this.memberService.usernameStoreLogin(username, password);
            return ResultUtil.data(token);
        }catch (ServiceException e){
            return ResultUtil.error(e.getResultCode());
        }
    }

    @PostMapping("/userRole")
    public ResultMessage<Object> getUserRole(@NotNull(message = "用户名不能为空") String username){
        Map<String, String> map = new HashMap<>();
        map.put("role", "unknown");
        Member member = memberService.findByUsername(username);
        if(member == null){
            if(adminUserService.findByUsername(username) != null){
                map.put("role", "admin");
            }
        }else{
            if(member.getHaveStore()){
                Store store= storeService.getById(member.getStoreId());

                map.put("role", "store");
            }else{
                map.put("role", "member");
            }
        }
        return ResultUtil.data(map);
    }

    @PostMapping("/userRegister")
    public ResultMessage<Object> userRegister(@NotNull(message = "用户名不能为空") @RequestParam String username,
                                              @NotNull(message = "密码不能为空") @RequestParam String password,
                                              @NotNull(message = "手机号不能为空") @RequestParam String mobile,
                                              @RequestHeader String uuid
    ) {
        this.memberService.storeRegister(username, password, mobile);
        return ResultUtil.success();
    }

    @ApiOperation(value = "店铺注册接口，注册第一步")
    @ApiImplicitParams({
            @ApiImplicitParam(name="companyV0", value="表格数据", required = true)
    })
    @PostMapping(value="/storeRegister")
    public ResultMessage<Object> storeRegister(@Valid CompanyVo vo) {
        System.out.println(vo);
//        验证账户
        Member member = memberService.findByUsername(vo.getUsername());
        if(member == null || !new BCryptPasswordEncoder().matches(vo.getPassword(), member.getPassword())){
            return ResultUtil.error(ResultCode.USER_PASSWORD_ERROR);
        }
        Store store = null;
        StoreDetail storeDetail = null;
//        存在store
        if(member.getHaveStore()){
            System.out.println("当前用户存在店铺");
            store = storeService.getById(member.getStoreId());
            String status = store.getStoreDisable();

            if(!status.equals(StoreStatusEnum.REFUSED.name())){
                return ResultUtil.error(ResultCode.ERROR);
            }

//            if(status.equals(StoreStatusEnum.OPEN.name())){
//                return ResultUtil.error(ResultCode.STORE_APPLY_DOUBLE_ERROR);
//            }else if(status.equals(StoreStatusEnum.CLOSED.name())){
//                return ResultUtil.error(ResultCode.STORE_CLOSE_ERROR);
//            }else if(status.equals(StoreStatusEnum.APPLY_FIRST_STEP.name())){
////                正常
//            }else if(status.equals(StoreStatusEnum.APPLY_SECOND_STEP.name())){
////                当前为第一步申请，店铺已经进行过第一步申请
//                return ResultUtil.error(ResultCode.STORE_STEP_APPLY_REPEAT);
//            }else if(status.equals(StoreStatusEnum.APPLYING.name())){
////                申请信息已经提交 不允许再次申请
//                return ResultUtil.error(ResultCode.STORE_STEP_APPLY_REPEAT);
//            }else if(status.equals(StoreStatusEnum.REFUSED.name())){
////                申请被拒绝，允许从第一步开始重新申请
////                BaseWrapper<Store> baseWrapper =
//                UpdateWrapper<Store> updateWrapper = new UpdateWrapper<>();
//                updateWrapper.eq("id", store.getId())
//                        .set("store_disable", StoreStatusEnum.APPLY_FIRST_STEP);
//                storeService.update(updateWrapper);
//            }
//            之前申请被拒绝后重新申请 使用新的申请材料覆盖原有信息
            storeDetail = storeDetailService.getStoreDetail(store.getId());
            BeanUtil.copyProperties(vo, storeDetail);
            storeDetailService.updateById(storeDetail);
            System.out.println("存在申请材料，已覆盖");
        }else{
            //    不存在商店    注册商店
            store = new Store(member);
            store.setStoreName(vo.getCompanyName());
//            store.setStoreDisable(StoreStatusEnum.APPLY_SECOND_STEP.name());
            storeService.save(store);

            member.setStoreId(store.getId());
            member.setHaveStore(true);
            memberService.updateById(member);
            System.out.println("商店不存在，已注册");
//            创建storeDetail
            storeDetail = new StoreDetail(store.getId(),vo);
            storeDetailService.save(storeDetail);
            System.out.println("storeDetail已创建");
//            第一步注册完成 修改store状态为第二步
            store.setStoreDisable(StoreStatusEnum.APPLY_SECOND_STEP.name());
            storeService.updateById(store);
        }


        return ResultUtil.data(ResultCode.SUCCESS);
    }

    @PostMapping("/storeRegister2")
    public ResultMessage<Object> storeRegister2(@Valid CompanySecondVo vo){
        System.out.println(vo);
        //        验证账户
        Member member = memberService.findByUsername(vo.getUsername());
        if(member == null || !new BCryptPasswordEncoder().matches(vo.getPassword(), member.getPassword())){
            return ResultUtil.error(ResultCode.USER_PASSWORD_ERROR);
        }
        if(!member.getHaveStore()){
            return ResultUtil.error(ResultCode.STORE_NOT_OPEN);
        }
        Store store = storeService.getById(member.getStoreId());
        if(!StoreStatusEnum.APPLY_SECOND_STEP.name().equals(store.getStoreDisable())){
            return ResultUtil.error(ResultCode.ERROR);
        }
        storeMaterialService.add(vo.getBusLicPhotoList(), store.getId(), StoreMaterialEnum.BUSINESS_LICENSE);
        storeMaterialService.add(vo.getBankLicPhotoList(), store.getId(), StoreMaterialEnum.BANK_OPEN_LICENSE);
        storeMaterialService.add(vo.getLegalLicPhotoList(), store.getId(), StoreMaterialEnum.LEGAL_LICENSE);
        storeMaterialService.add(vo.getOrgCodeLicPhotosList(), store.getId(), StoreMaterialEnum.ORG_CODE_LICENSE);

//        修改store状态为待审核
        store.setStoreDisable(StoreStatusEnum.APPLYING.name());
        storeService.updateById(store);

//        临时方法store状态直接审核通过
        store.setStoreDisable(StoreStatusEnum.OPEN.name());
        storeService.updateById(store);

        return ResultUtil.success();
    }

    @ApiOperation(value = "注销接口")
    @PostMapping("/logout")
    public ResultMessage<Object> logout() {
        this.memberService.logout(UserEnums.STORE);
        return ResultUtil.success();
    }

    @ApiOperation(value = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "旧密码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, paramType = "query")
    })
    @PostMapping("/modifyPass")
    public ResultMessage<Member> modifyPass(@NotNull(message = "旧密码不能为空") @RequestParam String password,
                                            @NotNull(message = "新密码不能为空") @RequestParam String newPassword) {
        return ResultUtil.data(memberService.modifyPass(password, newPassword));
    }

    @ApiOperation(value = "刷新token")
    @GetMapping("/refresh/{refreshToken}")
    public ResultMessage<Object> refreshToken(@NotNull(message = "刷新token不能为空") @PathVariable String refreshToken) {
        return ResultUtil.data(this.memberService.refreshStoreToken(refreshToken));
    }
}
