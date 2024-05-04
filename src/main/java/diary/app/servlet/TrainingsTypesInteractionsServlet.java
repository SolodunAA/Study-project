package diary.app.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import diary.app.annotations.Auditable;
import diary.app.annotations.Loggable;
import diary.app.config.StaticConfig;
import diary.app.dao.UserRolesDao;
import diary.app.dto.TrainingTypeDto;
import diary.app.dto.UserAction;
import diary.app.service.Impl.TokenServiceImpl;
import diary.app.service.Impl.TrainingServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

import static diary.app.servlet.GetParams.*;

@Auditable
@Loggable
public class TrainingsTypesInteractionsServlet extends HttpServlet {
    private final TrainingServiceImpl trainingServiceImpl;
    private final TokenServiceImpl tokenService;
    private final UserRolesDao rolesDao;
    private final ObjectMapper objectMapper;

    public TrainingsTypesInteractionsServlet() {
        this.trainingServiceImpl = StaticConfig.SERVICES_FACTORY.getTrainingService();
        this.tokenService = StaticConfig.SERVICES_FACTORY.getTokenService();
        this.rolesDao = StaticConfig.SERVICES_FACTORY.getDaoFactory().getUserRolesDao();
        this.objectMapper = new ObjectMapper();
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
        TrainingTypeDto trainingTypeDto = objectMapper.readValue(requestBody, TrainingTypeDto.class);

        String training_type = trainingTypeDto.getTrainingType();

        String login = req.getParameter(LOGIN_PARAM);
        String token = req.getParameter(TOKEN_PARAM);

        if (tokenService.validateToken(login, token)) {
            if (!rolesDao.getUserRole(login).isActionAllowed(UserAction.CHANGE_APP_SETTINGS)) {
                String err = "action not allowed";
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getOutputStream().write(err.getBytes());
            } else {
                boolean isSaved = trainingServiceImpl.addTrainingType(training_type);
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

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                boolean isDeleted = trainingServiceImpl.deleteTrainingType(training_type);
                resp.setStatus(isDeleted ? 200 : 404);
                resp.setContentType("application/json");
                String res = isDeleted ? "delete" : "failed to delete";
                resp.getOutputStream().write(res.getBytes());
            }
        } else {
            String err = "token expired";
            resp.setStatus(500);
            resp.setContentType("application/json");
            resp.getOutputStream().write(err.getBytes());
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(LOGIN_PARAM);
        String token = req.getParameter(TOKEN_PARAM);
        if (tokenService.validateToken(login, token)) {
            if (!rolesDao.getUserRole(login).isActionAllowed(UserAction.CHANGE_APP_SETTINGS)) {
                String err = "action not allowed";
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getOutputStream().write(err.getBytes());
            } else {
                Set<String> trainings = trainingServiceImpl.getTrainingTypes();
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
