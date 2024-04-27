package diary.app.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import diary.app.annotations.Auditable;
import diary.app.annotations.Loggable;
import diary.app.config.StaticConfig;
import diary.app.dao.TokenDao;
import diary.app.service.TokenService;
import diary.app.service.TrainingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

import static diary.app.servlet.GetParams.LOGIN_PARAM;
import static diary.app.servlet.GetParams.TOKEN_PARAM;

@Auditable
@Loggable
public class GetTrainingsTypesServlet extends HttpServlet {
    private final TrainingService trainingService;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    public GetTrainingsTypesServlet() {
        this.trainingService = StaticConfig.SERVICES_FACTORY.getTrainingService();
        this.objectMapper = new ObjectMapper();
        this.tokenService = StaticConfig.SERVICES_FACTORY.getTokenService();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(LOGIN_PARAM);
        String token = req.getParameter(TOKEN_PARAM);
        if(tokenService.validateToken(login, token)){
            Set<String> trainings = trainingService.getTrainingTypes();
            String json = objectMapper.writeValueAsString(trainings);
            resp.setStatus(200);
            resp.setContentType("application/json");
            resp.getOutputStream().write(json.getBytes());
        } else {
            String err = "token expired";
            resp.setStatus(500);
            resp.setContentType("application/json");
            resp.getOutputStream().write(err.getBytes());
        }
    }
}
