package smartgrids;

import java.time.LocalDateTime;

//planta de fuente renovable.
//1. Cumple el IREP de Planta.
//2. fuentePrimaria no es null ni vacía.
//3. 0 <= eficienciaPromedio <= 1.

public abstract class PlantaRenovable extends Planta{
	
	private String fuentePrimaria;
	private double eficienciaPromedio;
	
	public PlantaRenovable (String nombre, String direccion, double capacidadMaxima,
							LocalDateTime fechaFuncionamiento, String fuentePrimaria,
							double eficienciaPromedio) {
		
		super (nombre, direccion, capacidadMaxima, fechaFuncionamiento); //valida
		
		if (fuentePrimaria == null || fuentePrimaria.isBlank()) {
			throw new IllegalArgumentException("Se necesita una fuente primaria.");
		}
		if (eficienciaPromedio < 0 || eficienciaPromedio > 1) {
			throw new IllegalArgumentException("La eficiencia debe estar entre 0 y 1.");
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
    public boolean esRenovable() {  //si es.
		return true;
    }
	
	
	@Override
	public double cuotaEsperada() { //se calcula asi para renovables. Solar la redefine.
		return capacidadMaxima() * eficienciaPromedio;
	}
	
	@Override
	public String toString() {
		return super.toString() + "-Fuente: " + fuentePrimaria + " -Eficiencia:" + eficienciaPromedio;
 	}
}
