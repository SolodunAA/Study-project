package diary.app.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import diary.app.annotations.Auditable;
import diary.app.annotations.Loggable;
import diary.app.config.StaticConfig;
import diary.app.service.AuthenticationService;
import diary.app.service.TokenService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static diary.app.servlet.GetParams.LOGIN_PARAM;
import static diary.app.servlet.GetParams.PASSWORD_PARAM;

@Auditable
@Loggable
public class LoginServlet extends HttpServlet {

    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private  final ObjectMapper objectMapper = new ObjectMapper();

    public LoginServlet() {
        this.authenticationService = StaticConfig.SERVICES_FACTORY.getAuthenticationService();
        this.tokenService = StaticConfig.SERVICES_FACTORY.getTokenService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter(LOGIN_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);
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
