package com.shezcode.taskMan.servlet.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.ProjectDao;
import com.shezcode.taskMan.dao.TaskDao;
import com.shezcode.taskMan.domain.Project;
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


@WebServlet("/modifyProject")
public class ModifyProject extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 Bad Request
            response.getWriter().write("{\"error\": \"Tarea ID is required.\"}");
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

        Project project;
        try {
            // Deserialize JSON request to User object
            project = gson.fromJson(jsonBuilder.toString(), Project.class);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 Bad Request
            response.getWriter().write("{\"error\": \"Invalid JSON format.\"}");
            return;
        }


        try {

            Database.connect();

            Project projectDb= Database.jdbi.withExtension(ProjectDao.class, dao -> dao.getProjectById(id));
            if (projectDb != null) {
                int updatedRows = Database.jdbi.withExtension(ProjectDao.class, dao -> dao.modifyProject(projectDb.getNombre(), projectDb.getDescripcion(), projectDb.getFe_inicio(), projectDb.getFe_fin(), projectDb.getEstado(), projectDb.getPrioridad(), projectDb.getPresupuesto(), projectDb.getId_Proyecto()));
                if (updatedRows == 1) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Proyecto modificado.\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("{\"error\": \"Error al modificar el proyecto.\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);  // 401
                response.getWriter().write("{\"error\": \"Proyecto no encontrado.\"}");
            }
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

