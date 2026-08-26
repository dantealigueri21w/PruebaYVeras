-- Prueba y Verás — datos semilla que la app carga en su primer arranque.
--
-- Estos son los mismos datos que viven en
-- app/src/main/java/.../data/seed/Semilla*.kt, transcritos a SQL para poder
-- inspeccionarlos sin abrir el proyecto. La app no ejecuta este archivo: lo
-- siembra Room desde Kotlin al detectar que la tabla `isla` está vacía
-- (`CienciaLabRepository.sembrarSiEsPrimeraVez()`).

-- ---------------------------------------------------------------------------
-- Las 9 islas del archipiélago. Marea y Viento empiezan disponibles; el resto
-- se desbloquea completando la isla que indica `requisitoDesbloqueo`.
-- ---------------------------------------------------------------------------
INSERT INTO isla (idIsla, nombre, fenomeno, requisitoDesbloqueo, completada) VALUES
  ('isla_marea',   'Isla de la Marea',   'densidad y flotabilidad',       NULL,          0),
  ('isla_viento',  'Isla del Viento',    'resistencia del aire',          NULL,          0),
  ('isla_jardin',  'Isla del Jardín',    'germinación',                   'isla_marea',  0),
  ('isla_risco',   'Isla del Risco',     'fricción',                      'isla_marea',  0),
  ('isla_cueva',   'Isla de la Cueva',   'sonido y eco',                  'isla_viento', 0),
  ('isla_faro',    'Isla del Faro',      'electricidad estática',         'isla_viento', 0),
  ('isla_olas',    'Isla de las Olas',   'disolución',                    'isla_jardin', 0),
  ('isla_iman',    'Isla del Imán',      'magnetismo',                    'isla_risco',  0),
  ('isla_reflejo', 'Isla del Reflejo',   'absorción de luz y calor',      'isla_cueva',  0);

-- ---------------------------------------------------------------------------
-- Los 27 retos, 3 por isla. `variableIndependiente` es la variable que hay
-- que cambiar para responder la corazonada del texto — el motor de prueba
-- justa la usa para verificar que el montaje de prueba difiere del de
-- control en esa única variable.
-- ---------------------------------------------------------------------------
INSERT INTO reto (idReto, idIsla, dificultad, textoCorazonada, variableIndependiente, completado) VALUES
  -- Isla de la Marea
  ('reto_marea_facil',   'isla_marea',  'FACIL',   'Chirimbolo dice: "este huevo no se hunde igual en todos los charcos. ¿Será la sal?"', 'sal', 0),
  ('reto_marea_medio',   'isla_marea',  'MEDIO',   'Una isleña pregunta si el volumen de agua también cambia si el huevo flota.',          'sal', 0),
  ('reto_marea_dificil', 'isla_marea',  'DIFICIL', 'Prueba con 1, 3 y 6 cucharadas de sal y arma la tendencia completa de cuánto flota.',  'sal', 0),

  -- Isla del Viento
  ('reto_viento_facil',   'isla_viento', 'FACIL',   'Chirimbolo quiere saber si su aleta lo ayuda a caer más despacio, como un paracaídas.', 'paracaidas', 0),
  ('reto_viento_medio',   'isla_viento', 'MEDIO',   'Un isleño duda si la altura también cambia cuánto tarda en llegar al suelo.',           'paracaidas', 0),
  ('reto_viento_dificil', 'isla_viento', 'DIFICIL', 'Prueba desde tres alturas distintas y arma la tendencia del tiempo de caída.',          'altura',     0),

  -- Isla del Jardín
  ('reto_jardin_facil',   'isla_jardin', 'FACIL',   'Chirimbolo tiene una hojita que le creció sola. ¿Necesita luz para crecer más?', 'luz',  0),
  ('reto_jardin_medio',   'isla_jardin', 'MEDIO',   'Una isleña quiere saber si sin agua la hojita crece igual, con o sin luz.',      'agua', 0),
  ('reto_jardin_dificil', 'isla_jardin', 'DIFICIL', 'Prueba el crecimiento a los 2, 5 y 8 días y arma la tendencia completa.',        'dias', 0),

  -- Isla de la Cueva
  ('reto_cueva_facil',   'isla_cueva', 'FACIL',   'Chirimbolo no está seguro de si su tímpano de lata escucha bien de lejos.', 'distancia', 0),
  ('reto_cueva_medio',   'isla_cueva', 'MEDIO',   'Un isleño pregunta si una tela puesta en la pared cambia el eco.',         'material',  0),
  ('reto_cueva_dificil', 'isla_cueva', 'DIFICIL', 'Prueba a tres distancias distintas y arma la tendencia de la intensidad del eco.', 'distancia', 0),

  -- Isla del Faro
  ('reto_faro_facil',   'isla_faro', 'FACIL',   'Chirimbolo frotó su antena contra su propio brazo y ahora atrae cosas. ¿Frotar más ayuda?', 'frotadas', 0),
  ('reto_faro_medio',   'isla_faro', 'MEDIO',   'Una isleña quiere saber si la distancia a los papelitos también importa.',                  'frotadas', 0),
  ('reto_faro_dificil', 'isla_faro', 'DIFICIL', 'Prueba con 3, 9 y 15 frotadas y arma la tendencia de cuántos papelitos atrae.',              'frotadas', 0),

  -- Isla de las Olas
  ('reto_olas_facil',   'isla_olas', 'FACIL',   'Chirimbolo tiene una junta oxidada. ¿El agua tibia la afloja más rápido que la fría?', 'temperatura', 0),
  ('reto_olas_medio',   'isla_olas', 'MEDIO',   'Un isleño pregunta si la cantidad de azúcar también cambia el tiempo.',               'temperatura', 0),
  ('reto_olas_dificil', 'isla_olas', 'DIFICIL', 'Prueba con agua a 5, 20 y 40 grados y arma la tendencia del tiempo de disolución.',    'temperatura', 0),

  -- Isla del Risco
  ('reto_risco_facil',   'isla_risco', 'FACIL',   'Chirimbolo quiere saber si sus patas agarran mejor una rampa lisa o una rugosa.', 'superficie', 0),
  ('reto_risco_medio',   'isla_risco', 'MEDIO',   'Una isleña pregunta si la altura de la rampa también cambia la distancia.',       'superficie', 0),
  ('reto_risco_dificil', 'isla_risco', 'DIFICIL', 'Prueba la misma superficie en tres alturas y arma la tendencia de la distancia recorrida.', 'altura', 0),

  -- Isla del Imán
  ('reto_iman_facil',   'isla_iman', 'FACIL',   'Chirimbolo no sabe si su imán interno sigue funcionando bajo un poco de arena.', 'grosor',   0),
  ('reto_iman_medio',   'isla_iman', 'MEDIO',   'Un isleño duda si el tipo de material entre el imán y el clip también importa.', 'material', 0),
  ('reto_iman_dificil', 'isla_iman', 'DIFICIL', 'Prueba con tres grosores distintos y arma la tendencia de si el clip se mueve.',  'grosor',   0),

  -- Isla del Reflejo
  ('reto_reflejo_facil',   'isla_reflejo', 'FACIL',   'Chirimbolo se preocupa de que su placa oscura se calienta más que la clara.', 'color',   0),
  ('reto_reflejo_medio',   'isla_reflejo', 'MEDIO',   'Una isleña pregunta si el tiempo al sol también cambia cuánto se calienta.',  'color',   0),
  ('reto_reflejo_dificil', 'isla_reflejo', 'DIFICIL', 'Prueba a los 5, 10 y 20 minutos al sol y arma la tendencia de la temperatura.', 'minutos', 0);

