package hbsh2330.security.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다. (Security ContextHolder)
// 시큐리티 세션에 들어갈 수 있는 오브젝트 타입은 Authentication 타입의 객체
// Authentication 안에 User 정보가 있어야함.
// User오브젝트 타입은 UserDetails 타입 객체

import hbsh2330.security.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티가 가지고 있는 Security ContextHolder 여기에 들어갈 수 있는 객체가 Authentication 타입의 객체 안에 들어갈 수 있는 객체가 UserDetails 타입의 객체
// ContextHolder.get(); => Authentication.get(); => UserDetails.get(); => user

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    //일반 로그인
    public PrincipalDetails(User user){
        this.user = user;
    }

    //Oauth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //UserDetails 타입
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {// 해당 유저의 권한을 리턴하는 곳
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() { // 유저 패스워드
        return user.getPassword();
    }

    @Override
    public String getUsername() { // 유저 이름
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { //계정이 만료되지 않았는가? false = 만료되었음 true = 만료되지 않았음
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //계정이 잠기지 않았는가? false =잠겼음 true = 잠기지 않았음
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 여기에서 자격 증명 만료 여부를 결정하는 로직을 구현
        // 만료되지 않았으면 true를 반환
        // 만료되었으면 false를 반환
        return true;
    }

    @Override
    public boolean isEnabled() { //계정이 활성화 되었는가? true 활성화 false 활성화되지 않음 휴면계정 등록시 사용
        return true;
    }

    //Oauth2 유저 타입
    @Override
    public String getName() {
        return null;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
