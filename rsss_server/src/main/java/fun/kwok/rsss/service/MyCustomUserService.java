package fun.kwok.rsss.service;

import fun.kwok.rsss.bean.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class MyCustomUserService implements UserDetailsService {
    private final UserService userService;

    public  MyCustomUserService(UserService userService){
        this.userService=userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user=userService.getUserByName(username);
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
            if (user!=null){
                if (user.getRole()==2){
                    GrantedAuthority au = new SimpleGrantedAuthority("ROLE_USER");
                    list.add(au);
                }
                else if (user.getRole()==0||user.getRole()==1){
                    GrantedAuthority au = new SimpleGrantedAuthority("ROLE_ADMIN");
                    list.add(au);
                   }
                return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), list);
            }
        return null;
    }
}
