# Rolgar II - TP2 - Algoritmos y Estructuras de Datos
**Cátedra Ing. Gustavo Schmidt**
[cite_start]**Materia:** CB100 - Algoritmos y Estructuras de Datos [cite: 1]
[cite_start]**Cuatrimestre:** 2do Cuatrimestre 2025 [cite: 2]

## 🎯 Objetivo del Proyecto

[cite_start]Este repositorio contiene el desarrollo del **Trabajo Práctico 2 (TP2): Rolgar II**[cite: 3]. [cite_start]El objetivo es expandir el juego desarrollado en el TP1 a una versión multijugador (hasta $N$ jugadores) sobre un tablero tridimensional ($X \times Y \times Z$)[cite: 5, 8].

[cite_start]El proyecto introduce nuevas mecánicas de juego, incluyendo un sistema de cartas con poderes especiales y la posibilidad de formar alianzas temporales entre los jugadores[cite: 6].

## ✨ Características Principales

### 🧊 Tablero y Entorno
* [cite_start]**Mundo 3D:** El juego se desarrolla en un tablero de dimensiones $X \times Y \times Z$ (ingresado por teclado o fijo)[cite: 8].
* [cite_start]**Implementación:** El tablero **debe estar implementado utilizando listas**[cite: 10].
* **Terreno:** El tablero no es uniforme. [cite_start]Puede contener casilleros de **Roca** (no transitables), **rampas**, **agua** y **espacios vacíos**[cite: 9].
* [cite_start]**Visión Limitada:** Un jugador no puede ver todo el mapa[cite: 39]. [cite_start]La visibilidad está restringida a las **26 casillas vecinas en 3D** (adyacentes en los 3 ejes)[cite: 65].

### 🧑‍🤝‍🧑 Jugadores, Enemigos y Combate
* [cite_start]**Multijugador:** El juego soporta hasta $N$ personajes (jugadores) en simultáneo[cite: 6].
* [cite_start]**Atributos del Personaje:** Cada personaje se define por[cite: 18]:
    1.  [cite_start]Nombre [cite: 19]
    2.  [cite_start]Vida (energía) [cite: 20]
    3.  [cite_start]Posición (posX, posY, posZ) [cite: 21]
    4.  [cite_start]Poderes (colección de cartas) [cite: 22]
    5.  [cite_start]Visión (cantidad de casilleros) [cite: 23, 39]
    6.  [cite_start]Fuerza (daño de ataque) [cite: 24]
    7.  [cite_start]Salud (% de recuperación por turno) [cite: 25]
* [cite_start]**Enemigos:** Se colocarán $M$ enemigos de forma aleatoria en el tablero[cite: 36].
* [cite_start]**Combate:** La pelea se inicia cuando un jugador se encuentra en la misma celda que un enemigo u otro jugador[cite: 37]. [cite_start]El alumno debe definir una forma de pelea[cite: 37].
* [cite_start]**Regeneración:** El personaje recupera un porcentaje de su vida (definido por el atributo "Salud") al pasar los turnos[cite: 25, 38].

### 🃏 Cartas y Alianzas
* [cite_start]**Cartas de Poder:** Existen 8 tipos de cartas predefinidas + 2 definidas por el grupo[cite: 43]. [cite_start]Ejemplos[cite: 44]:
    * [cite_start]Aumento de vida [cite: 45]
    * [cite_start]Ataque doble [cite: 46]
    * [cite_start]Escudo (reduce daño) [cite: 47]
    * [cite_start]Teletransportación [cite: 48]
    * [cite_start]Robo de carta [cite: 49]
    * [cite_start]Invisibilidad (1 turno) [cite: 51]
* [cite_start]**Inventario:** Las cartas aparecen aleatoriamente en casillas vacías [cite: 53] [cite_start]y se recogen al entrar en la celda[cite: 54]. [cite_start]El inventario tiene un **máximo de 10 cartas**[cite: 55].
* [cite_start]**Alianzas:** Los jugadores pueden formar y romper alianzas temporales[cite: 57, 62]. Los aliados pueden:
    * [cite_start]Compartir información de posiciones[cite: 59].
    * [cite_start]Ayudarse en combate[cite: 60].
    * [cite_start]Intercambiar cartas de poder[cite: 61].

