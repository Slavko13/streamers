package com.streamers.app.controller.auth;

import com.streamers.app.controller.AbstractController;
import com.streamers.app.dto.auth.TokenResponse;
import com.streamers.app.dto.auth.UserDto;

import com.streamers.app.utils.EmailValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class KeyCloakController extends AbstractController
{

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    private final Keycloak keycloak;

    public KeyCloakController(RestTemplate restTemplate, final Keycloak keycloak) {
        this.restTemplate = restTemplate;
        this.keycloak = keycloak;
    }


    @Operation(summary = "Регистрация", description = "Этот метод отвечает за регистрацию пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно создан пользователь"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/register")
    public ResponseEntity registerUser(@Parameter(name = "Интерфейс для создания пользователя", required = true) @RequestBody UserDto userDto) {
        System.out.println(getCurrentUsername());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName("slavko");
        user.setLastName("slavko");
        user.setEnabled(true);

        // Устанавливаем учетные данные пользователя
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.getPassword());
        credential.setTemporary(false); // пользователь не будет вынужден менять пароль при первом входе
        user.setCredentials(Collections.singletonList(credential));


        // Создаем пользователя в Keycloak
        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() == 201) {
            return new ResponseEntity<>("created", HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(response.getStatus()).build();
        }
    }


    @PostMapping("/login")
    @Operation(summary = "Регистрация", description = "Этот метод отвечает за логин и получение токена пользователя.")
    public ResponseEntity<TokenResponse> authenticate(@Parameter(name = "Интерфейс для аунтитефикации пользователя", required = true) @RequestBody UserDto userDto) {
        String authServerUrl = "http://localhost:8081/auth/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", userDto.getUsername());
        map.add("password", userDto.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());

        TokenResponse tokenResponse;

        try {
            tokenResponse = restTemplate.postForObject(authServerUrl, request, TokenResponse.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getStatusCode());
        }
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/userinfo")
    public String getUserInfo() {
        return "Hello, " + getCurrentUsername();
    }


}
