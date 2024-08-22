package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.User;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

public interface UserDao {
    @SqlQuery("SELECT * FROM Usuario")
    @UseRowMapper(UserMapper.class)
    List<User> getAllUsers();

    @SqlQuery("SELECT * FROM Usuario where Id_Usuario = ?")
    @UseRowMapper(UserMapper.class)
    User getUserById(String id);

}
