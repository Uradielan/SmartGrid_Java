package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

/**
 * Planta que usa una fuente renovable (clase abstracta).
 *
 * IREP:
 * 1. Cumple el IREP de Planta.
 * 2. fuentePrimaria != null y no es vacia.
 * 3. 0 <= eficienciaPromedio <= 1.
 */
public abstract class PlantaRenovable extends Planta {

    private String fuentePrimaria;
    private double eficienciaPromedio;

    protected PlantaRenovable(String nombre, String direccion, double capacidadMaxima,
            LocalDateTime fechaFuncionamiento, String fuentePrimaria, double eficienciaPromedio) {
        super(nombre, direccion, capacidadMaxima, fechaFuncionamiento);
        if (fuentePrimaria == null || fuentePrimaria.trim().isEmpty()) {
            throw new IllegalArgumentException("La fuente primaria es obligatoria");
        }
        if (Double.isNaN(eficienciaPromedio) || eficienciaPromedio < 0 || eficienciaPromedio > 1) {
            throw new IllegalArgumentException("La eficiencia promedio debe estar entre 0 y 1: " + eficienciaPromedio);
        }
        this.fuentePrimaria = fuentePrimaria;
        this.eficienciaPromedio = eficienciaPromedio;
    }

    @Override
    public String fuentePrimaria() {
        return fuentePrimaria;
    }

    public double eficienciaPromedio() {
        return eficienciaPromedio;
    }

    @Override
    public boolean esRenovable() {
        return true;
    }

    /** capacidadMaxima * eficienciaPromedio. La heredan eolica e hidraulica; la solar la redefine. */
    @Override
    public double cuotaEsperada() {
        return capacidadMaxima() * eficienciaPromedio;
    }

    @Override
    protected String caracteristicas() {
        return "Fuente primaria: " + fuentePrimaria + " | Eficiencia promedio: " + eficienciaPromedio;
    }
}
