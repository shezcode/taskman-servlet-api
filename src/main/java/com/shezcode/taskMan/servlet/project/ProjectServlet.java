package com.shezcode.taskMan.servlet.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.ProjectDao;
import com.shezcode.taskMan.dao.UserDao;
import com.shezcode.taskMan.domain.Project;
import com.shezcode.taskMan.domain.User;
import com.shezcode.taskMan.utils.CorsHeaders;
import com.shezcode.taskMan.utils.LocalDateAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet(name = "getProjects", urlPatterns = {"/getProjects"})
public class ProjectServlet extends HttpServlet {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CorsHeaders.setCorsHeaders(response);
        response.setContentType("application/json");

        var out = response.getWriter();


        String id;
        String user_id;
        String name;

        if (request.getParameter("user_id") != null){
            user_id = request.getParameter("user_id");
        } else {
            user_id = "";
        }

        if (request.getParameter("id") != null){
            id = request.getParameter("id");
        } else {
            id = "";
        }

        if (request.getParameter("name") != null){
            name = request.getParameter("name");
        } else {
            name = "";
        }


        try {
            Database.connect();

            Map<String, String[]> parameterMap = request.getParameterMap();

            if (parameterMap.size() > 1){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 Error
                response.getWriter().write("{\"error\": \"Too many params.\"}");
                out.close();
            }

            if (parameterMap.isEmpty()){
                List<Project> projects = Database.jdbi.withExtension(ProjectDao.class, ProjectDao::getAllProjects);
                response.getWriter().print(gson.toJson(projects));
            }

            Project project = null;

            if (!name.isEmpty()){
                project = Database.jdbi.withExtension(ProjectDao.class, dao -> dao.getProjectByName(name));
            }

            if (!user_id.isEmpty()){
                project = Database.jdbi.withExtension(ProjectDao.class, dao -> dao.getProjectByUserId(user_id));
            }

            if (!id.isEmpty()){
                project = Database.jdbi.withExtension(ProjectDao.class, dao -> dao.getProjectById(id));
            }

            if (project != null){
                response.getWriter().print(gson.toJson(project));
            }

            response.setStatus(HttpServletResponse.SC_OK);  // 200 OK
            out.flush();
            out.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 Error
            response.getWriter().write("{\"error\": \"Unable to retrieve data.\"}");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle preflight request
        CorsHeaders.setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
