# TP: N° 1

## Table of Contents
1. [Sockets](#1-sockets-simple-connection)
2. [Sockets: with threads](#2-sockets-with-threads)
3. [Third Example](#3-sockets-message-server)

### 1. Sockets: simple connection
- "Escriba un servidor que usando sockets, reciba un mensaje de texto y lo repita a su cliente. Programe también el cliente para verificar y probar el comportamiento del servidor. Realice la implementación en TCP y comente los resultados".

- [Code](tp1.Ex01)

### 2. Sockets: with threads

- "Modifique el programa anterior para que pueda atender varios clientes a la vez".

- [Code](tp1.Ex02)

### 3. Sockets: message server

- "Escriba un servidor de mensajes en colas, que permita a los clientes dejar un mensaje (identificando de alguna forma a quién se lo dejan), y bajar los mensajes que le están dirigidos. La comunicación entre cliente y servidor debe ser mediante sockets, y el servidor debe poder atender varios clientes a la vez".

- [Code](tp1.Ex03)

### 4. Sockets: message server with Ack

- "Modifique el programa anterior para que el mensaje de la cola sea borrado por el servidor una vez que el cliente confirma, mediante un mensaje de tipo ack, que efectivamente recibió el mensaje que estaba en la cola.".

- [Code](tp1.Ex04)

### 5. RMI: Weather Service

- "Escribir un servicio que devuelva información de clima del lugar donde reside el servidor. Esta información podrá generarse de forma aleatoria. Deberá ser realizado con RMI. Para ello considere la interface remota, la clase (lado servidor) que implementa esa interface, el servidor, y un cliente que permita probar este funcionamiento".

- [Code](tp1.Ex05)
