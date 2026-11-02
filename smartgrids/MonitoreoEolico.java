package smartgrids;

import java.time.LocalDateTime;

public class MonitoreoEolico extends Monitoreo {
    private double velocidadViento;

    public MonitoreoEolico(LocalDateTime fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {
        super(fechaHora, energiaGenerada, incidencias);
        if (velocidadViento < 0) {
            throw new IllegalArgumentException("La velocidad del viento no puede ser negativa");
        }
        this.velocidadViento = velocidadViento;
    }

    public double velocidadViento() {
        return velocidadViento;
    }
}