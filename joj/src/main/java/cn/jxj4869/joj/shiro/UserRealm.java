package cn.jxj4869.joj.shiro;

import cn.jxj4869.joj.entity.Resource;
import cn.jxj4869.joj.entity.Role;
import cn.jxj4869.joj.entity.User;
import cn.jxj4869.joj.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author JiangXiaoju
 * @date 2020-06-10 1:09
 */
public class UserRealm extends AuthorizingRealm {


    @Autowired
    IUserService userService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了=>授权doGetAuthorizationInfo");
        //获取用户
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<String> list = new ArrayList<>();
        List<String> roleNameList = new ArrayList<>();
        Set<Role> roles =user.getRoles();
        if(CollectionUtils.isNotEmpty(roles)){
            for (Role role: roles) {
                roleNameList.add(role.getRoleName());
                Set<Resource> permissionSet = role.getResources();
                if(CollectionUtils.isNotEmpty(permissionSet)) {
                    for (Resource permission :
                            permissionSet) {
                        list.add(permission.getResourceName());
                    }
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(list);
        info.addRoles(roleNameList);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了=>认证doGetAuthenticationInfo");

        //token携带了用户信息
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        //获取用户名
        String username = usernamePasswordToken.getUsername();
        String password =String.valueOf( usernamePasswordToken.getPassword());
        System.out.println(password);
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("userName",username);
        User user = userService.getOne(wrapper);
        if (user == null) {
            throw new UnknownAccountException("Username does not exist！");
        } else {
            //当前realm对象的name
            String realmName = getName();
            //盐值

            ByteSource credentialsSalt = ByteSource.Util.bytes(user.getUserName());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), credentialsSalt, realmName);
            return info;
        }


    }
}
