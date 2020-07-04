package cn.jxj4869.joj.mapper;

import cn.jxj4869.joj.entity.Role;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jxj4869
 * @since 2020-06-11
 */
public interface RoleMapper extends BaseMapper<Role> {


    @Override
    List<Role> selectList(Wrapper<Role> queryWrapper);
/*
    private Integer roleId;

    @TableField("roleName")
    private String roleName;
 */

    @Select("select joj_role.* from joj_role inner join joj_user_role on joj_role.roleId=joj_user_role.roleId where joj_user_role.uid=#{uid}")
    @Results({
            @Result(property = "roleId",column = "roleId"),
            @Result(property = "roleName",column = "roleName")
    })
    List<Role> selectByUid(Serializable uid);
}
