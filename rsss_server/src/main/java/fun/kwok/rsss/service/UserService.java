package fun.kwok.rsss.service;

import fun.kwok.rsss.bean.User;
import fun.kwok.rsss.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;


    public User getUserByName(String username){
        return userMapper.getUserByName(username);
    }


    public User getUser(Long id){
        return userMapper.getUser(id);
    }
    public User getUserByLogin(String username,String password){
        return userMapper.getUserByLogin(username,password);
    }
    public int UpdatLastLoginTime(User user){
        return userMapper.UpdatLastLoginTime(user);
    }
    public int UpdatUser(User user){return userMapper.UpdatUser(user);}
    public int addUser(User user){
        return userMapper.addUser(user);
    }
    public List<User> getUsersByRole(Integer role){
        List<User> userList=userMapper.getUsersByRole(role);
        userList.forEach(user -> user.setPassword(""));
        return userList;
    }
    public List<User> getAllUser(){
        List<User> userList=userMapper.getAllUser();
        userList.forEach(user -> user.setPassword(""));
        return  userList;
    }
    public int delUser(Long id){
        return userMapper.delUser(id);
    }
}
