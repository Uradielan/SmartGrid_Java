package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

/**
 * Planta hidraulica. Hereda cuotaEsperada() de PlantaRenovable.
 *
 * IREP:
 * 1. Cumple el IREP de PlantaRenovable.
 * 2. cantidadTurbinas > 0.
 * 3. capacidadEmbalse > 0.
 * 4. Ninguno de sus monitoreos es MonitoreoEolico ni MonitoreoTermico.
 */
public class PlantaHidraulica extends PlantaRenovable {

    private int cantidadTurbinas;
    private double capacidadEmbalse;

    public PlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
            LocalDateTime fechaFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            int cantidadTurbinas, double capacidadEmbalse) {
        super(nombre, direccion, capacidadMaxima, fechaFuncionamiento, fuentePrimaria, eficienciaPromedio);
        if (cantidadTurbinas <= 0) {
            throw new IllegalArgumentException("La cantidad de turbinas debe ser mayor a 0");
        }
        if (Double.isNaN(capacidadEmbalse) || capacidadEmbalse <= 0) {
            throw new IllegalArgumentException("La capacidad del embalse debe ser mayor a 0");
        }
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
    public void registrarMonitoreo(LocalDateTime fechaHora, double energiaGenerada, int incidencias) {
        agregarMonitoreo(new Monitoreo(fechaHora, energiaGenerada, incidencias));
    }

    @Override
    protected String caracteristicas() {
        return super.caracteristicas() + " | Turbinas: " + cantidadTurbinas
                + " | Capacidad del embalse: " + capacidadEmbalse;
    }
}
