package smartgrids;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Sistema SmartGrids. Guarda las plantas por nombre y delega en cada planta
 * las operaciones sobre sus monitoreos.
 *
 * Representacion: LinkedHashMap nombre -> planta. Busca por nombre en O(1)
 * y recorre las plantas en el orden en que se registraron.
 *
 * IREP:
 * 1. plantas != null.
 * 2. Ningun valor de plantas es null.
 * 3. Para toda clave k de plantas: plantas[k].nombre().equals(k)
 *    (no hay dos plantas con el mismo nombre).
 */
public class SmartGrids implements ISmartGrids {

    //private LinkedHashMap<String, Planta> plantas;

    public SmartGrids() {
        //plantas = new LinkedHashMap<>();
    }

    // ---------------------------------------------------- auxiliares

    // private Planta buscar(String nombre) {
    //     // Planta planta = plantas.get(nombre);
    //     // if (planta == null) {
    //     //     throw new IllegalArgumentException("La planta no existe: " + nombre);
    //     // }
    //     // return planta;
    // }

    // private void agregar(Planta planta) {
    //     // if (plantas.containsKey(planta.nombre())) {
    //     //     throw new IllegalArgumentException("Ya existe una planta con el nombre: " + planta.nombre());
    //     // }
    //     // plantas.put(planta.nombre(), planta);
    // }

