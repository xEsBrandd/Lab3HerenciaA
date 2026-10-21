package Model;

public class MantenimientoAireAcondicionado
        extends OrdenDeServicio {

    private final int capacidadBTU;

    public MantenimientoAireAcondicionado(
            String codigo,
            String nombreCliente,
            String direccion,
            double horasEstimadas,
            boolean esMetropolitana,
            int capacidadBTU) {

        super(
                codigo,
                nombreCliente,
                direccion,
                horasEstimadas,
                esMetropolitana
        );

        if (!esMetropolitana) {
            throw new IllegalArgumentException(
                    "El mantenimiento solo admite zona metropolitana.");
        }

        if (capacidadBTU < 9000 || capacidadBTU > 60000) {
            throw new IllegalArgumentException(
                    "Los BTU deben estar entre 9000 y 60000.");
        }

        this.capacidadBTU = capacidadBTU;
    }

    @Override
    public double calcularTotal() {
        return 100 + 110 * getHorasEstimadas() + 80;
    }

    @Override
    public int calcularDiasAtencion() {
        return 2;
    }

    @Override
    public int obtenerMaximoVisitas() {
        return 1;
    }

    @Override
    public String obtenerCategoria() {
        return "Mantenimiento de aire acondicionado";
    }

    @Override
    public boolean esCompatible(Cuadrilla cuadrilla) {
        return cuadrilla != null && cuadrilla.estaCertificadaEnRefrigeracion();
    }
}