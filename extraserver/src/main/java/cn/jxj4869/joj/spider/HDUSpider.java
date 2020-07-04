package cn.jxj4869.joj.spider;

import cn.jxj4869.joj.entity.Description;
import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.pojo.Sample;
import cn.jxj4869.joj.utils.Tools;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-10 22:03
 */
@Component
public class HDUSpider implements Spider {

    private final String URL = "http://acm.hdu.edu.cn/showproblem.php";


    @Value("${joj.spider.uid}")
    private Integer spiderUid;

    @Value("${joj.spider.userName}")
    private String userName;


    @Override
    public Description crawl(Problem problem) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder(URL);
        uriBuilder.setParameter("pid", problem.getOriginProb());

        HttpGet httpGet = new HttpGet(uriBuilder.build());

        CloseableHttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new Exception();
        }

        String html = EntityUtils.toString(response.getEntity(), "utf8");
        html = html.replaceAll("src=[^'\"]*?/images", "src=http://acm.hdu.edu.cn/data/images");
        html = html.replaceAll("src='[^'\"]*?/images", "src='http://acm.hdu.edu.cn/data/images");
        html = html.replaceAll("src=\"[^'\"]*?/images", "src=\"http://acm.hdu.edu.cn/data/images");


        Description description = new Description();

        if (html.contains("<DIV>No such problem")) {
            throw new Exception();
        }

        Document doc = Jsoup.parse(html);


//        timeLimit
//        memoryLimit
        problem.setTimeLimit(Integer.parseInt(Tools.regFind(html, "(\\d*) MS")));
        problem.setMemoryLimit(Integer.parseInt(Tools.regFind(html, "/(\\d*) K")));
//          title
//        body > table > tbody > tr:nth-child(4) > td > h1
        Elements title = doc.select("body > table > tbody > tr:nth-child(4) > td > h1");
        problem.setTitle(title.text().trim());

        if (problem.getTitle().isEmpty()) {
            throw new Exception();
        }

        problem.setUrl(uriBuilder.build().toString());

        //        source
        problem.setSource(Tools.regFind(html, "Source</div> <div class=panel_content>(.*?)</div>"));
        if (!StringUtils.isEmpty(problem.getSource())) {
            Document source = Jsoup.parse(problem.getSource());
            String attr = source.select("a").attr("href");
            source.select("a").attr("href", "http://acm.hdu.edu.cn" + attr).attr("target", "_blank");
            problem.setSource(source.select("a").toString());
        }
//
//        private String description; //题目描述

//        body > table > tbody > tr:nth-child(4) > td > div:nth-child(6)
        Elements desc = doc.select("body > table > tbody > tr:nth-child(4) > td > div:nth-child(6)");
        description.setDescription(desc.toString());

//        private String input; //输入样例描述

//        body > table > tbody > tr:nth-child(4) > td > div:nth-child(10)

        Elements input = doc.select(" body > table > tbody > tr:nth-child(4) > td > div:nth-child(10)");
        description.setInput(input.toString());


//        private String output; //输出样例描述
//        body > table > tbody > tr:nth-child(4) > td > div:nth-child(14)
        Elements output = doc.select(" body > table > tbody > tr:nth-child(4) > td > div:nth-child(14)");
        description.setOutput(output.toString());

//        private String samples; //样例  样子转成json格式保存
        List<Sample> samples = new ArrayList<>();
        Sample sample = new Sample();
        Elements sampleInput = doc.select("body > table > tbody > tr:nth-child(4) > td > div:nth-child(18) > pre");

        sample.setInput("<pre>" + sampleInput.text().replaceAll("<div style=\"font-family:Courier New,Courier,monospace;\">", "")
                .replaceAll("</div>", "").trim() + "</pre>");

        Elements sampleOutput = doc.select("body > table > tbody > tr:nth-child(4) > td > div:nth-child(22)>pre");
        Elements hint = sampleOutput.select("pre>div>div");
        sample.setOutput("<pre>" + sampleOutput.text().replaceAll("<div style=\"font-family:Courier New,Courier,monospace;\">", "")
                .replaceAll("</div>", "").trim() + "</pre>");

        samples.add(sample);
        ObjectMapper mapper = new ObjectMapper();
        description.setSamples(mapper.writeValueAsString(samples));

//        private String hint; //提示信息
        description.setHint(hint.toString());


//        private LocalDateTime updateTime;  //更新时间
        description.setUpdateTime(new Date());
        problem.setUpdateTime(new Date());
        description.setAuthor(userName);
        description.setUid(spiderUid);


        httpGet.releaseConnection();
        httpClient.close();

        return description;
    }

    public static void main(String[] args) throws Exception {

        HDUSpider hduSpider = new HDUSpider();
        Problem problem = new Problem();
        problem.setOriginProb(1080 + "");
        Description crawl = hduSpider.crawl(problem);
        System.out.println(problem);
        System.out.println(crawl);

    }


}
