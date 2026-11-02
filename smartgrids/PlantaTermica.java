package smartgrids;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantaTermica extends Planta {
    private String tipoCombustible;
    private double objetivoCO2;
    private double almacenamiento;

    public PlantaTermica(String nombre, String direccion, double capacidadMaxima, LocalDate fechaFuncionamiento,
                         String tipoCombustible, double objetivoCO2, double almacenamiento) {
        super(nombre, direccion, capacidadMaxima, fechaFuncionamiento);
        this.tipoCombustible = tipoCombustible;
        this.objetivoCO2 = objetivoCO2;
        this.almacenamiento = almacenamiento;
    }

    public String tipoCombustible() {
        return tipoCombustible;
    }

    public double objetivoCO2() {
        return objetivoCO2;
    }

    public double almacenamiento() {
        return almacenamiento;
    }

    @Override
    public boolean esRenovable() {
        return false;
    }

    @Override
    public double cuotaEsperada() {
        return capacidadMaxima * 0.8;
    }

    @Override
    public boolean cumpleCuota(Monitoreo monitoreo) {
        if (monitoreo == null) {
            throw new IllegalArgumentException("El monitoreo no puede ser nulo");
        }
        boolean cumpleEnergia = monitoreo.energiaGenerada() >= cuotaEsperada();
        if (monitoreo instanceof MonitoreoTermico) {
            MonitoreoTermico mt = (MonitoreoTermico) monitoreo;
            return cumpleEnergia && (mt.CO2Generado() <= this.objetivoCO2);
        }
        return cumpleEnergia;
    }

    @Override
    public void registrarMonitoreoTermico(LocalDateTime fechaHora, double energia, int incidencias,
                                          double co2, double combustible) {
        if (!estado) {
            throw new IllegalStateException("La planta está inactiva");
        }
        MonitoreoTermico m = new MonitoreoTermico(fechaHora, energia, incidencias, co2, combustible);
        monitoreos.put(fechaHora, m);
    }
}