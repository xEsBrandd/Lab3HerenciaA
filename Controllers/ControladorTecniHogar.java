package Controllers;

import Model.*;
import View.Vista1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.concurrent.CancellationException;

public class ControladorTecniHogar implements ActionListener {

    private final SistemaTecniHogar sistema;
    private final Vista1 vista;

    public ControladorTecniHogar(
            SistemaTecniHogar sistema,
            Vista1 vista) {

        this.sistema = sistema;
        this.vista = vista;

        vista.conectarControlador(this);
    }

    public void iniciar() {
        sistema.cargarDatosIniciales();

        vista.mostrarResultado(
                "Bienvenido a TecniHogar.\n\n"
                + "Se cargaron 8 órdenes pendientes y 3 cuadrillas.\n"
                + "Ingresos iniciales: Q0.00.\n\n"
                + "Selecciona una operación a la izquierda."
        );

        vista.mostrarVentana();
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        try {
            switch (evento.getActionCommand()) {
                case "GENERAR":
                    generarCotizacion();
                    break;

                case "REGISTRAR_ORDEN":
                    registrarOrden();
                    break;

                case "REGISTRAR_CUADRILLA":
                    registrarCuadrilla();
                    break;

                case "CREAR_JORNADA":
                    crearJornada();
                    break;

                case "ASIGNAR":
                    asignarOrden();
                    break;

                case "RETIRAR":
                    retirarOrden();
                    break;

                case "INICIAR":
                    iniciarJornada();
                    break;

                case "RESULTADO":
                    registrarResultado();
                    break;

                case "ORDENES":
                    consultarOrdenes();
                    break;

                case "CUADRILLAS":
                    consultarCuadrillas();
                    break;

                case "REPORTES":
                    mostrarReportes();
                    break;

                default:
                    vista.mostrarError("Operación desconocida.");
            }

        } catch (CancellationException e) {
            vista.mostrarMensaje(
                    "Operación cancelada. No se realizaron cambios.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private OrdenDeServicio leerDatosServicio() {
        int categoria = vista.elegirCategoria();

        String codigo = vista.pedirTexto("Código de orden:");
        String cliente = vista.pedirTexto("Nombre del cliente:");
        String direccion = vista.pedirTexto("Dirección:");
        double horas = vista.pedirDecimal("Horas estimadas:");

        boolean metro = vista.pedirConfirmacion(
                "¿La zona es metropolitana?\n"
                + "Sí: metropolitana. No: periférica."
        );

        // Solo se selecciona qué objeto construir.
        // Las reglas particulares están en las clases del modelo.
        switch (categoria) {
            case 1:
                return new Diagnostico(
                        codigo,
                        cliente,
                        direccion,
                        horas,
                        metro
                );

            case 2:
                return new Instalacion(
                        codigo,
                        cliente,
                        direccion,
                        horas,
                        metro,
                        vista.pedirEntero("Cantidad de puntos:")
                );

            case 3:
                return new InstalacionElectricaCertificada(
                        codigo,
                        cliente,
                        direccion,
                        horas,
                        metro,
                        vista.pedirEntero("Cantidad de puntos:"),
                        vista.pedirDecimal("Valor de materiales (Q):")
                );

            case 4:
                return new MantenimientoAireAcondicionado(
                        codigo,
                        cliente,
                        direccion,
                        horas,
                        metro,
                        vista.pedirEntero("Capacidad en BTU:")
                );

            default:
                throw new IllegalArgumentException(
                        "Categoría inválida.");
        }
    }

    private void generarCotizacion() {
        OrdenDeServicio orden = leerDatosServicio();
        double costo = orden.calcularTotal();

        if (!Double.isFinite(costo) || costo <= 0) {
            throw new IllegalArgumentException(
                    "Costo fuera de rango.");
        }

        vista.mostrarResultado(String.format(
                Locale.US,
                "COTIZACIÓN\n\n"
                        + "Servicio: %s\n"
                        + "Cliente: %s\n"
                        + "Costo: Q%.2f\n"
                        + "Atención: %d días hábiles.\n\n"
                        + "No se registró la orden ni se generaron ingresos.",
                orden.obtenerCategoria(),
                orden.getNombreCliente(),
                costo,
                orden.calcularDiasAtencion()
        ));
    }

    private void registrarOrden() {
        OrdenDeServicio orden = leerDatosServicio();
        double costo = orden.calcularTotal();

        if (!Double.isFinite(costo) || costo <= 0) {
            throw new IllegalArgumentException(
                    "Costo fuera de rango.");
        }

        boolean acepta = vista.pedirConfirmacion(String.format(
                Locale.US,
                "Costo: Q%.2f. Atención: %d días hábiles.\n"
                        + "¿Acepta registrar y cobrar la orden?",
                costo,
                orden.calcularDiasAtencion()
        ));

        if (!acepta) {
            return;
        }

        sistema.registrarOrden(orden);

        vista.mostrarResultado(String.format(
                Locale.US,
                "ORDEN REGISTRADA\n\n"
                        + "Código: %s\n"
                        + "Cobro: Q%.2f\n"
                        + "Estado: PENDIENTE\n"
                        + "Visitas: 0",
                orden.getCodigo(),
                costo
        ));
    }

    private void registrarCuadrilla() {
        String codigo = vista.pedirTexto(
                "Código de cuadrilla:");

        double horas = vista.pedirDecimal(
                "Capacidad máxima de horas:");

        boolean certificada = vista.pedirConfirmacion(
                "¿Está certificada en refrigeración?");

        Cuadrilla cuadrilla = new Cuadrilla(
                codigo,
                horas,
                certificada
        );

        sistema.registrarCuadrilla(cuadrilla);

        vista.mostrarResultado(
                "Cuadrilla " + codigo + " registrada correctamente.");
    }

    private void crearJornada() {
        String codigo = vista.pedirTexto(
                "Código de la nueva jornada:");

        String cuadrilla = vista.pedirTexto(
                "Código de cuadrilla:");

        sistema.crearJornada(codigo, cuadrilla);

        vista.mostrarResultado(
                "Jornada " + codigo
                + " creada en preparación con la cuadrilla "
                + cuadrilla + ".");
    }

    private void asignarOrden() {
        String jornada = vista.pedirTexto(
                "Código de jornada:");

        String orden = vista.pedirTexto(
                "Código de orden:");

        sistema.asignarOrden(jornada, orden);

        vista.mostrarResultado(
                "Orden " + orden + " asignada a " + jornada
                + ".\nNo se generaron cobros.");
    }

    private void retirarOrden() {
        String jornada = vista.pedirTexto(
                "Código de jornada:");

        String orden = vista.pedirTexto(
                "Código de orden:");

        sistema.retirarOrden(jornada, orden);

        vista.mostrarResultado(
                "Orden " + orden + " retirada.\n"
                + "Estado: PENDIENTE.\n"
                + "Sin cargos adicionales.");
    }

    private void iniciarJornada() {
        String codigo = vista.pedirTexto(
                "Código de jornada:");

        double antes = sistema.calcularIngresoPorConcepto(
                ConceptoIngreso.VISITA_ADICIONAL);

        sistema.iniciarJornada(codigo);

        double despues = sistema.calcularIngresoPorConcepto(
                ConceptoIngreso.VISITA_ADICIONAL);

        double cargo = despues - antes;

        vista.mostrarResultado(String.format(
                Locale.US,
                "Jornada %s iniciada.\n"
                        + "Órdenes en atención.\n"
                        + "Cargos por visitas adicionales: Q%.2f",
                codigo,
                cargo
        ));
    }

    private void registrarResultado() {
        String jornada = vista.pedirTexto(
                "Código de jornada:");

        String orden = vista.pedirTexto(
                "Código de orden:");

        ResultadoVisita resultado = vista.elegirResultado();

        sistema.registrarResultado(
                jornada,
                orden,
                resultado
        );

        vista.mostrarResultado(
                "Resultado registrado: " + resultado + ".\n"
                + "Si era el último resultado, la jornada quedó cerrada.\n"
                + "Consulta las órdenes para ver su estado actualizado.");
    }

    private void consultarOrdenes() {
        StringBuilder texto = new StringBuilder("ÓRDENES\n\n");

        for (OrdenDeServicio orden : sistema.consultarOrdenes()) {
            texto.append(String.format(
                    Locale.US,
                    "%s | %s\n"
                            + "Cliente: %s\n"
                            + "Dirección: %s\n"
                            + "Zona: %s\n"
                            + "Horas estimadas: %.2f\n"
                            + "Estado: %s\n"
                            + "Visitas: %d/%d\n"
                            + "Costo del servicio: Q%.2f\n"
                            + "Atención: %d días hábiles\n\n",
                    orden.getCodigo(),
                    orden.obtenerCategoria(),
                    orden.getNombreCliente(),
                    orden.getDireccion(),
                    orden.esMetropolitana()
                            ? "Metropolitana" : "Periférica",
                    orden.getHorasEstimadas(),
                    orden.getEstado(),
                    orden.getCantidadVisitas(),
                    orden.obtenerMaximoVisitas(),
                    orden.calcularTotal(),
                    orden.calcularDiasAtencion()
            ));
        }

        vista.mostrarResultado(texto.toString());
    }

    private void consultarCuadrillas() {
        StringBuilder texto = new StringBuilder("CUADRILLAS\n\n");

        for (Cuadrilla cuadrilla : sistema.consultarCuadrillas()) {
            texto.append(String.format(
                    Locale.US,
                    "%s | Capacidad: %.2f horas | Refrigeración: %s\n",
                    cuadrilla.getCodigo(),
                    cuadrilla.getCapacidadHoras(),
                    cuadrilla.estaCertificadaEnRefrigeracion()
                            ? "Sí" : "No"
            ));
        }

        vista.mostrarResultado(texto.toString());
    }

    private void mostrarReportes() {
        StringBuilder texto = new StringBuilder(
                "ÓRDENES POR CATEGORÍA Y ESTADO\n\n");

        texto.append(
                sistema.contarOrdenesPorCategoriaYEstado());

        texto.append(String.format(
                Locale.US,
                "\nINGRESOS\n"
                        + "Registro: Q%.2f\n"
                        + "Visitas adicionales: Q%.2f\n"
                        + "Total: Q%.2f\n\n",
                sistema.calcularIngresoPorConcepto(
                        ConceptoIngreso.REGISTRO),
                sistema.calcularIngresoPorConcepto(
                        ConceptoIngreso.VISITA_ADICIONAL),
                sistema.calcularIngresoTotal()
        ));

        texto.append(
                sistema.calcularIngresosPorCategoria());

        texto.append("\nJORNADAS ABIERTAS\n");

        if (sistema.consultarJornadasAbiertas().isEmpty()) {
            texto.append("No hay jornadas abiertas.\n");
        }

        for (Jornada jornada : sistema.consultarJornadasAbiertas()) {
            texto.append(String.format(
                    Locale.US,
                    "%s | Cuadrilla: %s | "
                            + "Horas asignadas: %.2f | Disponibles: %.2f\n",
                    jornada.getCodigo(),
                    jornada.getCuadrilla().getCodigo(),
                    jornada.calcularHorasAsignadas(),
                    jornada.calcularCapacidadDisponible()
            ));

            for (OrdenDeServicio orden : jornada.getOrdenes()) {
                texto.append("  Orden: ")
                        .append(orden.getCodigo())
                        .append("\n");
            }
        }

        vista.mostrarResultado(texto.toString());
    }
}