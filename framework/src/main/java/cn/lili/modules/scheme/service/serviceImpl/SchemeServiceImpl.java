package cn.lili.modules.scheme.service.serviceImpl;


import cn.lili.modules.scheme.entity.Scheme;
import cn.lili.modules.scheme.entity.SchemeSearchParams;
import cn.lili.modules.scheme.mapper.SchemeMapper;
import cn.lili.modules.scheme.service.SchemeService;
import cn.lili.mybatis.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SchemeServiceImpl extends ServiceImpl<SchemeMapper, Scheme> implements SchemeService {
    @Resource
    SchemeMapper schemeMapper;

    @Autowired
    private SchemeService schemeService;

    @Override
    public IPage<Scheme> queryByParams(SchemeSearchParams schemeSearchParams) {
        return this.page(PageUtil.initPage(schemeSearchParams), schemeSearchParams.queryWrapper());
    }

    @Override
    public List<Scheme> getSchemeList(){
        List<Scheme> schemeList=schemeMapper.findAllScheme();
        return schemeList;
    }

}
