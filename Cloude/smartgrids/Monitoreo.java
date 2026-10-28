package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Registro de monitoreo de una hora de una planta.
 *
 * IREP:
 * 1. fechaHora != null y tiene minutos, segundos y nanosegundos en cero
 *    (el registro representa una hora completa).
 * 2. energiaGenerada >= 0.
 * 3. incidencias >= 0.
 */
public class Monitoreo {

    private LocalDateTime fechaHora;
    private double energiaGenerada;
    private int incidencias;

    /**
     * Crea un registro de monitoreo en una fecha y hora (se trunca a la hora),
     * con la energia generada y la cantidad de incidencias detectadas.
     *
     * @throws IllegalArgumentException si fechaHora es null, si la energia generada
     *         es menor a 0 o si las incidencias son menores a 0.
     */
    public Monitoreo(LocalDateTime fechaHora, double energiaGenerada, int incidencias) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora del monitoreo es obligatoria");
        }
        if (Double.isNaN(energiaGenerada) || energiaGenerada < 0) {
            throw new IllegalArgumentException("La energia generada no puede ser negativa: " + energiaGenerada);
        }
        if (incidencias < 0) {
            throw new IllegalArgumentException("Las incidencias no pueden ser negativas: " + incidencias);
        }
        this.fechaHora = fechaHora.truncatedTo(ChronoUnit.HOURS);
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

    /** Indica si la energia generada y las incidencias son ambas cero. */
    public boolean sinDatos() {
        return energiaGenerada == 0 && incidencias == 0;
    }

    @Override
    public String toString() {
        return fechaHora + " | energia: " + energiaGenerada + " | incidencias: " + incidencias;
    }
}
