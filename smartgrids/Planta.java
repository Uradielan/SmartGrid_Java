package smartgrids;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Cloude.smartgrids.PeriodoInactividad;

public abstract class Planta {
    protected String nombre;
    protected String direccion;
    protected double capacidadMaxima;
    protected LocalDate fechaFuncionamiento;
    protected boolean estado;
    protected HashMap<LocalDateTime, Monitoreo> monitoreos;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public Planta(String nombre, String direccion, double capacidadMaxima, LocalDate fechaFuncionamiento) {
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
        this.estado = true;
        this.monitoreos = new HashMap<>();
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

    public void cambiarEstado(boolean nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean consultarEstado() {
        return estado;
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

    public void registrarMonitoreoEolico(LocalDateTime fechaHora, double energia, int incidencias, double velocidadViento) {
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

    public int incidencias(LocalDateTime fechaHora) {
        Monitoreo m = monitoreos.get(fechaHora);
        return (m != null) ? m.incidencias() : 0;
    }

    public boolean cumplioCuota(LocalDateTime fechaHora) {
        Monitoreo m = monitoreos.get(fechaHora);
        return m != null && cumpleCuota(m);
    }

    public Map<Integer, Integer> histograma(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null || desde.isAfter(hasta)) {
            throw new IllegalArgumentException("Rango de fechas inválido");
        }
        Map<Integer, Integer> resultado = new TreeMap<>();
        for (int hora = 0; hora < 24; hora++) {
            resultado.put(hora, 0);
        }
        for (LocalDate dia = desde; !dia.isAfter(hasta); dia = dia.plusDays(1)) {
            for (int hora = 0; hora < 24; hora++) {
                LocalDateTime fh = dia.atTime(hora, 0);
                Monitoreo m = monitoreos.get(fh);
                if (m != null && cumpleCuota(m)) {
                    resultado.put(hora, resultado.get(hora) + 1);
                }
            }
        }
        return resultado;
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

    public void desactivar(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora es obligatoria");
        }
        if (!estado) {
            return;
        }
        estado = false;
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

    @Override
    public String toString() {
        String estadoTexto = this.estado ? "ACTIVA" : "INACTIVA";
        return this.nombre + " (" + estadoTexto + "): " + this.fechaFuncionamiento + "T00:00:00";
    }
}