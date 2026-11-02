package smartgrids;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantaSolar extends PlantaRenovable {
    private double superficieCubierta;
    private int cantidadPaneles;
    private double horasRadiacion;

    public PlantaSolar(String nombre, String direccion, double capacidadMaxima, LocalDate fechaFuncionamiento,
                       String fuentePrimaria, double eficienciaPromedio, double superficieCubierta, int cantidadPaneles, double horasRadiacion) {
        super(nombre, direccion, capacidadMaxima, fechaFuncionamiento, fuentePrimaria, eficienciaPromedio);
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

    @Override
    public double cuotaEsperada() {
        return super.cuotaEsperada();
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