package cn.jxj4869.joj.service.impl;

import cn.jxj4869.joj.entity.CalendarContest;
import cn.jxj4869.joj.entity.OjDailyBoard;
import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.mapper.CalendarContestMapper;
import cn.jxj4869.joj.mapper.ContestMapper;
import cn.jxj4869.joj.mapper.OjDailyBoardMapper;
import cn.jxj4869.joj.mapper.SubmissionMapper;
import cn.jxj4869.joj.service.ICalendarContestService;
import cn.jxj4869.joj.service.ITaskService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author JiangXiaoju
 * @date 2020-06-11 19:29
 */

@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private CalendarContestMapper calendarContestMapper;

    @Autowired
    private ICalendarContestService calendarContestService;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private OjDailyBoardMapper ojDailyBoardMapper;

    /**
     * 每天零点更新全网比赛信息，从牛客网爬取。
     * @throws Exception
     */
    @Override
    @Scheduled(cron = "0 0 0 1/1 * ?")
    @Transactional
    public void calendarCompetitions() throws Exception {
        String url = "https://ac.nowcoder.com/acm/calendar/contest?token=&month=";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        System.out.println("hello world");
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


    /**
     * 每分钟更新比赛状态
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateContestStatus() {
        System.out.println("update status");
        contestMapper.updateStatus();
    }


    /**
     * 每天0：30分更新前一天的统计数据。
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 30 0 1/1 * ?")
    public void updateOJDailyBoard() {
      while (true){
          try {
              Calendar cal = Calendar.getInstance();
              cal.add(Calendar.DATE, -1);
              Date d = cal.getTime();

              SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
              String yesterday = sp.format(d);//获取昨天日期
              System.out.println(yesterday);

              Integer countSubmit = submissionMapper.countSubmit(yesterday);
              Integer countACSubmit = submissionMapper.countACSubmit(yesterday);
              System.out.println(countSubmit);
              System.out.println(countACSubmit);

              UpdateWrapper<OjDailyBoard> wrapper = new UpdateWrapper<>();
              wrapper.eq("collectTime", yesterday);
              int delete = ojDailyBoardMapper.delete(wrapper);
              System.out.println(delete);

              OjDailyBoard ojDailyBoard = new OjDailyBoard();
              ojDailyBoard.setAccepted(countACSubmit);
              ojDailyBoard.setSubmitCount(countSubmit);
              ojDailyBoard.setCollectTime(sp.parse(yesterday));
              ojDailyBoardMapper.insert(ojDailyBoard);
              break;
          }catch (Exception e){
                try {
                    Thread.sleep(1000*20);
                }catch (Exception e1){

                }
          }
      }
    }
}
