package com.shezcode.taskMan.servlet.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.UserDao;
import com.shezcode.taskMan.domain.User;
import com.shezcode.taskMan.utils.LocalDateAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/modifyUser")
public class ModifyUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 Bad Request
            response.getWriter().write("{\"error\": \"User ID is required.\"}");
            return;
        }


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


        try {

            Database.connect();

            User userDb = Database.jdbi.withExtension(UserDao.class, dao -> dao.getUserById(id));
            if (userDb != null) {
                int updatedRows = Database.jdbi.withExtension(UserDao.class, dao -> dao.modifyUser(user.getNombre(), user.getEmail(), user.getId_Departamento(), user.getId_Usuario()));
                if (updatedRows == 1) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Usuario modificado.\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("{\"error\": \"Error al modificar el usuario.\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);  // 401
                response.getWriter().write("{\"error\": \"Tarea no encontrada.\"}");
            }
            Database.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 Internal Server Error
            response.getWriter().write("{\"error\": \"Error en el servidor o con base de datos.\"}");
        } finally {
            response.getWriter().flush();
            response.getWriter().close();
        }
    }
}

