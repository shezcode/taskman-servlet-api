package com.shezcode.taskMan.domain;

import com.shezcode.taskMan.utils.Estado;
import com.shezcode.taskMan.utils.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String Id_Tarea;
    private String Nombre;
    private String Descripcion;
    private String Asignada_a_Id_Usuario;
    private LocalDate Fe_creacion;
    private LocalDate Fe_limite;
    private String Estado;
    private String Prioridad;
    private String Id_Proyecto;
}

