package hbsh2330.security.config;

import hbsh2330.security.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;


//@Configuration //기존 extends WebSecurityConfigurerAdapter 를 통해 세팅
//
//-> 이제 WebSecurityConfigurerAdapter를 상속받는 방식은 삭제되었음. SecurityFilterChain 빈을 스프링 컨테이너에 등록해줘야 함.
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults());
//    }
//}
@Configuration //bean등록시 사용
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.
@EnableMethodSecurity(securedEnabled = true) // secured 어노테이션 활성화
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable); //csrf비활성화
        // 권한 설정
        http.authorizeHttpRequests(
                authorize -> {
                    authorize
                            .requestMatchers("/user/**").authenticated() // 로그인 한 사람만 접근가능
                            .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN") // 로그인 했지만 ROLE_ADMIN 또는ROLE_MANAGER권한이 있어야지 접근 가능
                            .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                            .anyRequest().permitAll();// 위의 3개 주소가 아닌 모든 것은 권한 허용
                }
        );

        //접근 권한이 없는 페이지 접근시 로그인 페이지로 이동
        http.formLogin(
                login -> {
                    login
                            .loginPage("/loginForm") // 권한이 없는 유저가 입장할 때 loginForm으로 이동
                            .loginProcessingUrl("/login") // login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행함 /login을 컨트롤러에 따로 구현할 필요가 없다!!
                            .defaultSuccessUrl("/"); // 로그인이 성공하면 갈 주소
                }
        );
        http.oauth2Login(
                oauth2 -> {
                    oauth2
                            .loginPage("/loginForm") // 구글 로그인이 완료된 뒤에 후처리가 필요 1. 코드 받기 2. 엑세스 토큰 받기 3. 사용자 프로필 정보 가져와서 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 하거나, 4-2 정보가 부족할 경우 추가로 받아서 회원가입시킴
                            .userInfoEndpoint(
                                    userInfoEndpoint -> {
                                        userInfoEndpoint
                                                .userService(principalOauth2UserService); // 타입이 oauth2 userservice 가 되어야함
                                    });
                }
        );
        return http.build();
    }
}
