package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

/**
 * Planta eolica. Hereda cuotaEsperada() de PlantaRenovable.
 *
 * IREP:
 * 1. Cumple el IREP de PlantaRenovable.
 * 2. cantidadAerogeneradores > 0.
 * 3. alturaTorres > 0.
 * 4. vientoMinimo >= 0.
 * 5. Todos sus monitoreos son MonitoreoEolico.
 */
public class PlantaEolica extends PlantaRenovable {

    private int cantidadAerogeneradores;
    private double alturaTorres;
    private double vientoMinimo;

    public PlantaEolica(String nombre, String direccion, double capacidadMaxima,
            LocalDateTime fechaFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            int cantidadAerogeneradores, double alturaTorres, double vientoMinimo) {
        super(nombre, direccion, capacidadMaxima, fechaFuncionamiento, fuentePrimaria, eficienciaPromedio);
        if (cantidadAerogeneradores <= 0) {
            throw new IllegalArgumentException("La cantidad de aerogeneradores debe ser mayor a 0");
        }
        if (Double.isNaN(alturaTorres) || alturaTorres <= 0) {
            throw new IllegalArgumentException("La altura de las torres debe ser mayor a 0");
        }
        if (Double.isNaN(vientoMinimo) || vientoMinimo < 0) {
            throw new IllegalArgumentException("La velocidad minima de viento no puede ser negativa");
        }
        this.cantidadAerogeneradores = cantidadAerogeneradores;
        this.alturaTorres = alturaTorres;
        this.vientoMinimo = vientoMinimo;
    }

    public int cantidadAerogeneradores() {
        return cantidadAerogeneradores;
    }

    public double alturaTorres() {
        return alturaTorres;
    }

    public double vientoMinimo() {
        return vientoMinimo;
    }

    @Override
    public void registrarMonitoreoEolico(LocalDateTime fechaHora, double energiaGenerada,
            int incidencias, double velocidadViento) {
        agregarMonitoreo(new MonitoreoEolico(fechaHora, energiaGenerada, incidencias, velocidadViento));
    }

    /**
     * Texto del punto 10 para esta planta: "- {nombre}:  {vientoMinimo} m/s" y, debajo,
     * una linea por cada hora (0 a 23) con registros en la que ningun registro alcanzo
     * vientoMinimo, con la mayor velocidad registrada en esa hora.
     */
    @Override
    public String detalleHorasConPocoViento() {
        double[] mayorVelocidad = new double[24];
        boolean[] hayRegistros = new boolean[24];
        for (Monitoreo m : monitoreos()) {
            int hora = m.fechaHora().getHour();
            double velocidad = ((MonitoreoEolico) m).velocidadViento();
            if (!hayRegistros[hora] || velocidad > mayorVelocidad[hora]) {
                mayorVelocidad[hora] = velocidad;
            }
            hayRegistros[hora] = true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("- ").append(nombre()).append(":  ").append(velocidad(vientoMinimo)).append(" m/s\n");
        for (int hora = 0; hora < 24; hora++) {
            if (hayRegistros[hora] && mayorVelocidad[hora] < vientoMinimo) {
                sb.append(String.format("       %02d: %s m/s%n", hora, velocidad(mayorVelocidad[hora])));
            }
        }
        return sb.toString();
    }

    /** Muestra la velocidad sin decimales si es un numero entero. */
    private static String velocidad(double valor) {
        if (valor == Math.rint(valor)) {
            return String.valueOf((long) valor);
        }
        return String.valueOf(valor);
    }

    @Override
    protected String caracteristicas() {
        return super.caracteristicas() + " | Aerogeneradores: " + cantidadAerogeneradores
                + " | Altura de torres: " + alturaTorres + " | Viento minimo: " + vientoMinimo;
    }
}
