package smartgrids;

import java.time.LocalDateTime;

//1. Cumple el IREP de PlantaRenovable.
//2. superficieCubierta > 0.
//3. cantidadPaneles > 0.
//4. 0 <= horasRadiacion <= 24.

public class PlantaSolar extends PlantaRenovable {

    private double superficieCubierta;
    private int cantidadPaneles;
    private double horasRadiacion;

    public PlantaSolar(String nombre, String direccion, double capacidadMaxima,
                       LocalDateTime fechaFuncionamiento, String fuentePrimaria,
                       double eficienciaPromedio, double superficieCubierta,
                       int cantidadPaneles, double horasRadiacion) {
    	
		super (nombre, direccion, capacidadMaxima, fechaFuncionamiento, fuentePrimaria, eficienciaPromedio);
		
		if (superficieCubierta <= 0) {
			throw new IllegalArgumentException("Necesita tener superficie cubierta.");
		}
		if (cantidadPaneles <= 0) {
			throw new IllegalArgumentException("Necesita tener al menos un panel.");
		}
		if (horasRadiacion < 0 || horasRadiacion > 24) {
			throw new IllegalArgumentException("Las horas de radiacion no son validas.");
		}
		
		this.superficieCubierta = superficieCubierta;
		this.cantidadPaneles = cantidadPaneles;
		this.horasRadiacion = horasRadiacion;
		
    }

    public double superficieCubierta() {
		return superficieCubierta;
	}
    public int cantidadPaneles() {
		return cantidadPaneles;
	}
    public double horasRadiacion() {
		return horasRadiacion;
	}
    

    @Override
    public double cuotaEsperada() { // Solar tiene en cuenta las horas de sol del dia.

		return capacidadMaxima() * eficienciaPromedio() * horasRadiacion / 24;
    }
    
    //MONITOREO======================
    @Override
    public void registrarMonitoreo(LocalDateTime fechaHora, double energia, int incidencias) {
    	agregarMonitoreo(new Monitoreo(fechaHora, energia, incidencias));
    }
    //MONITOREO======================


    @Override
    public String toString() {
		return super.toString() + "-Superficie Cubierta: " + superficieCubierta + 
								  " -Cantidad de paneles: " + cantidadPaneles +
								  " -Horas de radiacion: " + horasRadiacion;
    }
}