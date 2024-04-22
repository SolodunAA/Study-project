package diary.app.dao.postgres;

import diary.app.dao.TrainingDao;
import diary.app.dto.Training;
import diary.app.out.ConsolePrinter;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class PostgresTrainingDao implements TrainingDao {

    private final String url;
    private final String userName;
    private final String password;
    private final String schema;

    private final static String INSERT_NEW_TRAINING_SQL = """
                    INSERT INTO :SCHEMA.trainings_table
                    (login_id, date, training_type_id, time, calories, extra_info)
                    VALUES ((SELECT login_id FROM admin_data.login_table WHERE login = ?),?,
                    (SELECT training_type_id FROM :SCHEMA.trainings_type_table WHERE training_type = ?),?,?,?)
            """;

    private final static String RETRIEVE_TRAINING_RECORD_SQL = """
            SELECT time, calories, extra_info
            FROM :SCHEMA.trainings_table
            JOIN admin_data.login_table USING(login_id)
            JOIN :SCHEMA.trainings_type_table USING(training_type_id)
            WHERE
            login = ?
            AND date = ?
            AND training_type = ?
            """;
    private final static String DELETE_TRAINING_RECORD_SQL = """
            DELETE FROM :SCHEMA.trainings_table
            WHERE
            login_id = (SELECT login_id FROM admin_data.login_table WHERE login = ?)
            AND date = ?
            AND training_type_id = (SELECT training_type_id FROM :SCHEMA.trainings_type_table WHERE training_type = ?)
            """;
    private final static String RETRIEVE_ALL_TRAINING_RECORD_SQL = """
            SELECT date, training_type, time, calories, extra_info
            FROM :SCHEMA.trainings_table
            JOIN admin_data.login_table USING(login_id)
            JOIN :SCHEMA.trainings_type_table USING(training_type_id)
            WHERE login = ?
            ORDER BY date
            """;
    private final static String RETRIEVE_ALL_TRAINING_RECORD_FOR_PERIOD_SQL = """
            SELECT date, training_type, time, calories, extra_info
            FROM :SCHEMA.trainings_table
            JOIN admin_data.login_table USING(login_id)
            JOIN :SCHEMA.trainings_type_table USING(training_type_id)
            WHERE login = ?
            AND date BETWEEN ? AND ?
            ORDER BY date
            """;
    private final static String RETRIEVE_TRAINING_TYPES_SQL =
            "SELECT training_type FROM :SCHEMA.trainings_type_table ";
    private final static String INSERT_NEW_TRAINING_TYPE_SQL = """
            INSERT INTO :SCHEMA.trainings_type_table
            (training_type) VALUES (?)
            """;
    private final static String DELETE_TRAINING_TYPE_SQL = """
            DELETE FROM :SCHEMA.trainings_type_table
            WHERE training_type = ?
            """;

    public PostgresTrainingDao(String url, String userName, String password, String schema) {
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.schema = schema;
    }

    @Override
    public void addNewTraining(String login, Training tng) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, INSERT_NEW_TRAINING_SQL, schema)) {
            ps.setString(1, login);
            ps.setDate(2, Date.valueOf(tng.getDate()));
            ps.setString(3, tng.getType());
            ps.setDouble(4, tng.getTimeInMinutes());
            ps.setInt(5, tng.getCalories());
            ps.setString(6, tng.getAdditionalInfo());
            ps.executeUpdate();
        } catch (PSQLException e) {
            if ("23505".equals(e.getSQLState())) {
                ConsolePrinter.print("duplicated training was rejected");
            } else {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Training> getTraining(String login, LocalDate date, String type) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, RETRIEVE_TRAINING_RECORD_SQL, schema)) {
            ps.setString(1, login);
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, type);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                double time = resultSet.getDouble("time");
                int calories = resultSet.getInt("calories");
                String extraInfo = resultSet.getString("extra_info");
                var training = new Training(date, type, time, calories, extraInfo);
                return Optional.of(training);
            } else {
                ConsolePrinter.print("training does not exist");
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTraining(String login, LocalDate date, String type) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, DELETE_TRAINING_RECORD_SQL, schema)) {
            ps.setString(1, login);
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, type);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Training> getAllTrainings(String login) {
        List<Training> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, RETRIEVE_ALL_TRAINING_RECORD_SQL, schema)) {
            ps.setString(1, login);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("date").toLocalDate();
                String type = resultSet.getString("training_type");
                double time = resultSet.getDouble("time");
                int calories = resultSet.getInt("calories");
                String extraInfo = resultSet.getString("extra_info");
                Training training = new Training(date, type, time, calories, extraInfo);
                list.add(training);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Training> getTrainingsFromThePeriod(String login, LocalDate startDate, LocalDate endDate) {
        List<Training> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, RETRIEVE_ALL_TRAINING_RECORD_FOR_PERIOD_SQL, schema)) {
            ps.setString(1, login);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("date").toLocalDate();
                String type = resultSet.getString("training_type");
                double time = resultSet.getDouble("time");
                int calories = resultSet.getInt("calories");
                String extraInfo = resultSet.getString("extra_info");
                Training training = new Training(date, type, time, calories, extraInfo);
                list.add(training);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Set<String> getTrainingTypes() {
        Set<String> set = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SqlUtils.setSchema(RETRIEVE_TRAINING_TYPES_SQL, schema))) {
            while (resultSet.next()) {
                String type = resultSet.getString("training_type");
                set.add(type);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return set;
    }

    @Override
    public void addTrainingType(String trainingType) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, INSERT_NEW_TRAINING_TYPE_SQL, schema)) {
            ps.setString(1, trainingType);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTrainingType(String trainingType) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = SqlUtils.createPreparedStatement(connection, DELETE_TRAINING_TYPE_SQL, schema)) {
            ps.setString(1, trainingType);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
