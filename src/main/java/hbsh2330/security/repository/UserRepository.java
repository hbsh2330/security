package hbsh2330.security.repository;

import hbsh2330.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//CRUD 함수를 JpaRepository가 들고있음
// @Repository라는 어노테이션이 없어도 IOC됨 이유는 JpaRepository를 상속받았기 때문
public interface UserRepository extends JpaRepository<User, Long> {

    //select * from user where email = String email;
    User findByEmail(String email);
    //select * from user where username = String username;
    User findByUsername(String username);
}
