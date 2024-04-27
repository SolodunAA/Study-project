package diary.app.servlet;

import diary.app.annotations.Auditable;
import diary.app.annotations.Loggable;
import diary.app.config.StaticConfig;
import diary.app.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static diary.app.servlet.GetParams.LOGIN_PARAM;
import static diary.app.servlet.GetParams.PASSWORD_PARAM;

@Auditable
@Loggable
public class RegistrationServlet extends HttpServlet {
    private final RegistrationService registrationService;

    public RegistrationServlet() {
        this.registrationService = StaticConfig.SERVICES_FACTORY.getRegistrationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //get params from get request
        String login = req.getParameter(LOGIN_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);

        //run authorization
        boolean isRegistrationSuccessful = registrationService.register(login, password);
        //set status
        resp.setStatus(isRegistrationSuccessful ? 500 : 200);
        //set content type
        resp.setContentType("application/json");
        //create string to send to user
        String res =  isRegistrationSuccessful ? "success" : "failed to register";
        //send result to user
        resp.getOutputStream().write(res.getBytes());
    }
}
