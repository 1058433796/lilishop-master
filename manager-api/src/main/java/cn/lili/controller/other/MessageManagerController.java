package cn.lili.controller.other;

import cn.lili.common.enums.ResultUtil;
import cn.lili.common.vo.PageVO;
import cn.lili.common.vo.ResultMessage;
import cn.lili.modules.message.entity.dos.Message;
import cn.lili.modules.message.entity.dos.StoreMessage;
import cn.lili.modules.message.entity.vos.MessageVO;
import cn.lili.modules.message.service.MessageService;
import cn.lili.modules.message.service.StoreMessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理端,消息发送管理接口
 *
 * @author pikachu
 * @since 2020-05-06 15:18:56
 */
@RestController
@Api(tags = "管理端,消息发送管理接口")
@RequestMapping("/manager/other/message")
public class MessageManagerController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private StoreMessageService storeMessageService;
    @GetMapping
    @ApiOperation(value = "多条件分页获取")
    public ResultMessage<IPage<Message>> getByCondition(MessageVO messageVO,
                                                        PageVO pageVo) {
        return ResultUtil.data(messageService.getPage(messageVO, pageVo));
    }

    @PostMapping
    @ApiOperation(value = "发送消息")
    public ResultMessage<Boolean> sendMessage(Message message) {
        boolean a=messageService.sendMessage(message);
        List<StoreMessage> storeMessageList=new ArrayList<>();
        System.out.println(message.getUserIds().length+"userid");
        if(message.getMessageClient().equals("store") && message.getUserIds().length>0){
            for (int i=0;i<message.getUserIds().length;i++){
            StoreMessage storeMessage=new StoreMessage();
            storeMessage.setMessageId(message.getId());
            storeMessage.setStoreId(message.getUserIds()[i]);
            storeMessage.setStoreName(message.getUserNames()[i]);
            storeMessage.setContent(message.getContent());
            storeMessage.setTitle(message.getTitle());
            storeMessageList.add(storeMessage);
            }
            storeMessageService.save(storeMessageList);
        }
        else if(message.getMessageClient().equals("store") && message.getMessageRange().equals("ALL")){
            StoreMessage storeMessage=new StoreMessage();
            storeMessage.setMessageId(message.getId());
            storeMessage.setContent(message.getContent());
            storeMessage.setTitle(message.getTitle());
            storeMessageList.add(storeMessage);
            storeMessageService.save(storeMessageList);
        } else if (message.getMessageClient().equals("member") && message.getUserIds().length>0) {


        }

        return ResultUtil.data(a);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "消息id", required = true, dataType = "String", paramType = "path")
    })
    public ResultMessage<Boolean> deleteMessage(@PathVariable String id) {

        return ResultUtil.data(messageService.deleteMessage(id));
    }

}
