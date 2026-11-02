package smartgrids;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantaHidraulica extends PlantaRenovable {
    private int cantidadTurbinas;
    private double capacidadEmbalse;

    public PlantaHidraulica(String nombre, String direccion, double capacidadMaxima, LocalDate fechaFuncionamiento,
                             String fuentePrimaria, double eficienciaPromedio, int cantidadTurbinas, double capacidadEmbalse) {
        super(nombre, direccion, capacidadMaxima, fechaFuncionamiento, fuentePrimaria, eficienciaPromedio);
        this.cantidadTurbinas = cantidadTurbinas;
        this.capacidadEmbalse = capacidadEmbalse;
    }

    public int cantidadTurbinas() {
        return cantidadTurbinas;
    }

    public double capacidadEmbalse() {
        return capacidadEmbalse;
    }

    @Override
    public void registrarMonitoreo(LocalDateTime fechaHora, double energia, int incidencias) {
        if (!estado) {
            throw new IllegalStateException("La planta está inactiva");
        }
        Monitoreo m = new Monitoreo(fechaHora, energia, incidencias);
        monitoreos.put(fechaHora, m);
    }
}