    private static LocalDateTime fechaHora(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException("La fecha y hora es obligatoria");
        }
        try {
            return LocalDateTime.parse(texto);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha y hora invalida (yyyy-MM-ddTHH:mm:ss): " + texto, e);
        }
    }

    private static LocalDate fecha(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        try {
            return LocalDate.parse(texto);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha invalida (yyyy-MM-dd): " + texto, e);
        }
    }

    @Override
    public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarPlantaSolar'");
    }

    @Override
    public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarPlantaEolica'");
    }

    @Override
    public void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            int cantidadTurbinas, double capacidadEmbalse) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarPlantaHidraulica'");
    }

    @Override
    public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String tipoCombustible, double objetivoEmisionesCo2PorHora,
            double capacidadAlmacenamientoCombustible) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarPlantaTermica'");
    }

    @Override
    public List<String> consultarPlantas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consultarPlantas'");
    }

    @Override
    public double capacidadMinimaCuota(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'capacidadMinimaCuota'");
    }

    @Override
    public void activarPlanta(String nombre, String fechaHora) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'activarPlanta'");
    }

    @Override
    public void desactivarPlanta(String nombre, String fechaHora) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'desactivarPlanta'");
    }

    @Override
    public boolean estaActiva(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'estaActiva'");
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarMonitoreo'");
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
            double velocidadViento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarMonitoreo'");
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
            double co2Emitido, double combustibleUtilizado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarMonitoreo'");
    }

    @Override
    public double totalGenerado(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'totalGenerado'");
    }

    @Override
    public int[] incidenciasPorHora(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'incidenciasPorHora'");
    }

    @Override
    public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'histogramaCumplimiento'");
    }

    @Override
    public Map<String, Integer> plantasRenovablesPorFuente() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'plantasRenovablesPorFuente'");
    }

    @Override
    public List<String> plantasQueNuncaCumplieronCuota() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'plantasQueNuncaCumplieronCuota'");
    }

    @Override
    public String horasSinVelocidadMinimaPorPlantaEolica() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'horasSinVelocidadMinimaPorPlantaEolica'");
    }

    @Override
    public int limpiarMonitoreosSinDatos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'limpiarMonitoreosSinDatos'");
    }

    @Override
    public List<String> plantasOrdenadasPorEnergiaGenerada() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'plantasOrdenadasPorEnergiaGenerada'");
    }

    @Override
    public List<String> plantasInactivasEn(String fechaHora) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'plantasInactivasEn'");
    }

    // ------------------------------------------------- 1.a) registro

    // @Override
    // public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima,
    //         String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
    //         double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
    //     agregar(new PlantaSolar(nombre, direccion, capacidadMaxima,
    //             fechaHora(fechaHoraPuestaEnFuncionamiento), fuentePrimaria, eficienciaPromedio,
    //             superficieCubierta, cantidadPaneles, horasRadiacionDiaria));
    // }

    // @Override
    // public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima,
    //         String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
    //         int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
    //     agregar(new PlantaEolica(nombre, direccion, capacidadMaxima,
    //             fechaHora(fechaHoraPuestaEnFuncionamiento), fuentePrimaria, eficienciaPromedio,
    //             cantidadAerogeneradores, alturaTorres, velocidadMinimaViento));
    // }

    // @Override
    // public void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
    //         String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
    //         int cantidadTurbinas, double capacidadEmbalse) {
    //     agregar(new PlantaHidraulica(nombre, direccion, capacidadMaxima,
    //             fechaHora(fechaHoraPuestaEnFuncionamiento), fuentePrimaria, eficienciaPromedio,
    //             cantidadTurbinas, capacidadEmbalse));
    // }

    // @Override
    // public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima,
    //         String fechaHoraPuestaEnFuncionamiento, String tipoCombustible,
    //         double objetivoEmisionesCo2PorHora, double capacidadAlmacenamientoCombustible) {
    //     agregar(new PlantaTermica(nombre, direccion, capacidadMaxima,
    //             fechaHora(fechaHoraPuestaEnFuncionamiento), tipoCombustible,
    //             objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible));
    // }

    // ------------------------------------------------- 1.b) consulta

    // @Override
    // public List<String> consultarPlantas() {
    //     List<String> detalles = new ArrayList<>();
    //     for (Planta planta : plantas.values()) {
    //         detalles.add(planta.detalle());
    //     }
    //     return detalles;
    // }

    // ---------------------------------------------------------- 2)

    // @Override
    // public double capacidadMinimaCuota(String nombre) {
    //     return buscar(nombre).cuotaEsperada();
    // }

    // ---------------------------------------------------------- 3)

    // @Override
    // public void activarPlanta(String nombre, String fechaHora) {
    //     buscar(nombre).activar(fechaHora(fechaHora));
    // }

    // @Override
    // public void desactivarPlanta(String nombre, String fechaHora) {
    //     buscar(nombre).desactivar(fechaHora(fechaHora));
    // }

    // @Override
    // public boolean estaActiva(String nombre) {
    //     return buscar(nombre).estaActiva();
    // }

    // ---------------------------------------------------------- 4)

    // @Override
    // public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada,
    //         int incidencias) {
    //     buscar(nombre).registrarMonitoreo(fechaHora(fechaHora), energiaGenerada, incidencias);
    // }

    // @Override
    // public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada,
    //         int incidencias, double velocidadViento) {
    //     buscar(nombre).registrarMonitoreoEolico(fechaHora(fechaHora), energiaGenerada,
    //             incidencias, velocidadViento);
    // }

    // @Override
    // public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada,
    //         int incidencias, double co2Emitido, double combustibleUtilizado) {
    //     buscar(nombre).registrarMonitoreoTermico(fechaHora(fechaHora), energiaGenerada,
    //             incidencias, co2Emitido, combustibleUtilizado);
    // }

    // ------------------------------------------------------ 5) a 7)

    // @Override
    // public double totalGenerado(String nombre) {
    //     return buscar(nombre).totalGenerado();
    // }

    // @Override
    // public int[] incidenciasPorHora(String nombre) {
    //     return buscar(nombre).incidenciasPorHora();
    // }

    // @Override
    // public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
    //     Planta planta = buscar(nombre);
    //     return planta.histograma(fecha(fechaDesde), fecha(fechaHasta));
    // }

    // ---------------------------------------------------------- 8)

    // @Override
    // public Map<String, Integer> plantasRenovablesPorFuente() {
    //     TreeMap<String, Integer> porFuente = new TreeMap<>();
    //     for (Planta planta : plantas.values()) {
    //         if (planta.esRenovable()) {
    //             porFuente.merge(planta.fuentePrimaria(), 1, Integer::sum);
    //         }
    //     }
    //     return porFuente;
    // }

    // ---------------------------------------------------------- 9)

    // @Override
    // public List<String> plantasQueNuncaCumplieronCuota() {
    //     List<String> nombres = new ArrayList<>();
    //     for (Planta planta : plantas.values()) {
    //         if (planta.nuncaCumplioCuota()) {
    //             nombres.add(planta.nombre());
    //         }
    //     }
    //     return nombres;
    // }

    // --------------------------------------------------------- 10)

    // @Override
    // public String horasSinVelocidadMinimaPorPlantaEolica() {
    //     StringBuilder sb = new StringBuilder("Plantas Eolicas: Horas con poco viento\n");
    //     for (Planta planta : plantas.values()) {
    //         sb.append(planta.detalleHorasConPocoViento());
    //     }
    //     return sb.toString();
    // }

    // --------------------------------------------------------- 11)

    // @Override
    // public int limpiarMonitoreosSinDatos() {
    //     int eliminados = 0;
    //     for (Planta planta : plantas.values()) {
    //         eliminados += planta.limpiarMonitoreosSinDatos();
    //     }
    //     return eliminados;
    // }

    // --------------------------------------------------------- 12)

    // @Override
    // public List<String> plantasOrdenadasPorEnergiaGenerada() {
    //     List<Planta> ordenadas = new ArrayList<>(plantas.values());
    //     // List.sort es estable: ante empates se mantiene el orden de registro.
    //     ordenadas.sort(Comparator.comparingDouble(Planta::totalGenerado).reversed());
    //     List<String> resultado = new ArrayList<>();
    //     for (Planta planta : ordenadas) {
    //         resultado.add(planta.nombre() + ": " + planta.totalGenerado());
    //     }
    //     return resultado;
    // }

    // --------------------------------------------------------- 13)

    // @Override
    // public List<String> plantasInactivasEn(String fechaHora) {
    //     LocalDateTime momento = fechaHora(fechaHora);
    //     List<String> nombres = new ArrayList<>();
    //     for (Planta planta : plantas.values()) {
    //         if (planta.estuvoInactivaEn(momento)) {
    //             nombres.add(planta.nombre());
    //         }
    //     }
    //     return nombres;
    // }

    // ------------------------------------------------------ toString

    // @Override
    // public String toString() {
    //     StringBuilder sb = new StringBuilder();
    //     sb.append("SmartGrids - plantas registradas: ").append(plantas.size()).append('\n');
    //     if (plantas.isEmpty()) {
    //         sb.append("  (sin plantas)\n");
    //     }
    //     for (Planta planta : plantas.values()) {
    //         sb.append("  * ").append(planta);
    //     }
    //     return sb.toString();
    // }
}
