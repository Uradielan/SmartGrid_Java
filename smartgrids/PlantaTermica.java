package smartgrids;

import java.time.LocalDateTime;

//1. Cumple el IREP de Planta.
//2. tipoCombustible no es null ni vacío.
//3. objetivoCO2 > 0.
//4. almacenamiento > 0.
//5. Todo monitoreo cumple combustible() <= almacenamiento (no se puede usar más
//combustible del que hay).

public class PlantaTermica extends Planta {

    private String tipoCombustible;
    private double objetivoCO2;
    private double almacenamiento;

    public PlantaTermica (String nombre, String direccion, double capacidadMaxima,
			LocalDateTime fechaFuncionamiento, String tipoCombustible,
			double objetivoCO2, double almacenamiento) {
    	
    	super (nombre, direccion, capacidadMaxima, fechaFuncionamiento); //valida
    	
		if (tipoCombustible == null || tipoCombustible.isBlank()) {
			throw new IllegalArgumentException("Se necesita un tipo de combustible.");
		}
		if (objetivoCO2 <= 0) {
			throw new IllegalArgumentException("El objetivo de emisiones de CO2 no puede ser negativa ni 0.");
		}
		if (almacenamiento <= 0) {
			throw new IllegalArgumentException("El almacenamiento no puede ser negativo o 0.");
		}
		
		this.tipoCombustible = tipoCombustible;
		this.objetivoCO2 = objetivoCO2;
		this.almacenamiento = almacenamiento;
		
    }

    public String tipoCombustible() {
		return tipoCombustible;
	}
    public double objetivoCO2() {
		return objetivoCO2;
	}
    public double almacenamiento() {
		return almacenamiento;
	}
    
    @Override   
    public boolean esRenovable() {  //no es.
		return false;
    }
    @Override
	public double cuotaEsperada() { 
		return capacidadMaxima() / 2;
	}

    //MONITOREO======================
    @Override
    public void registrarMonitoreoTermico(LocalDateTime fechaHora, double energia, int incidencias,
    		double co2, double combustible) {
    	if (combustible > almacenamiento) {
			throw new IllegalArgumentException("El combustible no puede superar la capacidad de alacenamiento");
    	}
    	
    	agregarMonitoreo(new MonitoreoTermico(fechaHora, energia, incidencias, co2, combustible));
    }
    //MONITOREO======================

    @Override
    public String toString() {
		return super.toString() + "-Tipo de combustible: " + tipoCombustible + 
								  " -Objetivo de emisiones de CO2: " + objetivoCO2 +
								  " -Almacenamiento: " + almacenamiento;
    }
}