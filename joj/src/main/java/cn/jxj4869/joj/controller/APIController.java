package cn.jxj4869.joj.controller;

import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.utils.CheckCodeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author JiangXiaoju
 * @date 2020-06-12 14:21
 */
@Controller
@RequestMapping("api")
public class APIController {


    @Value("${file.upload.path}")
    private String url;

    @GetMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session) throws IOException {

        Map<String, Object> map = CheckCodeGenerator.generateCodeAndPic();
        session.setAttribute("checkCode", map.get("code").toString());
        ImageIO.write((RenderedImage) map.get("codePic"), "PNG", response.getOutputStream());
    }



    @PostMapping("/checkContestTime")
    @ResponseBody
    @RequiresAuthentication
    public Info checkContestTime(@RequestParam("beginTime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  Date endTime){
        DateFormat df2 = DateFormat.getDateTimeInstance();
        System.out.println(df2.format(beginTime));
        System.out.println(df2.format(endTime));
        Info info = new Info();
        if(beginTime.getTime()<System.currentTimeMillis()){
            info.error("Begin time should be later than the current time!");
            info.put("type",1);
        }else if (beginTime.getTime()>=endTime.getTime()){
            info.error("End time cannot be earlier than start time") ;
            info.put("type",2);
        }else{
            info.ok("ok");
        }


        return info;
    }


    /**
     * 上传图片或者文件之类的。
     * 三个函数之后要整合缩减一下，太不优雅，要抽取公共部分代码出来
     * @param uploads
     * @return
     * @throws IOException
     */
    @RequestMapping("/fileupload")
    @ResponseBody

    public Info fileupload(@RequestParam("upload") MultipartFile[] uploads) throws IOException {
        System.out.println("跨服务器上传文件上传");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (MultipartFile upload : uploads) {
            String filename = upload.getOriginalFilename();
            if (filename == null || filename.equals(""))
                continue;
            builder.addBinaryBody("upload", upload.getBytes(), ContentType.MULTIPART_FORM_DATA, filename);
        }
        Info info = new Info();

        try {
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println(response.getStatusLine().getStatusCode());
            String str = EntityUtils.toString(response.getEntity(), "utf8");
            ObjectMapper mapper = new ObjectMapper();
            info = mapper.readValue(str, Info.class);
        } catch (Exception e) {
            info.error("fail upload,try again");
        } finally {
            httpClient.close();
        }

        return info;
    }


    @RequestMapping("/fileupload/simditor")
    @ResponseBody
    @RequiresAuthentication
    public Info simditor(@RequestParam("upload") MultipartFile[] uploads) throws IOException {
        System.out.println("simditor跨服务器上传文件上传");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (MultipartFile upload : uploads) {
            String filename = upload.getOriginalFilename();
            if (filename == null || filename.equals(""))
                continue;
            builder.addBinaryBody("upload", upload.getBytes(), ContentType.MULTIPART_FORM_DATA, filename);
        }

        /*
            文件服务器相应数据格式：
            flag: 标记 true为成功， false失败
            msg: 相应的消息
            url: list数组


            相应simditor的JSON格式
            {
              "success": true/false,
              "msg": "error message", # optional
              "file_path": "[real file path]"
            }
         */
        Info info = new Info();
        try {
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println(response.getStatusLine().getStatusCode());
            String str = EntityUtils.toString(response.getEntity(), "utf8");
            ObjectMapper mapper = new ObjectMapper();
            Info readValue = mapper.readValue(str, Info.class);
            if ((Boolean) readValue.get("flag")) {
                info.put("success", true);
                info.put("msg", "upload success");
                info.put("file_path", readValue.get("url"));
            } else {
                info.put("success",false);
                info.put("msg","upload fail");
            }
            System.out.println(str);
        } catch (Exception e) {
            info.put("success",false);
            info.put("msg","upload fail");
        } finally {
            httpClient.close();
        }
        return info;
    }


    @RequestMapping("/fileupload/editormd")
    @ResponseBody
    @RequiresAuthentication
    public Info editormd(@RequestParam("editormd-image-file") MultipartFile[] uploads) throws IOException {
        System.out.println("跨服务器上传文件上传");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        for (MultipartFile upload : uploads) {
            String filename = upload.getOriginalFilename();
            if (filename == null || filename.equals(""))
                continue;
            builder.addBinaryBody("upload", upload.getBytes(), ContentType.MULTIPART_FORM_DATA, filename);
        }
        /*
        相应数据格式json
        {
            success : 0 | 1,           // 0 表示上传失败，1 表示上传成功
            message : "提示的信息，上传成功或上传失败及错误信息等。",
            url     : "图片地址"        // 上传成功时才返回
        }
         */
        Info info = new Info();
        try {
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println(response.getStatusLine().getStatusCode());
            String str = EntityUtils.toString(response.getEntity(), "utf8");
            ObjectMapper mapper = new ObjectMapper();
            Info readValue = mapper.readValue(str, Info.class);
            if ((Boolean) readValue.get("flag")) {
                info.put("success", 1);
                info.put("message", "upload success");
                info.put("url", readValue.get("url"));
            } else {
                info.put("success", 0);
                info.put("message", "upload fail");
            }

        } catch (Exception e) {
            info.put("success", 0);
            info.put("message", "upload fail");
        } finally {
            httpClient.close();
        }
        return info;
    }

}
