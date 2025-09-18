package com.example._Fantasticos_Back;

public class Inscripcion {
    private Grupo grupo;
    private int id;
    private String estado;
    private double notaFinal;

    public Inscripcion(Grupo grupo, int id, String estado, double notaFinal) {
        this.grupo = grupo;
        this.id = id;
        this.estado = estado;
        this.notaFinal = notaFinal;
    }

    public void cancelar() {
        // Lógica para cancelar la inscripción, creo que falta implementar
        this.estado = "cancelada";
    }

    public void evaluar() {
        if (notaFinal >= 30) {
            this.estado = "aprobada";
        } else {
            this.estado = "reprobada";
        }
    }

    public void cambiarGrupo(Grupo nuevoGrupo) {
        this.grupo = nuevoGrupo;
    }

    public boolean validarChoque(Inscripcion otra) {
        for (SesionClase sesion1 : this.grupo.getSesiones()) {
            for (SesionClase sesion2 : otra.grupo.getSesiones()) {
                if (sesion1.verificarChoque(sesion2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
