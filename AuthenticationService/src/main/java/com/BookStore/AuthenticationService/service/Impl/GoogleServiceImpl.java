package com.BookStore.AuthenticationService.service.Impl;

import com.BookStore.AuthenticationService.service.AuthService;
import com.BookStore.AuthenticationService.service.GoogleService;
import com.BookStore.AuthenticationService.dto.BookStoreResponse;
import com.BookStore.AuthenticationService.dto.UserInfoDTO;
import com.BookStore.AuthenticationService.jwt.JwtTokenProvider;
import com.BookStore.AuthenticationService.repository.TaiKhoanRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
public class GoogleServiceImpl implements GoogleService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoUri;

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String authorizationUri;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.google.user-name-attribute}")
    private String userNameAttribute;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    public BookStoreResponse generateURL() {
        return BookStoreResponse.<String>builder()
                .code(200)
                .status("")
                .data(buildOAuthUrl()).build();
    }

    private String buildOAuthUrl(
    ) {
        String baseUrl = "https://accounts.google.com/o/oauth2/v2/auth?";
        StringBuilder urlBuilder = new StringBuilder(baseUrl);

        urlBuilder.append("redirect_uri=").append(redirectUri);
        urlBuilder.append("&response_type=").append("code");
        urlBuilder.append("&client_id=").append(clientId);
        scope = scope.replace(",","+");
        urlBuilder.append("&scope=").append(scope);
        urlBuilder.append("&access_type=").append("offline");

        return urlBuilder.toString();
    }

    @Override
    public BookStoreResponse loginGoogle(Map<String, String> data) {
        String accessToken = getOauthAccessTokenGoogle( data.get("code"));
        if(accessToken == null) {return BookStoreResponse.<UserInfoDTO>builder()
                .code(501)
                .status("Lỗi đăng nhập")
                .data(null).build();}
        UserInfoDTO user = getProfileDetailsGoogle(accessToken);
        return BookStoreResponse.<UserInfoDTO>builder()
                .code(200)
                .status("Đăng nhập với google thành công!")
                .data(user).build();
    }

    private String getOauthAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("scope", "profile");
        params.add("scope", "email");
//        params.add("scope", "openid");
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";
        try {
            String response = restTemplate.postForObject(url, requestEntity, String.class);
            JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);

            return jsonObject.get("access_token").toString().replace("\"", "");
        } catch (Exception exception) {
                System.out.println(exception.getMessage());
            return null;
        }

    }


    private UserInfoDTO getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        JsonObject jsonObject = new Gson().fromJson(response.getBody(), JsonObject.class);

        String email = jsonObject.get("email").toString().replace("\"", "");
        String id =  jsonObject.get("id").toString().replace("\"", "");
        String ho = jsonObject.get("given_name").toString().replace("\"", "");
        String ten = jsonObject.get("family_name")==null?jsonObject.get("name").toString().replace("\"", "")
                : jsonObject.get("family_name").toString().replace("\"", "");
        String picture = jsonObject.get("picture").toString().replace("\"", "");
        System.out.println(picture);
        String role = "KHACHHANG";
        String username = taiKhoanRepository.loadTenDangNhap(email);
        if(username == null)
        {
            username = taiKhoanRepository.loadMaNhanVien(email);
            role = "NHANVIEN";
        }
        System.out.println(username);
        if(username == null ) {
            try {

                taiKhoanRepository.taoTaiKhoan(id, "", 2);
                taiKhoanRepository.taoKhachHang(id, email,
                        ho.equals(ten)?null:ho, ten, true, null, null, null);
                username = id;

            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                System.out.println("ERROR-62: Đang ký tài khoản--- " + e.getMessage());
            }
        }
        String token = jwtTokenProvider.generateToken1(username,role);
        System.out.println(token);
        Map<String, Object> userDefault = taiKhoanRepository.layThongTinUser(username);
        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .username((String) userDefault.get("TENDANGNHAP"))
                .role((String) userDefault.get("QUYEN"))
                .token(token)
                .hoTen((String) userDefault.get("HOTEN"))
                .hinhAnh((String) userDefault.get("HINHANH"))
                .pictureGoogle(picture)
                .build();

        return userInfoDTO;
    }

}
