package com.shezcode.taskMan.servlet.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.TaskDao;
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

@WebServlet(name = "getAllTasks", urlPatterns = {"/getAllTasks"})
public class TaskServlet extends HttpServlet {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        var out = response.getWriter();

        try {
            Database.connect();

            List<Task> tasks = Database.jdbi.withExtension(TaskDao.class, TaskDao::getAllTasks);

            response.getWriter().print(gson.toJson(tasks));
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
