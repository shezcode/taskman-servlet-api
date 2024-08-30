package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Project;
import com.shezcode.taskMan.domain.Task;
import com.shezcode.taskMan.domain.User;
import com.shezcode.taskMan.utils.Estado;
import com.shezcode.taskMan.utils.Prioridad;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TaskMapper implements RowMapper<Task> {

    @Override
    public Task map(ResultSet rs, StatementContext ctx) throws SQLException {

        Date sqlCreacion = rs.getDate("Fe_creacion");
        LocalDate feCreacion = sqlCreacion != null ? sqlCreacion.toLocalDate() : null;

        Date sqlLimite = rs.getDate("Fe_limite");
        LocalDate feLimite = sqlLimite != null ? sqlLimite.toLocalDate() : null;

        String estadoStr = rs.getString("Estado");
        Estado estado = Estado.valueOf(estadoStr.toUpperCase());

        String prioridadStr = rs.getString("Prioridad");
        Prioridad prioridad = Prioridad.valueOf(prioridadStr.toUpperCase());

        User user= Database.jdbi.withExtension(UserDao.class, dao -> dao.getUserById(rs.getString("Asignada_a_Id_Usuario")));
        Project project = Database.jdbi.withExtension(ProjectDao.class, dao -> dao.getProjectById(rs.getString("Id_Proyecto")));

        return new Task(
                rs.getString("Id_Tarea"),
                rs.getString("Nombre"),
                rs.getString("Descripcion"),
                rs.getString("Asignada_a_Id_Usuario"),
                feCreacion,
                feLimite,
                estado.toString(),
                prioridad.toString(),
                rs.getString("Id_Proyecto"));
    }
}