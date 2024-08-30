package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Departament;
import com.shezcode.taskMan.domain.User;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;
import java.util.UUID;

public interface UserDao {
    @SqlQuery("SELECT * FROM Usuario ORDER BY Fe_alta DESC")
    @UseRowMapper(UserMapper.class)
    List<User> getAllUsers();

    @SqlQuery("SELECT * FROM Usuario where Id_Usuario = ?")
    @UseRowMapper(UserMapper.class)
    User getUserById(String id);

    @SqlQuery("SELECT * FROM Usuario where Email = ?")
    @UseRowMapper(UserMapper.class)
    User getUserByEmail(String email);

    @SqlQuery("SELECT * FROM Usuario where upper(Nombre) like upper(CONCAT(?, '%'))")
    @UseRowMapper(UserMapper.class)
    List<User> getUserByName(String name);

    @SqlQuery("SELECT * FROM Usuario where Id_Departamento = (SELECT Id_Departamento FROM Departamento WHERE Nombre = ?)")
    @UseRowMapper(UserMapper.class)
    List<User> getUserByDep(String depName);

    @SqlUpdate("INSERT INTO Usuario (Nombre, Email, Password, Id_Departamento) VALUES (?, ?, ?, ?)")
    @UseRowMapper(UserMapper.class)
    int saveUser(String nombre, String email, String password, String id_Departamento);

    @SqlQuery("SELECT * FROM Usuario where Email = ? AND Password = ?")
    @UseRowMapper(UserMapper.class)
    User loginUser(String email, String password);
}
