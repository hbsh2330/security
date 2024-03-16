package hbsh2330.security.config.oauth;

import hbsh2330.security.config.auth.PrincipalDetails;
import hbsh2330.security.config.oauth.provider.FacebookUserInfo;
import hbsh2330.security.config.oauth.provider.GoogleUserInfo;
import hbsh2330.security.config.oauth.provider.KakaoUserInfo;
import hbsh2330.security.config.oauth.provider.NaverUserInfo;
import hbsh2330.security.entity.User;
import hbsh2330.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); //registrationId로 어떤 Oauth로 로그인 했는지 확인가능
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인 창 -> 로그인을 완료 -> 코드를 리턴받음 -> oauth2라이브러리가 받음 -> accessToken요청 = userRequest정보
        // -> loadUser함수 호출 -> 구글로부터 회원프로필을 받아줌 ->
        System.out.println("getAttributes : " + oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
            System.out.println("페이스북 로그인 요청");
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
            System.out.println("네이버 로그인 로그인 요청");
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            System.out.println(oAuth2User.getAttributes());
            System.out.println("카카오 로그인 로그인 요청");
        } else {
            System.out.println("우리는 구글과 페이스북과 네이버와 카카오만 지원해요 ㅎㅎ");
        }

        //페이스북으로 로그인시 페이스북, 구글로 로그인시 구글로그인 기능
        String provider = oAuth2UserInfo.getProvider(); //google
        String providerId = oAuth2UserInfo.getProviderId(); // 구글 프로바이더 id
        String username = provider+"_"+providerId; //google_
        String email = oAuth2UserInfo.getEmail(); // 구글 프로바이더 id
        String password = bCryptPasswordEncoder.encode("비밀번호");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null){
            System.out.println(provider + "로그인이 최초입니다.");
            userEntity = User.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .username(username)
                    .email(email)
                    .password(password)
                    .role(role)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("당신은" +provider+ "로그인을 이미 한적이 있습니다.");
        }
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
