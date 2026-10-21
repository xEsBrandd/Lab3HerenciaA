package Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

public class SistemaTecniHogar {

    private final ArrayList<OrdenDeServicio> ordenes;
    private final ArrayList<Cuadrilla> cuadrillas;
    private final ArrayList<Jornada> jornadas;
    private final ArrayList<MovimientoIngreso> ingresos;

    public SistemaTecniHogar() {
        ordenes = new ArrayList<>();
        cuadrillas = new ArrayList<>();
        jornadas = new ArrayList<>();
        ingresos = new ArrayList<>();
    }

    public void cargarDatosIniciales() {
        if (!ordenes.isEmpty()
                || !cuadrillas.isEmpty()
                || !jornadas.isEmpty()
                || !ingresos.isEmpty()) {

            throw new IllegalStateException(
                    "Los datos iniciales solo se cargan en un sistema vacío.");
        }

        // Se agregan directamente para no generar ingresos iniciales.
        ordenes.add(new Diagnostico(
                "D01", "Ana López", "Zona 1", 1, true));

        ordenes.add(new Diagnostico(
                "D02", "Luis Pérez", "Villa Nueva", 1.5, false));

        ordenes.add(new Instalacion(
                "I01", "María Díaz", "Zona 10", 2.5, true, 5));

        ordenes.add(new Instalacion(
                "I02", "Carlos Ruiz", "Amatitlán", 2.2, false, 1));

        ordenes.add(new InstalacionElectricaCertificada(
                "E01", "Sofía Paz", "Zona 7",
                2.5, true, 5, 2000));

        ordenes.add(new InstalacionElectricaCertificada(
                "E02", "Pedro León", "Mixco",
                1.2, false, 2, 1000));

        ordenes.add(new MantenimientoAireAcondicionado(
                "M01", "Elena Soto", "Zona 4",
                1.5, true, 9000));

        ordenes.add(new MantenimientoAireAcondicionado(
                "M02", "Diego Sol", "Zona 14",
                2, true, 60000));

        cuadrillas.add(new Cuadrilla("C01", 8, true));
        cuadrillas.add(new Cuadrilla("C02", 6, false));
        cuadrillas.add(new Cuadrilla("C03", 4, true));
    }

    public void registrarOrden(OrdenDeServicio orden) {
        if (orden == null) {
            throw new IllegalArgumentException(
                    "Debe proporcionar una orden.");
        }

        for (OrdenDeServicio registrada : ordenes) {
            if (registrada.getCodigo().equals(orden.getCodigo())) {
                throw new IllegalArgumentException(
                        "El código de orden ya existe.");
            }
        }

        if (orden.getEstado() != EstadoOrden.PENDIENTE
                || orden.getCantidadVisitas() != 0) {

            throw new IllegalArgumentException(
                    "Solo se pueden registrar órdenes nuevas.");
        }

        // Se valida el cobro antes de modificar las listas.
        MovimientoIngreso cobro = new MovimientoIngreso(
                orden,
                ConceptoIngreso.REGISTRO,
                orden.calcularTotal()
        );

        ordenes.add(orden);
        ingresos.add(cobro);
    }

    public void registrarCuadrilla(Cuadrilla cuadrilla) {
        if (cuadrilla == null) {
            throw new IllegalArgumentException(
                    "Debe proporcionar una cuadrilla.");
        }

        for (Cuadrilla registrada : cuadrillas) {
            if (registrada.getCodigo().equals(cuadrilla.getCodigo())) {
                throw new IllegalArgumentException(
                        "El código de cuadrilla ya existe.");
            }
        }

        cuadrillas.add(cuadrilla);
    }

    private OrdenDeServicio buscarOrden(String codigo) {
        String clave = OrdenDeServicio.textoValido(
                codigo, "Código de orden");

        for (OrdenDeServicio orden : ordenes) {
            if (orden.getCodigo().equals(clave)) {
                return orden;
            }
        }

        throw new IllegalArgumentException(
                "No existe la orden " + clave + ".");
    }

    private Cuadrilla buscarCuadrilla(String codigo) {
        String clave = OrdenDeServicio.textoValido(
                codigo, "Código de cuadrilla");

        for (Cuadrilla cuadrilla : cuadrillas) {
            if (cuadrilla.getCodigo().equals(clave)) {
                return cuadrilla;
            }
        }

        throw new IllegalArgumentException(
                "No existe la cuadrilla " + clave + ".");
    }

    private Jornada buscarJornada(String codigo) {
        String clave = OrdenDeServicio.textoValido(
                codigo, "Código de jornada");

        for (Jornada jornada : jornadas) {
            if (jornada.getCodigo().equals(clave)) {
                return jornada;
            }
        }

        throw new IllegalArgumentException(
                "No existe la jornada " + clave + ".");
    }

