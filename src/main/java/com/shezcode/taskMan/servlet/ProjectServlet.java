package com.shezcode.taskMan.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.ProjectDao;
import com.shezcode.taskMan.dao.UserDao;
import com.shezcode.taskMan.domain.Project;
import com.shezcode.taskMan.domain.User;
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

@WebServlet(name = "getAllProjects", urlPatterns = {"/getAllProjects"})
public class ProjectServlet extends HttpServlet {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setContentType("application/json");

        var out = response.getWriter();

        try {
            Database.connect();

            List<Project> projects = Database.jdbi.withExtension(ProjectDao.class, ProjectDao::getAllProjects);

            response.getWriter().print(gson.toJson(projects));
            response.setStatus(HttpServletResponse.SC_OK);  // 200 OK
            out.flush();
            out.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 Error
            response.getWriter().write("{\"error\": \"Unable to retrieve data.\"}");
        }
    }
}
