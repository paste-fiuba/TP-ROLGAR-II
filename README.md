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

Aquí tienes el contenido completo para un archivo `README.md` basado en los archivos de tu proyecto, listo para copiar y pegar.

-----

# Rolgar II

Un RPG táctico por turnos multijugador, desarrollado en Java y Swing, basado en la consigna del TP2 de Algoritmos y Estructuras de Datos.

## Descripción

**Rolgar II** es una ampliación del juego original, convirtiéndolo en una experiencia multijugador en un tablero tridimensional ($X \times Y \times Z$). De 1 a 4 jugadores pueden explorar el calabozo, luchar contra enemigos, formar alianzas temporales y utilizar un mazo de cartas con poderes especiales.

El objetivo es sobrevivir, derrotar a todos los enemigos (incluyendo al jefe final, el **REY MAGO**) y ser el último jugador o alianza en pie.

## Características Principales

  * **Mundo 3D por Niveles:** Explora un calabozo de 10 niveles de profundidad. Utiliza las **Rampas** para moverte entre los pisos.
  * **Multijugador (1-4 Jugadores):** Juega solo o con hasta 3 amigos. El juego se maneja por turnos.
  * **Combate Táctico por Turnos:** Al encontrar un enemigo o atacar a un jugador, el juego entra en una pantalla de combate modal donde eliges tus acciones: **[1] Luchar**, **[2] Carta**, **[3] Huir**.
  * **Sistema de Cartas de Poder:** Recoge cartas repartidas por el mapa para ganar ventajas. El inventario tiene un límite de 10 cartas. Las cartas implementadas incluyen:
      * Ataque Doble
      * Escudo (Vida Temporal)
      * Teletransportación
      * Curación (Parcial, Total y Aliado)
      * Robo de Carta (a otro jugador)
      * Invisibilidad
      * Doble Movimiento
      * Evasión de Daño
  * **Mecánica de Alianzas:** Propón alianzas (`[L]`) a jugadores adyacentes. Si aceptan (`[Y]`), pueden ayudarse en combate y transferirse cartas (`[T]`).

## Estilo de Arte

El juego utiliza una interfaz gráfica basada en bitmaps (pixel art) renderizada en un `JPanel` de Swing.

   

## Cómo Jugar (Controles)

El juego distingue entre los controles en el mundo (exploración) y los controles en combate.

### Controles en el Mundo (Exploración)

| Tecla | Acción |
| :--- | :--- |
| **W, A, S, D** | [cite\_start]Movimiento Ortogonal (Arriba, Izquierda, Abajo, Derecha) [cite: 522-525] |
| **Q, E, Z, C** | Movimiento Diagonal |
| **1** al **0** | **Usar Carta:** Activa la carta en el *slot* correspondiente de tu inventario. **(Gasta el turno)** |
| **ENTER** | **Finalizar Turno:** Pasa tu turno si no deseas moverte o usar una carta. |
| **F** | **Atacar Jugador:** Inicia un combate con un jugador adyacente. |
| **L** | **Proponer Alianza:** Propone una alianza a un jugador adyacente. |
| **Y** / **N** | **Aceptar / Rechazar Alianza:** Responde a una propuesta de alianza. |
| **T** + (1-0) | **Transferir Carta:** Presiona `T` y luego el número (`1` al `0`) del *slot* de la carta que quieres dar a un aliado adyacente. |
| **ESC** | **Pausar Juego** / Volver al Menú Principal. |

### Controles en Combate

Una vez que el combate inicia, los controles cambian:

| Tecla | Acción |
| :--- | :--- |
| **[1]** | **Luchar:** Realiza un ataque físico básico contra el oponente. |
| **[2]** | **Carta:** Abre el menú de inventario para seleccionar una carta y usarla en combate. |
| **[3]** | **Huir:** Intenta escapar del combate (tiene una probabilidad de fallar). |
| **ESC** | **Cancelar:** Vuelve al menú de acciones si estás en el sub-menú de cartas. |

## Arquitectura Técnica

El proyecto está desarrollado en **Java** y utiliza la biblioteca **Swing** para toda la interfaz gráfica.

