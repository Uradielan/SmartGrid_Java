package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

/**
 * Monitoreo de una planta termica: agrega el CO2 generado y el combustible utilizado.
 *
 * IREP:
 * 1. Cumple el IREP de Monitoreo.
 * 2. co2Generado >= 0.
 * 3. combustible >= 0.
 */
public class MonitoreoTermico extends Monitoreo {

    private double co2Generado;
    private double combustible;

    /**
     * @throws IllegalArgumentException si co2Generado o combustible son menores a 0
     *         (o si no cumple las condiciones de Monitoreo).
     */
    public MonitoreoTermico(LocalDateTime fechaHora, double energiaGenerada, int incidencias,
            double co2Generado, double combustible) {
        super(fechaHora, energiaGenerada, incidencias);
        if (Double.isNaN(co2Generado) || co2Generado < 0) {
            throw new IllegalArgumentException("El CO2 generado no puede ser negativo: " + co2Generado);
        }
        if (Double.isNaN(combustible) || combustible < 0) {
            throw new IllegalArgumentException("El combustible utilizado no puede ser negativo: " + combustible);
        }
        this.co2Generado = co2Generado;
        this.combustible = combustible;
    }

    public double co2Generado() {
        return co2Generado;
    }

    public double combustible() {
        return combustible;
    }

    @Override
    public String toString() {
        return super.toString() + " | CO2: " + co2Generado + " | combustible: " + combustible;
    }
}
