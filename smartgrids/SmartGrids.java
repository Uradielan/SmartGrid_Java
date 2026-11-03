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

    private LinkedHashMap<String, Planta> plantas = new LinkedHashMap<>();

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

        Planta planta = new PlantaSolar(nombre, direccion, capacidadMaxima,
                fechaHora(fechaHoraPuestaEnFuncionamiento), fuentePrimaria, eficienciaPromedio,
                superficieCubierta, cantidadPaneles, horasRadiacionDiaria);

        if (plantas.containsKey(planta.nombre())) {
            throw new IllegalArgumentException("Ya existe una planta con el nombre: " + planta.nombre());
        }
        plantas.put(planta.nombre(), planta);
    }

    @Override
    public void registrarPlantaEolica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            int cantidadAerogeneradores, double alturaTorres, double velocidadMinimaViento) {
        Planta planta = new PlantaEolica(nombre, direccion, capacidadMaxima,
                fechaHora(fechaHoraPuestaEnFuncionamiento), fuentePrimaria, eficienciaPromedio,
                cantidadAerogeneradores, alturaTorres, velocidadMinimaViento);
        if (plantas.containsKey(planta.nombre())) {
            throw new IllegalArgumentException("Ya existe una planta con el nombre: " + planta.nombre());
        }
        plantas.put(planta.nombre(), planta);
    }

    @Override
    public void registrarPlantaHidraulica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
            int cantidadTurbinas, double capacidadEmbalse) {
        Planta planta = new PlantaHidraulica(nombre, direccion, capacidadMaxima,
                fechaHora(fechaHoraPuestaEnFuncionamiento), fuentePrimaria, eficienciaPromedio,
                cantidadTurbinas, capacidadEmbalse);
        if (plantas.containsKey(planta.nombre())) {
            throw new IllegalArgumentException("Ya existe una planta con el nombre: " + planta.nombre());
        }
        plantas.put(planta.nombre(), planta);
    }

    @Override
    public void registrarPlantaTermica(String nombre, String direccion, double capacidadMaxima,
            String fechaHoraPuestaEnFuncionamiento, String tipoCombustible, double objetivoEmisionesCo2PorHora,
            double capacidadAlmacenamientoCombustible) {
        Planta planta = new PlantaTermica(nombre, direccion, capacidadMaxima,
                fechaHora(fechaHoraPuestaEnFuncionamiento), tipoCombustible,
                objetivoEmisionesCo2PorHora, capacidadAlmacenamientoCombustible);
        if (plantas.containsKey(planta.nombre())) {
            throw new IllegalArgumentException("Ya existe una planta con el nombre: " + planta.nombre());
        }
        plantas.put(planta.nombre(), planta);
    }

    @Override
    public List<String> consultarPlantas() {
        List<String> lista = new ArrayList<>();
        for (Planta planta : this.plantas.values()) {
            lista.add(planta.toString());
        }
        return lista;
    }

    @Override
    public double capacidadMinimaCuota(String nombre) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'capacidadMinimaCuota'");
    }

    @Override
    public void activarPlanta(String nombre, String fechaHora) {

        Planta planta = this.plantas.get(nombre);
        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe: " + nombre);
        }
        planta.cambiarEstado(true);

    }

    @Override
    public void desactivarPlanta(String nombre, String fechaHora) {

        Planta planta = this.plantas.get(nombre);
        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe: " + nombre);
        }
        planta.desactivar(fechaHora(fechaHora));
    }

    @Override
    public boolean estaActiva(String nombre) {
        Planta planta = this.plantas.get(nombre);
        return planta != null && planta.consultarEstado();
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
        Planta planta = this.plantas.get(nombre);
        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe: " + nombre);
        }
        planta.registrarMonitoreo(fechaHora(fechaHora), energiaGenerada, incidencias);
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
            double velocidadViento) {
        Planta planta = this.plantas.get(nombre);
        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe: " + nombre);
        }
        planta.registrarMonitoreoEolico(fechaHora(fechaHora), energiaGenerada, incidencias, velocidadViento);
    }

    @Override
    public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
            double co2Emitido, double combustibleUtilizado) {
        Planta planta = this.plantas.get(nombre);
        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe: " + nombre);
        }
        planta.registrarMonitoreoTermico(fechaHora(fechaHora), energiaGenerada, incidencias, co2Emitido, combustibleUtilizado);
    }

    @Override
    public double totalGenerado(String nombre) {
        Planta planta = this.plantas.get(nombre);
        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe: " + nombre);
        }
        return planta.totalGenerado();
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
        Map<String, Integer> resultado = new TreeMap<>();
        for (Planta planta : plantas.values()) {
            if (planta.esRenovable()) {
                String fuente = planta.fuentePrimaria();
                resultado.put(fuente, resultado.getOrDefault(fuente, 0) + 1);
            }
        }
        return resultado;
    }

    @Override
    public List<String> plantasQueNuncaCumplieronCuota() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'plantasQueNuncaCumplieronCuota'");
    }

    @Override
    public String horasSinVelocidadMinimaPorPlantaEolica() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plantas Eolicas: Horas con poco viento\n");
        for (Planta planta : plantas.values()) {
            if (planta.esEolica()) {
                List<Integer> horas = planta.horasSinVientoMinimo();
                if (!horas.isEmpty()) {
                    sb.append("- ").append(planta.nombre()).append(": ").append(((PlantaEolica) planta).vientoMinimo()).append(" m/s\n");
                    for (Integer hora : horas) {
                        sb.append("       ").append(String.format("%02d", hora)).append(": ")
                                .append(((PlantaEolica) planta).horasSinVientoMinimo()).append(" m/s\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    @Override
    public int limpiarMonitoreosSinDatos() {
        int totalEliminados = 0;
        for (Planta planta : plantas.values()) {
            totalEliminados += planta.limpiarMonitoreosSinDatos();
        }
        return totalEliminados;
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

    // ------------------------------------------------------ toString

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SmartGrids - plantas registradas: ").append(plantas.size()).append('\n');
        if (plantas.isEmpty()) {
            stringBuilder.append("  (sin plantas)\n");
        }
        for (Planta planta : plantas.values()) {
            stringBuilder.append("  * ").append(planta);
        }
        return stringBuilder.toString();
    }
}
