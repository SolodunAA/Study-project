package diary.app.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import diary.app.annotations.Auditable;
import diary.app.annotations.Loggable;
import diary.app.config.StaticConfig;
import diary.app.dto.LoginPasswordDto;
import diary.app.service.AuthenticationService;
import diary.app.service.Impl.TokenServiceImpl;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@Auditable
@Loggable
public class LoginServlet extends HttpServlet {

    private final AuthenticationService authenticationService;
    private final TokenServiceImpl tokenService;
    private  final ObjectMapper objectMapper = new ObjectMapper();

    public LoginServlet() {
        this.authenticationService = StaticConfig.SERVICES_FACTORY.getAuthenticationService();
        this.tokenService = StaticConfig.SERVICES_FACTORY.getTokenService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        String requestBody = buffer.toString();
        LoginPasswordDto loginPasswordDto = objectMapper.readValue(requestBody, LoginPasswordDto.class);

        String login = loginPasswordDto.getLogin();
        String password = loginPasswordDto.getPassword();

        String authResult = authenticationService.auth(login, password);
        boolean isLoginSuccessful = authResult != null;
        resp.setStatus(isLoginSuccessful ? 500 : 200);
        resp.setContentType("application/json");
        String res = isLoginSuccessful
                ? objectMapper.writeValueAsString(tokenService.createToken(login))
                : "failed to login";
        resp.getOutputStream().write(res.getBytes());
    }
}
