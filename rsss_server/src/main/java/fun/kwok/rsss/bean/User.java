package fun.kwok.rsss.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class User  {
    private Long id;
    private String username;
    private String password;
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    private Date lastlogintime;
    private Integer role; //0超级管理员 1管理员 2终端机器
    public User(Long id, String username, String password, Date lastlogintime, Integer role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastlogintime = lastlogintime;
        this.role = role;
    }
    public User(Long id, String username,Date lastlogintime, Integer role) {
        this.id = id;
        this.username = username;
        this.lastlogintime = lastlogintime;
        this.role = role;
    }

    public User(Long id, String username, String password, Integer role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
