package cn.jxj4869.joj.controller;

import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.pojo.MyPage;
import cn.jxj4869.joj.pojo.vo.StatusVo;
import cn.jxj4869.joj.service.ISubmissionService;
import cn.jxj4869.joj.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author JiangXiaoju
 * @date 2020-06-15 16:29
 */
@Controller
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private ISubmissionService submissionService;



    @RequestMapping({"/","/list/{page}","/list/","","/list"})
    public String list(StatusVo statusVo, Model model){
        MyPage<Submission> submissionMyPage = submissionService.submissionListPage(statusVo);
        List<Submission> submissions = submissionMyPage.getRecords();
        System.out.println("Status hello world ==============");
        model.addAttribute("ojList", Constants.OJList);
        model.addAttribute("statusVo",statusVo);
        model.addAttribute("submissionsList",submissions);
        model.addAttribute("page",submissionMyPage);
        return "status/status";
    }



}
