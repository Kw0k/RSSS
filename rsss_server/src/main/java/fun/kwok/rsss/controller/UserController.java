package fun.kwok.rsss.controller;


import fun.kwok.rsss.bean.ResultInfo;
import fun.kwok.rsss.bean.User;
import fun.kwok.rsss.service.UserService;
import fun.kwok.rsss.utils.CheckCodeUtil;
import fun.kwok.rsss.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
@Controller
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})

public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/CheckCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckCodeUtil.code(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @ResponseBody
    @GetMapping("/UserRole2")
    public ResultInfo getRole2Users(){
        return new ResultInfo(true,"success",userService.getUsersByRole(2));
    }

    @ResponseBody
    @PostMapping("/User")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo addOrUpdateUser(  @RequestParam(value = "id",required = false) Long id,
                                        @RequestParam(value = "username",required = false) String username,
                                        @RequestParam(value = "password",required = false) String password,
                                        @RequestParam(value = "role",required = false) int role,
                                        HttpSession httpSession){
        User user=(User) httpSession.getAttribute("user");
        User newuser=new User(id,username,password,role);
        if (!"".equals(newuser.getPassword()))
            newuser.setPassword(MD5Util.MD5Lower(newuser.getPassword()));
        else
            newuser.setPassword(null);
        int flag=0;
        if (user.getRole()==0){// 超级管理员操作
            if (newuser.getId()!=null){//修改用户
                if (userService.getUser(id).getRole()==0)
                    if (newuser.getRole()!=0)
                        return new ResultInfo(false,"无法更改超级管理员的用户类型",null);
                flag=  userService.UpdatUser(newuser);}
            else {//添加用户
                if (newuser.getRole()==0)
                    return new ResultInfo(false,"只能有一个超级管理员",null);
                try{
                    flag=   userService.addUser(newuser);
                }catch (Exception e){

                }
            }
            if (flag>0)
            return new ResultInfo(true,"操作完成",null);
            else
                return new ResultInfo(false,"操作失败",null);
        }else {//管理员操作
            if (newuser.getId()!=null){//修改用户
                if (userService.getUser(id).getRole()<2)
                    return new ResultInfo(false,"没有权限修改该类型的用户",null);
                if (newuser.getRole()<2)
                    return new ResultInfo(false,"没有权限将用户更改为该类型",null);
                flag=  userService.UpdatUser(newuser);}
            else {//添加用户
                if (newuser.getRole()<2)
                    return new ResultInfo(false,"没有权限添加该类型的用户",null);
                try{
                    flag=   userService.addUser(newuser);
                }catch (Exception e){

                }
            }
            if (flag>0)
                return new ResultInfo(true,"操作完成",null);
            else
                return new ResultInfo(false,"操作失败",null);
        }

    }

    @ResponseBody
    @GetMapping("/User")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo getAllUser(HttpSession httpSession){
       User user=(User)httpSession.getAttribute("user");
        List<User> users=new ArrayList<User>();
       if (user.getRole()==0)
        users=userService.getAllUser();
       else users=userService.getUsersByRole(2);
        int tatal=users.size();
        if (tatal>0)
            return new ResultInfo(true,"",tatal,users);
        else
            return new ResultInfo(false,"查询出错",users);
    }
    @ResponseBody
    @DeleteMapping("/User/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResultInfo delUser(@PathVariable("id") Long id,
                              HttpSession httpSession
                              ){
        User user= (User) httpSession.getAttribute("user");
        if (user.getRole()>0)
            if (userService.getUser(id).getRole()<2)
                return new ResultInfo(false,"没有权限操作该类型的用户",null);
        if (userService.getUser(id).getRole()==0)
            return new ResultInfo(false,"不能删除超级管理员用户",null);
        try {
            if (userService.delUser(id)>0)
                return new ResultInfo(true,"删除成功",null);
            else
                return  new ResultInfo(false,"删除失败",null);
        }catch (Exception e){
            return  new ResultInfo(false,"删除失败，请检查该用户下是否有订单",null);
        }

    }


}
