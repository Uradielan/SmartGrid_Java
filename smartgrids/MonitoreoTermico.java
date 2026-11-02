package smartgrids;

import java.time.LocalDateTime;

public class MonitoreoTermico extends Monitoreo {
    private double CO2Generado;
    private double combustible;

    public MonitoreoTermico(LocalDateTime fechaHora, double energiaGenerada, int incidencias,
                            double CO2Generado, double combustible) {
        super(fechaHora, energiaGenerada, incidencias);
        if (CO2Generado < 0 || combustible < 0) {
            throw new IllegalArgumentException("CO2 y combustible no pueden ser negativos");
        }
        this.CO2Generado = CO2Generado;
        this.combustible = combustible;
    }

    public double CO2Generado() {
        return CO2Generado;
    }

    public double combustible() {
        return combustible;
    }
}