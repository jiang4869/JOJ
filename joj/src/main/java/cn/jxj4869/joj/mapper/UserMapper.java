package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.User;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface UserMapper extends BaseMapper<User> {
    /*
        @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;

    @TableField("userName")
    private String userName;

    @TableField("nickName")
    private String nickName;

    private String password;

    private String email;

    private String qq;

    private String avatar;
    @TableField("createTime")
    private Date createTime;

    @TableField("updateTime")
    private Date updateTime;
    @TableField(exist = false)
    private Set<Role> roles;
     */

    @Override
    @Select("select * from joj_user ${ew.customSqlSegment}")
    @Results({
            @Result(property = "uid",column = "uid"),
            @Result(property = "userName",column = "userName"),
            @Result(property = "nickName",column = "nickName"),
            @Result(property = "password",column = "password"),
            @Result(property = "email",column = "email"),
            @Result(property = "qq",column = "qq"),
            @Result(property = "avatar",column = "avatar"),
            @Result(property = "createTime",column = "createTime"),
            @Result(property = "updateTime",column = "updateTime"),
            @Result(property = "roles",column = "uid",many = @Many(select = "cn.jxj4869.joj.mapper.RoleMapper.selectByUid"))
    })
    User selectOne( @Param(Constants.WRAPPER)Wrapper<User> queryWrapper);



    @Select("select joj_user.* ,joj_participate.cid from joj_user inner join joj_participate on joj_user.uid=joj_participate.uid ${ew.customSqlSegment}")
    @Results({
            @Result(property = "uid",column = "uid"),
            @Result(property = "userName",column = "userName"),
            @Result(property = "nickName",column = "nickName"),
    })
    List<User> selectParticipateContest(@Param(Constants.WRAPPER)Wrapper<User> queryWrapper);
}
