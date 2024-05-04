package diary.app.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import diary.app.annotations.Auditable;
import diary.app.annotations.Loggable;
import diary.app.config.StaticConfig;
import diary.app.dto.LoginPasswordDto;
import diary.app.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@Auditable
@Loggable
public class RegistrationServlet extends HttpServlet {
    private final RegistrationService registrationService;
    private final ObjectMapper objectMapper;

    public RegistrationServlet() {
        this.objectMapper = new ObjectMapper();
        this.registrationService = StaticConfig.SERVICES_FACTORY.getRegistrationService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
        System.out.println("login = " + login);
        System.out.println("password = " + password);

        boolean isRegistrationSuccessful = registrationService.register(login, password);
        resp.setStatus(isRegistrationSuccessful ? 500 : 200);
        resp.setContentType("application/json");
        String res =  isRegistrationSuccessful ? "success" : "failed to register";
        resp.getOutputStream().write(res.getBytes());
    }
}
