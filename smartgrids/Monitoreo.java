package smartgrids;

import java.time.LocalDateTime;

//Que genero una planta en fecha y hora.
//IREP
//1. fechaHora no es null.
//2. energiaGenerada >= 0.
//3. incidencias >= 0.

public class Monitoreo {

	private LocalDateTime fechaHora;
	private double energiaGenerada;
	private int incidencias;
	
	public Monitoreo (LocalDateTime fechaHora, double energiaGenerada, int incidencias) {
		
		if (fechaHora == null) {
			throw new IllegalArgumentException("Se necesita una fecha y hora.");
		}
		if (energiaGenerada < 0) {
			throw new IllegalArgumentException("La energia generada no puede ser negativa.");
		}
		if (incidencias < 0) {
			throw new IllegalArgumentException("Las incidencias no pueden ser negativas");
		}
		
		this.fechaHora = fechaHora;
		this.energiaGenerada = energiaGenerada;
		this.incidencias = incidencias;
		
	}
	
	public LocalDateTime fechaHora() {
		return fechaHora;
	}
	public double energiaGenerada() {
		return energiaGenerada;
	}
	public int incidencias() {
		return incidencias;
	}
	
	//public sinDatos(){
	//	return energiaGenerada == 0 && incidencias == 0;
	//}
	
    @Override
    public String toString() {
    	return "Fecha y hora: " + fechaHora + "||" + energiaGenerada + " Energia, " + incidencias + " Incidencias.";	
    }

	
	
}
