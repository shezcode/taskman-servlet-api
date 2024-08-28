package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Task;
import com.shezcode.taskMan.domain.User;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

public interface TaskDao {

    @SqlQuery("SELECT * FROM Tarea")
    @UseRowMapper(TaskMapper.class)
    List<Task> getAllTasks();

    @SqlQuery("SELECT * FROM Tarea where Id_Tarea= ?")
    @UseRowMapper(TaskMapper.class)
    Task getTaskById(String id);

    @SqlUpdate("DELETE FROM Tarea WHERE Id_Tarea = ?")
    @UseRowMapper(ProjectMapper.class)
    int deleteTask(String id);
}
