package cn.jxj4869.joj.submitter;

import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.mapper.ProblemMapper;
import cn.jxj4869.joj.mapper.SubmissionMapper;
import cn.jxj4869.joj.utils.Tools;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author JiangXiaoju
 * @date 2020-06-16 10:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CodeForcesSubmitter{


    protected Submission submission;

    protected String userName;
    protected String password;

    @Autowired
    protected SubmissionMapper submissionMapper;
    @Autowired
    protected ProblemMapper problemMapper;

    protected CloseableHttpClient httpClient = HttpClients.createDefault();
    protected RequestConfig config = RequestConfig.custom().setConnectTimeout(1000)
            .setConnectionRequestTimeout(500)
            .setSocketTimeout(10 * 1000)
            .build();

    public CodeForcesSubmitter(String username, String password) throws Exception {
//        super(username, password);
        this.userName=username;
        this.password=password;
        login();
    }

//    @Override
    protected void login() throws Exception {
/*
csrf_token: 16e18906343e1e21268a8c0c6bdf183b
action: enter
ftaa: mwu2j5qj3t0xt6zh9q
bfaa: 1e91e9e05e41fd649ae75383abc6ae61
handleOrEmail: 1121429190@qq.com
password: sherlock
remember: on
_tta: 772
 */

        String url = "http://codeforces.com/enter";
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> params = new ArrayList<>();
        System.out.println("login Success......");
        //创建表单的Entity对象,第一个参数就是封装好的表单数据，第二个参数就是编码
        params.add(new BasicNameValuePair("csrf_token", getCsrf(url)));
        params.add(new BasicNameValuePair("action", "enter"));
        params.add(new BasicNameValuePair("ftaa", getFtaa(url)));
        params.add(new BasicNameValuePair("bfaa", getBfaa(url)));
        params.add(new BasicNameValuePair("_tta", getTta(url)));
        params.add(new BasicNameValuePair("handleOrEmail", userName));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("remember", "on"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
        httpPost.setEntity(formEntity);
        CloseableHttpResponse execute = httpClient.execute(httpPost);
        System.out.println(EntityUtils.toString(execute.getEntity(),"utf8"));
        System.out.println("login Success");
    }

//    @Override
    protected void submit() throws Exception {
/*
csrf_token: f13596065497bcb6a3e9f0ce6da8a786
ftaa: mwu2j5qj3t0xt6zh9q
bfaa: 1e91e9e05e41fd649ae75383abc6ae61
action: submitSolutionFormSubmitted
submittedProblemCode: 1359D
programTypeId: 42
source:  #include<bits/stdc++.h>
tabSize: 4
sourceFile:
_tta: 772
 */

        String url = "http://codeforces.com/problemset/submit";

        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> params = new ArrayList<>();
        //创建表单的Entity对象,第一个参数就是封装好的表单数据，第二个参数就是编码
        params.add(new BasicNameValuePair("csrf_token", getCsrf(url)));
        params.add(new BasicNameValuePair("ftaa", getFtaa(url)));
        params.add(new BasicNameValuePair("bfaa", getBfaa(url)));
        params.add(new BasicNameValuePair("_tta", getTta(url)));
        params.add(new BasicNameValuePair("action", "submitSolutionFormSubmitted"));
        params.add(new BasicNameValuePair("programTypeId", submission.getLanguage()));
        params.add(new BasicNameValuePair("submittedProblemCode", "1359D"));
        params.add(new BasicNameValuePair("source", submission.getSourceCode()));
        params.add(new BasicNameValuePair("tabSize", "4"));

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String utf8 = EntityUtils.toString(response.getEntity(), "utf8");
        System.out.println(utf8);


    }

//    @Override
    protected void getAns() throws Exception {

        HttpGet httpGet = new HttpGet("http://codeforces.com/submissions/" + userName);
        int count = 0;
        while (true) {
            Thread.sleep(1000);
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() != 200) {
                    Thread.sleep(2000);
                    count++;
                    continue;
                }
                String content = EntityUtils.toString(response.getEntity(), "utf8");
                Document doc = Jsoup.parse(content);

                Elements tds = doc.select("#pageContent > div.datatable > div:nth-child(6) > table > tbody > tr:nth-child(2) > td");
                String status = tds.get(5).text().trim().replaceAll("<[\\s\\S]*?>", "").trim().replaceAll("judge\\b", "judging").replaceAll("queue", "queueing");
                ;
                if (status.contains("ing")) {
                    continue;
                }
                if (status.isEmpty()) {
                    status = "processing";
                }
                String runId = tds.get(0).text().trim();
                String time = tds.get(6).text().trim();
                int idx = 0;
                while (time.charAt(idx) >= '0' && time.charAt(idx) <= '9') {
                    idx++;
                }
                time = time.substring(0, idx);
                String memory = tds.get(7).text().trim();
                idx = 0;
                while (memory.charAt(idx) >= '0' && memory.charAt(idx) <= '9') {
                    idx++;
                }
                memory = memory.substring(0, idx);
                submission.setRealRunId(runId);
                submission.setTime(Integer.parseInt(time));
                submission.setMemory(Integer.parseInt(memory));
                submission.setStatus(status);
                submission.setStatusType(Tools.findStatusType(status));
                if (status.contains("compilation")) {
                    getAdditionalInfo();
                }


                break;
            } catch (Exception e) {

            } finally {
                if (count > 4) {
                    throw new Exception();
                }
            }
        }

    }

    private String getFtaa(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);

        int count = 4;
        while (count-- > 0) {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                if (response.getStatusLine().getStatusCode() != 200) {
                    Thread.sleep(2000);
                    continue;
                }

                String html = EntityUtils.toString(response.getEntity(), "utf8");

                String _ftaa = Tools.regFind(html, "window._ftaa = \"(.*?)\"");
                return _ftaa;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getBfaa(String url) throws IOException {
//
        HttpGet httpGet = new HttpGet(url);
        int count = 4;
        while (count-- > 0) {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                if (response.getStatusLine().getStatusCode() != 200) {
                    Thread.sleep(2000);
                    continue;
                }

                String html = EntityUtils.toString(response.getEntity(), "utf8");

                String bfaa = Tools.regFind(html, "window._bfaa = \"(.*?)\"");
                return bfaa;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getTta(String url) throws IOException {
//        $('[name=_tta]').val()


        HttpGet httpGet = new HttpGet(url);
        int count = 4;
        while (count-- > 0) {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                if (response.getStatusLine().getStatusCode() != 200) {
                    Thread.sleep(2000);
                    continue;
                }

                String html = EntityUtils.toString(response.getEntity(), "utf8");
                Document doc = Jsoup.parse(html);
                String _tta = doc.select("[name=_tta]").val();
                return _tta;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    private String getCsrf(String url) throws IOException {

        HttpGet httpGet = new HttpGet(url);
        int count = 4;
        while (count-- > 0) {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                if (response.getStatusLine().getStatusCode() != 200) {
                    Thread.sleep(2000);
                    continue;
                }

                String html = EntityUtils.toString(response.getEntity(), "utf8");
                String xsrf = Tools.regFind(html, "data-csrf='(\\w+)'");
                return xsrf;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

//    @Override
    public void wait2SubmitTimeLimit() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }    //CodeForces貌似不限制每两次提交之间的提交间隔
    }


//    @Override
    protected void getAdditionalInfo() throws Exception {

    }

    public void work() {
        try {
            try {
                submit();
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(2000);
                // 长时间未提交会自动注销，所以第一次提交失败后，先尝试登录一下。
                login();
                Thread.sleep(2000);
                submit();
            }
            submission.setStatus("Running & Judging");
            submission.setStatusType(Tools.findStatusType("Running & Judging"));
            getAns();
        } catch (Exception e) {
            submission.setStatus("Submit Fail");
            submission.setStatusType(Tools.findStatusType("Submit Fail"));
            e.printStackTrace();
        }finally {
            this.submission = null;
        }

    }
}
