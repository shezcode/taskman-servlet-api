package com.shezcode.taskMan.servlet.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.UserDao;
import com.shezcode.taskMan.domain.User;
import com.shezcode.taskMan.utils.LocalDateAdapter;
import com.shezcode.taskMan.utils.PasswordEncryption;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet("/login")
public class LoginUser extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegisterUser.class.getName());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        String requestBody = jsonBuilder.toString();
        Gson gson = new Gson();
        Map<String, String> requestBodyMap = gson.fromJson(requestBody, Map.class);

        String email = requestBodyMap.get("Email");
        String password = requestBodyMap.get("Password");
        // Database operations
        try {
            Database.connect();

            // Check if the user already exists based on email
            User doesExist = Database.jdbi.withExtension(UserDao.class, dao -> dao.getUserByEmail(email));

            if (doesExist != null) {
                logger.log(Level.INFO, "login password attempt with: " + password);
                logger.log(Level.INFO, "Stored password is : " + doesExist.getPassword());

                boolean passwordMatch = PasswordEncryption.checkPassword(password, doesExist.getPassword());

                logger.log(Level.INFO, "Password match is: " + passwordMatch);

                if (passwordMatch){
                    Database.jdbi.withExtension(UserDao.class,
                            dao -> dao.loginUser(doesExist.getEmail(), doesExist.getPassword()));

                    response.setStatus(HttpServletResponse.SC_OK);

                    response.getWriter().write("{\"message\": \"Login correcto.\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401
                    response.getWriter().write("{\"error\": \"Contraseña incorrecta.\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 409 Conflict
                response.getWriter().write("{\"error\": \"Usuario no encontrado con este correo.\"}");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 Internal Server Error
            response.getWriter().write("{\"error\": \"Error en el servidor o con base de datos.\"}");
        }
    }

}