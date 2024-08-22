package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Departament;
import com.shezcode.taskMan.domain.Project;
import com.shezcode.taskMan.domain.User;
import com.shezcode.taskMan.utils.Estado;
import com.shezcode.taskMan.utils.Prioridad;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProjectMapper implements RowMapper<Project> {

    @Override
    public Project map(ResultSet rs, StatementContext ctx) throws SQLException {

        Date sqlCreacion = rs.getDate("Fe_creacion");
        LocalDate feCreacion = sqlCreacion != null ? sqlCreacion.toLocalDate() : null;

        Date sqlActualizacion = rs.getDate("Fe_actualizacion");
        LocalDate feActualizacion = sqlActualizacion != null ? sqlActualizacion.toLocalDate() : null;

        Date sqlInicio = rs.getDate("Fe_inicio");
        LocalDate feInicio = sqlInicio != null ? sqlInicio.toLocalDate() : null;

        Date sqlFin = rs.getDate("Fe_fin");
        LocalDate feFin = sqlFin != null ? sqlFin.toLocalDate() : null;

        String estadoStr = rs.getString("Estado");
        Estado estado = Estado.valueOf(estadoStr.toUpperCase());

        String prioridadStr = rs.getString("Prioridad");
        Prioridad prioridad = Prioridad.valueOf(prioridadStr.toUpperCase());

        User user = Database.jdbi.withExtension(UserDao.class, dao -> dao.getUserById(rs.getString("Id_Usuario")));
        return new Project(
                rs.getString("Id_Proyecto"),
                rs.getString("Nombre"),
                rs.getString("Descripcion"),
                feCreacion,
                feActualizacion,
                feInicio,
                feFin,
                estado,
                prioridad,
                rs.getInt("Presupuesto"),
                user);
    }
}
