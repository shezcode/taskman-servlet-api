package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Task;
import com.shezcode.taskMan.domain.User;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

public interface TaskDao {

    @SqlQuery("SELECT * FROM Tarea")
    @UseRowMapper(TaskMapper.class)
    List<Task> getAllTasks();

    @SqlQuery("SELECT * FROM Tarea where Id_Tarea= ?")
    @UseRowMapper(TaskMapper.class)
    User getTaskById(String id);

}
