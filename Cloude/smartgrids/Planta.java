package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Planta generadora de energia (clase abstracta). Guarda sus propios monitoreos
 * y los periodos en los que estuvo inactiva.
 *
 * Representacion:
 *  - monitoreos: HashMap con clave fechaHora (consulta por fecha y hora en O(1)).
 *  - periodosInactividad: lista ordenada de periodos; estado resume si esta activa (O(1)).
 *
 * IREP:
 * 1. nombre != null y no es vacio.
 * 2. direccion != null y no es vacia.
 * 3. capacidadMaxima > 0.
 * 4. fechaFuncionamiento != null.
 * 5. monitoreos != null y ningun valor es null.
 * 6. Para toda clave f de monitoreos: monitoreos[f].fechaHora().equals(f)
 *    (no hay dos monitoreos en la misma hora).
 * 7. Todo monitoreo cumple 0 <= energiaGenerada <= capacidadMaxima.
 * 8. Todo monitoreo cumple fechaHora >= fechaFuncionamiento (truncada a la hora).
 * 9. periodosInactividad != null y ningun elemento es null.
 * 10. Solo el ultimo periodo puede estar abierto y, para cada par consecutivo,
 *     p[i].fin() <= p[i+1].inicio().
 * 11. estado == true si y solo si no hay periodos o el ultimo esta cerrado.
 */
public abstract class Planta {

    static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private String nombre;
    private String direccion;
    private double capacidadMaxima;
    private LocalDateTime fechaFuncionamiento;
    private boolean estado; // true = activa
    private ArrayList<PeriodoInactividad> periodosInactividad;
    private HashMap<LocalDateTime, Monitoreo> monitoreos;

