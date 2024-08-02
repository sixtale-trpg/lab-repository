package org.infinity.sixtalebackend.domain.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.infinity.sixtalebackend.domain.member.domain.Member;
import org.infinity.sixtalebackend.domain.member.domain.Provider;
import org.infinity.sixtalebackend.domain.member.dto.AuthResponse;
import org.infinity.sixtalebackend.domain.member.repository.AuthRepository;
import org.springframework.core.env.Environment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    public Member socialLogin(String code, String registrationID) {
        log.info("======================================================");
        String accessToken = getAccessToken(code, registrationID);
        JsonNode userResourceNode = getUserResource(accessToken, registrationID);

        AuthResponse authResponse = new AuthResponse();
        log.info("userResource = {}", authResponse);

        switch (registrationID) {
            case "google": {
                authResponse.setEmail(userResourceNode.get("email").asText());
                authResponse.setNickname(UUID.randomUUID().toString());
                break;
            } case "naver": {
                authResponse.setEmail(userResourceNode.get("response").get("email").asText());
                authResponse.setNickname(UUID.randomUUID().toString());
                break;
            } default: {
                throw new RuntimeException("UNSUPPORTED SOCIAL TYPE");
            }
        }

        log.info("email = {}", authResponse.getEmail());
        log.info("======================================================");

        Member findMember = authRepository.findByEmail(authResponse.getEmail());
        if (findMember == null) {
            findMember = Member.builder()
                    .email(authResponse.getEmail())
                    .nickname(authResponse.getNickname())
                    .accessToken(accessToken)
                    .provider(Provider.valueOf(registrationID.toUpperCase()))
                    .providerUserID(String.valueOf(userResourceNode.get("id")))
                    .isWithdrawn(false)
                    .build();

            authRepository.save(findMember);
        }

        return findMember;
    }

    public String getAccessToken(String code, String registrationID) {
        String envPath = "spring.security.oauth2.client.";

        String clientID = env.getProperty(envPath + "registration." + registrationID + ".client-id");
        String clientSecret = env.getProperty(envPath + "registration." + registrationID + ".client-secret");
        String redirectURI = env.getProperty(envPath + "registration." + registrationID + ".redirect-uri");
        String tokenURI = env.getProperty(envPath + "provider." + registrationID + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientID);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectURI);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenURI, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    public JsonNode getUserResource(String accessToken, String registrationID) {
        String envPath = "spring.security.oauth2.client.";
        String resourceURI = null;
        if (registrationID.equals("google"))
            resourceURI = env.getProperty(envPath + registrationID + ".resource-uri");
        else if (registrationID.equals("naver"))
            resourceURI = env.getProperty(envPath + "provider." + registrationID + ".user-info-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceURI, HttpMethod.GET, entity, JsonNode.class).getBody();
    }

    @Override
    public void withdraw(Member member) {
        Member findMember = authRepository.findById(member.getId()).get();
        findMember.setIsWithdrawn(true);
    }

}
