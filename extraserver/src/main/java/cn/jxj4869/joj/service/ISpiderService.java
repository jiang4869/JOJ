package cn.jxj4869.joj.service;

import cn.jxj4869.joj.entity.Problem;

public interface ISpiderService {
    public void receive1(Problem problem);
    public void receive2(Problem problem);
    public void receive3(Problem problem);
    public void crawl(Problem problem) throws Exception;
}