    /**
     * Crea una planta activa.
     *
     * @throws IllegalArgumentException si algun dato viola el IREP.
     */
    protected Planta(String nombre, String direccion, double capacidadMaxima,
            LocalDateTime fechaFuncionamiento) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la planta es obligatorio");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La direccion de la planta es obligatoria");
        }
        if (Double.isNaN(capacidadMaxima) || capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad maxima debe ser mayor a 0: " + capacidadMaxima);
        }
        if (fechaFuncionamiento == null) {
            throw new IllegalArgumentException("La fecha de puesta en funcionamiento es obligatoria");
        }
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadMaxima = capacidadMaxima;
        this.fechaFuncionamiento = fechaFuncionamiento;
        this.estado = true;
        this.periodosInactividad = new ArrayList<>();
        this.monitoreos = new HashMap<>();
    }

    // ---------------------------------------------------------------- datos

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

    // --------------------------------------------- operaciones abstractas

    /** Indica si la planta utiliza fuentes de energia renovable. */
    public abstract boolean esRenovable();

    /**
     * Devuelve la fuente primaria de energia. Por defecto genera error (la planta no es
     * renovable). PlantaRenovable lo redefine.
     */
    public String fuentePrimaria() {
        throw new UnsupportedOperationException("La planta " + nombre + " no es renovable");
    }

    /** Energia minima que debe generar la planta en una hora para cumplir la cuota. */
    public abstract double cuotaEsperada();

    /** Texto con las caracteristicas propias de cada tipo de planta (para toString). */
    protected abstract String caracteristicas();

    // -------------------------------------------------------------- estado

    /** Indica si la planta esta activa. O(1). */
    public boolean estaActiva() {
        return estado;
    }

    /**
     * Desactiva la planta desde fechaHora y abre un periodo de inactividad. O(1).
     * Si ya esta inactiva, no hace nada.
     *
     * @throws IllegalArgumentException si fechaHora es null o es anterior al fin del
     *         ultimo periodo de inactividad.
     */
    public void desactivar(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora es obligatoria");
        }
        if (!estado) {
            return;
        }
        if (!periodosInactividad.isEmpty()) {
            PeriodoInactividad ultimo = periodosInactividad.get(periodosInactividad.size() - 1);
            if (fechaHora.isBefore(ultimo.fin())) {
                throw new IllegalArgumentException("La fecha y hora (" + fechaHora
                        + ") es anterior al fin de la ultima inactividad (" + ultimo.fin() + ")");
            }
        }
        periodosInactividad.add(new PeriodoInactividad(fechaHora));
        estado = false;
    }

    /**
     * Activa la planta y cierra el periodo de inactividad abierto. O(1).
     * Si ya esta activa, no hace nada.
     *
     * @throws IllegalArgumentException si fechaHora es null o es anterior al inicio
     *         del periodo abierto.
     */
    public void activar(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora es obligatoria");
        }
        if (estado) {
            return;
        }
        periodosInactividad.get(periodosInactividad.size() - 1).cerrar(fechaHora);
        estado = true;
    }

    /** Indica si la planta estaba inactiva en la fecha y hora indicada. */
    public boolean estuvoInactivaEn(LocalDateTime fechaHora) {
        for (PeriodoInactividad periodo : periodosInactividad) {
            if (periodo.contiene(fechaHora)) {
                return true;
            }
        }
        return false;
    }

    // ----------------------------------------------------------- monitoreos

    /**
     * Registra un monitoreo comun (planta solar o hidraulica).
     * Por defecto genera error: el tipo de monitoreo no corresponde a esta planta.
     * PlantaSolar y PlantaHidraulica lo redefinen para aceptarlo.
     */
    public void registrarMonitoreo(LocalDateTime fechaHora, double energiaGenerada, int incidencias) {
        throw new UnsupportedOperationException(
                "El monitoreo comun no corresponde a la planta " + nombre);
    }

    /**
     * Registra un monitoreo eolico. Por defecto genera error. PlantaEolica lo redefine.
     */
    public void registrarMonitoreoEolico(LocalDateTime fechaHora, double energiaGenerada,
            int incidencias, double velocidadViento) {
        throw new UnsupportedOperationException(
                "El monitoreo eolico no corresponde a la planta " + nombre);
    }

    /**
     * Registra un monitoreo termico. Por defecto genera error. PlantaTermica lo redefine.
     */
    public void registrarMonitoreoTermico(LocalDateTime fechaHora, double energiaGenerada,
            int incidencias, double co2Generado, double combustible) {
        throw new UnsupportedOperationException(
                "El monitoreo termico no corresponde a la planta " + nombre);
    }

    /**
     * Guarda el monitoreo en la planta.
     *
     * @throws IllegalStateException si la planta esta inactiva, si ya hay un registro
     *         para esa hora, o si la fecha es anterior a la puesta en funcionamiento.
     * @throws IllegalArgumentException si la energia supera la capacidad maxima.
     */
    protected void agregarMonitoreo(Monitoreo monitoreo) {
        if (!estado) {
            throw new IllegalStateException("La planta " + nombre + " esta inactiva");
        }
        if (monitoreo.fechaHora().isBefore(fechaFuncionamiento.truncatedTo(ChronoUnit.HOURS))) {
            throw new IllegalArgumentException("El monitoreo (" + monitoreo.fechaHora()
                    + ") es anterior a la puesta en funcionamiento (" + fechaFuncionamiento + ")");
        }
        if (monitoreo.energiaGenerada() > capacidadMaxima) {
            throw new IllegalArgumentException("La energia generada (" + monitoreo.energiaGenerada()
                    + ") supera la capacidad maxima (" + capacidadMaxima + ")");
        }
        if (monitoreos.containsKey(monitoreo.fechaHora())) {
            throw new IllegalStateException("Ya existe un monitoreo de " + nombre
                    + " para " + monitoreo.fechaHora());
        }
        monitoreos.put(monitoreo.fechaHora(), monitoreo);
    }

    /** Vista de solo lectura de los monitoreos, para las subclases. */
    protected Collection<Monitoreo> monitoreos() {
        return Collections.unmodifiableCollection(monitoreos.values());
    }

    // ------------------------------------------------------------- cuota

    /**
     * Indica si el monitoreo cumple la cuota: la energia generada alcanza o supera
     * cuotaEsperada(). PlantaTermica lo redefine: ademas exige emitir menos CO2 que el objetivo.
     *
     * @throws IllegalArgumentException si monitoreo es null.
     */
    public boolean cumpleCuota(Monitoreo monitoreo) {
        if (monitoreo == null) {
            throw new IllegalArgumentException("El monitoreo es obligatorio");
        }
        return monitoreo.energiaGenerada() >= cuotaEsperada();
    }

    // ---------------------------------------------------------- consultas

    /** Total acumulado de energia generada (0 si no hay monitoreos). */
    public double totalGenerado() {
        double total = 0;
        for (Monitoreo m : monitoreos.values()) {
            total += m.energiaGenerada();
        }
        return total;
    }

    /** Incidencias acumuladas por hora del dia (24 posiciones), sumando todos los dias. */
    public int[] incidenciasPorHora() {
        int[] porHora = new int[24];
        for (Monitoreo m : monitoreos.values()) {
            porHora[m.fechaHora().getHour()] += m.incidencias();
        }
        return porHora;
    }

    /**
     * Para cada hora del dia (24 posiciones), en cuantos dias entre desde y hasta
     * (inclusive) la planta cumplio la cuota.
     * Consulta directamente en el mapa cada hora de cada dia del rango: O(24 * dias).
     *
     * @throws IllegalArgumentException si desde o hasta son null, o desde es posterior a hasta.
     */
    public int[] histograma(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new IllegalArgumentException("Las fechas desde y hasta son obligatorias");
        }
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha desde (" + desde
                    + ") es posterior a la fecha hasta (" + hasta + ")");
        }
        int[] histograma = new int[24];
        for (LocalDate dia = desde; !dia.isAfter(hasta); dia = dia.plusDays(1)) {
            for (int hora = 0; hora < 24; hora++) {
                Monitoreo m = monitoreos.get(dia.atTime(hora, 0));
                if (m != null && cumpleCuota(m)) {
                    histograma[hora]++;
                }
            }
        }
        return histograma;
    }

    /** Indica si ninguno de sus monitoreos cumplio la cuota (true tambien si no tiene monitoreos). */
    public boolean nuncaCumplioCuota() {
        for (Monitoreo m : monitoreos.values()) {
            if (cumpleCuota(m)) {
                return false;
            }
        }
        return true;
    }

    /** Elimina los monitoreos con energia e incidencias en cero. Devuelve la cantidad eliminada. */
    public int limpiarMonitoreosSinDatos() {
        int eliminados = 0;
        Iterator<Monitoreo> it = monitoreos.values().iterator();
        while (it.hasNext()) {
            if (it.next().sinDatos()) {
                it.remove();
                eliminados++;
            }
        }
        return eliminados;
    }

    /**
     * Detalle de horas con poco viento para el punto 10. Por defecto devuelve ""
     * (la planta no es eolica). PlantaEolica lo redefine.
     */
    public String detalleHorasConPocoViento() {
        return "";
    }

    // ------------------------------------------------------------- texto

    /** Una linea: "{NOMBRE} ({ACTIVA|INACTIVA}): {fecha de puesta en funcionamiento}". */
    public String detalle() {
        return nombre + " (" + (estado ? "ACTIVA" : "INACTIVA") + "): "
                + fechaFuncionamiento.format(FORMATO_FECHA_HORA);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(detalle()).append(" [").append(getClass().getSimpleName()).append("]\n");
        sb.append("    Direccion: ").append(direccion)
          .append(" | Capacidad maxima: ").append(capacidadMaxima)
          .append(" | Cuota esperada por hora: ").append(cuotaEsperada()).append('\n');
        sb.append("    ").append(caracteristicas()).append('\n');
        if (periodosInactividad.isEmpty()) {
            sb.append("    Periodos de inactividad: ninguno\n");
        } else {
            sb.append("    Periodos de inactividad:\n");
            for (PeriodoInactividad p : periodosInactividad) {
                sb.append("      - ").append(p).append('\n');
            }
        }
        if (monitoreos.isEmpty()) {
            sb.append("    Monitoreos: ninguno\n");
        } else {
            sb.append("    Monitoreos (").append(monitoreos.size()).append("):\n");
            for (Monitoreo m : new TreeMap<>(monitoreos).values()) {
                sb.append("      - ").append(m).append('\n');
            }
        }
        return sb.toString();
    }
}
