package cn.lili.controller.common;

import cn.lili.common.enums.ResultCode;
import cn.lili.common.enums.ResultUtil;
import cn.lili.common.utils.PDFUtil;
import cn.lili.common.vo.ResultMessage;
import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Slf4j
@RestController
@Api(tags = "文件转PDF接口")
@RequestMapping("/common/common/pdf")
public class PDFController {

    @Autowired
    UploadController uploadController;
    @PostMapping
    public ResultMessage<Object> htmlToPDF(HttpServletResponse response, @RequestParam("html") String html, @RequestHeader String accessToken){


        OutputStream os = null;
        InputStream is = null;
        File file = null;
        MultipartFile multipartFile = null;
        try {
            html = URLDecoder.decode(html, "utf-8");
            file = File.createTempFile("tempPDF", ".pdf");
            os = Files.newOutputStream(file.toPath());
            PDFUtil.writeStringToOutputStreamAsPDF(html, os);
            is = Files.newInputStream(file.toPath());
            multipartFile = new MockMultipartFile("商品详情.pdf", file.getName(), "application/pdf", is);
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return uploadController.upload(multipartFile, null, accessToken);
    }


}
