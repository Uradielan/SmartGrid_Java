package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

/**
 * Planta termica (no renovable).
 *
 * IREP:
 * 1. Cumple el IREP de Planta.
 * 2. tipoCombustible != null y no es vacio.
 * 3. objetivoCO2 > 0.
 * 4. almacenamiento > 0.
 * 5. Todos sus monitoreos son MonitoreoTermico.
 * 6. Todo monitoreo cumple combustible() <= almacenamiento.
 */
public class PlantaTermica extends Planta {

    private String tipoCombustible;
    private double objetivoCO2;
    private double almacenamiento;

    public PlantaTermica(String nombre, String direccion, double capacidadMaxima,
            LocalDateTime fechaFuncionamiento, String tipoCombustible, double objetivoCO2,
            double almacenamiento) {
        super(nombre, direccion, capacidadMaxima, fechaFuncionamiento);
        if (tipoCombustible == null || tipoCombustible.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de combustible es obligatorio");
        }
        if (Double.isNaN(objetivoCO2) || objetivoCO2 <= 0) {
            throw new IllegalArgumentException("El objetivo de CO2 debe ser mayor a 0");
        }
        if (Double.isNaN(almacenamiento) || almacenamiento <= 0) {
            throw new IllegalArgumentException("La capacidad de almacenamiento debe ser mayor a 0");
        }
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

    /** capacidadMaxima / 2. El limite de CO2 se verifica en cumpleCuota(Monitoreo). */
    @Override
    public double cuotaEsperada() {
        return capacidadMaxima() / 2;
    }

    /**
     * Cumple si la energia generada alcanza la cuota y el CO2 generado es menor al objetivo.
     *
     * @throws IllegalArgumentException si el monitoreo es null o no es un MonitoreoTermico.
     */
    @Override
    public boolean cumpleCuota(Monitoreo monitoreo) {
        if (!(monitoreo instanceof MonitoreoTermico)) {
            throw new IllegalArgumentException("Se esperaba un monitoreo termico");
        }
        return super.cumpleCuota(monitoreo) && ((MonitoreoTermico) monitoreo).co2Generado() < objetivoCO2;
    }

    @Override
    public void registrarMonitoreoTermico(LocalDateTime fechaHora, double energiaGenerada,
            int incidencias, double co2Generado, double combustible) {
        MonitoreoTermico monitoreo =
                new MonitoreoTermico(fechaHora, energiaGenerada, incidencias, co2Generado, combustible);
        if (combustible > almacenamiento) {
            throw new IllegalArgumentException("El combustible utilizado (" + combustible
                    + ") supera el almacenamiento (" + almacenamiento + ")");
        }
        agregarMonitoreo(monitoreo);
    }

    @Override
    protected String caracteristicas() {
        return "Combustible: " + tipoCombustible + " | Objetivo de CO2 por hora: " + objetivoCO2
                + " | Almacenamiento: " + almacenamiento;
    }
}
