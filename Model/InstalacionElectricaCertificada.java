package Model;

public class InstalacionElectricaCertificada extends Instalacion {

    private final double valorMateriales;

    public InstalacionElectricaCertificada(
            String codigo,
            String nombreCliente,
            String direccion,
            double horasEstimadas,
            boolean esMetropolitana,
            int cantidadPuntos,
            double valorMateriales) {

        super(
                codigo,
                nombreCliente,
                direccion,
                horasEstimadas,
                esMetropolitana,
                cantidadPuntos
        );

        positivo(valorMateriales, "Valor de materiales");

        this.valorMateriales = valorMateriales;
    }

    @Override
    public double calcularTotal() {
        double certificado = 120;
        double garantia = valorMateriales * 0.05;

        return super.calcularTotal() + certificado + garantia;
    }

    @Override
    public int calcularDiasAtencion() {
        return super.calcularDiasAtencion() + 2;
    }

    @Override
    public int obtenerMaximoVisitas() {
        return 2;
    }

    @Override
    public String obtenerCategoria() {
        return "Instalación eléctrica certificada";
    }
}