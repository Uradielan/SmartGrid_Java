package smartgrids;

import java.time.LocalDateTime;

//1. Cumple el IREP de PlantaRenovable.
//2. cantidadTurbinas > 0.
//3. capacidadEmbalse > 0.

public class PlantaHidraulica extends PlantaRenovable {

    private int cantidadTurbinas;
    private double capacidadEmbalse;

    public PlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
                       LocalDateTime fechaFuncionamiento, String fuentePrimaria,
                       double eficienciaPromedio, int cantidadTurbinas,
                       double capacidadEmbalse) {
    	
		super (nombre, direccion, capacidadMaxima, fechaFuncionamiento, fuentePrimaria, eficienciaPromedio);
		
		if (cantidadTurbinas <= 0) {
			throw new IllegalArgumentException("Se debe tener al menos una turbina.");
		}
		if (capacidadEmbalse <= 0) {
			throw new IllegalArgumentException("El embalse no puede tener capacidad negativa o 0.");
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
    public String toString() {
		return super.toString() + "-Cantidad de turbinas: " + cantidadTurbinas + 
								  " -Capacidad del embalse: " + capacidadEmbalse;
    }
}