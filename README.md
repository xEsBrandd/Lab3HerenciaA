# TecniHogar realizado por Brándon Endervy Fuentes Bautista Carné 26515 - POO 2026 - Santiago Solorzano

Este programa fue desarrollado en Java para el Laboratorio 3 de Herencia. Permite administrar las órdenes de servicio de TecniHogar, organizar las jornadas de sus cuadrillas y consultar los ingresos.

Desde la interfaz gráfica se pueden cotizar y registrar servicios, registrar cuadrillas, crear jornadas, asignar o retirar órdenes y registrar los resultados de las visitas. También se pueden consultar las órdenes, las cuadrillas y los reportes.

## Organización

La carpeta Model contiene las clases y las reglas de los servicios; View contiene la interfaz gráfica en el archivo Vista1.java; y Controllers conecta los botones de la ventana con las operaciones del sistema. El archivo Main.java inicia el programa prinicipall.

Estas carpetas y el archivo Main.java se encuentran directamente en la carpeta principal del proyecto.

## Cómo compilar y ejecutar

Se necesita tener instalado JDK 17 o superior. Para comprobarlo, se pueden ejecutar estos comandos en la terminal:


java -version
javac -version


Después de descargar o clonar el repositorio, se debe abrir una terminal en la carpeta principal del proyecto, donde se encuentran Model, View, Controllers y Main.java.

Primero se crea la carpeta donde se guardarán los archivos compilados. Si ya existe puede omitir lo siguiente: 


mkdir out


Luego se compila el programa agregando la siguiente línea de codigo a la terminal:


javac -encoding UTF-8 -d out Model/*.java View/*.java Controllers/*.java Main.java


Finalmente, se ejecutará el programa:


java -cp out Main


Al ejecutar esta últmima línea, se vera reflejado la interfaz gráfica de TecniHogar.
## Uso

Al iniciar, el programa carga ocho órdenes pendientes, dos por cada categoría, y tres cuadrillas. Estos datos iniciales no generan ingresos.

Para utilizarlo, se presiona el botón de la operación que se desea realizar y se ingresan los datos solicitados en los cuadros de diálogo. Las consultas y los reportes aparecen en el cuadro de resultados. Para salir, se cierra la ventana.

El botón Generar cotización muestra el costo y el tiempo estimado sin guardar la orden ni generar ingresos. Para registrar el servicio se utiliza el botón Registrar orden y se confirma el cobro. Cancelar un formulario no modifica los datos.

Las visitas posteriores a la primera generan un cargo de Q40 cuando inicia la jornada correspondiente.

Toda la información se guarda en memoria. Al cerrar el programa se pierden los cambios y, al abrirlo nuevamente, se cargan los datos iniciales. Es un programa básico que permite demostrar las reglas de los servicios y observar los resultados durante su ejecución.
