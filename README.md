# Rolgar II - TP2 - Algoritmos y Estructuras de Datos
**Cátedra Ing. Gustavo Schmidt**
**Materia:** CB100 - Algoritmos y Estructuras de Datos
**Cuatrimestre:** 2do Cuatrimestre 2025

## 🎯 Objetivo del Proyecto

Este repositorio contiene el desarrollo del **Trabajo Práctico 2 (TP2): Rolgar II**. El objetivo es expandir el juego desarrollado en el TP1 a una versión multijugador (hasta $N$ jugadores) sobre un tablero tridimensional ($X \times Y \times Z$).

El proyecto introduce nuevas mecánicas de juego, incluyendo un sistema de cartas con poderes especiales y la posibilidad de formar alianzas temporales entre los jugadores.

## ✨ Características Principales

### 🧊 Tablero y Entorno
* **Mundo 3D:** El juego se desarrolla en un tablero de dimensiones $X \times Y \times Z$ (ingresado por teclado o fijo).
* **Implementación:** El tablero **debe estar implementado utilizando listas**.
* **Terreno:** El tablero no es uniforme. Puede contener casilleros de **Roca** (no transitables), **rampas**, **agua** y **espacios vacíos**.
* **Visión Limitada:** Un jugador no puede ver todo el mapa. La visibilidad está restringida a las **26 casillas vecinas en 3D** (adyacentes en los 3 ejes).

### 🧑‍🤝‍🧑 Jugadores, Enemigos y Combate
* **Multijugador:** El juego soporta hasta $N$ personajes (jugadores) en simultáneo.
* **Atributos del Personaje:** Cada personaje se define por:
    1.  Nombre
    2.  Vida (energía)
    3.  Posición (posX, posY, posZ)
    4.  Poderes (colección de cartas)
    5.  Visión (cantidad de casilleros)
    6.  Fuerza (daño de ataque)
    7.  Salud (% de recuperación por turno)
* **Enemigos:** Se colocarán $M$ enemigos de forma aleatoria en el tablero.
* **Combate:** La pelea se inicia cuando un jugador se encuentra en la misma celda que un enemigo u otro jugador. El alumno debe definir una forma de pelea.
* **Regeneración:** El personaje recupera un porcentaje de su vida (definido por el atributo "Salud") al pasar los turnos.

### 🃏 Cartas y Alianzas
* **Cartas de Poder:** Existen 8 tipos de cartas predefinidas + 2 definidas por el grupo. Ejemplos:
    * Aumento de vida
    * Ataque doble
    * Escudo (reduce daño)
    * Teletransportación
    * Robo de carta
    * Invisibilidad (1 turno)
* **Inventario:** Las cartas aparecen aleatoriamente en casillas vacías y se recogen al entrar en la celda. El inventario tiene un **máximo de 10 cartas**.
* **Alianzas:** Los jugadores pueden formar y romper alianzas temporales. Los aliados pueden:
    * Compartir información de posiciones.
    * Ayudarse en combate.
    * Intercambiar cartas de poder.

### 🏆 Condiciones de Victoria
* Un jugador gana si elimina a todos los enemigos y queda como el último jugador en pie.
* Se puede definir una victoria compartida si una alianza sobrevive sin rivales.
* Se deben definir reglas sobre qué pasa con las pertenencias de un jugador eliminado.

## 🛠️ Requerimientos Técnicos y de Interfaz
* **Interfaz de Usuario:** Toda la interfaz debe ser **basada en texto**.
* **Menú:** El menú debe ser sencillo. No es necesario limpiar la pantalla; se puede reimprimir cada vez.
* **Gráficos:** "Toda la parte grafica del juego se maneja con bitmaps".
* **Movimiento:** El control del personaje se realiza con las siguientes teclas:
    * `W`: Atrás (y-1)
    * `S`: Adelante (y+1)
    * `A`: Izquierda (x-1)
    * `D`: Derecha (x+1)
    * Se deben definir teclas para los 4 movimientos diagonales.

## 📜 Normas de Programación (Apéndice A)

Todo el código en este repositorio debe adherirse estrictamente a las siguientes normas:

1.  **Nomenclatura**:
    * **Clases/Structs:** `PascalCase` (Ej: `CentralTelefonica`). Los nombres deben ser simples y descriptivos.
    * **Métodos/Variables:** `camelCase` (Ej: `arrancarCoche`, `padronElectoral`). Los métodos deben ser verbos de preferencia.
    * **Constantes:** `UPPER_SNAKE_CASE` (Ej: `COLOR_BASE`, `ANCHO`).
2.  **Rutas:** Utilizar **siempre rutas relativas**, nunca absolutas.
3.  **Modularización:** El código debe estar modularizado. No entregar 1 o 2 archivos; separar cada clase en sus propios archivos.
4.  **Comentarios:** Todos los tipos, métodos y funciones deben tener sus comentarios correspondientes.
5.  **Variables Globales:** Está **prohibido** el uso de variables globales.
6.  **Bloques de Control:** Utilizar siempre llaves `{}` para los bloques de control (if, for, while, etc.), incluso si contienen una sola línea.

## 👥 Integrantes del Grupo

* **Nombre del Grupo:** [Nombre De Grupo]
* **Integrante 1:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 2:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 3:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 4:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 5:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 6:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 7:** [Nombre y Apellido] - [Padrón] - [Email]
*(El TP es grupal, para 7 personas)*

## 📋 Checklist de Entrega

La fecha de entrega vence el **viernes 08/11/25 a las 23:59hs**.

El entregable es un único archivo `Nombre De Grupo-TP2.zip` que debe contener (todo en un mismo PDF, excepto los fuentes):

* [ ] Archivos fuentes (sin binarios).
* [ ] Datos personales (padrón, nombre, mail).
* [ ] Informe del trabajo realizado.
* [ ] Respuestas al Cuestionario:
    1.  ¿Qué es un svn?
    2.  ¿Que es una "Ruta absoluta" o una "Ruta relativa"?
    3.  ¿Qué es git?
* [ ] Manual del Usuario.
* [ ] Manual del Programador.
* [ ] Archivos de prueba (si se requieren).

