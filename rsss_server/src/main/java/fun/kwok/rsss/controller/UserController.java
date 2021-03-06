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
        if (user.getRole()==0){// ?????????????????????
            if (newuser.getId()!=null){//????????????
                if (userService.getUser(id).getRole()==0)
                    if (newuser.getRole()!=0)
                        return new ResultInfo(false,"??????????????????????????????????????????",null);
                flag=  userService.UpdatUser(newuser);}
            else {//????????????
                if (newuser.getRole()==0)
                    return new ResultInfo(false,"??????????????????????????????",null);
                try{
                    flag=   userService.addUser(newuser);
                }catch (Exception e){

                }
            }
            if (flag>0)
            return new ResultInfo(true,"????????????",null);
            else
                return new ResultInfo(false,"????????????",null);
        }else {//???????????????
            if (newuser.getId()!=null){//????????????
                if (userService.getUser(id).getRole()<2)
                    return new ResultInfo(false,"????????????????????????????????????",null);
                if (newuser.getRole()<2)
                    return new ResultInfo(false,"???????????????????????????????????????",null);
                flag=  userService.UpdatUser(newuser);}
            else {//????????????
                if (newuser.getRole()<2)
                    return new ResultInfo(false,"????????????????????????????????????",null);
                try{
                    flag=   userService.addUser(newuser);
                }catch (Exception e){

                }
            }
            if (flag>0)
                return new ResultInfo(true,"????????????",null);
            else
                return new ResultInfo(false,"????????????",null);
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
            return new ResultInfo(false,"????????????",users);
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
                return new ResultInfo(false,"????????????????????????????????????",null);
        if (userService.getUser(id).getRole()==0)
            return new ResultInfo(false,"?????????????????????????????????",null);
        try {
            if (userService.delUser(id)>0)
                return new ResultInfo(true,"????????????",null);
            else
                return  new ResultInfo(false,"????????????",null);
        }catch (Exception e){
            return  new ResultInfo(false,"???????????????????????????????????????????????????",null);
        }

    }


}
