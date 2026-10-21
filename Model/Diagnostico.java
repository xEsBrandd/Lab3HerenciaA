package Model;

public class Diagnostico extends OrdenDeServicio {

    public Diagnostico(
            String codigo,
            String nombreCliente,
            String direccion,
            double horasEstimadas,
            boolean esMetropolitana) {

        super(
                codigo,
                nombreCliente,
                direccion,
                horasEstimadas,
                esMetropolitana
        );

        if (horasEstimadas > 1.5) {
            throw new IllegalArgumentException(
                    "El diagnóstico admite hasta 1.5 horas.");
        }
    }

    @Override
    public double calcularTotal() {
        return esMetropolitana() ? 150 : 225;
    }

    @Override
    public int calcularDiasAtencion() {
        return esMetropolitana() ? 1 : 2;
    }

    @Override
    public int obtenerMaximoVisitas() {
        return 3;
    }

    @Override
    public String obtenerCategoria() {
        return "Diagnóstico";
    }
}