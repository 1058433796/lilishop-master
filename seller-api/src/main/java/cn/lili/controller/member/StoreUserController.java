package cn.lili.controller.member;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.entity.dto.StoreUserUpdateDTO;
import cn.lili.modules.member.service.MemberService;
import cn.lili.modules.store.entity.dto.StoreSettingDTO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


/**
 * 店铺端,管理员接口
 *
 * @author Chopper
 * @since 2020/11/16 10:57
 */
@RestController
@Api(tags = "店铺端,管理员接口")
@RequestMapping("/store/member/user")
public class StoreUserController {
    @Autowired
    private MemberService memberService;


    @GetMapping(value = "/info")
    @ApiOperation(value = "获取当前登录用户接口")
    public ResultMessage<Member> getUserInfo() {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser != null) {
            Member member = memberService.findByUsername(tokenUser.getUsername());
            member.setPassword(null);
            return ResultUtil.data(member);
        }
        throw new ServiceException(ResultCode.USER_NOT_LOGIN);
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "修改当前登录用户信息接口")
    public ResultMessage<Object> updateUser(StoreUserUpdateDTO storeUserUpdateDTO) {
        return ResultUtil.data(memberService.StoreUpdateMember(storeUserUpdateDTO));
    }
}
