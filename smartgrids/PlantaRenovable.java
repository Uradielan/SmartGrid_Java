package smartgrids;

import java.time.LocalDate;

public abstract class PlantaRenovable extends Planta {
    private String fuentePrimaria;
    private double eficienciaPromedio;

    public PlantaRenovable(String nombre, String direccion, double capacidadMaxima,
                           LocalDate fechaFuncionamiento, String fuentePrimaria, double eficienciaPromedio) {
        super(nombre, direccion, capacidadMaxima, fechaFuncionamiento);
        if (fuentePrimaria == null || fuentePrimaria.trim().isEmpty()) {
            throw new IllegalArgumentException("La fuente primaria es obligatoria");
        }
        if (eficienciaPromedio < 0 || eficienciaPromedio > 1) {
            throw new IllegalArgumentException("La eficiencia promedio debe estar entre 0 y 1");
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

    @Override
    public double cuotaEsperada() {
        return capacidadMaxima * eficienciaPromedio;
    }
}