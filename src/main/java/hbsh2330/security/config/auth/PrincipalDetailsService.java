package hbsh2330.security.config.auth;

import hbsh2330.security.entity.User;
import hbsh2330.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 .loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IOC 되어있는 loadUserByUsername 함수가 실행
// 시큐리티 session(내부 Authentication(내부 UserDetails))
@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //loadUserByUsername(String username)과
        //html name="username"의 이름이 같아야함
        System.out.println("username : " + username);
        User user = userRepository.findByUsername(username);
        if (user != null){
            return new PrincipalDetails(user);
        }
        return null;
    }
}
