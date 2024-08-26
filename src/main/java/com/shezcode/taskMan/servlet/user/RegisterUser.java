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

@WebServlet("/register")
public class RegisterUser extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        // Read and build JSON body from request
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        User user;
        try {
            // Deserialize JSON request to User object
            user = gson.fromJson(jsonBuilder.toString(), User.class);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 Bad Request
            response.getWriter().write("{\"error\": \"Invalid JSON format.\"}");
            return;
        }

        // Database operations
        try {
            Database.connect();

            // Check if the user already exists based on email
            User finalUser = user;
            User doesExist = Database.jdbi.withExtension(UserDao.class, dao -> dao.getUserByEmail(finalUser.getEmail()));
            String password = finalUser.getPassword();
            String hashedPassword = PasswordEncryption.encryptPassword(password);

            if (doesExist == null) {
                int dbReturn = Database.jdbi.withExtension(UserDao.class,
                        dao -> dao.saveUser(finalUser.getNombre(), finalUser.getEmail(), hashedPassword, finalUser.getId_Departamento()));
                if (dbReturn == 1){
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Usuario registrado exitosamente.\"}");
                }
            } else {
                // User already exists
                response.setStatus(HttpServletResponse.SC_CONFLICT);  // 409 Conflict
                response.getWriter().write("{\"error\": \"Usuario ya registrado con el mismo correo.\"}");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 Internal Server Error
            response.getWriter().write("{\"error\": \"Error en el servidor o con base de datos.\"}");
        }
    }

}
