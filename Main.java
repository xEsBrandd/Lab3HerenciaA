import Controllers.ControladorTecniHogar;
import Model.SistemaTecniHogar;
import View.Vista1;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaTecniHogar sistema = new SistemaTecniHogar();

            Vista1 vista = new Vista1();

            ControladorTecniHogar controlador =
                    new ControladorTecniHogar(sistema, vista);

            controlador.iniciar();
        });
    }
}