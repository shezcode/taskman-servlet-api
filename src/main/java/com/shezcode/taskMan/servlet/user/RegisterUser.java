package com.shezcode.taskMan.servlet.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.UserDao;
import com.shezcode.taskMan.domain.Departament;
import com.shezcode.taskMan.domain.User;
import com.shezcode.taskMan.utils.LocalDateAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

@WebServlet(name = "register", urlPatterns = {"/register"})
public class RegisterUser extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        User user = gson.fromJson(jsonBuilder.toString(), User.class);

        try {
            Database.connect();
            User finalUser = user;
            User doesExist = Database.jdbi.withExtension(UserDao.class, dao -> dao.getUserByEmail(finalUser.getEmail()));

            if (doesExist == null) {
                Departament departament = user.getDepartamento();
                UUID id_dep = UUID.fromString(departament.getId_Departamento());

                user = Database.jdbi.withExtension(UserDao.class,
                        dao -> dao.saveUser(finalUser.getNombre(), finalUser.getEmail(), finalUser.getPassword(), id_dep));

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Usuario registrado exitosamente.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);  // 409 Conflict
                response.getWriter().write("{\"error\": \"Usuario ya registrado con el mismo correo.\"}");
            }

        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error de conexión al servidor.\"}");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error conectando con base de datos.\"}");
        }
    }
}
