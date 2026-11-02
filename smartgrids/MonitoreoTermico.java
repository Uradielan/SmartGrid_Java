package smartgrids;

import java.time.LocalDateTime;
//IREP
//1. Cumple el IREP de Monitoreo.
//2. CO2Generado >= 0.
//3. combustible >= 0.

public class MonitoreoTermico extends Monitoreo{
	
	private double CO2Generado;
	private double combustible;
	
	public MonitoreoTermico (LocalDateTime fechaHora, double energiaGenerada, int incidencias,
			double CO2Generado, double combustible) {
		
		super (fechaHora, energiaGenerada, incidencias);
		
		if (CO2Generado < 0) {
            throw new IllegalArgumentException("El CO2 generado no puede ser negativo");
		}
		if (combustible < 0) {
            throw new IllegalArgumentException("El combustible no puede ser negativo");
		}
		
		this.CO2Generado = CO2Generado;
		this.combustible = combustible;
		
	}
	
	public double CO2Generado () {
		return CO2Generado;
	}
	public double combustible () {
		return combustible;
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + CO2Generado + " CO2 Generado, " + combustible + " Combustible." ;
	}
	

}
