package cn.lili.modules.company.mapper;
import cn.lili.modules.company.entity.dos.Company;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CompanyMapper extends BaseMapper<Company> {

    @Select("select * from company")
    List<Company> getAllCompany();
}
