package com.example.security.config.oauth;

import java.util.Map;

public class GoogleInfo extends OAuth2UserInfo{

    public GoogleInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getUsername() {
        return "Google_" + String.valueOf(attributes.get("sub"));
    }

    @Override
    public String getId() {
        return "Google_" + (String) attributes.get("googleId");
    }
}
