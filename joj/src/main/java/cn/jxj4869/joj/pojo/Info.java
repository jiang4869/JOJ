package cn.jxj4869.joj.pojo;

import java.util.HashMap;

/**
 *
 * @author JiangXiaoju
 * @date 2020-06-9 9:10
 *
 */

public class Info extends HashMap<String, Object> {
    public Info ok(String msg) {

        this.put("flag", true);
        this.put("msg", msg);
        return this;
    }

    public Info error(String msg) {
        this.put("flag", false);
        this.put("msg", msg);
        return this;
    }
}
