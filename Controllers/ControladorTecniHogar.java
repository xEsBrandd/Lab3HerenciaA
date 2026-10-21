package Controllers;

import java.util.Locale;
import java.util.NoSuchElementException;

import Model.ConceptoIngreso;
import Model.Cuadrilla;
import Model.Diagnostico;
import Model.Instalacion;
import Model.InstalacionElectricaCertificada;
import Model.MantenimientoAireAcondicionado;
import Model.OrdenDeServicio;
import Model.ResultadoVisita;
import Model.SistemaTecniHogar;
import View.VistaConsola;

public class ControladorTecniHogar {

    private final SistemaTecniHogar sistema;
    private final VistaConsola vista;

    public ControladorTecniHogar(
            SistemaTecniHogar sistema,
            VistaConsola vista) {

        this.sistema = sistema;
        this.vista = vista;
    }

    public void ejecutar() {
        sistema.cargarDatosIniciales();

        vista.mostrarMensaje(
                "Bienvenido. Se cargaron 8 órdenes y 3 cuadrillas "
                + "sin ingresos iniciales.");

        boolean continuar = true;

        while (continuar) {
            try {
                int opcion = vista.mostrarMenu();

                switch (opcion) {
                    case 1:
                        cotizar();
                        break;

                    case 2:
                        registrarOrden();
                        break;

                    case 3:
                        registrarCuadrilla();
                        break;

                    case 4:
                        crearJornada();
                        break;

                    case 5:
                        asignarOrden();
                        break;

                    case 6:
                        retirarOrden();
                        break;

                    case 7:
                        iniciarJornada();
                        break;

                    case 8:
                        registrarResultado();
                        break;

                    case 9:
                        vista.mostrarOrdenes(
                                sistema.consultarOrdenes());
                        break;

                    case 10:
                        vista.mostrarCuadrillas(
                                sistema.consultarCuadrillas());
                        break;

                    case 11:
                        mostrarReportes();
                        break;

                    case 0:
                        continuar = false;
                        break;

                    default:
                        vista.mostrarMensaje(
                                "Opción inválida. Selecciona de 0 a 11.");
                }

            } catch (IllegalArgumentException | IllegalStateException e) {
                vista.mostrarMensaje(
                        "Operación rechazada: " + e.getMessage());

            } catch (NoSuchElementException e) {
                continuar = false;
            }
        }

        vista.mostrarMensaje("Programa finalizado.");
    }

    private OrdenDeServicio leerDatosServicio() {
        int categoria = vista.elegirCategoria();

        String codigo = vista.leerTexto("Código: ");
        String cliente = vista.leerTexto("Cliente: ");
        String direccion = vista.leerTexto("Dirección: ");

        double horas = vista.leerDecimal("Horas estimadas: ");

        boolean metropolitana = vista.leerSiNo(
                "¿Zona metropolitana?");

        // El switch solo selecciona qué objeto construir.
        // Las reglas del servicio se resuelven en sus clases.
        switch (categoria) {
            case 1:
                return new Diagnostico(
                        codigo,
                        cliente,
                        direccion,
                        horas,
                        metropolitana
                );

            case 2:
                int puntos = vista.leerEntero(
                        "Cantidad de puntos: ");

                return new Instalacion(
                        codigo,
                        cliente,
                        direccion,
                        horas,
                        metropolitana,
                        puntos
                );

            case 3:
                int puntosCertificados = vista.leerEntero(
                        "Cantidad de puntos: ");

                double materiales = vista.leerDecimal(
                        "Valor de materiales (Q): ");

                return new InstalacionElectricaCertificada(
                        codigo,
                        cliente,
                        direccion,
                        horas,
                        metropolitana,
                        puntosCertificados,
                        materiales
                );

            case 4:
                int btu = vista.leerEntero(
                        "Capacidad en BTU: ");

                return new MantenimientoAireAcondicionado(
                        codigo,
                        cliente,
                        direccion,
                        horas,
                        metropolitana,
                        btu
                );

            default:
                throw new IllegalArgumentException(
                        "Categoría inválida.");
        }
    }

    private void validarCotizacion(OrdenDeServicio orden) {
        double costo = orden.calcularTotal();

        if (!Double.isFinite(costo) || costo <= 0) {
            throw new IllegalArgumentException(
                    "El costo calculado está fuera de rango.");
        }
    }

