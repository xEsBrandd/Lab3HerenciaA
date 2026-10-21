package View;

import Model.ResultadoVisita;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.concurrent.CancellationException;

import javax.swing.*;

public class Vista1 extends JFrame {

    private final JButton btnGenerar =
            new JButton("Generar cotización");

    private final JButton btnRegistrarOrden =
            new JButton("Registrar orden");

    private final JButton btnRegistrarCuadrilla =
            new JButton("Registrar cuadrilla");

    private final JButton btnCrearJornada =
            new JButton("Crear jornada");

    private final JButton btnAsignarOrden =
            new JButton("Asignar orden");

    private final JButton btnRetirarOrden =
            new JButton("Retirar orden");

    private final JButton btnIniciarJornada =
            new JButton("Iniciar jornada");

    private final JButton btnRegistrarResultado =
            new JButton("Registrar resultado");

    private final JButton btnConsultarOrdenes =
            new JButton("Consultar órdenes");

    private final JButton btnConsultarCuadrillas =
            new JButton("Consultar cuadrillas");

    private final JButton btnReportes =
            new JButton("Reportes");

    private final JTextArea txtResultado = new JTextArea();

    public Vista1() {
        super("TecniHogar — Servicios técnicos");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(750, 500));
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(
                new BorderLayout(15, 15));

        principal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel(
                "TecniHogar | Gestión de servicios");

        titulo.setFont(
                new Font(Font.SANS_SERIF, Font.BOLD, 22));

        principal.add(titulo, BorderLayout.NORTH);

        JPanel botones = new JPanel(
                new GridLayout(0, 1, 5, 5));

        botones.add(btnGenerar);
        botones.add(btnRegistrarOrden);
        botones.add(btnRegistrarCuadrilla);
        botones.add(btnCrearJornada);
        botones.add(btnAsignarOrden);
        botones.add(btnRetirarOrden);
        botones.add(btnIniciarJornada);
        botones.add(btnRegistrarResultado);
        botones.add(btnConsultarOrdenes);
        botones.add(btnConsultarCuadrillas);
        botones.add(btnReportes);

        principal.add(botones, BorderLayout.WEST);

        txtResultado.setEditable(false);
        txtResultado.setFont(
                new Font(Font.MONOSPACED, Font.PLAIN, 13));

        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);

        JScrollPane desplazamiento =
                new JScrollPane(txtResultado);

        desplazamiento.setBorder(
                BorderFactory.createTitledBorder("Resultados"));

        principal.add(desplazamiento, BorderLayout.CENTER);

        principal.add(
                new JLabel(
                        "Los datos se conservan solo mientras "
                        + "el programa está abierto."),
                BorderLayout.SOUTH
        );

        setContentPane(principal);
    }

    public void mostrarVentana() {
        setVisible(true);
    }

    public void mostrarResultado(String texto) {
        txtResultado.setText(texto);
        txtResultado.setCaretPosition(0);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "TecniHogar",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Revisar datos",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public String pedirTexto(String mensaje) {
        while (true) {
            String texto = JOptionPane.showInputDialog(
                    this,
                    mensaje,
                    "TecniHogar",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (texto == null) {
                throw new CancellationException();
            }

            if (!texto.trim().isEmpty()) {
                return texto.trim();
            }

            mostrarError("El dato no puede estar vacío.");
        }
    }

    public int pedirEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(pedirTexto(mensaje));
            } catch (NumberFormatException e) {
                mostrarError("Ingresa un número entero válido.");
            }
        }
    }

    public double pedirDecimal(String mensaje) {
        while (true) {
            try {
                String texto = pedirTexto(mensaje).replace(',', '.');
                double numero = Double.parseDouble(texto);

                if (Double.isFinite(numero)) {
                    return numero;
                }
            } catch (NumberFormatException e) {
                // Se vuelve a solicitar el dato.
            }

            mostrarError(
                    "Ingresa un número finito, "
                    + "sin separadores de miles.");
        }
    }

    public boolean pedirConfirmacion(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                mensaje,
                "TecniHogar",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.CANCEL_OPTION
                || respuesta == JOptionPane.CLOSED_OPTION) {

            throw new CancellationException();
        }

        return respuesta == JOptionPane.YES_OPTION;
    }

    public int elegirCategoria() {
        String[] opciones = {
                "Diagnóstico",
                "Instalación",
                "Instalación eléctrica certificada",
                "Mantenimiento de aire acondicionado"
        };

        Object seleccion = JOptionPane.showInputDialog(
                this,
                "Selecciona el servicio:",
                "Categoría",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == null) {
            throw new CancellationException();
        }

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccion)) {
                return i + 1;
            }
        }

        throw new CancellationException();
    }

    public ResultadoVisita elegirResultado() {
        ResultadoVisita seleccion =
                (ResultadoVisita) JOptionPane.showInputDialog(
                        this,
                        "Resultado de la visita:",
                        "Resultado",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        ResultadoVisita.values(),
                        ResultadoVisita.COMPLETADA
                );

        if (seleccion == null) {
            throw new CancellationException();
        }

        return seleccion;
    }

    public void conectarControlador(ActionListener oyente) {
        JButton[] botones = {
                btnGenerar,
                btnRegistrarOrden,
                btnRegistrarCuadrilla,
                btnCrearJornada,
                btnAsignarOrden,
                btnRetirarOrden,
                btnIniciarJornada,
                btnRegistrarResultado,
                btnConsultarOrdenes,
                btnConsultarCuadrillas,
                btnReportes
        };

        String[] acciones = {
                "GENERAR",
                "REGISTRAR_ORDEN",
                "REGISTRAR_CUADRILLA",
                "CREAR_JORNADA",
                "ASIGNAR",
                "RETIRAR",
                "INICIAR",
                "RESULTADO",
                "ORDENES",
                "CUADRILLAS",
                "REPORTES"
        };

        for (int i = 0; i < botones.length; i++) {
            botones[i].setActionCommand(acciones[i]);
            botones[i].addActionListener(oyente);
        }
    }
}