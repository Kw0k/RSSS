package fun.kwok.rsss.controller;

import fun.kwok.rsss.bean.ResultInfo;
import fun.kwok.rsss.bean.User;
import fun.kwok.rsss.service.UserService;
import fun.kwok.rsss.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
@Controller
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "*",methods = {})
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    UserService userService;


    @ResponseBody
    @GetMapping("/login")
    public ResultInfo getLoginUser(HttpSession session){
        User user=(User)session.getAttribute("user");
        if (user!=null)
            return new ResultInfo(true,"success",user);
        else
            return new ResultInfo(false,"请先登陆",null);
    }
    @ResponseBody
    @PostMapping("/login")
    public ResultInfo getUserById(@RequestParam("username") String username,
                                  @RequestParam("password") String password,
                                  @RequestParam("checkcode") String checkcode,
                                  HttpSession session){
        if(session.getAttribute("check_code")!=null&&checkcode.toUpperCase().equals(session.getAttribute("check_code")))
        {
            session.setAttribute("check_code",null);
            User user=userService.getUserByLogin(username, MD5Util.MD5Lower(password));
            if(user!=null){
                User temp=new User(user.getId(),user.getUsername(),"",user.getLastlogintime(),user.getRole());
                session.setAttribute("user",temp);
                user.setLastlogintime(new Date());
                userService.UpdatLastLoginTime(user);
                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(username, password);
                Authentication authentication = authenticationManager.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return new ResultInfo(true,"登录成功",null);
            }else {
                return new ResultInfo(false,"用户名或密码错误",null);
            }
        }else {
            return  new ResultInfo(false,"验证码错误",null);
        }
    }

    @ResponseBody
    @GetMapping("/logout")
    public ResultInfo loginOut(HttpSession session){
        User user=(User)session.getAttribute("user");
        if (user.getRole()!=2){
            SecurityContextHolder.clearContext();
            session.invalidate();
            return new ResultInfo(true,"退出成功",null);
        }else {
            return new ResultInfo(false,"非法请求",null);
        }
    }

    @ResponseBody
    @PostMapping("/logout")
    public ResultInfo loginOutByPwd(
            @RequestParam("password") String password
            ,HttpSession session){
        User user=(User)session.getAttribute("user");
        if (user.getRole()==2){
            User user2=userService.getUserByLogin(user.getUsername(), MD5Util.MD5Lower(password));
            if (user2!=null){
                SecurityContextHolder.clearContext();
                session.invalidate();
                return new ResultInfo(true,"退出成功",null);
            }else {
                return new ResultInfo(false,"密码错误",null);
            }
        }else {
            return new ResultInfo(false,"非法请求",null);
        }
    }
}
