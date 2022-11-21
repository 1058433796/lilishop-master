package cn.lili.modules.item.service.serviceImpl;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.exception.ServiceException;
import cn.lili.modules.item.entity.*;
import cn.lili.modules.item.mapper.ItemMapper;
import cn.lili.modules.item.service.ItemService;
import cn.lili.modules.member.entity.dos.Member;
import cn.lili.modules.member.service.MemberService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {
    @Resource
    ItemMapper itemMapper;
    @Autowired
    MemberService memberService;
    @Autowired
    private  ItemService itemService;
    @Override
    public List<Item> findAll(){
        List<Item> itemList=itemMapper.findData();
        return itemList;
    }
    @Override
    public IPage<Item> queryByParams(ItemSearchParams itemSearchParams) {
        return this.page(PageUtil.initPage(itemSearchParams), itemSearchParams.queryWrapper());
    }
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveItem(Item item) {
        item.setDesignerPass(new BCryptPasswordEncoder().encode(item.getDesignerPass()));
        boolean save = this.save(item);
        return save;
    }

    @Override
    public Long getStoreProductNum(String id) {
        return this.baseMapper.getStoreProductNum(id);
    }

    @Override
    public ItemVO getItemVO(String id) {
        Item item = this.getById(id);
        if (item == null) {
            log.error("项目id[" + id + "]不存在！");
            throw new ServiceException(ResultCode.PINTUAN_NOT_EXIST_ERROR);
        }
        ItemVO itemVO = new ItemVO(item);
        ItemSearchParams searchParams = new ItemSearchParams();
        searchParams.setItemId(item.getItemId());
        itemVO.setItemList(itemService.listFindAll(searchParams));
        return itemVO;
    }
    @Override
    public List<Item> listFindAll(ItemSearchParams searchParams){
        return this.list(searchParams.queryWrapper());
    }

    @Override
    public LoginItem queryLogin(String name, String pass){
        LoginItem loginItem=new LoginItem();
        loginItem.setName(name);
        Member member = memberService.findByUsername(name);
        //判断用户是否存在
        if (member == null ) {
            //不是业主
             System.out.println("designer");
             String Dpass=this.baseMapper.queryDesignerPass(name);
             System.out.println(Dpass);
             if(new BCryptPasswordEncoder().matches(pass, Dpass)){
                 List<ShortItem> queryDesigner=this.baseMapper.queryDesigner(name,Dpass);
                 loginItem.setProjectInfo(queryDesigner);
                 loginItem.setRole("designer");}

        }

        else{
            //是业主，判断密码是否输入正确
            if (new BCryptPasswordEncoder().matches(pass, member.getPassword())) {
                System.out.println("proprietor");
//            throw new ServiceException(ResultCode.USER_PASSWORD_ERROR);
                List<ShortItem> queryBuyer=this.baseMapper.queryBuyer(name);
                System.out.println(queryBuyer);
                loginItem.setProjectInfo(queryBuyer);
                loginItem.setRole("proprietor");
        }
        }
        return loginItem;
    }
}
