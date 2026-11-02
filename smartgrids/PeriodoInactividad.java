package smartgrids;

import java.time.LocalDateTime;

//Periodo en el que una planta estuvo inactiva.
//Mientras este inactiva el fin es null, porque el periodo esta abierto.

//IREP
//1. inicio != null
//2. fin es null o fin no es anterior a inicio.

public class PeriodoInactividad {
	
	private LocalDateTime inicio;
	private LocalDateTime fin;

	public PeriodoInactividad(LocalDateTime inicio){
		
		if (inicio == null) {
            throw new IllegalArgumentException("Debe haber un inicio.");
		}
		
		this.inicio = inicio;
		this.fin = null;
	}
	
	public void cerrar(LocalDateTime fin) {
		if (fin == null) {
            throw new IllegalArgumentException("Debe haber un fin para el periodo.");
		}
		if (this.fin != null) {
            throw new IllegalArgumentException("El periodo ya esta cerrado.");
		}
		if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("El periodo no puede finalizar antes de empezar.");
		}
		
		this.fin = fin;
	}
	
	public LocalDateTime inicio() {
		return inicio;
	}
	public LocalDateTime fin() {
		return fin;
	}
	
	public boolean estaAbierto() { //Si esta inactiva la planta, da true. Sino, false.
		return fin == null; //mientras este inactivo, el fin es null.
	}
	
	public boolean contiene(LocalDateTime fechaHora) {
		if(fechaHora.isBefore(inicio)) { 
            return false; //lo anterior al inicio no importa.
		}
		return fin == null || fechaHora.isBefore(fin); //veo si la hora es antes que el fin. Si es, está.
		//Si el periodo esta abierto, la hora esta si o si.
	}
	
	@Override
	public String toString() {
		if (fin != null) {
			return "[" + inicio + " " + fin + "]";
		}
		return  "[" + inicio + " " + "sigue inactiva" + "]";
	}
	
}
