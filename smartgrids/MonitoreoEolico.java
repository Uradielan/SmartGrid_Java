package smartgrids;

import java.time.LocalDateTime;

//Que genero una planta en fecha y hora.
//IREP
//1. Cumple el IREP de Monitoreo.
//2. velocidadViento >= 0.

public class MonitoreoEolico extends Monitoreo {

	private double velocidadViento;

	public MonitoreoEolico(LocalDateTime fechaHora, double energiaGenerada, int incidencias, double velocidadViento) {

		super(fechaHora, energiaGenerada, incidencias);

		if (velocidadViento < 0) {
			throw new IllegalArgumentException("La velocidad del viento no puede ser negativa");
		}

		this.velocidadViento = velocidadViento;

	}

	public double velocidadViento() {
		return velocidadViento;
	}

	@Override
	public String toString() {
		return super.toString() + " " + velocidadViento + " Velocidad Viento.";
	}

}
