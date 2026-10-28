package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

/**
 * Planta solar.
 *
 * IREP:
 * 1. Cumple el IREP de PlantaRenovable.
 * 2. superficieCubierta > 0.
 * 3. cantidadPaneles > 0.
 * 4. 0 <= horasRadiacion <= 24.
 * 5. Ninguno de sus monitoreos es MonitoreoEolico ni MonitoreoTermico.
 */
public class PlantaSolar extends PlantaRenovable {

    private double superficieCubierta;
    private int cantidadPaneles;
    private double horasRadiacion;

    public PlantaSolar(String nombre, String direccion, double capacidadMaxima,
            LocalDateTime fechaFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            double superficieCubierta, int cantidadPaneles, double horasRadiacion) {
        super(nombre, direccion, capacidadMaxima, fechaFuncionamiento, fuentePrimaria, eficienciaPromedio);
        if (Double.isNaN(superficieCubierta) || superficieCubierta <= 0) {
            throw new IllegalArgumentException("La superficie cubierta debe ser mayor a 0");
        }
        if (cantidadPaneles <= 0) {
            throw new IllegalArgumentException("La cantidad de paneles debe ser mayor a 0");
        }
        if (Double.isNaN(horasRadiacion) || horasRadiacion < 0 || horasRadiacion > 24) {
            throw new IllegalArgumentException("Las horas de radiacion deben estar entre 0 y 24");
        }
        this.superficieCubierta = superficieCubierta;
        this.cantidadPaneles = cantidadPaneles;
        this.horasRadiacion = horasRadiacion;
    }

    public double superficieCubierta() {
        return superficieCubierta;
    }

    public int cantidadPaneles() {
        return cantidadPaneles;
    }

    public double horasRadiacion() {
        return horasRadiacion;
    }

    /** capacidadMaxima * eficienciaPromedio * (horasRadiacion / 24). */
    @Override
    public double cuotaEsperada() {
        return capacidadMaxima() * eficienciaPromedio() * (horasRadiacion / 24);
    }

    @Override
    public void registrarMonitoreo(LocalDateTime fechaHora, double energiaGenerada, int incidencias) {
        agregarMonitoreo(new Monitoreo(fechaHora, energiaGenerada, incidencias));
    }

    @Override
    protected String caracteristicas() {
        return super.caracteristicas() + " | Superficie cubierta: " + superficieCubierta
                + " | Paneles: " + cantidadPaneles + " | Horas de radiacion: " + horasRadiacion;
    }
}
