package Model;

public class Cuadrilla {

    private final String codigo;
    private final double capacidadHoras;
    private final boolean certificadaRefrigeracion;

    public Cuadrilla(
            String codigo,
            double capacidadHoras,
            boolean certificadaRefrigeracion) {

        this.codigo = OrdenDeServicio.textoValido(
                codigo, "Código de cuadrilla");

        OrdenDeServicio.positivo(
                capacidadHoras, "Capacidad de horas");

        this.capacidadHoras = capacidadHoras;
        this.certificadaRefrigeracion = certificadaRefrigeracion;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getCapacidadHoras() {
        return capacidadHoras;
    }

    public boolean estaCertificadaEnRefrigeracion() {
        return certificadaRefrigeracion;
    }
}