package com.shezcode.taskMan.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Departament {
    private String Id_Departamento;
    private String Nombre;
    private String Email;
    private LocalDate Fe_creacion;
    private int Presupuesto;
}
