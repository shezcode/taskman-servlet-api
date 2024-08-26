package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Project;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

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
    @UseRowMapper(ProjectMapper.class)
    int deleteProject(String id);
}
