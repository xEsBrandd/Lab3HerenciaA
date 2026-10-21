import Controllers.ControladorTecniHogar;
import Model.SistemaTecniHogar;
import View.VistaConsola;

public class Main {

    public static void main(String[] args) {
        SistemaTecniHogar sistema = new SistemaTecniHogar();

        VistaConsola vista = new VistaConsola();

        ControladorTecniHogar controlador =
                new ControladorTecniHogar(sistema, vista);

        controlador.ejecutar();
    }
}