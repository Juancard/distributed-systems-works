# TP: N° 1

## Table of Contents
1. [Sockets](#1-sockets-simple-connection)
2. [Sockets: with threads](#2-sockets-with-threads)
3. [Sockets: Message Server](#3-sockets-message-server)
4. [Sockets: Message Server with Ack](#3-sockets-message-server-with-ack)
5. [RMI: Weather Service](#3-rmi-weather-service)
6. [RMI: Parameters passing](#3-rmi-parameters-passing)
7. [RMI: Message Server](#3-rmi-message-server)
8. [RMI: Generic Tasks Solver](#3-rmi-generic-tasks-solver)


### 1. Sockets: simple connection
- "Escriba un servidor que usando sockets, reciba un mensaje de texto y lo repita a su cliente. Programe también el cliente para verificar y probar el comportamiento del servidor. Realice la implementación en TCP y comente los resultados".

- [Code](tp1.Ex01)

### 2. Sockets: with Threads

- "Modifique el programa anterior para que pueda atender varios clientes a la vez".

- [Code](tp1.Ex02)

### 3. Sockets: Message Server

- "Escriba un servidor de mensajes en colas, que permita a los clientes dejar un mensaje (identificando de alguna forma a quién se lo dejan), y bajar los mensajes que le están dirigidos. La comunicación entre cliente y servidor debe ser mediante sockets, y el servidor debe poder atender varios clientes a la vez".

- [Code](tp1.Ex03)

### 4. Sockets: Message Server with Ack

- "Modifique el programa anterior para que el mensaje de la cola sea borrado por el servidor una vez que el cliente confirma, mediante un mensaje de tipo ack, que efectivamente recibió el mensaje que estaba en la cola.".

- [Code](tp1.Ex04)

### 5. RMI: Weather Service

- "Escribir un servicio que devuelva información de clima del lugar donde reside el servidor. Esta información podrá generarse de forma aleatoria. Deberá ser realizado con RMI. Para ello considere la interface remota, la clase (lado servidor) que implementa esa interface, el servidor, y un cliente que permita probar este funcionamiento".

- [Code](tp1.Ex05)

### 6. RMI: Parameters Passing

- "Escribir un servidor utilizando RMI, que ofrezca la posibilidad de sumar y restar vectores de enteros. Introduzca un error en su código que modifique los vectores  recibidos por parámetro. Qué impacto se genera? Que conclusión saca sobre la forma de pasaje de parámetros en RMI?".

### 7. RMI: Message Server

- "Implemente el servidor del ejercicio 3, utilizando ahora RMI".

### 8. RMI: Generic Tasks Solver

- "Implemente un servidor RMI que resuelva tareas genéricas. Para ello tener en cuenta la interface Tarea, 	que tendra un método ejecutar(). El objetivo es que desde el cliente se puedan escribir objetos (que implementen la interface Tarea) que hagan un cálculo concreto (calcular un número aleatorio, un primo, el valor de Pi con cierta precisión, etc), y que esa tarea se pase al servidor RMI, quien hará el cálculo y devolverá el valor al cliente".
