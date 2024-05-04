package diary.app.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import diary.app.config.StaticConfig;
import diary.app.dao.UserRolesDao;
import diary.app.dto.ChangeTrainingDto;
import diary.app.dto.TrainingDto;
import diary.app.dto.UserAction;
import diary.app.service.Impl.TokenServiceImpl;
import diary.app.service.Impl.TrainingServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Optional;

import static diary.app.servlet.GetParams.*;

public class OneTrainingInteractionsServlet extends HttpServlet {
    private final TrainingServiceImpl trainingServiceImpl;
    private final ObjectMapper objectMapper;
    private final TokenServiceImpl tokenService;
    private final UserRolesDao rolesDao;

    public OneTrainingInteractionsServlet() {
        this.trainingServiceImpl = StaticConfig.SERVICES_FACTORY.getTrainingService();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(df);
        this.tokenService = StaticConfig.SERVICES_FACTORY.getTokenService();
        this.rolesDao = StaticConfig.SERVICES_FACTORY.getDaoFactory().getUserRolesDao();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        String requestBody = buffer.toString();
        TrainingDto trainingDto = objectMapper.readValue(requestBody, TrainingDto.class);

        String login = req.getParameter(LOGIN_PARAM);
        String token = req.getParameter(TOKEN_PARAM);

        if (tokenService.validateToken(login, token)) {
            if (!rolesDao.getUserRole(login).isActionAllowed(UserAction.EDIT_USER_TRAININGS)) {
                String err = "action not allowed";
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getOutputStream().write(err.getBytes());
            } else {

                boolean isSaved = trainingServiceImpl.addTraining(login, trainingDto);
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(LOGIN_PARAM);
        String token = req.getParameter(TOKEN_PARAM);
        String date = req.getParameter(DATE_PARAM);
        String type = req.getParameter(TRAINING_TYPE_PARAM);
        if (tokenService.validateToken(login, token)) {
            if (!rolesDao.getUserRole(login).isActionAllowed(UserAction.SEE_USER_TRAININGS)) {
                String err = "action not allowed";
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getOutputStream().write(err.getBytes());
            } else {
                Optional<TrainingDto> trainingOpt = trainingServiceImpl.getTraining(login, LocalDate.parse(date), type);
                System.out.println(date + " date");
                System.out.println(type + " type");
                System.out.println(trainingOpt.toString());
                String json = trainingOpt.isPresent()
                        ? objectMapper.writeValueAsString(trainingOpt.get())
                        : "nothing found";
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

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(LOGIN_PARAM);
        String token = req.getParameter(TOKEN_PARAM);
        String date = req.getParameter(DATE_PARAM);
        String type = req.getParameter(TRAINING_TYPE_PARAM);

        if (tokenService.validateToken(login, token)) {
            if(!rolesDao.getUserRole(login).isActionAllowed(UserAction.EDIT_USER_TRAININGS)){
                String err = "action not allowed";
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getOutputStream().write(err.getBytes());
            } else {
                boolean isDeleted = trainingServiceImpl.deleteTraining(login, LocalDate.parse(date), type);
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

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        String requestBody = buffer.toString();
        ChangeTrainingDto changeTrainingDto = objectMapper.readValue(requestBody, ChangeTrainingDto.class);

        String login = req.getParameter(LOGIN_PARAM);
        String token = req.getParameter(TOKEN_PARAM);

        if (tokenService.validateToken(login, token)) {
            if (!rolesDao.getUserRole(login).isActionAllowed(UserAction.EDIT_USER_TRAININGS)) {
                String err = "action not allowed";
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getOutputStream().write(err.getBytes());
            } else {
                boolean isSaved = trainingServiceImpl.changeTheTraining(login, changeTrainingDto);
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
