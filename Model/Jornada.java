package Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jornada {

    private final String codigo;
    private final Cuadrilla cuadrilla;
    private final List<OrdenDeServicio> ordenes;
    private final Map<String, ResultadoVisita> resultados;

    private EstadoJornada estado;

    public Jornada(String codigo, Cuadrilla cuadrilla) {
        this.codigo = OrdenDeServicio.textoValido(
                codigo, "Código de jornada");

        if (cuadrilla == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una cuadrilla.");
        }

        this.cuadrilla = cuadrilla;
        this.ordenes = new ArrayList<>();
        this.resultados = new HashMap<>();
        this.estado = EstadoJornada.EN_PREPARACION;
    }

    private void exigirEstado(EstadoJornada esperado) {
        if (estado != esperado) {
            throw new IllegalStateException(
                    "La jornada " + codigo + " está " + estado + ".");
        }
    }

    // Evita errores de suma decimal al comprobar la capacidad.
    private BigDecimal horasAsignadas() {
        BigDecimal total = BigDecimal.ZERO;

        for (OrdenDeServicio orden : ordenes) {
            total = total.add(
                    BigDecimal.valueOf(orden.getHorasEstimadas()));
        }

        return total;
    }

    void asignarOrden(OrdenDeServicio orden) {
        exigirEstado(EstadoJornada.EN_PREPARACION);

        if (orden == null
                || orden.getEstado() != EstadoOrden.PENDIENTE
                || !orden.tieneVisitasDisponibles()) {

            throw new IllegalStateException(
                    "La orden debe estar pendiente y tener visitas disponibles.");
        }

        if (!orden.esCompatible(cuadrilla)) {
            throw new IllegalArgumentException(
                    "La cuadrilla no es compatible con este servicio.");
        }

        BigDecimal nuevasHoras = horasAsignadas().add(
                BigDecimal.valueOf(orden.getHorasEstimadas()));

        BigDecimal capacidad = BigDecimal.valueOf(
                cuadrilla.getCapacidadHoras());

        if (nuevasHoras.compareTo(capacidad) > 0) {
            throw new IllegalArgumentException(
                    "La asignación supera la capacidad de la cuadrilla.");
        }

        orden.marcarAsignada();
        ordenes.add(orden);
    }

    void retirarOrden(OrdenDeServicio orden) {
        exigirEstado(EstadoJornada.EN_PREPARACION);

        if (!ordenes.contains(orden)) {
            throw new IllegalArgumentException(
                    "La orden no pertenece a esta jornada.");
        }

        orden.retirarAsignacion();
        ordenes.remove(orden);
    }

    void validarInicio() {
        exigirEstado(EstadoJornada.EN_PREPARACION);

        if (ordenes.isEmpty()) {
            throw new IllegalStateException(
                    "No se puede iniciar una jornada vacía.");
        }

        for (OrdenDeServicio orden : ordenes) {
            if (orden.getEstado() != EstadoOrden.ASIGNADA
                    || !orden.tieneVisitasDisponibles()
                    || !orden.esCompatible(cuadrilla)) {

                throw new IllegalStateException(
                        "Una orden no cumple las condiciones de inicio.");
            }
        }
    }

    void iniciar() {
        validarInicio();

        for (OrdenDeServicio orden : ordenes) {
            orden.iniciarVisita();
        }

        estado = EstadoJornada.EN_CURSO;
    }

    void registrarResultado(
            String codigoOrden,
            ResultadoVisita resultado) {

        exigirEstado(EstadoJornada.EN_CURSO);

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "Debe indicar un resultado.");
        }

        String clave = OrdenDeServicio.textoValido(
                codigoOrden, "Código de orden");

        if (resultados.containsKey(clave)) {
            throw new IllegalStateException(
                    "Esta visita ya tiene un resultado registrado.");
        }

        OrdenDeServicio encontrada = null;

        for (OrdenDeServicio orden : ordenes) {
            if (orden.getCodigo().equals(clave)) {
                encontrada = orden;
                break;
            }
        }

        if (encontrada == null) {
            throw new IllegalArgumentException(
                    "La orden no pertenece a esta jornada.");
        }

        encontrada.registrarResultado(resultado);
        resultados.put(clave, resultado);

        if (resultados.size() == ordenes.size()) {
            estado = EstadoJornada.CERRADA;
        }
    }

    public double calcularHorasAsignadas() {
        return horasAsignadas().doubleValue();
    }

    public double calcularCapacidadDisponible() {
        BigDecimal capacidad = BigDecimal.valueOf(
                cuadrilla.getCapacidadHoras());

        return capacidad.subtract(horasAsignadas()).doubleValue();
    }

    public boolean estaCerrada() {
        return estado == EstadoJornada.CERRADA;
    }

    public String getCodigo() {
        return codigo;
    }

    public Cuadrilla getCuadrilla() {
        return cuadrilla;
    }

    public List<OrdenDeServicio> getOrdenes() {
        return Collections.unmodifiableList(
                new ArrayList<>(ordenes));
    }
}