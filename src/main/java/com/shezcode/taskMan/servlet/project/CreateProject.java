package com.shezcode.taskMan.servlet.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.ProjectDao;
import com.shezcode.taskMan.domain.Project;
import com.shezcode.taskMan.servlet.user.LoginUser;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/createProject")
public class CreateProject extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CreateProject.class.getName());
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

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

            logger.log(Level.INFO, project.toString());
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 Bad Request
            response.getWriter().write("{\"error\": \"Invalid JSON format.\"}");
            return;
        }

        try {

            Database.connect();

            int createdProject = Database.jdbi.withExtension(ProjectDao.class, dao -> dao.createProject(
                    project.getNombre(),
                    project.getDescripcion(),
                    project.getFe_inicio(),
                    project.getFe_fin(),
                    project.getEstado(),
                    project.getPrioridad(),
                    project.getPresupuesto(),
                    project.getId_Usuario()
            ));

            logger.log(Level.INFO, "VALOR DE INT CREATE PROJ: " + createdProject);

            if (createdProject == 1) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Proyecto creado.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("{\"error\": \"Error al crear el proyecto.\"}");
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
