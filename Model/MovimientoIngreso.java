package Model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MovimientoIngreso {

    private final OrdenDeServicio orden;
    private final ConceptoIngreso concepto;
    private final double monto;

    public MovimientoIngreso(
            OrdenDeServicio orden,
            ConceptoIngreso concepto,
            double monto) {

        if (orden == null || concepto == null) {
            throw new IllegalArgumentException(
                    "La orden y el concepto son obligatorios.");
        }

        OrdenDeServicio.positivo(monto, "Monto");

        this.orden = orden;
        this.concepto = concepto;

        this.monto = BigDecimal.valueOf(monto)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public OrdenDeServicio getOrden() {
        return orden;
    }

    public ConceptoIngreso getConcepto() {
        return concepto;
    }

    public double getMonto() {
        return monto;
    }
}