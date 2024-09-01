package com.shezcode.taskMan.servlet.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.UserDao;
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
import java.util.Map;

@WebServlet(name = "getUser", urlPatterns = {"/getUser"})
public class UserServlet extends HttpServlet {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //CorsHeaders.setCorsHeaders(response);
        response.setContentType("application/json");

        var out = response.getWriter();

        try {
            Database.connect();

            Map<String, String[]> parameterMap = request.getParameterMap();

            if (parameterMap.size() > 1){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 Error
                response.getWriter().write("{\"error\": \"Too many params.\"}");
                out.close();
            }

            if (parameterMap.isEmpty()){
                List<User> users = Database.jdbi.withExtension(UserDao.class, UserDao::getAllUsers);
                response.getWriter().print(gson.toJson(users));
            }

            String name;
            String user_id;
            String depName;
            String email;

            if (request.getParameter("user_id") != null){
                user_id = request.getParameter("user_id");
            } else {
                user_id = "";
            }

            if (request.getParameter("email") != null){
                email = request.getParameter("email");
            } else {
                email = "";
            }

            if (request.getParameter("depName") != null){
                depName = request.getParameter("depName");
            } else {
                depName = "";
            }

            if (request.getParameter("name") != null){
                name = request.getParameter("name");
            } else {
                name = "";
            }

            User user = null;

            if (!name.isEmpty()){
                List<User> users = Database.jdbi.withExtension(UserDao.class, dao -> dao.getUserByName(name));
                response.getWriter().print(gson.toJson(users));
            }

            if (!email.isEmpty()){
                List<User> users = Database.jdbi.withExtension(UserDao.class, dao -> dao.getMultipleUsersByEmail(email));
                response.getWriter().print(gson.toJson(users));
            }

            if (!user_id.isEmpty()){
                user = Database.jdbi.withExtension(UserDao.class, dao -> dao.getUserById(user_id));
            }

            if (!depName.isEmpty()){
                List<User> users = Database.jdbi.withExtension(UserDao.class, dao -> dao.getUserByDep(depName));
                response.getWriter().print(gson.toJson(users));
            }

            if (user != null){
                response.getWriter().print(gson.toJson(user));
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
