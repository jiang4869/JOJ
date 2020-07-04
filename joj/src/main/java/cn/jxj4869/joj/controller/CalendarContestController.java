package cn.jxj4869.joj.controller;


import cn.jxj4869.joj.entity.CalendarContest;
import cn.jxj4869.joj.service.ICalendarContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@Controller
@RequestMapping("/calendarContest")
public class CalendarContestController {

    @Autowired
    private ICalendarContestService calendarContestService;

    @RequestMapping("list")
    public String list(Model model) {

        List<CalendarContest> list = calendarContestService.list();
        model.addAttribute("contestList",list);
        return "index::contestList";
    }

}
