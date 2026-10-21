package Model;

public abstract class OrdenDeServicio {

    private final String codigo;
    private final String nombreCliente;
    private final String direccion;
    private final double horasEstimadas;
    private final boolean esMetropolitana;

    private EstadoOrden estado;
    private int cantidadVisitas;

    protected OrdenDeServicio(
            String codigo,
            String nombreCliente,
            String direccion,
            double horasEstimadas,
            boolean esMetropolitana) {

        this.codigo = textoValido(codigo, "Código");
        this.nombreCliente = textoValido(nombreCliente, "Cliente");
        this.direccion = textoValido(direccion, "Dirección");

        positivo(horasEstimadas, "Horas estimadas");

        this.horasEstimadas = horasEstimadas;
        this.esMetropolitana = esMetropolitana;
        this.estado = EstadoOrden.PENDIENTE;
        this.cantidadVisitas = 0;
    }

    static String textoValido(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " no puede estar vacío.");
        }

        return valor.trim();
    }

    static void positivo(double valor, String campo) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(
                    campo + " debe ser un número positivo y finito.");
        }
    }

    public abstract double calcularTotal();

    public abstract int calcularDiasAtencion();

    public abstract int obtenerMaximoVisitas();

    public abstract String obtenerCategoria();

    public boolean esCompatible(Cuadrilla cuadrilla) {
        return cuadrilla != null;
    }

    public boolean tieneVisitasDisponibles() {
        return cantidadVisitas < obtenerMaximoVisitas();
    }

    private void exigirEstado(EstadoOrden esperado) {
        if (estado != esperado) {
            throw new IllegalStateException(
                    "La orden " + codigo + " está " + estado + ".");
        }
    }

    void marcarAsignada() {
        exigirEstado(EstadoOrden.PENDIENTE);

        if (!tieneVisitasDisponibles()) {
            throw new IllegalStateException(
                    "La orden no tiene visitas disponibles.");
        }

        estado = EstadoOrden.ASIGNADA;
    }

    void retirarAsignacion() {
        exigirEstado(EstadoOrden.ASIGNADA);
        estado = EstadoOrden.PENDIENTE;
    }

    void iniciarVisita() {
        exigirEstado(EstadoOrden.ASIGNADA);

        if (!tieneVisitasDisponibles()) {
            throw new IllegalStateException(
                    "La orden no tiene visitas disponibles.");
        }

        cantidadVisitas++;
        estado = EstadoOrden.EN_ATENCION;
    }

    void registrarResultado(ResultadoVisita resultado) {
        exigirEstado(EstadoOrden.EN_ATENCION);

        if (resultado == null) {
            throw new IllegalArgumentException(
                    "Debe indicar el resultado.");
        }

        if (resultado == ResultadoVisita.COMPLETADA) {
            estado = EstadoOrden.COMPLETADA;
        } else if (tieneVisitasDisponibles()) {
            estado = EstadoOrden.PENDIENTE;
        } else {
            estado = EstadoOrden.CANCELADA;
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getHorasEstimadas() {
        return horasEstimadas;
    }

    public boolean esMetropolitana() {
        return esMetropolitana;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public int getCantidadVisitas() {
        return cantidadVisitas;
    }
}