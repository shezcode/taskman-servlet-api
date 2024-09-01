package com.shezcode.taskMan.servlet.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet(name = "getAllTasks", urlPatterns = {"/getAllTasks"})
public class TaskServlet extends HttpServlet {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        var out = response.getWriter();

        String id;
        String name;
        String proyecto;

        if (request.getParameter("id") != null){
            id = request.getParameter("id");
        } else {
            id = "";
        }

        if (request.getParameter("proyecto") != null){
            proyecto = request.getParameter("proyecto");
        } else {
            proyecto = "";
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
                List<Task> tasks = Database.jdbi.withExtension(TaskDao.class, TaskDao::getAllTasks);
                response.getWriter().print(gson.toJson(tasks));
            }

            Task task = null;

            if (!id.isEmpty()){
                task = Database.jdbi.withExtension(TaskDao.class, dao -> dao.getTaskById(id));
            }

            if (!proyecto.isEmpty()){
                List<Task> tasks = Database.jdbi.withExtension(TaskDao.class, dao -> dao.getTasksByProjectName(proyecto));
                response.getWriter().print(gson.toJson(tasks));
            }

            if (!name.isEmpty()){
                List<Task> tasks = Database.jdbi.withExtension(TaskDao.class, dao -> dao.getTasksByName(name));
                response.getWriter().print(gson.toJson(tasks));
            }


            if (task != null){
                response.getWriter().print(gson.toJson(task));
            }

            response.setStatus(HttpServletResponse.SC_OK);  // 200 OK
            out.flush();
            out.close();
            Database.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 Error
            response.getWriter().write("{\"error\": \"Unable to retrieve data.\"}");
        }
    }

}
