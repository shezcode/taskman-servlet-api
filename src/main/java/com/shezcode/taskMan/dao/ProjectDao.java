package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Project;
import com.shezcode.taskMan.utils.Estado;
import com.shezcode.taskMan.utils.Prioridad;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.time.LocalDate;
import java.util.List;

import static com.shezcode.taskMan.dao.Database.jdbi;

public interface ProjectDao {
    @SqlQuery("SELECT * FROM Proyecto")
    @UseRowMapper(ProjectMapper.class)
    List<Project> getAllProjects();

    @SqlQuery("SELECT * FROM Proyecto WHERE Id_Proyecto = ?")
    @UseRowMapper(ProjectMapper.class)
    Project getProjectById(String id);

    @SqlQuery("SELECT * FROM Proyecto WHERE Id_Usuario = ?")
    @UseRowMapper(ProjectMapper.class)
    Project getProjectByUserId(String id_user);

    @SqlQuery("SELECT * FROM Proyecto WHERE Nombre = ?")
    @UseRowMapper(ProjectMapper.class)
    Project getProjectByName(String name);

    @SqlUpdate("DELETE FROM Proyecto WHERE Id_Proyecto = ?")
    int deleteProject(String id);

    @SqlUpdate("INSERT INTO Proyecto (Nombre, Descripcion, Fe_inicio, Fe_fin, Estado, Prioridad, Presupuesto, Id_Usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
    int createProject(String Nombre, String Descripcion, LocalDate Fe_inicio, LocalDate Fe_fin, String Estado, String Prioridad, int Presupuesto, String Id_Usuario);

    @SqlUpdate("UPDATE Proyecto SET Nombre = ?, Descripcion = ?, Fe_inicio = ?, Fe_fin = ?, Estado = ?, Prioridad = ?, Presupuesto = ? WHERE Id_Proyecto = ?")
    int modifyProject(String Nombre, String Descripcion, LocalDate Fe_inicio, LocalDate Fe_fin, String Estado, String Prioridad, int Presupuesto, String Id_Proyecto);
}
