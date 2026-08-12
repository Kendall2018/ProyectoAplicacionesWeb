
package com.mycompany.ProyectoAplicacionesWeb.Domain;

public enum EstadoVenta {
    Activa("Activa"),
    Pagada("Pagada"),
    Anulada("Anulada");

    private final String valorBD;

    EstadoVenta(String valorBD) {
        this.valorBD = valorBD;
    }

    public String getValorBD() {
        return valorBD;
    }
}

