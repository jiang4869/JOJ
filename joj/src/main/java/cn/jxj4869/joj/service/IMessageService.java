package cn.jxj4869.joj.service;

import cn.jxj4869.joj.entity.Message;
import cn.jxj4869.joj.pojo.Info;
import cn.jxj4869.joj.pojo.MyPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface IMessageService extends IService<Message> {
    public MyPage<Message> messagePage(Integer currentPage);

    public Info messageDelete(Integer mid);

    public Info messageSave(Message message);

    public MyPage<Message> messageAllPage(Integer currentPage);
}
