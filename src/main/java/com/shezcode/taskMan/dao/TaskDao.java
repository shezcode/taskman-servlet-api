package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Task;
import com.shezcode.taskMan.domain.User;
import com.shezcode.taskMan.utils.Estado;
import com.shezcode.taskMan.utils.Prioridad;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.time.LocalDate;
import java.util.List;

public interface TaskDao {

    @SqlQuery("SELECT * FROM Tarea")
    @UseRowMapper(TaskMapper.class)
    List<Task> getAllTasks();

    @SqlQuery("SELECT * FROM Tarea where Id_Tarea= ?")
    @UseRowMapper(TaskMapper.class)
    Task getTaskById(String id);

    @SqlQuery("SELECT * FROM Tarea where upper(Nombre) LIKE upper(CONCAT(?, '%'))")
    @UseRowMapper(TaskMapper.class)
    List<Task> getTasksByName(String name);

    @SqlQuery("SELECT * FROM Tarea where Id_Proyecto IN (SELECT Id_Proyecto FROM Proyecto WHERE upper(Nombre) LIKE upper(CONCAT(?, '%')))")
    @UseRowMapper(TaskMapper.class)
    List<Task> getTasksByProjectName(String proyecto);

    @SqlUpdate("DELETE FROM Tarea WHERE Id_Tarea = ?")
    int deleteTask(String id);

    @SqlUpdate("UPDATE Tarea SET Nombre = ?, Descripcion = ?, Fe_limite = ?, Asignada_a_Id_Usuario = ?, Estado = ?, Prioridad = ?, Id_Proyecto = ? WHERE Id_Tarea = ?")
    int modifyTask(String nombre, String Descripcion, LocalDate Fe_limite, String Id_Usuario, String estado, String prioridad, String Id_Proyecto, String Id_Tarea);

    @SqlUpdate("INSERT INTO Tarea (Nombre, Descripcion, Asignada_a_Id_Usuario, Fe_limite, Estado, Prioridad, Id_Proyecto) VALUES (?, ?, ?, ?, ?, ?, ?)")
    int createTask(String Nombre, String Descripcion, String Asignada_a_Id_Usuario, LocalDate Fe_limite, String Estado, String Prioridad, String Id_Proyecto);
}
