package View;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Model.Cuadrilla;
import Model.Jornada;
import Model.OrdenDeServicio;
import Model.ResultadoVisita;

public class VistaConsola {

    private final Scanner entrada;

    public VistaConsola() {
        entrada = new Scanner(System.in);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);

            if (!entrada.hasNextLine()) {
                throw new NoSuchElementException("Fin de entrada.");
            }

            String texto = entrada.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            mostrarMensaje("El dato no puede estar vacío.");
        }
    }

    public int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                mostrarMensaje("Ingresa un número entero válido.");
            }
        }
    }

    public double leerDecimal(String mensaje) {
        while (true) {
            try {
                String texto = leerTexto(mensaje).replace(',', '.');
                double valor = Double.parseDouble(texto);

                if (Double.isFinite(valor)) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // Se vuelve a solicitar el dato.
            }

            mostrarMensaje(
                    "Ingresa un decimal válido y finito, "
                    + "sin separadores de miles.");
        }
    }

    public boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje + " (s/n): ");

            if (respuesta.equalsIgnoreCase("s")) {
                return true;
            }

            if (respuesta.equalsIgnoreCase("n")) {
                return false;
            }

            mostrarMensaje("Escribe s o n.");
        }
    }

    public int mostrarMenu() {
        mostrarMensaje("\n===== TECNIHOGAR =====");
        mostrarMensaje("1. Cotizar un servicio");
        mostrarMensaje("2. Registrar una orden");
        mostrarMensaje("3. Registrar una cuadrilla");
        mostrarMensaje("4. Crear una jornada");
        mostrarMensaje("5. Asignar una orden");
        mostrarMensaje("6. Retirar una orden");
        mostrarMensaje("7. Iniciar una jornada");
        mostrarMensaje("8. Registrar resultado de visita");
        mostrarMensaje("9. Consultar órdenes");
        mostrarMensaje("10. Consultar cuadrillas");
        mostrarMensaje("11. Reportes");
        mostrarMensaje("0. Salir");

        return leerEntero("Opción: ");
    }

    public int elegirCategoria() {
        mostrarMensaje("\nCategorías:");
        mostrarMensaje("1. Diagnóstico");
        mostrarMensaje("2. Instalación");
        mostrarMensaje("3. Instalación eléctrica certificada");
        mostrarMensaje("4. Mantenimiento de aire acondicionado");

        while (true) {
            int opcion = leerEntero("Categoría: ");

            if (opcion >= 1 && opcion <= 4) {
                return opcion;
            }

            mostrarMensaje("Selecciona una categoría de 1 a 4.");
        }
    }

    public ResultadoVisita leerResultado() {
        while (true) {
            int opcion = leerEntero(
                    "Resultado (1 completada / 2 fallida): ");

            if (opcion == 1) {
                return ResultadoVisita.COMPLETADA;
            }

            if (opcion == 2) {
                return ResultadoVisita.FALLIDA;
            }

            mostrarMensaje("Selecciona 1 o 2.");
        }
    }

    public void mostrarCotizacion(OrdenDeServicio orden) {
        mostrarMensaje(String.format(
                Locale.US,
                "Costo: Q%.2f | Atención: %d días hábiles.",
                orden.calcularTotal(),
                orden.calcularDiasAtencion()
        ));
    }

    public void mostrarOrdenes(List<OrdenDeServicio> ordenes) {
        if (ordenes.isEmpty()) {
            mostrarMensaje("No hay órdenes.");
            return;
        }

        for (OrdenDeServicio orden : ordenes) {
            mostrarMensaje("\nCódigo: " + orden.getCodigo());
            mostrarMensaje("Servicio: " + orden.obtenerCategoria());
            mostrarMensaje("Cliente: " + orden.getNombreCliente());
            mostrarMensaje("Dirección: " + orden.getDireccion());

            mostrarMensaje("Zona: "
                    + (orden.esMetropolitana()
                    ? "Metropolitana" : "Periférica"));

            mostrarMensaje(String.format(
                    Locale.US,
                    "Horas estimadas: %.2f",
                    orden.getHorasEstimadas()
            ));

            mostrarMensaje("Estado: " + orden.getEstado());

            mostrarMensaje("Visitas: "
                    + orden.getCantidadVisitas()
                    + "/"
                    + orden.obtenerMaximoVisitas());

            mostrarMensaje(String.format(
                    Locale.US,
                    "Costo del servicio: Q%.2f",
                    orden.calcularTotal()
            ));

            mostrarMensaje(
                    "Atención estimada: "
                    + orden.calcularDiasAtencion()
                    + " días hábiles.");
        }
    }

    public void mostrarCuadrillas(List<Cuadrilla> cuadrillas) {
        if (cuadrillas.isEmpty()) {
            mostrarMensaje("No hay cuadrillas.");
            return;
        }

        for (Cuadrilla cuadrilla : cuadrillas) {
            mostrarMensaje(String.format(
                    Locale.US,
                    "%s | Capacidad: %.2f horas | Refrigeración: %s",
                    cuadrilla.getCodigo(),
                    cuadrilla.getCapacidadHoras(),
                    cuadrilla.estaCertificadaEnRefrigeracion()
                            ? "Sí" : "No"
            ));
        }
    }

    public void mostrarJornadas(List<Jornada> jornadas) {
        if (jornadas.isEmpty()) {
            mostrarMensaje("No hay jornadas abiertas.");
            return;
        }

        for (Jornada jornada : jornadas) {
            mostrarMensaje(String.format(
                    Locale.US,
                    "%s | Cuadrilla: %s | "
                            + "Asignadas: %.2f horas | "
                            + "Disponibles: %.2f horas",
                    jornada.getCodigo(),
                    jornada.getCuadrilla().getCodigo(),
                    jornada.calcularHorasAsignadas(),
                    jornada.calcularCapacidadDisponible()
            ));

            for (OrdenDeServicio orden : jornada.getOrdenes()) {
                mostrarMensaje("  Orden: " + orden.getCodigo());
            }
        }
    }

    public void mostrarIngresos(
            double registro,
            double adicionales,
            double total,
            String categorias) {

        mostrarMensaje(String.format(
                Locale.US,
                "Registro: Q%.2f%n"
                        + "Visitas adicionales: Q%.2f%n"
                        + "Total: Q%.2f%n%s",
                registro,
                adicionales,
                total,
                categorias
        ));
    }
}