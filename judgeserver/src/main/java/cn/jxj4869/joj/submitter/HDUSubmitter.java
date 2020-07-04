package cn.jxj4869.joj.submitter;

import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.utils.Tools;
import com.alibaba.druid.support.spring.DruidNativeJdbcExtractor;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

/**
 * @author JiangXiaoju
 * @date 2020-06-15 19:42
 */

public class HDUSubmitter extends Submitter {


    public HDUSubmitter(String userName, String password) throws Exception {
        super(userName, password);
    }

    @Override
    protected void login() throws Exception {
        HttpPost httpPost = new HttpPost("http://acm.hdu.edu.cn/userloginex.php?action=login&cid=0&notice=0");
        ArrayList<NameValuePair> params = new ArrayList<>();

        //创建表单的Entity对象,第一个参数就是封装好的表单数据，第二个参数就是编码
        params.add(new BasicNameValuePair("login", "Sign In"));
        params.add(new BasicNameValuePair("username", userName));
        params.add(new BasicNameValuePair("userpass", password));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
        httpPost.setEntity(formEntity);
        httpClient.execute(httpPost);
    }

    @Override
    protected void submit() throws Exception {
        HttpPost httpPost = new HttpPost("http://acm.hdu.edu.cn/submit.php?action=submit");
        Problem problem = problemMapper.selectById(submission.getPid());
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("check", "0"));
        params.add(new BasicNameValuePair("language", submission.getLanguage()));
        params.add(new BasicNameValuePair("problemid", problem.getOriginProb()));
        params.add(new BasicNameValuePair("usercode", submission.getSourceCode()));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(response.getEntity(), "utf8");
            System.out.println(content);
        } else {
            System.out.println(response.getStatusLine());

        }
    }

    @Override
    protected void getAns() throws Exception {
        URIBuilder uriBuilder = new URIBuilder("http://acm.hdu.edu.cn/status.php");
        uriBuilder.setParameter("user", userName);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
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
                Elements tds = doc.select("#fixed_table > table > tbody > tr:nth-child(3) > td");
                String status = tds.get(2).text();
                if (status.contains("ing")) {
                    continue;
                }


                String runId = tds.get(0).text().trim();
                String time = tds.get(4).text().trim();

                time=time.substring(0,time.length()-2);
                String memory = tds.get(5).text().trim();

                memory=memory.substring(0,memory.length()-1);

                submission.setRealRunId(runId);
                submission.setTime(Integer.parseInt(time));
                submission.setMemory(Integer.parseInt(memory));
                submission.setStatus(status);
                submission.setStatusType(Tools.findStatusType(status));


                // 判断是提交成功，如果realRunId不在数据库中的话则没有，否则抛异常。
                QueryWrapper<Submission> wrapper=new QueryWrapper<>();
                wrapper.eq("realRunId",runId).eq("pid",submission.getPid());
                Submission submission = submissionMapper.selectOne(wrapper);
                if(!ObjectUtils.isEmpty(submission)){
                    throw new Exception("fail");
                }


                if (status.toLowerCase().contains("compilation")) {
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


    /**
     * hdu oj限制每两次提交之间至少隔5秒
     */
    @Override
    public void wait2SubmitTimeLimit() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getAdditionalInfo() throws Exception {
        URIBuilder uriBuilder = new URIBuilder("http://acm.hdu.edu.cn/viewerror.php");
        uriBuilder.setParameter("rid", submission.getRealRunId());
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String content = EntityUtils.toString(response.getEntity(), "utf8");
        submission.setAdditionalInfo(Tools.regFind(content, "(<pre>[\\s\\S]*?</pre>)"));

    }


}
