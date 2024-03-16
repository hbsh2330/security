package hbsh2330.security.config.oauth.provider;

import hbsh2330.security.config.oauth.OAuth2UserInfo;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes; //oAuth2User.getAttributes()
    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