    private void cotizar() {
        OrdenDeServicio orden = leerDatosServicio();

        validarCotizacion(orden);
        vista.mostrarCotizacion(orden);

        boolean acepta = vista.leerSiNo(
                "¿Acepta y desea registrar esta orden?");

        if (acepta) {
            sistema.registrarOrden(orden);

            vista.mostrarMensaje(
                    "Orden registrada y cobrada. Estado: PENDIENTE.");
        } else {
            vista.mostrarMensaje(
                    "Cotización finalizada sin registro ni cobro.");
        }
    }

    private void registrarOrden() {
        OrdenDeServicio orden = leerDatosServicio();

        sistema.registrarOrden(orden);
        vista.mostrarCotizacion(orden);

        vista.mostrarMensaje(
                "Orden registrada y cobrada. Estado: PENDIENTE.");
    }

    private void registrarCuadrilla() {
        String codigo = vista.leerTexto(
                "Código de cuadrilla: ");

        double capacidad = vista.leerDecimal(
                "Capacidad de horas: ");

        boolean certificada = vista.leerSiNo(
                "¿Certificada en refrigeración?");

        Cuadrilla cuadrilla = new Cuadrilla(
                codigo,
                capacidad,
                certificada
        );

        sistema.registrarCuadrilla(cuadrilla);
        vista.mostrarMensaje("Cuadrilla registrada.");
    }

    private void crearJornada() {
        vista.mostrarCuadrillas(
                sistema.consultarCuadrillas());

        String codigo = vista.leerTexto(
                "Código de nueva jornada: ");

        String codigoCuadrilla = vista.leerTexto(
                "Código de cuadrilla: ");

        sistema.crearJornada(codigo, codigoCuadrilla);

        vista.mostrarMensaje(
                "Jornada creada en preparación.");
    }

    private void asignarOrden() {
        String jornada = vista.leerTexto(
                "Código de jornada: ");

        String orden = vista.leerTexto(
                "Código de orden: ");

        sistema.asignarOrden(jornada, orden);

        vista.mostrarMensaje(
                "Orden asignada sin cobro adicional.");
    }

    private void retirarOrden() {
        String jornada = vista.leerTexto(
                "Código de jornada: ");

        String orden = vista.leerTexto(
                "Código de orden: ");

        sistema.retirarOrden(jornada, orden);

        vista.mostrarMensaje(
                "Orden retirada; vuelve a pendiente sin cobro.");
    }

    private void iniciarJornada() {
        String codigo = vista.leerTexto(
                "Código de jornada: ");

        double anterior = sistema.calcularIngresoPorConcepto(
                ConceptoIngreso.VISITA_ADICIONAL);

        sistema.iniciarJornada(codigo);

        double actual = sistema.calcularIngresoPorConcepto(
                ConceptoIngreso.VISITA_ADICIONAL);

        double cargo = actual - anterior;

        vista.mostrarMensaje(String.format(
                Locale.US,
                "Jornada iniciada. Cargos adicionales: Q%.2f",
                cargo
        ));
    }

    private void registrarResultado() {
        String jornada = vista.leerTexto(
                "Código de jornada: ");

        String orden = vista.leerTexto(
                "Código de orden: ");

        ResultadoVisita resultado = vista.leerResultado();

        sistema.registrarResultado(
                jornada,
                orden,
                resultado
        );

        vista.mostrarMensaje(
                "Resultado registrado. "
                + "Si era el último, la jornada quedó cerrada.");
    }

    private void mostrarReportes() {
        vista.mostrarMensaje(
                "\nÓRDENES POR CATEGORÍA Y ESTADO");

        vista.mostrarMensaje(
                sistema.contarOrdenesPorCategoriaYEstado());

        vista.mostrarMensaje("INGRESOS");

        vista.mostrarIngresos(
                sistema.calcularIngresoPorConcepto(
                        ConceptoIngreso.REGISTRO),

                sistema.calcularIngresoPorConcepto(
                        ConceptoIngreso.VISITA_ADICIONAL),

                sistema.calcularIngresoTotal(),

                sistema.calcularIngresosPorCategoria()
        );

        vista.mostrarMensaje("JORNADAS ABIERTAS");

        vista.mostrarJornadas(
                sistema.consultarJornadasAbiertas());
    }
}