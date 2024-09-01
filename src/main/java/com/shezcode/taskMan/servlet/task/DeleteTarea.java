package com.shezcode.taskMan.servlet.task;


import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.ProjectDao;
import com.shezcode.taskMan.dao.TaskDao;
import com.shezcode.taskMan.domain.Project;
import com.shezcode.taskMan.domain.Task;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/deleteTarea")
public class DeleteTarea extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 Bad Request
            response.getWriter().write("{\"error\": \"Tarea ID is required.\"}");
            return;
        }

        try {

            Database.connect();

            Task task = Database.jdbi.withExtension(TaskDao.class, dao -> dao.getTaskById(id));
            if (task != null) {
                int deletedRows = Database.jdbi.withExtension(TaskDao.class, dao -> dao.deleteTask(id));
                if (deletedRows == 1) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"Tarea borrada.\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("{\"error\": \"Error al borrar la tarea.\"}");
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