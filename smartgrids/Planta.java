package smartgrids;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class Planta {
    private String nombre;
    private String direccion;
    private double capacidadMaxima;
    private LocalDateTime fechaFuncionamiento;
    private boolean activa;
    protected HashMap<LocalDateTime, Monitoreo> monitoreos;
    private ArrayList<PeriodoInactividad> periodosInactividad;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public Planta(String nombre, String direccion, double capacidadMaxima, LocalDateTime fechaFuncionamiento) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria");
        }
        if (Double.isNaN(capacidadMaxima) || capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a 0");
        }
        if (fechaFuncionamiento == null) {
            throw new IllegalArgumentException("La fecha de funcionamiento es obligatoria");
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

    public boolean estaActiva() {
        return activa;
    }

    public abstract boolean esRenovable();

    public abstract double cuotaEsperada();

    public boolean cumpleCuota(Monitoreo monitoreo) {
        if (monitoreo == null) {
            throw new IllegalArgumentException("El monitoreo no puede ser nulo");
        }
        return monitoreo.energiaGenerada() >= cuotaEsperada();
    }

    public void registrarMonitoreo(LocalDateTime fechaHora, double energia, int incidencias) {
        throw new UnsupportedOperationException("Tipo de monitoreo no soportado para " + nombre);
    }

    public void registrarMonitoreoEolico(LocalDateTime fechaHora, double energia, int incidencias,
            double velocidadViento) {
        throw new UnsupportedOperationException("Monitoreo eólico no soportado para " + nombre);
    }

    public void registrarMonitoreoTermico(LocalDateTime fechaHora, double energia, int incidencias,
            double co2, double combustible) {
        throw new UnsupportedOperationException("Monitoreo térmico no soportado para " + nombre);
    }

    public double totalGenerado() {
        double total = 0;
        for (Monitoreo m : monitoreos.values()) {
            total += m.energiaGenerada();
        }
        return total;
    }


    public boolean nuncaCumplioCuota() {
        for (Monitoreo m : monitoreos.values()) {
            if (cumpleCuota(m)) {
                return false;
            }
        }
        return true;
    }

    public String fuentePrimaria() {
        throw new UnsupportedOperationException("La planta " + nombre + " no es renovable");
    }

    public boolean esEolica() {
        return false;
    }

    public List<Integer> horasSinVientoMinimo() {
        throw new UnsupportedOperationException("La planta " + nombre + " no es eólica");
    }

    public void desactivar(LocalDateTime momento) {

        if (momento == null) {
            throw new IllegalArgumentException("Se necesita un tiempo valido, fecha y hora.");
        }
        if (!activa) {
            throw new IllegalStateException("No se puede desactivar, ya esta inactiva.");
        }
        if (momento.isBefore(fechaFuncionamiento)) {
            throw new IllegalArgumentException("No se puede desactivar antes de que este funcionando.");
        }

        periodosInactividad.add(new PeriodoInactividad(momento)); // pum la desactivo.
        activa = false;
    }

    public void activar(LocalDateTime momento) {
        if (momento == null) {
            throw new IllegalArgumentException("Se necesita un tiempo valido, fecha y hora.");
        }
        if (activa) {
            throw new IllegalStateException("No se puede activar, ya esta activa.");
        }

        ((PeriodoInactividad) periodosInactividad.get(periodosInactividad.size() - 1)).cerrar(momento);
        activa = true;
    }

    public int limpiarMonitoreosSinDatos() {
        List<LocalDateTime> eliminarDato = new ArrayList<>();
        for (Map.Entry<LocalDateTime, Monitoreo> entry : monitoreos.entrySet()) {
            if (entry.getValue().energiaGenerada() == 0 && entry.getValue().incidencias() == 0) {
                eliminarDato.add(entry.getKey());
            }
        }
        for (LocalDateTime fecha : eliminarDato) {
            monitoreos.remove(fecha);
        }
        return eliminarDato.size();
    }

    public boolean estuvoInactivaEn(LocalDateTime momento) { // estuvo inactiva a esta hora?
        for (PeriodoInactividad p : periodosInactividad) {
            if (p.contiene(momento)) { // Reviso todos los periodos de inactividad para ver si esta esa hora.
                return true;
            }
        }
        return false;
    }

    protected void agregarMonitoreo(Monitoreo monitoreo) { // guarda un monitoreo.

        LocalDateTime fh = monitoreo.fechaHora(); // fechaHora

        if (!activa) {
            throw new IllegalStateException("No se puede monitorear, la planta esta inactiva.");
        }

        if (fh.isBefore(fechaFuncionamiento)) {
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

    public int[] incidenciasPorHora() {
        int[] porHora = new int[24]; // veo la fecha del monitoreo, agarro la hora y sumo las incidencias de ESA
                                     // hora. Por todas las horas.
        for (Monitoreo m : monitoreos.values()) {
            porHora[m.fechaHora().getHour()] += m.incidencias(); // sumo
        }
        return porHora;

    }

    //==============Histograma
    public int[] histograma(LocalDate inicio, LocalDate fin) {
    	if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Ingrese fechas de inicio y fin.");
    	}
    	
    	if (inicio.isAfter(fin)){
    		throw new IllegalArgumentException("Inicio no puede ser despues de fin.");
    	}
        

        int[] hist = new int[24];  
        
        LocalDate dia = inicio;
     
        while (!dia.isAfter(fin)) { //arranco desde el primer dia y voy hasta pasarme del fin.
        	
        	for (int h = 0; h < 24; h++){ //voy por todas las horas del dia
        		Monitoreo m = monitoreos.get(dia.atTime(h, 0));//monitoreo de esa fecha y hora.
        		
        		if (m != null && cumpleCuota(m)){
        			hist[h]++; //si hay monitoreo y cumple la cuota, va al histograma.
        		}
        	}

        	dia = dia.plusDays(1); 
        }

          
        return hist;
    }
    
    
    //==============Histograma
    
    
    
   
    public String detalle() {
        String estadoTexto = activa ? "ACTIVA" : "INACTIVA";
        return nombre + " (" + estadoTexto + "): " + fechaFuncionamiento.format(FORMATO_FECHA);
    }

    @Override
    public String toString() {
        return detalle()
                +" -Direccion: " + direccion
                +" -Capacidad: " + capacidadMaxima
                +" -Monitoreos: " + monitoreos.size()
                +" -Periodos inactivos: " + periodosInactividad.size();
    }
}