    public Jornada crearJornada(
            String codigo,
            String codigoCuadrilla) {

        String clave = OrdenDeServicio.textoValido(
                codigo, "Código de jornada");

        for (Jornada jornada : jornadas) {
            if (jornada.getCodigo().equals(clave)) {
                throw new IllegalArgumentException(
                        "El código de jornada ya existe.");
            }
        }

        Cuadrilla cuadrilla = buscarCuadrilla(codigoCuadrilla);

        for (Jornada jornada : jornadas) {
            if (jornada.getCuadrilla() == cuadrilla
                    && !jornada.estaCerrada()) {

                throw new IllegalStateException(
                        "La cuadrilla ya tiene una jornada sin cerrar.");
            }
        }

        Jornada nueva = new Jornada(clave, cuadrilla);
        jornadas.add(nueva);

        return nueva;
    }

    public void asignarOrden(
            String codigoJornada,
            String codigoOrden) {

        Jornada jornada = buscarJornada(codigoJornada);
        OrdenDeServicio orden = buscarOrden(codigoOrden);

        jornada.asignarOrden(orden);
    }

    public void retirarOrden(
            String codigoJornada,
            String codigoOrden) {

        Jornada jornada = buscarJornada(codigoJornada);
        OrdenDeServicio orden = buscarOrden(codigoOrden);

        jornada.retirarOrden(orden);
    }

    public void iniciarJornada(String codigoJornada) {
        Jornada jornada = buscarJornada(codigoJornada);

        // Primero se validan todas las condiciones.
        jornada.validarInicio();

        ArrayList<MovimientoIngreso> cargos = new ArrayList<>();

        for (OrdenDeServicio orden : jornada.getOrdenes()) {
            if (orden.getCantidadVisitas() >= 1) {
                cargos.add(new MovimientoIngreso(
                        orden,
                        ConceptoIngreso.VISITA_ADICIONAL,
                        40
                ));
            }
        }

        // Los cargos solo se guardan cuando inicia la jornada.
        jornada.iniciar();
        ingresos.addAll(cargos);
    }

    public void registrarResultado(
            String codigoJornada,
            String codigoOrden,
            ResultadoVisita resultado) {

        Jornada jornada = buscarJornada(codigoJornada);
        jornada.registrarResultado(codigoOrden, resultado);
    }

    public ArrayList<OrdenDeServicio> consultarOrdenes() {
        return new ArrayList<>(ordenes);
    }

    public ArrayList<Cuadrilla> consultarCuadrillas() {
        return new ArrayList<>(cuadrillas);
    }

    public ArrayList<Jornada> consultarJornadasAbiertas() {
        ArrayList<Jornada> abiertas = new ArrayList<>();

        for (Jornada jornada : jornadas) {
            if (!jornada.estaCerrada()) {
                abiertas.add(jornada);
            }
        }

        return abiertas;
    }

    private ArrayList<String> categorias() {
        ArrayList<String> categorias = new ArrayList<>();

        for (OrdenDeServicio orden : ordenes) {
            String categoria = orden.obtenerCategoria();

            if (!categorias.contains(categoria)) {
                categorias.add(categoria);
            }
        }

        return categorias;
    }

    public String contarOrdenesPorCategoriaYEstado() {
        StringBuilder texto = new StringBuilder();

        for (String categoria : categorias()) {
            texto.append(categoria).append(":\n");

            for (EstadoOrden estado : EstadoOrden.values()) {
                int cantidad = 0;

                for (OrdenDeServicio orden : ordenes) {
                    if (orden.obtenerCategoria().equals(categoria)
                            && orden.getEstado() == estado) {

                        cantidad++;
                    }
                }

                texto.append("  ")
                        .append(estado)
                        .append(": ")
                        .append(cantidad)
                        .append("\n");
            }
        }

        if (texto.length() == 0) {
            return "No hay órdenes.";
        }

        return texto.toString();
    }

    public double calcularIngresoPorConcepto(
            ConceptoIngreso concepto) {

        if (concepto == null) {
            throw new IllegalArgumentException(
                    "Debe indicar un concepto.");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (MovimientoIngreso ingreso : ingresos) {
            if (ingreso.getConcepto() == concepto) {
                total = total.add(
                        BigDecimal.valueOf(ingreso.getMonto()));
            }
        }

        return total.doubleValue();
    }

    public double calcularIngresoTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (MovimientoIngreso ingreso : ingresos) {
            total = total.add(
                    BigDecimal.valueOf(ingreso.getMonto()));
        }

        return total.doubleValue();
    }

    public String calcularIngresosPorCategoria() {
        StringBuilder texto = new StringBuilder();

        for (String categoria : categorias()) {
            BigDecimal total = BigDecimal.ZERO;

            for (MovimientoIngreso ingreso : ingresos) {
                String categoriaOrden =
                        ingreso.getOrden().obtenerCategoria();

                if (categoriaOrden.equals(categoria)) {
                    total = total.add(
                            BigDecimal.valueOf(ingreso.getMonto()));
                }
            }

            texto.append(String.format(
                    Locale.US,
                    "%s: Q%.2f%n",
                    categoria,
                    total
            ));
        }

        if (texto.length() == 0) {
            return "No hay ingresos.";
        }

        return texto.toString();
    }
}