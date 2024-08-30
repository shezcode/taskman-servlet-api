package com.shezcode.taskMan.utils;
public enum Estado {
    EN_PROGRESO("En_progreso"),
    FINALIZADO("Finalizado"),
    PENDIENTE("Pendiente"),
    CANCELADO("Cancelado");

    private String value;

    Estado(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    @Override
    public String toString(){
        return value;
    }
}
