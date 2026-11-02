package smartgrids;

import java.time.LocalDateTime;

public class Monitoreo {
    private LocalDateTime fechaHora;
    private double energiaGenerada;
    private int incidencias;

    public Monitoreo(LocalDateTime fechaHora, double energiaGenerada, int incidencias) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora es obligatoria");
        }
        if (energiaGenerada < 0) {
            throw new IllegalArgumentException("La energía generada no puede ser negativa");
        }
        if (incidencias < 0) {
            throw new IllegalArgumentException("Las incidencias no pueden ser negativas");
        }
        this.fechaHora = fechaHora;
        this.energiaGenerada = energiaGenerada;
        this.incidencias = incidencias;
    }

    public LocalDateTime fechaHora() {
        return fechaHora;
    }

    public double energiaGenerada() {
        return energiaGenerada;
    }

    public int incidencias() {
        return incidencias;
    }
}