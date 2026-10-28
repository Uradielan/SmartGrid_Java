package ar.edu.ungs.prog2.smartgrids;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

/** Tests propios del grupo: casos que complementan el JUnit de la catedra. */
public class SmartGridsPropioTest {

    private static final String PUESTA = "2020-01-01T00:00:00";
    private static final String D1 = "2026-03-01";

    private ISmartGrids sistema;

    @Before
    public void setUp() {
        sistema = new SmartGrids();
        sistema.registrarPlantaSolar("Solar1", "Ruta 1", 1000, PUESTA, "Sol", 0.5, 5000, 2000, 12);
        sistema.registrarPlantaEolica("Eolica1", "Ruta 2", 400, PUESTA, "Viento", 0.5, 10, 80, 5);
        sistema.registrarPlantaTermica("Termica1", "Ruta 4", 600, PUESTA, "Gas", 50, 900);
    }

    @Test
    public void sistemaVacioSePuedeImprimirYConsultar() {
        ISmartGrids vacio = new SmartGrids();
        assertTrue(vacio.consultarPlantas().isEmpty());
        assertTrue(vacio.plantasQueNuncaCumplieronCuota().isEmpty());
        assertTrue(vacio.plantasOrdenadasPorEnergiaGenerada().isEmpty());
        assertEquals(0, vacio.limpiarMonitoreosSinDatos());
        assertTrue(vacio.toString().contains("sin plantas"));
        assertEquals("Plantas Eolicas: Horas con poco viento\n", vacio.horasSinVelocidadMinimaPorPlantaEolica());
    }

    @Test
    public void formatoExactoDelPunto10() {
        sistema.registrarMonitoreo("Eolica1", D1 + "T07:00:00", 10, 0, 2);
        sistema.registrarMonitoreo("Eolica1", D1 + "T11:00:00", 10, 0, 3.5);
        sistema.registrarMonitoreo("Eolica1", D1 + "T12:00:00", 10, 0, 6); // alcanzo: no figura
        String esperado = "Plantas Eolicas: Horas con poco viento\n"
                + "- Eolica1:  5 m/s\n"
                + "       07: 2 m/s\n"
                + "       11: 3.5 m/s\n";
        assertEquals(esperado, sistema.horasSinVelocidadMinimaPorPlantaEolica().replace("\r\n", "\n"));
    }

    @Test
    public void periodosDeInactividadYBordes() {
        sistema.desactivarPlanta("Solar1", D1 + "T10:00:00");
        sistema.activarPlanta("Solar1", D1 + "T12:00:00");
        assertTrue(sistema.estaActiva("Solar1"));
        assertTrue(sistema.plantasInactivasEn(D1 + "T09:00:00").isEmpty());
        assertEquals(Collections.singletonList("Solar1"), sistema.plantasInactivasEn(D1 + "T10:00:00"));
        assertEquals(Collections.singletonList("Solar1"), sistema.plantasInactivasEn(D1 + "T11:59:59"));
        assertTrue(sistema.plantasInactivasEn(D1 + "T12:00:00").isEmpty());

        // segundo periodo: la planta recuerda los dos
        sistema.desactivarPlanta("Solar1", D1 + "T20:00:00");
        assertEquals(Collections.singletonList("Solar1"), sistema.plantasInactivasEn(D1 + "T11:00:00"));
        assertEquals(Collections.singletonList("Solar1"), sistema.plantasInactivasEn(D1 + "T23:00:00"));
        assertTrue(sistema.plantasInactivasEn(D1 + "T15:00:00").isEmpty());
    }

    @Test
    public void activarODesactivarDosVecesNoCambiaNada() {
        sistema.activarPlanta("Solar1", D1 + "T10:00:00");
        assertTrue(sistema.estaActiva("Solar1"));
        sistema.desactivarPlanta("Solar1", D1 + "T10:00:00");
        sistema.desactivarPlanta("Solar1", D1 + "T11:00:00");
        assertFalse(sistema.estaActiva("Solar1"));
        assertTrue(sistema.plantasInactivasEn(D1 + "T10:30:00").contains("Solar1"));
    }

    @Test
    public void rechazaFechasInvalidasYDatosInvalidos() {
        assertThrows(RuntimeException.class, () -> sistema.activarPlanta("Solar1", "ayer"));
        assertThrows(RuntimeException.class, () -> sistema.activarPlanta("Nada", D1 + "T10:00:00"));
        assertThrows(RuntimeException.class, () -> sistema.registrarMonitoreo("Solar1", null, 1, 0));
        assertThrows(RuntimeException.class, () -> sistema.registrarMonitoreo("Solar1", D1 + "T01:00:00", -1, 0));
        assertThrows(RuntimeException.class, () -> sistema.registrarMonitoreo("Solar1", D1 + "T01:00:00", 1, -1));
        // supera la capacidad maxima
        assertThrows(RuntimeException.class, () -> sistema.registrarMonitoreo("Solar1", D1 + "T01:00:00", 1001, 0));
        // combustible mayor al almacenamiento
        assertThrows(RuntimeException.class,
                () -> sistema.registrarMonitoreo("Termica1", D1 + "T01:00:00", 300, 0, 10, 901));
        assertThrows(RuntimeException.class,
                () -> sistema.registrarPlantaSolar("X", "x", 0, PUESTA, "Sol", 0.5, 1, 1, 12));
        assertThrows(RuntimeException.class,
                () -> sistema.registrarPlantaSolar("X", "x", 10, "2020-13-45", "Sol", 0.5, 1, 1, 12));
        assertThrows(RuntimeException.class,
                () -> sistema.histogramaCumplimiento("Solar1", "2026/03/01", "2026-03-02"));
    }

    @Test
    public void monitoreosEnLaMismaHoraSeConsideranDuplicados() {
        sistema.registrarMonitoreo("Solar1", D1 + "T12:15:00", 300, 0);
        assertThrows(RuntimeException.class, () -> sistema.registrarMonitoreo("Solar1", D1 + "T12:45:00", 10, 0));
        int[] hist = sistema.histogramaCumplimiento("Solar1", D1, D1);
        assertEquals(1, hist[12]);
    }

    @Test
    public void plantaSinMonitoreosNuncaCumplioLaCuota() {
        assertEquals(Arrays.asList("Solar1", "Eolica1", "Termica1"), sistema.plantasQueNuncaCumplieronCuota());
        sistema.registrarMonitoreo("Solar1", D1 + "T12:00:00", 250, 0); // cumple justo
        assertEquals(Arrays.asList("Eolica1", "Termica1"), sistema.plantasQueNuncaCumplieronCuota());
    }

    @Test
    public void ordenPorEnergiaMantieneOrdenDeRegistroEnEmpates() {
        sistema.registrarMonitoreo("Eolica1", D1 + "T01:00:00", 100, 0, 6);
        sistema.registrarMonitoreo("Termica1", D1 + "T01:00:00", 100, 0, 1, 1);
        assertEquals(Arrays.asList("Eolica1: 100.0", "Termica1: 100.0", "Solar1: 0.0"),
                sistema.plantasOrdenadasPorEnergiaGenerada());
    }

    @Test
    public void incidenciasPorHoraEnPlantaSinMonitoreos() {
        assertArrayEquals(new int[24], sistema.incidenciasPorHora("Solar1"));
        assertArrayEquals(new int[24], sistema.histogramaCumplimiento("Solar1", D1, D1));
    }
}
