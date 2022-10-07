package cn.lili.controller.itemScheme;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.exception.ServiceException;
import cn.lili.common.security.AuthUser;
import cn.lili.common.security.context.UserContext;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.item.entity.*;
import cn.lili.modules.item.service.ItemGuarantyService;
import cn.lili.modules.item.service.ItemSchemeService;
import cn.lili.modules.scheme.entity.Scheme;
import cn.lili.modules.scheme.entity.SchemeSearchParams;
import cn.lili.modules.scheme.entity.SchemeVO;
import cn.lili.modules.scheme.service.SchemeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/store/itemScheme/itemScheme")
public class SchemeController {
    @Resource
    private SchemeService schemeService;
    @Resource
    private ItemGuarantyService itemGuarantyService;
    @Resource
    private ItemSchemeService itemschemeService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "通过项目id获取项目方案列表")
    public ResultMessage<IPage<Scheme>> getByPage(SchemeSearchParams schemeSearchParams) {
        return ResultUtil.data(schemeService.queryByParams(schemeSearchParams));
    }

    @GetMapping(value = "/itemSchemeList")
    @ApiOperation(value = "通过项目id获取项目方案列表")
    public ResultMessage<IPage<ItemScheme>> getByPage(ItemSchemeSearchParams itemschemeSearchParams) {
        return ResultUtil.data(itemschemeService.queryByParams(itemschemeSearchParams));
    }

    @PostMapping(value = "/{id}")
    @ApiOperation(value = "项目添加方案,id是项目ID")
    public ResultMessage<String> setItemScheme(@PathVariable String id) {
        List<Scheme> schemelist=schemeService.getSchemeList();
        int finish=0;
        for (int i=0;i<schemelist.size();i++){
            //按照每一条方案进行添加
            ItemSchemeVO itemScheme=new ItemSchemeVO();
            String schemeid= schemelist.get(i).getSchemeId();
            itemScheme.setSchemeId(schemeid);
            itemScheme.setItemId(id);
            itemScheme.setDoorId(schemelist.get(i).getDoorId());
            itemScheme.setLocation(schemelist.get(i).getLocation());
            itemScheme.setOpenMethod(schemelist.get(i).getOpenMethod());
            itemScheme.setOpenDirection(schemelist.get(i).getOpenDirection());
            itemScheme.setHeight(schemelist.get(i).getHeight());
            itemScheme.setWidth(schemelist.get(i).getWidth());
            itemScheme.setThickness(schemelist.get(i).getThickness());
            itemScheme.setGuard(schemelist.get(i).getGuard());
            itemScheme.setTexture(schemelist.get(i).getTexture());
            itemScheme.setHandle(schemelist.get(i).getHandle());
            itemScheme.setFirerating(schemelist.get(i).getFirerating());
            itemScheme.setWjgroup(schemelist.get(i).getWjgroup());
            itemScheme.setUpdateTime(schemelist.get(i).getUpdateTime());
            if (itemschemeService.saveItemScheme(itemScheme)) {
                finish++;
                System.out.println(finish);
//                return ResultUtil.success(ResultCode.PINTUAN_ADD_SUCCESS);
            }
        }
        if (finish==schemelist.size()){
            return ResultUtil.success(ResultCode.PINTUAN_ADD_SUCCESS);
        }
        throw new ServiceException(ResultCode.PINTUAN_ADD_ERROR);
    }

    @PostMapping(value = "/{id}/check")
    @ApiOperation(value = "确认项目方案")
    public  ResultMessage<ItemScheme> checkItemScheme(@PathVariable String id){
        itemschemeService.checkItemScheme(id);
        return ResultUtil.success(ResultCode.SUCCESS);}

}
