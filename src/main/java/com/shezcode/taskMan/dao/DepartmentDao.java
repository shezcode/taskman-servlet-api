package com.shezcode.taskMan.dao;

import com.shezcode.taskMan.domain.Departament;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;
import java.util.UUID;

public interface DepartmentDao {
    @SqlQuery("SELECT * FROM Departamento")
    @UseRowMapper(DepartamentMapper.class)
    List<Departament> getAllDepartments();

    @SqlQuery("SELECT * FROM Departamento WHERE Id_Departamento = ?")
    @UseRowMapper(DepartamentMapper.class)
    Departament getDepartmentById(String id);

    @SqlQuery("SELECT * FROM Departamento WHERE upper(Nombre) like upper(CONCAT(?, '%'))")
    @UseRowMapper(DepartamentMapper.class)
    List<Departament> getDepartmentByName(String name);
}
