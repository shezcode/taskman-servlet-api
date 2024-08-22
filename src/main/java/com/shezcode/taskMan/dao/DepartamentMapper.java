package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Departament;
import com.shezcode.taskMan.domain.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class DepartamentMapper implements RowMapper<Departament> {

    @Override
    public Departament map(ResultSet rs, StatementContext ctx) throws SQLException {

        Date sqlDate = rs.getDate("Fe_creacion");
        LocalDate feCreacion = sqlDate != null ? sqlDate.toLocalDate() : null;

        return new Departament(
                rs.getString("Id_Departamento"),
                rs.getString("Nombre"),
                rs.getString("Email"),
                feCreacion,
                rs.getInt("Presupuesto"));
    }
}
