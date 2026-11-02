package smartgrids;

import java.time.LocalDateTime;

//1. Cumple el IREP de PlantaRenovable.
//2. cantidadAerogeneradores > 0.
//3. alturaTorres > 0.
//4. vientoMinimo >= 0.


public class PlantaEolica extends PlantaRenovable {

    private int cantidadAerogeneradores;
    private double alturaTorres;
    private double vientoMinimo;

    public PlantaEolica(String nombre, String direccion, double capacidadMaxima,
                       LocalDateTime fechaFuncionamiento, String fuentePrimaria,
                       double eficienciaPromedio, int cantidadAerogeneradores,
                       double alturaTorres, double vientoMinimo) {
    	
		super (nombre, direccion, capacidadMaxima, fechaFuncionamiento, fuentePrimaria, eficienciaPromedio);
		
		if (cantidadAerogeneradores <= 0) {
			throw new IllegalArgumentException("Debe haber un aerogenerador como minimo.");
		}
		if (alturaTorres <= 0) {
			throw new IllegalArgumentException("La altura no puede ser menor o igual a 0.");
		}
		if (vientoMinimo < 0) {
			throw new IllegalArgumentException("Viento minimo invalido.");
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
    public String toString() {
		return super.toString() + "Cantidad de aerogeneradores: " + cantidadAerogeneradores + 
								  " Altura de torres: " + alturaTorres +
								  " Viento minimo: " + vientoMinimo;
    }
}