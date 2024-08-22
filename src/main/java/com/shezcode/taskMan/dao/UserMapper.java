package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Departament;
import com.shezcode.taskMan.domain.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class UserMapper implements RowMapper<User> {

    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
//        String uuidString = rs.getString("Id_Usuario");
//        UUID id = uuidString != null ? UUID.fromString(uuidString) : null;
//
        Date sqlDate = rs.getDate("Fe_alta");
        LocalDate feAlta = sqlDate != null ? sqlDate.toLocalDate() : null;


        Departament departament= Database.jdbi.withExtension(DepartmentDao.class, dao -> dao.getDepartmentById(rs.getString("Id_Departamento")));
        return new User(
                rs.getString("Id_Usuario"),
                rs.getString("Nombre"),
                rs.getString("Email"),
                rs.getString("Password"),
                feAlta,
                departament);
    }
}