Sigue una arquitectura **Modelo-Vista-Controlador (MVC)**:

  * **Modelo:** Contiene los datos y el estado del juego.

      * `com.entidades` (Personaje, Enemigo, Alianza)
      * `com.items` (Carta, Inventario)
      * `com.tablero` (Tablero, Casillero)
      * `PartidaDeRolgar` (Clase "Factory" que genera el mundo)

  * **Vista:** Se encarga del renderizado en pantalla.

      * `VentanaJuego` (El `JFrame` principal).
      * `PanelJuego` (El `JPanel` que recibe los eventos de teclado y orquesta el dibujado).
      * `RenderizadorMundo`, `RenderizadorUI`, `RenderizarData`, `RenderizarMenu`: Clases especializadas en dibujar partes específicas de la interfaz.

  * **Controlador:** Maneja el flujo del juego y la entrada del usuario.

      * `ControladorJuego`: El cerebro principal. Contiene la máquina de estados (`GameState`) que decide si el juego está en `MENU`, `RUNNING` o `EN_COMBATE`.
      * `AdministradorDeJuego`: Controlador secundario para la lógica del mundo (movimiento, turnos, alianzas).
      * `AdministradorDeCombate`: Controlador secundario para la lógica de batalla (ataques, uso de cartas en combate).

## Estructura de Paquetes

```
src/
├── com/
│   └── Main.java                 # Punto de entrada de la aplicación
├── com/entidades/
│   ├── Entidad.java              # Clase abstracta base (Personaje, Enemigo)
│   ├── Personaje.java            # Lógica del jugador (inventario, buffs)
│   ├── Enemigo.java              # Lógica de la IA (movimiento, ataque)
│   └── Alianza.java              # TDA para alianzas
├── com/items/
│   ├── Carta.java                # Clase abstracta base
│   ├── Inventario.java           # TDA para el inventario (máx 10 cartas)
│   ├── CartaAtaqueDoble.java     # ... (y todas las demás implementaciones)
│   └── ...
├── com/logica/
│   ├── ControladorJuego.java     # Controlador principal (Máquina de Estados)
│   ├── AdministradorDeJuego.java # Controlador del mundo (RUNNING)
│   ├── AdministradorDeCombate.java # Controlador de batalla (EN_COMBATE)
│   └── PartidaDeRolgar.java      # Factory para crear una nueva partida
├── com/tablero/
│   ├── Tablero.java              # TDA del mundo (List<List<List<Casillero>>>)
│   ├── Casillero.java            # Contiene un TipoCasillero y una Carta
│   └── TipoCasillero.java        # Enum (VACIO, ROCA, AGUA, RAMPA)
├── com/ui/
│   ├── VentanaJuego.java         # El JFrame principal
│   ├── PanelJuego.java           # El JPanel (oyente de teclado, paintComponent)
│   ├── RenderizadorMundo.java    # Dibuja el tablero, jugadores, enemigos
│   ├── RenderizadorUI.java       # Orquesta el dibujado de menús, combate y HUD
│   ├── RenderizarData.java       # Dibuja el HUD (hotbar, HP, log)
│   ├── RenderizarMenu.java       # Dibuja los menús de estado
│   └── RenderizarFinJuego.java   # Dibuja las pantallas de victoria/derrota
├── doc/
│   └── TP 2 - Consigna.pdf       # La consigna del proyecto
└── sprites/
    └── ... (Todos los assets .png)
```

## Cómo Compilar y Ejecutar

El proyecto es una aplicación Java estándar. Desde la raíz del directorio `src`:

1.  **Compilar:**
    ```bash
    javac com/Main.java
    ```
2.  **Ejecutar:**
    ```bash
    java com.Main
    ```

## Autores

*(Reemplaza esta sección con los nombres de los miembros de tu grupo)*

  * Nombre Apellido (Padrón)
  * Nombre Apellido (Padrón)
  * Nombre Apellido (Padrón)
  * Nombre Apellido (Padrón)
  * Nombre Apellido (Padrón)
  * Nombre Apellido (Padrón)
  * Nombre Apellido (Padrón)
