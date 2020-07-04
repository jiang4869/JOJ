package cn.jxj4869.joj.spider;

import cn.jxj4869.joj.entity.Description;
import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.pojo.Sample;
import cn.jxj4869.joj.utils.Tools;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-11 16:00
 */

public class CodeForcesSpider implements Spider {

    private final String URL = "http://codeforces.com/problemset/problem/";

    @Value("${joj.spider.uid}")
    private Integer spiderUid;

    @Value("${joj.spider.userName}")
    private String userName;

    @Override
    public Description crawl(Problem problem) throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        int splitIndex = 0;
        while (problem.getOriginProb().charAt(splitIndex) <= '9') {
            ++splitIndex;
        }
        String contestNum = problem.getOriginProb().substring(0, splitIndex);
        String problemNum = problem.getOriginProb().substring(splitIndex);


        HttpGet httpGet = new HttpGet(URL + contestNum + "/" + problemNum);


        CloseableHttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new Exception();
        }

        String html = EntityUtils.toString(response.getEntity(), "utf8");


        Description description = new Description();

        Document doc = Jsoup.parse(html);
        //          title
        problem.setTitle(Tools.regFind(html, "<div class=\"title\">" + problemNum + "\\. ([\\s\\S]*?)</div>").trim());
        if (problem.getTitle().isEmpty()){
            throw new Exception();
        }

//        timeLimit
//        memoryLimit
        Double timeLimit = 1000 * Double.parseDouble(Tools.regFind(html, "</div>([\\d\\.]+) seconds?</div>"));
        problem.setTimeLimit(timeLimit.intValue());
        problem.setMemoryLimit(1024 * Integer.parseInt(Tools.regFind(html, "</div>(\\d+) megabytes</div>")));


        problem.setUrl(URL + contestNum + "/" + problemNum);


//
//        private String description; //题目描述

        Elements desc = doc.select("#pageContent > div.problemindexholder > div > div > div:nth-child(2)");
        description.setDescription(desc.toString());

//        private String input; //输入样例描述


        Elements input = doc.select("#pageContent > div.problemindexholder > div > div > div.input-specification");
        input.select(".section-title").remove();
        description.setInput(input.toString());


//        private String output; //输出样例描述
//        body > table > tbody > tr:nth-child(4) > td > div:nth-child(14)
        Elements output = doc.select("#pageContent > div.problemindexholder > div > div > div.output-specification");
        output.select(".section-title").remove();
        description.setOutput(output.toString());


//        private String samples; //样例  样例转成json格式保存
        List<Sample> list = new ArrayList<>();
        Elements elements = doc.select("#pageContent > div.problemindexholder > div > div > div.sample-tests > div.sample-test>div");
        for (int i = 0; i < elements.size(); i++) {
            Element inputElement = elements.get(i++);
            Element outputElement = elements.get(i);
            String inputSample = inputElement.select("pre").removeAttr("id").toString();
            String outputSample = outputElement.select("pre").removeAttr("id").toString();
            Sample sample = new Sample();
            sample.setInput(inputSample).setOutput(outputSample);
            list.add(sample);
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(list);
        description.setSamples(str);


//        private String hint; //提示信息

        Elements hint = doc.select("#pageContent > div.problemindexholder > div > div > div.note");
        hint.select(".section-title").remove();

        description.setHint(hint.toString());


//        private LocalDateTime updateTime;  //更新时间
        description.setUpdateTime(new Date());
        problem.setUpdateTime(new Date());
        description.setAuthor(userName);
        description.setUid(spiderUid);
        return description;
    }


    public static void main(String[] args) throws Exception {
        Problem problem = new Problem();
        problem.setOriginOJ("CodeForces");
        problem.setUid(1);
        problem.setOriginProb("1361B");
        CodeForcesSpider codeForcesSpider = new CodeForcesSpider();

        Description description = codeForcesSpider.crawl(problem);
        System.out.println(description);
        System.out.println("==========================");
        System.out.println(problem);
    }

}
