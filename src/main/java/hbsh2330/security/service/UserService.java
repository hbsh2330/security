package hbsh2330.security.service;

import hbsh2330.security.entity.User;
import hbsh2330.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public String confirmEmail(String email) {
        User byEmail = userRepository.findByEmail(email);
        if (byEmail != null) {
            System.out.println(byEmail);
            return "FAILURE_DUPLICATED_USER_EMAIL";
        }
        return "SUCCESS";
    }

    public String join(User user) { // 회원가입
            user.setRole("ROLE_USER");
            String rawPassword = user.getPassword();
            String encPassword = bCryptPasswordEncoder.encode(rawPassword);
            user.setPassword(encPassword);
            userRepository.save(user);
            return "SUCCESS";
    }
}
