# TecniHogar realizado por Brándon Endervy Fuentes Bautista Carné 26515 - POO 2026 - Santiago Solorzano

Este programa fue desarrollado en Java para el Laboratorio 3 de Herencia. Permite administrar las órdenes de servicio de TecniHogar, organizar las jornadas de sus cuadrillas y consultar los ingresos.

Desde el menú se pueden cotizar y registrar servicios, registrar cuadrillas, crear jornadas, asignar o retirar órdenes y registrar los resultados de las visitas. También se pueden consultar las órdenes, las cuadrillas y los reportes.

## Organización

El código está dentro de la carpeta src. La carpeta Model contiene las clases y las reglas de los servicios; View contiene la interfaz de consola; y Controllers conecta las opciones del menú con las operaciones del sistema. El archivo Main.java inicia el programa.

## Cómo compilar y ejecutar

Se necesita tener instalado JDK 17 o superior. Para comprobarlo, se pueden ejecutar estos comandos en la terminal:

java -version
javac -version

Después de descargar o clonar el repositorio, se debe abrir una terminal en la carpeta principal del proyecto, donde se encuentra src.

Primero se crea la carpeta donde se guardarán los archivos compilados. Si ya existe, se puede omitir este paso:

mkdir out

Luego se compila el programa:

javac -encoding UTF-8 -d out src/modelo/*.java src/vista/*.java src/controlador/*.java src/Main.java

Finalmente, se ejecuta:


java -cp out Main


## Uso

Al iniciar, el programa carga ocho órdenes pendientes, dos por cada categoría, y tres cuadrillas. Estos datos iniciales no generan ingresos.

Para utilizarlo, se escribe el número de la opción que aparece en el menú y se ingresan los datos solicitados. Las preguntas de confirmación se responden con s o n. Para salir, se selecciona la opción 0.

Una cotización solo se registra y cobra si el cliente la acepta. Las visitas posteriores a la primera generan un cargo de Q40 cuando inicia la jornada correspondiente.
Toda la información se guarda en memoria. Al cerrar el programa se pierden los cambios y, al abrirlo nuevamente, se cargan los datos iniciales por parte de un programa básico con la intención de mostrar los resultados obtenidos durante la ejecución del mismo.