package cn.jxj4869.joj.service;

import cn.jxj4869.joj.entity.Submission;
import cn.jxj4869.joj.submitter.Submitter;

public interface IJudgeService {

    public void receive(Submission submission);

    public void judge(Submitter submitter, Submission submission);


}
