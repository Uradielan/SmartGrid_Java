package smartgrids;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

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
    public boolean esEolica() {
        return true;
    }
    
    @Override 
    public Map<Integer, Double> horasSinVientoMinimo() { 
    	TreeMap<Integer, Double> maxPorHora = new TreeMap<>();
        
        for (Monitoreo m : monitoreos.values()) { //recorro los monitoreos
        	if (m instanceof MonitoreoEolico) {
        		MonitoreoEolico me = (MonitoreoEolico) m;
        		int h = me.fechaHora().getHour(); //hora
        		double v = me.velocidadViento(); //viento
        		
        		if (!maxPorHora.containsKey(h) || v > maxPorHora.get(h)) {
        			maxPorHora.put(h, v); //guardo max.
        		}
        	}
        }
        TreeMap<Integer, Double> resultado = new TreeMap<>(); 
        for (Map.Entry<Integer, Double> e : maxPorHora.entrySet()) { //recorro el mapa de maximos.
        	int hora = e.getKey();	
        	double max = e.getValue();//viento maximo

        	if (max < vientoMinimo) {
        		resultado.put(hora, max); //si el maximo es menor al vientoMinimo, esa hora nunca supero vientoMinimo. la cuento xd
        	}

        }
        return resultado;
    }

    //MONITOREO======================
    @Override
    public void registrarMonitoreoEolico(LocalDateTime fechaHora, double energia, int incidencias,
            double velocidadViento) {
    	agregarMonitoreo(new MonitoreoEolico(fechaHora, energia, incidencias, velocidadViento));
    }
    //MONITOREO======================

     @Override
     public String toString() {
	 	return super.toString() + "-Cantidad de aerogeneradores: " + cantidadAerogeneradores + 
	 							  " -Altura de torres: " + alturaTorres +
	 							  " -Viento minimo: " + vientoMinimo;
     }
}