### 🏆 Condiciones de Victoria
* [cite_start]Un jugador gana si elimina a todos los enemigos y queda como el último jugador en pie[cite: 67].
* [cite_start]Se puede definir una victoria compartida si una alianza sobrevive sin rivales[cite: 69].
* [cite_start]Se deben definir reglas sobre qué pasa con las pertenencias de un jugador eliminado[cite: 71].

## 🛠️ Requerimientos Técnicos y de Interfaz
* [cite_start]**Interfaz de Usuario:** Toda la interfaz debe ser **basada en texto**[cite: 74].
* [cite_start]**Menú:** El menú debe ser sencillo[cite: 75]. [cite_start]No es necesario limpiar la pantalla; se puede reimprimir cada vez[cite: 76].
* [cite_start]**Gráficos:** "Toda la parte grafica del juego se maneja con bitmaps"[cite: 77].
* [cite_start]**Movimiento:** El control del personaje se realiza con las siguientes teclas[cite: 28]:
    * [cite_start]`W`: Atrás (y-1) [cite: 29]
    * [cite_start]`S`: Adelante (y+1) [cite: 30]
    * [cite_start]`A`: Izquierda (x-1) [cite: 31]
    * [cite_start]`D`: Derecha (x+1) [cite: 32]
    * [cite_start]Se deben definir teclas para los 4 movimientos diagonales[cite: 33, 34].

## [cite_start]📜 Normas de Programación (Apéndice A) [cite: 85, 91]

Todo el código en este repositorio debe adherirse estrictamente a las siguientes normas:

1.  [cite_start]**Nomenclatura**[cite: 92]:
    * **Clases/Structs:** `PascalCase` (Ej: `CentralTelefonica`). [cite_start]Los nombres deben ser simples y descriptivos[cite: 93, 94].
    * **Métodos/Variables:** `camelCase` (Ej: `arrancarCoche`, `padronElectoral`). [cite_start]Los métodos deben ser verbos de preferencia[cite: 94, 95, 96].
    * [cite_start]**Constantes:** `UPPER_SNAKE_CASE` (Ej: `COLOR_BASE`, `ANCHO`)[cite: 97, 98].
2.  [cite_start]**Rutas:** Utilizar **siempre rutas relativas**, nunca absolutas[cite: 100].
3.  **Modularización:** El código debe estar modularizado. [cite_start]No entregar 1 o 2 archivos; separar cada clase en sus propios archivos[cite: 103].
4.  [cite_start]**Comentarios:** Todos los tipos, métodos y funciones deben tener sus comentarios correspondientes[cite: 102].
5.  [cite_start]**Variables Globales:** Está **prohibido** el uso de variables globales[cite: 104].
6.  [cite_start]**Bloques de Control:** Utilizar siempre llaves `{}` para los bloques de control (if, for, while, etc.), incluso si contienen una sola línea[cite: 105].

## 👥 Integrantes del Grupo

* [cite_start]**Nombre del Grupo:** [Nombre De Grupo] [cite: 84, 87]
* **Integrante 1:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 2:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 3:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 4:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 5:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 6:** [Nombre y Apellido] - [Padrón] - [Email]
* **Integrante 7:** [Nombre y Apellido] - [Padrón] - [Email]

## 📋 Checklist de Entrega

[cite_start]La fecha de entrega vence el **viernes 08/11/25 a las 23:59hs**[cite: 89].

[cite_start]El entregable es un único archivo `Nombre De Grupo-TP2.zip` [cite: 87] [cite_start]que debe contener (todo en un mismo PDF, excepto los fuentes)[cite: 88]:

* [cite_start][ ] Archivos fuentes (sin binarios)[cite: 88].
* [cite_start][ ] Datos personales (padrón, nombre, mail)[cite: 88].
* [cite_start][ ] Informe del trabajo realizado[cite: 88, 101].
* [cite_start][ ] Respuestas al Cuestionario[cite: 88, 79]:
    1.  [cite_start]¿Qué es un svn? [cite: 80]
    2.  [cite_start]¿Que es una "Ruta absoluta" o una "Ruta relativa"? [cite: 81]
    3.  [cite_start]¿Qué es git? [cite: 82]
* [cite_start][ ] Manual del Usuario[cite: 88, 101].
* [cite_start][ ] Manual del Programador[cite: 88, 101].
* [cite_start][ ] Archivos de prueba (si se requieren)[cite: 99].