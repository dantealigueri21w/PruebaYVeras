-- Prueba y Verás — esquema de la base de datos local (SQLite vía Room), versión 1.
--
-- Transcripción del esquema que Room exporta en
-- app/schemas/pe.appmobile.pruebayveras.data.AppDatabase/1.json,
-- con los nombres de tabla ya resueltos. Toda la información vive en el
-- dispositivo: no hay servidor, ni cuentas, ni sincronización.

-- Perfil del jugador. Fila única (id = 1).
-- Nunca guarda nombre real, correo ni ningún dato que identifique al niño:
-- solo un apodo elegido por él y el número de uno de los 12 avatares locales.
CREATE TABLE IF NOT EXISTS `perfil` (
    `id`       INTEGER NOT NULL,
    `alias`    TEXT    NOT NULL,
    `avatarId` INTEGER NOT NULL,
    PRIMARY KEY(`id`)
);

-- Las 9 islas del archipiélago y su avance real.
-- `requisitoDesbloqueo` guarda el id de la isla que hay que completar antes
-- (NULL en las dos islas iniciales: Marea y Viento).
CREATE TABLE IF NOT EXISTS `isla` (
    `idIsla`              TEXT    NOT NULL,
    `nombre`              TEXT    NOT NULL,
    `fenomeno`            TEXT    NOT NULL,
    `requisitoDesbloqueo` TEXT,
    `completada`          INTEGER NOT NULL,
    PRIMARY KEY(`idIsla`)
);

-- Los 27 retos, 3 por isla (fácil/medio/difícil).
-- `variableIndependiente` es el nombre exacto de la variable que el niño debe
-- cambiar para responder la corazonada de `textoCorazonada` — el motor de
-- prueba justa (`MotorPruebaJusta`) usa este valor para verificar que el
-- montaje de prueba difiere del de control en esa única variable.
CREATE TABLE IF NOT EXISTS `reto` (
    `idReto`                TEXT    NOT NULL,
    `idIsla`                TEXT    NOT NULL,
    `dificultad`            TEXT    NOT NULL,
    `textoCorazonada`       TEXT    NOT NULL,
    `variableIndependiente` TEXT    NOT NULL,
    `completado`            INTEGER NOT NULL,
    PRIMARY KEY(`idReto`)
);

-- Cada vez que el niño corre una prueba (justa o no) queda un registro real
-- aquí. `valorControl`/`valorPrueba` van como texto porque una variable puede
-- ser un número, un booleano o una categoría (superficie lisa/rugosa, etc.).
-- De esta tabla sale la tendencia real que usa el Cuaderno de Datos.
CREATE TABLE IF NOT EXISTS `intento` (
    `id`                INTEGER NOT NULL,
    `idReto`            TEXT    NOT NULL,
    `variableCambiada`  TEXT    NOT NULL,
    `valorControl`      TEXT    NOT NULL,
    `valorPrueba`       TEXT    NOT NULL,
    `resultadoControl`  REAL    NOT NULL,
    `resultadoPrueba`   REAL    NOT NULL,
    `fueJusta`          INTEGER NOT NULL,
    `timestamp`         INTEGER NOT NULL,
    PRIMARY KEY(`id` AUTOINCREMENT)
);

-- Las 9 piezas del Chirimbolo (una por isla). Se confirma cuando el niño
-- corre al menos una prueba justa en la isla correspondiente.
CREATE TABLE IF NOT EXISTS `pieza_chirimbolo` (
    `idPieza`    TEXT    NOT NULL,
    `idIsla`     TEXT    NOT NULL,
    `nombre`     TEXT    NOT NULL,
    `confirmada` INTEGER NOT NULL,
    PRIMARY KEY(`idPieza`)
);

-- Una página por cada vez que el niño responde la pregunta de tendencia
-- (¿sube, baja o no cambia?) al terminar un reto. `tendenciaCorrecta` compara
-- lo que eligió contra la tendencia real calculada desde `intento`.
CREATE TABLE IF NOT EXISTS `pagina_cuaderno` (
    `id`                 INTEGER NOT NULL,
    `idReto`             TEXT    NOT NULL,
    `tendenciaElegida`   TEXT    NOT NULL,
    `tendenciaCorrecta`  INTEGER NOT NULL,
    `timestamp`          INTEGER NOT NULL,
    PRIMARY KEY(`id` AUTOINCREMENT)
);

-- Las 12 insignias y cuándo se ganó cada una.
CREATE TABLE IF NOT EXISTS `insignia` (
    `idInsignia`        TEXT    NOT NULL,
    `nombre`            TEXT    NOT NULL,
    `obtenida`          INTEGER NOT NULL,
    `timestampObtenida` INTEGER,
    PRIMARY KEY(`idInsignia`)
);

-- Racha de días jugados. Fila única (id = 1).
CREATE TABLE IF NOT EXISTS `racha` (
    `id`               INTEGER NOT NULL,
    `diasSeguidos`     INTEGER NOT NULL,
    `ultimoDiaJugado`  INTEGER NOT NULL,
    PRIMARY KEY(`id`)
);
