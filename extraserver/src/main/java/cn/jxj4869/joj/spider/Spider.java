package cn.jxj4869.joj.spider;

import cn.jxj4869.joj.entity.Description;
import cn.jxj4869.joj.entity.Problem;

import java.io.IOException;
import java.net.URISyntaxException;

public interface Spider {
    public Description crawl(Problem problem) throws Exception;
}
