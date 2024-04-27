package diary.app.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import diary.app.annotations.Auditable;
import diary.app.annotations.Loggable;
import diary.app.config.StaticConfig;
import diary.app.dao.UserRolesDao;
import diary.app.dto.Training;
import diary.app.dto.UserAction;
import diary.app.service.TokenService;
import diary.app.service.TrainingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import static diary.app.servlet.GetParams.*;

@Auditable
@Loggable
public class GetTrainingsFromThePeriodServlet extends HttpServlet {

    private final TrainingService trainingService;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;
    private final UserRolesDao rolesDao;

    public GetTrainingsFromThePeriodServlet() {
        this.trainingService = StaticConfig.SERVICES_FACTORY.getTrainingService();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(df);
        this.tokenService = StaticConfig.SERVICES_FACTORY.getTokenService();
        this.rolesDao = StaticConfig.SERVICES_FACTORY.getDaoFactory().getUserRolesDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(LOGIN_PARAM);
        String token = req.getParameter(TOKEN_PARAM);
        String date_start = req.getParameter(DATE_START_PARAM);
        String date_finish = req.getParameter(DATE_END_PARAM);

        if (tokenService.validateToken(login, token)) {
            if (!rolesDao.getUserRole(login).isActionAllowed(UserAction.SEE_USER_TRAININGS)) {
                String err = "action not allowed";
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getOutputStream().write(err.getBytes());
            } else {
                List<Training> trainings = trainingService.getTrainingsFromThePeriod(login, LocalDate.parse(date_start), LocalDate.parse(date_finish));
                String json = objectMapper.writeValueAsString(trainings);
                resp.setStatus(200);
                resp.setContentType("application/json");
                resp.getOutputStream().write(json.getBytes());
            }
        } else {
            String err = "token expired";
            resp.setStatus(500);
            resp.setContentType("application/json");
            resp.getOutputStream().write(err.getBytes());
        }

    }
}
