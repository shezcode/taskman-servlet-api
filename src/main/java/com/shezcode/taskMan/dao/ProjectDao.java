package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Project;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

public interface ProjectDao {
    @SqlQuery("SELECT * FROM Proyecto")
    @UseRowMapper(ProjectMapper.class)
    List<Project> getAllProjects();

    @SqlQuery("SELECT * FROM Proyecto WHERE Id_Proyecto = ?")
    @UseRowMapper(ProjectMapper.class)
    Project getProjectById(String id);

}
