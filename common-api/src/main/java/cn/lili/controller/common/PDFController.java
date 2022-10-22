package cn.lili.controller.common;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.utils.PDFUtil;
import cn.lili.common.vo.ResultMessage;
import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RestController
@Api(tags = "文件转PDF接口")
@RequestMapping("/common/common/pdf")
public class PDFController {

    @GetMapping
    public ResultMessage<Object> htmlToPDF(String html){
        OutputStream os = null;
        File file = null;
        try {
              file = File.createTempFile("tempPDF", ".pdf");
              os = Files.newOutputStream(file.toPath());
            PDFUtil.writeStringToOutputStreamAsPDF(html, os);
        } catch (DocumentException | IOException e) {
            return ResultUtil.error(ResultCode.ERROR);
        }
        file.deleteOnExit();
        return ResultUtil.data(file);
    }
}
