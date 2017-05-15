# TP: N° 2

## Table of Contents
1. [File Server with Backup](#1-file-server-with-backup)
2. [Bank Server](#2-bank-server)
3. [Bank Server with Backup](#3-bank-server-with-backup)
4. [Sobel Operator](#4-sobel-operator)
5. [Sobel Operator: Handling Servers Crashes](#5-sobel-operator-handling-servers-crashes)
6. [Time Server](#6-time-server)
7. [Distributed File Directory](#7-distributed-file-directory)

### 1. File Server with Backup
- "Desarrolle un servidor de archivos, que acepte los comandos put, del,  get y dir, para poder subir un archivo, bajar un archivo, y obtener el listado de archivos existentes en el servidor, respectivamente. Deberá permitir varias conexiones en simultáneo. Este servidor deberá almacenar los archivos de forma local, y mantener una copia de los mismos en otra máquina. Para este último punto, el servidor deberá tener un hilo dedicado a comparar los dos almacenamientos, y al momento de detectar diferencias, corregirlo.

Deberá tambien registrar un log de las conexiones aceptadas, información del cliente y las acciones que realizan".

- [Code](Ex01)

### 2. Bank Server

- "Un banco tiene un proceso para realizar depósitos en cuentas, y otro para extracciones. Ambos procesos corren en simultáneo y aceptan varios clientes a la vez. El proceso que realiza un depósito tarda 40 mseg entre que consulta el saldo actual, y lo actualiza con el nuevo valor. El proceso que realiza una extracción tarda 80 mseg entre que consulta el saldo (y verifica que haya disponible) y lo actualiza con el nuevo valor.

Escribir los dos procesos y realizar pruebas con varios clientes en simultáneo, haciendo operaciones de extracción y depósito. Forzar, y mostrar cómo se logra, errores en el acceso al recurso compartido. El saldo de la cuenta puede ser simplemente un archivo de texto plano.

Escribir una segunda versión de los procesos, de forma tal que el acceso al recurso compartido esté sincronizado. Explicar y justificar qué partes se deciden modificar".

- [Code](Ex02)

### 3. Bank Server with Backup

- "Modificar el programa anterior, agregando un tercer proceso independiente, que se encarga de mantener una copia del saldo de la cuenta, en una máquina distinta a donde reside el saldo de la cuenta. Replicar el saldo de la cuenta, y así ofrecer un servicio más (otro thread) que pueda informar sin restricciones de sincronización o bloqueo el saldo de la cuenta (siempre lo informa de la réplica)".

- [Code](Ex03)

### 4. Sobel Operator

- "El operador de Sobel es una máscara que, aplicada a una imagen, permite detectar (resaltar) bordes. Este operador es una operación matemática que, aplicada a cada pixel y teniendo en cuenta los pixeles que lo rodean, obtiene un nuevo valor (color) para ese pixel. Aplicando la operación a cada pixel, se obtiene una nueva imagen que resalta los bordes.

Desarrollar un proceso que toma una imagen, aplica la máscara, y genera un nuevo archivo con el resultado. Este proceso debe partir la imagen en n pedazos, y asignar la tarea de aplicar la máscara a n procesos distribuidos. Después deberá juntar los resultados. Se sugiere implementar los procesos distribuidos usando RMI. Comentar resultados de perfomance".

- [Code](Ex04)


### 5. Sobel Operator: Handling Servers Crashes

- "Mejorar la aplicación del punto anterior para que, en caso de que un proceso distribuido (al que se le asignó parte de la imagen a procesar) se caiga y no responda, el proceso principal detecte esta situación y pida este cálculo a otro proceso".

- [Code](Ex05)


### 6. Time Server

- "En base a la necesidad de contar con un Sistema Distribuido Sincronizado, se requiere implementar un algoritmo de sincronización de relojes lógicos.  Para ello, debe existir un nodo maestro el cual basa su funcionamiento en la hora local del equipo.  Por otro lado, los clientes del sistema implementan un manejador que toma referencia de la información que proviene del reloj global del maestro. La idea es que los nodos actualicen su sincronización cada un minuto (aunque debe ser configurable)".

- [Code](Ex06)


### 7. Distributed File Directory

- "Siguiendo el ejercicio 1, se necesita agregar al mismo un servicio de Directorio Distribuido. Se requiere implementar un protocolo de comunicación y acceso a los datos que permita validar el acceso de usuarios a los archivos alojados (el sistema de archivos distribuidos).

Para ello, se debe tener registrado en una base de datos la información del perfil del usuario (usuario y permisos de acceso) y permisos sobre los diferentes recursos.  A partir de ello, la aplicación permitirá el acceso al sistema que el usuario desea loguearse.

Es requisito que los datos del directorio replicados y se requiere que la carga esté balanceada".

- [Code](Ex07)

- [Installation](Ex07/README.md)
