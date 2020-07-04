package cn.jxj4869.joj.controller;

import cn.jxj4869.joj.pojo.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RestController
public class FileController {


    @Value("${file.upload.save-path}")
    private String savePath;

    @Value("${file.upload.localhost}")
    private String localhost;


    @Value("${file.upload.url}")
    private String url;


    @RequestMapping("/fileupload")
    public Info fileupload(HttpServletRequest request, @RequestParam("upload") MultipartFile[] uploads) throws IOException {
        Info info = new Info();
        try {
            System.out.println("文件上传");


            File file = new File(savePath);
            file.setWritable(true, false);
            if (!file.exists()) {
                file.mkdir();
            }

            List<String> list = new LinkedList<>();
            String filepath = localhost + request.getContextPath() + url;
            for (MultipartFile upload : uploads) {
                String filename = upload.getOriginalFilename();

                String uuid = UUID.randomUUID().toString().replace("-", "");
                filename = uuid + "_" + filename;

                upload.transferTo(new File(savePath, filename));

                list.add(filepath + filename);
            }
            System.out.println("aa");

            info.ok("上传成功");
            info.put("url", list.toArray());
            System.out.println(info.toString());
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            info.error("上传失败");
            return info;
        }

    }


}
