package Model;

public class Instalacion extends OrdenDeServicio {

    private final int cantidadPuntos;

    public Instalacion(
            String codigo,
            String nombreCliente,
            String direccion,
            double horasEstimadas,
            boolean esMetropolitana,
            int cantidadPuntos) {

        super(
                codigo,
                nombreCliente,
                direccion,
                horasEstimadas,
                esMetropolitana
        );

        if (cantidadPuntos <= 0) {
            throw new IllegalArgumentException(
                    "Los puntos deben ser enteros positivos.");
        }

        this.cantidadPuntos = cantidadPuntos;
    }

    protected double calcularHorasFacturables() {
        double horasPorPuntos = cantidadPuntos * 0.75;

        double horas = Math.max(
                getHorasEstimadas(),
                horasPorPuntos
        );

        return Math.ceil(horas);
    }

    @Override
    public double calcularTotal() {
        double tarifaBase = esMetropolitana() ? 100 : 175;

        return tarifaBase + 90 * calcularHorasFacturables();
    }

    @Override
    public int calcularDiasAtencion() {
        return esMetropolitana() ? 3 : 5;
    }

    @Override
    public int obtenerMaximoVisitas() {
        return 3;
    }

    @Override
    public String obtenerCategoria() {
        return "Instalación";
    }
}