package com.shezcode.taskMan.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String Id_Usuario;
    private String Nombre;
    private String Email;
    private String Password;
    private LocalDate Fe_alta;
    private String Id_Departamento;
}
