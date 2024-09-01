package com.shezcode.taskMan.servlet.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.TaskDao;
import com.shezcode.taskMan.domain.Task;
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

@WebServlet("/createTask")
public class CreateTask extends HttpServlet {
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

        Task taskData;
        try {
            // Deserialize JSON request to User object
            taskData = gson.fromJson(jsonBuilder.toString(), Task.class);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 Bad Request
            response.getWriter().write("{\"error\": \"Invalid JSON format.\"}");
            return;
        }

        try {

            Database.connect();

            int createdTask = Database.jdbi.withExtension(TaskDao.class, dao -> dao.createTask(
                    taskData.getNombre(),
                    taskData.getDescripcion(),
                    taskData.getAsignada_a_Id_Usuario(),
                    taskData.getFe_limite(),
                    taskData.getEstado(),
                    taskData.getPrioridad(),
                    taskData.getId_Proyecto()
                    ));

            if (createdTask == 1) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Tarea creada.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("{\"error\": \"Error al crear la tarea.\"}");
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
