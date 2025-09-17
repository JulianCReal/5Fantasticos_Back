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
        // Lógica para cambiar de grupo, creo que falta implementar
        this.grupo = nuevoGrupo;
    }

    public void validarChoque(Inscripcion otra) {
        // Lógica para validar choque de horarios, hacen falta otras clases para realizar la implementación correspondiente
    }


}
