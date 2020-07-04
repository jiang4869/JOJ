package cn.jxj4869.joj.controller;


import cn.jxj4869.joj.service.IOjDailyBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
@RestController
@RequestMapping("/ojDailyBoard")
public class OjDailyBoardController {


    @Autowired
    private IOjDailyBoardService ojDailyBoardService;

    @GetMapping("/list")
    public Object data(){
        return ojDailyBoardService.list();
    }

}
