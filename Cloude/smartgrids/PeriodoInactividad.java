package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

/**
 * Intervalo de tiempo en el que una planta estuvo inactiva.
 *
 * IREP:
 * 1. inicio != null.
 * 2. fin == null (periodo abierto: la planta sigue inactiva) o fin >= inicio.
 */
public class PeriodoInactividad {

    private LocalDateTime inicio;
    private LocalDateTime fin;

    /**
     * Abre un periodo de inactividad que comienza en inicio.
     *
     * @throws IllegalArgumentException si inicio es null.
     */
    public PeriodoInactividad(LocalDateTime inicio) {
        if (inicio == null) {
            throw new IllegalArgumentException("El inicio del periodo de inactividad es obligatorio");
        }
        this.inicio = inicio;
        this.fin = null;
    }

    /**
     * Cierra el periodo en la fecha y hora indicadas.
     *
     * @throws IllegalArgumentException si fin es null o anterior a inicio.
     * @throws IllegalStateException si el periodo ya esta cerrado.
     */
    public void cerrar(LocalDateTime fin) {
        if (fin == null) {
            throw new IllegalArgumentException("El fin del periodo de inactividad es obligatorio");
        }
        if (this.fin != null) {
            throw new IllegalStateException("El periodo de inactividad ya esta cerrado");
        }
        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("El fin (" + fin + ") es anterior al inicio (" + inicio + ")");
        }
        this.fin = fin;
    }

    public LocalDateTime inicio() {
        return inicio;
    }

    /** Devuelve null si el periodo sigue abierto. */
    public LocalDateTime fin() {
        return fin;
    }

    public boolean estaAbierto() {
        return fin == null;
    }

    /**
     * Indica si la fecha y hora esta dentro del periodo: inicio inclusive, fin exclusive.
     * Si el periodo esta abierto, vale para toda fecha y hora desde inicio.
     */
    public boolean contiene(LocalDateTime fechaHora) {
        if (fechaHora.isBefore(inicio)) {
            return false;
        }
        return fin == null || fechaHora.isBefore(fin);
    }

    @Override
    public String toString() {
        return inicio + " -> " + (fin == null ? "(sigue inactiva)" : fin.toString());
    }
}
