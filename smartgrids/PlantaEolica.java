package smartgrids;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PlantaEolica extends PlantaRenovable {
    private int cantidadAerogeneradores;
    private double alturaTorres;
    private double vientoMinimo;

    public PlantaEolica(String nombre, String direccion, double capacidadMaxima, LocalDate fechaFuncionamiento,
                        String fuentePrimaria, double eficienciaPromedio, int cantidadAerogeneradores, double alturaTorres, double vientoMinimo) {
        super(nombre, direccion, capacidadMaxima, fechaFuncionamiento, fuentePrimaria, eficienciaPromedio);
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
    public void registrarMonitoreoEolico(LocalDateTime fechaHora, double energia, int incidencias, double velocidadViento) {
        if (!estado) {
            throw new IllegalStateException("La planta está inactiva");
        }
        MonitoreoEolico m = new MonitoreoEolico(fechaHora, energia, incidencias, velocidadViento);
        monitoreos.put(fechaHora, m);
    }

    @Override
    public List<Integer> horasSinVientoMinimo() {
        Set<Integer> horas = new TreeSet<>();
        for (Monitoreo m : monitoreos.values()) {
            if (m instanceof MonitoreoEolico) {
                MonitoreoEolico me = (MonitoreoEolico) m;
                if (me.velocidadViento() < this.vientoMinimo) {
                    horas.add(me.fechaHora().getHour());
                }
            }
        }
        return new ArrayList<>(horas);
    }
}