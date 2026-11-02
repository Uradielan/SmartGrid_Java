package smartgrids;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

//Clase abstracta de planta.
//1. nombre no es null ni vacío.
//2. direccion no es null ni vacía.
//3. capacidadMaxima > 0.
//4. fechaFuncionamiento no es null.
//5. monitoreos no es null y ningun valor es null.
//6. Para toda clave f de monitoreos: monitoreos[f].fechaHora().equals(f). Por lo tanto, no
//hay dos monitoreos en la misma fecha y hora.
//7. periodosInactividad no es null.
//8. Si esta inactiva, el ultimo periodo esta abierto, si esta activa, todos estan cerrados.

public abstract class Planta {

	private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	private String nombre;
	private String direccion;
	private double capacidadMaxima;
	private LocalDateTime fechaFuncionamiento;
	private boolean activa;
	private HashMap<LocalDateTime, Monitoreo> monitoreos;
	private ArrayList<PeriodoInactividad> periodosInactividad;
	
	public Planta (String nombre, String direccion, double capacidadMaxima, LocalDateTime fechaFuncionamiento) {
		
		if (nombre == null || nombre.isBlank()) {
			throw new IllegalArgumentException("Se necesita nombre.");
		}
		if (direccion == null || direccion.isBlank()) {
			throw new IllegalArgumentException("Se necesita una dirección.");
		}
		if (capacidadMaxima <= 0) {
			throw new IllegalArgumentException("La capacidad maxima es invalida.");
		}
		if (fechaFuncionamiento == null) {
			throw new IllegalArgumentException("Se necesita una fecha de funcionamiento.");
		}
		
		this.nombre = nombre;
		this.direccion = direccion;
		this.capacidadMaxima = capacidadMaxima;
		this.fechaFuncionamiento = fechaFuncionamiento;
		
		this.activa = true;
		
		this.monitoreos = new HashMap<>();
		this.periodosInactividad = new ArrayList<>();

	}
	
    public String nombre() {
        return nombre;
    }
    public String direccion() {
        return direccion;
    }
    public double capacidadMaxima() {
        return capacidadMaxima;
    }
    public LocalDateTime fechaFuncionamiento() {
        return fechaFuncionamiento;
    }
    public boolean estaActiva() {
        return activa;
    }
    
    
    //ABSTRACTOS==================================
    public abstract boolean esRenovable();
    public abstract double cuotaEsperada();
    //ABSTRACTOS==================================
    
    public void desactivar(LocalDateTime momento) { //lo uso en activarPlanta, desactivarPlanta y plantasInactivasEn.
    	
    	if (momento == null) {
			throw new IllegalArgumentException("Se necesita un tiempo valido, fecha y hora.");
    	}
    	if (!activa) {
			throw new IllegalStateException("No se puede desactivar, ya esta inactiva.");
    	}
    	if (momento.isBefore(fechaFuncionamiento)) {
			throw new IllegalArgumentException("No se puede desactivar antes de que este funcionando.");
    	}
    	
    	periodosInactividad.add(new PeriodoInactividad(momento)); //pum la desactivo.
    	activa = false;
    }
    
    public void activar(LocalDateTime momento) {
     	if (momento == null) {
			throw new IllegalArgumentException("Se necesita un tiempo valido, fecha y hora.");
    	}
    	if (activa) {
			throw new IllegalStateException("No se puede activar, ya esta activa.");
    	}
    	
    	periodosInactividad.get(periodosInactividad.size()-1).cerrar(momento); //agarro el ultimo periodo y lo cierro ahora.
    	activa = true;	
    }
    
    public boolean estuvoInactivaEn(LocalDateTime momento) { //estuvo inactiva a esta hora?
    	for (PeriodoInactividad p : periodosInactividad) {
    	    if (p.contiene(momento)) { //Reviso todos los periodos de inactividad para ver si esta esa hora.
    	    	return true;
    	    }
    	}    	
    	return false;
    }
    
    public String fuentePrimaria( ) { //default, despues plantarenovable lo sobreescribe.
		throw new UnsupportedOperationException("La planta no es renovable.");
    }
    
    public String detalle() { //detalle es lo que devuelve consultarPlanta. devuelve nombre, su estado y fechaFuncionamiento.
    	
    	String estadoTexto;
    	if (activa) {
    		estadoTexto = "ACTIVA";
    	} else {
    		estadoTexto = "INACTIVA";
    	}
    	return nombre + " (" + estadoTexto + "): " + fechaFuncionamiento.format(FORMATO); 
    	
    }
    
    //MONITOREOS================================================================================================================
    protected void agregarMonitoreo(Monitoreo monitoreo) { //guarda un monitoreo.
    	
    	LocalDateTime fh = monitoreo.fechaHora(); //fechaHora
    	
    	if (!activa) {
    		throw new IllegalStateException("No se puede monitorear, la planta esta inactiva.");
    	}
    	
    	if (fh.isBefore(fechaFuncionamiento)){
    		throw new IllegalStateException("Fecha invalida, es anterior al funcionamiento de la planta.");
    	}
    	if (monitoreos.containsKey(fh)) {
    		throw new IllegalStateException("Ya hay un monitoreo en esa fecha y hora.");
    	}
    	if (monitoreo.energiaGenerada() > capacidadMaxima) {
    		throw new IllegalStateException("La energia generada no puede ser mayor a la capacidad maxima.");
    	}
    	
    	monitoreos.put(fh, monitoreo);
    	
    }
    
    //Por default ninguna planta acepta monitoreos. Cada subclase redefine su monitoreo.
    public void registrarMonitoreo(LocalDateTime fechaHora, double energia, int incidencias) {
        throw new UnsupportedOperationException("Esta planta no acepta monitoreos comunes.");
    }

    public void registrarMonitoreoEolico(LocalDateTime fechaHora, double energia, int incidencias,
                                         double velocidadViento) {
        throw new UnsupportedOperationException("Esta planta no acepta monitoreos eolicos.");
    }

    public void registrarMonitoreoTermico(LocalDateTime fechaHora, double energia, int incidencias,
                                          double co2, double combustible) {
        throw new UnsupportedOperationException("Esta planta no acepta monitoreos termicos.");
    }
    //MONITOREOSX===============================================================================================================

    public double totalGenerado() {
        double total = 0; //recorro todos los monitoreos. Voy sumando sus energias al total.
        for (Monitoreo m : monitoreos.values()) {
            total += m.energiaGenerada();
        }
        return total;
    }
    
    public int[] incidenciasPorHora() {
    	int[] porHora = new int[24]; //veo la fecha del monitoreo, agarro la hora y sumo las incidencias de ESA hora. Por todas las horas.
        for (Monitoreo m : monitoreos.values()) {
            porHora[m.fechaHora().getHour()] += m.incidencias(); //sumo 
        }
        return porHora;

    }

    @Override
    public String toString() {
    	return detalle() + " -Direccion: " + direccion
    					 + " -Capacidad: " + capacidadMaxima
    					 + " -Monitoreos: " + monitoreos.size()
    					 + " -Periodos Inactivos: " + periodosInactividad.size();
    	
    }
    
    
    

}
