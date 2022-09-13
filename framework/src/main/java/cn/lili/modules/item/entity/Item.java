package cn.lili.modules.item.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@TableName("item")
@ApiModel(value = "项目")
public class Item {

    private String itemId;

    private String itemName;

    private String buyerId;

    private String buyerName;

    private String createLocation;

    private Date createTime;

    private Integer itemStatus;

    private Double itemLongitude;

    private Double itemLatitude;

    private String itemScale;

    private String itemOverview;

    private String itemAddress;

    private String itemLogo;

    private String itemBudget;

    private String replyBudget;

    private String replyTime;

    private Date startTime;

    private Date endTime;
}
