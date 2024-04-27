package diary.app.servlet;

import diary.app.annotations.Auditable;
import diary.app.annotations.Loggable;
import diary.app.config.StaticConfig;
import diary.app.dao.UserRolesDao;
import diary.app.dto.UserAction;
import diary.app.service.TokenService;
import diary.app.service.TrainingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static diary.app.servlet.GetParams.*;

@Auditable
@Loggable
public class AddTrainingTypeServlet extends HttpServlet {

    private final TrainingService trainingService;
    private final TokenService tokenService;
    private final UserRolesDao rolesDao;


    public AddTrainingTypeServlet() {
        this.trainingService = StaticConfig.SERVICES_FACTORY.getTrainingService();
        this.tokenService = StaticConfig.SERVICES_FACTORY.getTokenService();
        this.rolesDao = StaticConfig.SERVICES_FACTORY.getDaoFactory().getUserRolesDao();
    }

    public AddTrainingTypeServlet(TrainingService trainingService, TokenService tokenService, UserRolesDao rolesDao) {
        this.trainingService = trainingService;
        this.rolesDao = rolesDao;
        this.tokenService = tokenService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(LOGIN_PARAM);
        String token = req.getParameter(TOKEN_PARAM);
        String training_type = req.getParameter(TRAINING_TYPE_PARAM);
        if (tokenService.validateToken(login, token)) {
            if (!rolesDao.getUserRole(login).isActionAllowed(UserAction.CHANGE_APP_SETTINGS)) {
                String err = "action not allowed";
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getOutputStream().write(err.getBytes());
            } else {
                boolean isSaved = trainingService.addTrainingType(training_type);
                resp.setStatus(isSaved ? 200 : 404);
                resp.setContentType("application/json");
                String res = isSaved ? "saved" : "failed to save";
                resp.getOutputStream().write(res.getBytes());
            }
        } else {
            String err = "token expired";
            resp.setStatus(500);
            resp.setContentType("application/json");
            resp.getOutputStream().write(err.getBytes());
        }
    }
}