-- ---------------------------------------------------------------------------
-- Las 9 piezas del Chirimbolo, una por isla. Se confirma cuando el niño corre
-- al menos una prueba justa en la isla correspondiente.
-- ---------------------------------------------------------------------------
INSERT INTO pieza_chirimbolo (idPieza, idIsla, nombre, confirmada) VALUES
  ('pieza_tanque_flotador', 'isla_marea',   'Tanque Flotador',   0),
  ('pieza_aleta_trasera',   'isla_viento',  'Aleta Trasera',     0),
  ('pieza_hojita',          'isla_jardin',  'Hojita',            0),
  ('pieza_timpano_lata',    'isla_cueva',   'Tímpano de Lata',   0),
  ('pieza_antena',          'isla_faro',    'Antena',            0),
  ('pieza_junta_oxidada',   'isla_olas',    'Junta Oxidada',     0),
  ('pieza_patas',           'isla_risco',   'Patas de Resorte',  0),
  ('pieza_iman_interno',    'isla_iman',    'Imán Interno',      0),
  ('pieza_placa_pecho',     'isla_reflejo', 'Placa del Pecho',   0);

-- ---------------------------------------------------------------------------
-- Las 12 insignias. Ninguna se regala: todas se derivan de acciones guardadas
-- en las tablas `intento` y `pagina_cuaderno` (ver `MotorProgreso`).
-- ---------------------------------------------------------------------------
INSERT INTO insignia (idInsignia, nombre, obtenida, timestampObtenida) VALUES
  ('insignia_primera_prueba',        'Primera Prueba',        0, NULL),
  ('insignia_ojo_de_lupa',           'Ojo de Lupa',           0, NULL),
  ('insignia_pieza_confirmada',      'Pieza Confirmada',      0, NULL),
  ('insignia_chirimbolo_completo',   'Chirimbolo Completo',   0, NULL),
  ('insignia_buen_ojo_de_datos',     'Buen Ojo de Datos',     0, NULL),
  ('insignia_marea_alta',            'Marea Alta',            0, NULL),
  ('insignia_viento_en_contra',      'Viento en Contra',      0, NULL),
  ('insignia_brote_firme',           'Brote Firme',           0, NULL),
  ('insignia_eco_certero',           'Eco Certero',           0, NULL),
  ('insignia_chispa_propia',         'Chispa Propia',         0, NULL),
  ('insignia_buen_disolvente',       'Buen Disolvente',       0, NULL),
  ('insignia_cuesta_abajo',          'Cuesta Abajo',          0, NULL);

-- ---------------------------------------------------------------------------
-- Racha de días jugados, en cero al empezar.
-- ---------------------------------------------------------------------------
INSERT INTO racha (id, diasSeguidos, ultimoDiaJugado) VALUES (1, 0, 0);
