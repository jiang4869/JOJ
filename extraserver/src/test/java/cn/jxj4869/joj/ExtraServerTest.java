package cn.jxj4869.joj;


import cn.jxj4869.joj.entity.CalendarContest;
import cn.jxj4869.joj.entity.OjDailyBoard;
import cn.jxj4869.joj.entity.Problem;
import cn.jxj4869.joj.mapper.OjDailyBoardMapper;
import cn.jxj4869.joj.mapper.SubmissionMapper;
import cn.jxj4869.joj.pojo.Sample;
import cn.jxj4869.joj.mapper.CalendarContestMapper;
import cn.jxj4869.joj.mapper.ProblemMapper;
import cn.jxj4869.joj.service.impl.CalendarContestServiceImpl;
import cn.jxj4869.joj.spider.HDUSpider;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author JiangXiaoju
 * @date 2020-06-10 16:45
 */
@SpringBootTest
public class ExtraServerTest {

    @Autowired
    private ProblemMapper problemMapper;


    @Autowired
    private HDUSpider hduSpider;


    @Autowired
    private CalendarContestMapper calendarContestMapper;

    @Autowired
    private CalendarContestServiceImpl calendarContestService;

//    @Autowired
//    private SubmissionMapper submissionMapper;
//
//    @Autowired
//    private OjDailyBoardMapper ojDailyBoardMapper;

    @Test
    public void test() throws ParseException {



    }

    public void change(Sample sample) {
        sample.setInput("hhhh");
    }

    @Test
    public void testSpider() throws Exception {
//        Problem problem = new Problem();
//        problem.setOriginOJ("HDU");
//        problem.setUid(1);
//        problem.setOriginProb(6705 + "");
//        Description description = hduSpider.crawl(problem);
//        System.out.println(description);
//        System.out.println("==========================");
//        System.out.println(problem);
        System.out.println(LocalDateTime.now());
    }


    @Test
    public void testTimestamp() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;

        System.out.println(year + " 年 " + month + " 月");
    }


    public void testCalendarContest() throws Exception {
        String url = "https://ac.nowcoder.com/acm/calendar/contest?token=&month=";
        CloseableHttpClient httpClient = HttpClients.createDefault();

        while (true) {

            try {
                calendarContestMapper.truncate();
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                url = url + year + "-" + month;
                HttpGet httpGet = new HttpGet(url);
                CloseableHttpResponse response = httpClient.execute(httpGet);

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new Exception();
                }

                String content = EntityUtils.toString(response.getEntity(), "utf8");
                JSONObject jsonObject = new JSONObject(content);
                JSONArray data = (JSONArray) jsonObject.get("data");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<CalendarContest> list = new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject json = data.getJSONObject(i);
                    String contestName = (String) json.get("contestName");
                    String link = (String) json.get("link");
                    String ojName = (String) json.get("ojName");
                    Date startTime = new Date((Long) json.get("startTime"));
                    Date endTime = new Date((Long) json.get("endTime"));
//                    System.out.println(contestName);
//                    System.out.println(link);
//                    System.out.println(ojName);
//                    System.out.println(simpleDateFormat.format(startTime));
//                    System.out.println(simpleDateFormat.format(endTime));
//                    System.out.println("");
                    CalendarContest calendarCompetitions = new CalendarContest();
                    calendarCompetitions.setOJName(ojName).setTitle(contestName).setUrl(link).setBeginTime(startTime).setEndTime(endTime);
                    list.add(calendarCompetitions);
                }

                boolean flag = calendarContestService.saveBatch(list);
                if (flag == false) {
                    throw new Exception();
                }

                httpGet.releaseConnection();
                break;
            } catch (Exception e) {
                Thread.sleep(1000 * 60);
                e.printStackTrace();
            }
        }

        httpClient.close();
    }
}
