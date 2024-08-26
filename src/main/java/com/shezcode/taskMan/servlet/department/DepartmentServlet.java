package com.shezcode.taskMan.servlet.department;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shezcode.taskMan.dao.Database;
import com.shezcode.taskMan.dao.DepartmentDao;
import com.shezcode.taskMan.domain.Departament;
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

@WebServlet(name = "getDepBy", urlPatterns = {"/getDepBy"})
public class DepartmentServlet extends HttpServlet {

    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id;
        String name;
        if (request.getParameter("name") != null){
            name = request.getParameter("name");
        } else {
            name = "";
        }

        if (request.getParameter("id") != null){
            id = request.getParameter("id");
        } else {
            id = "";
        }

        var out = response.getWriter();

        try {
            Database.connect();

            List<Departament> deps;
            Departament dep;

            Map<String, String[]> parameterMap = request.getParameterMap();

            if (parameterMap.size() > 1){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // 500 Error
                response.getWriter().write("{\"error\": \"Too many params.\"}");
                out.close();
            }

            if (parameterMap.isEmpty()){
                deps = Database.jdbi.withExtension(DepartmentDao.class, DepartmentDao::getAllDepartments);
                response.getWriter().print(gson.toJson(deps));
            }

            if (!name.isEmpty()){
                deps = Database.jdbi.withExtension(DepartmentDao.class, dao -> dao.getDepartmentByName(name));
                response.getWriter().print(gson.toJson(deps));
            }

            if (!id.isEmpty()){
                dep = Database.jdbi.withExtension(DepartmentDao.class, dao -> dao.getDepartmentById(id));
                response.getWriter().print(gson.toJson(dep));
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

}

