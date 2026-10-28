package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

/**
 * Monitoreo de una planta eolica: agrega la velocidad promedio del viento.
 *
 * IREP:
 * 1. Cumple el IREP de Monitoreo.
 * 2. velocidadViento >= 0.
 */
public class MonitoreoEolico extends Monitoreo {

    private double velocidadViento;

    /**
     * @throws IllegalArgumentException si velocidadViento es menor a 0
     *         (o si no cumple las condiciones de Monitoreo).
     */
    public MonitoreoEolico(LocalDateTime fechaHora, double energiaGenerada, int incidencias,
            double velocidadViento) {
        super(fechaHora, energiaGenerada, incidencias);
        if (Double.isNaN(velocidadViento) || velocidadViento < 0) {
            throw new IllegalArgumentException("La velocidad del viento no puede ser negativa: " + velocidadViento);
        }
        this.velocidadViento = velocidadViento;
    }

    public double velocidadViento() {
        return velocidadViento;
    }

    @Override
    public String toString() {
        return super.toString() + " | viento: " + velocidadViento + " m/s";
    }
}
