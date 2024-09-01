package com.shezcode.taskMan.domain;

import com.shezcode.taskMan.utils.Estado;
import com.shezcode.taskMan.utils.Prioridad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    private String Id_Proyecto;
    private String Nombre;
    private String Descripcion;
    private LocalDate Fe_creacion;
    private LocalDate Fe_actualizacion;
    private LocalDate Fe_inicio;
    private LocalDate Fe_fin;
    private String Estado;
    private String Prioridad;
    private int Presupuesto;
    private String Id_Usuario;

}
