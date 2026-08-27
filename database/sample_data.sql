-- Prueba y Verás — datos semilla que la app carga en su primer arranque.
--
-- Estos son los mismos datos que viven en
-- app/src/main/java/.../data/seed/Semilla*.kt, transcritos a SQL para poder
-- inspeccionarlos sin abrir el proyecto. La app no ejecuta este archivo: lo
-- siembra Room desde Kotlin al detectar que la tabla `isla` está vacía
-- (`CienciaLabRepository.sembrarSiEsPrimeraVez()`), y esa siembra solo toca
-- `isla`, `reto`, `pieza_chirimbolo` e `insignia`. `perfil` la crea el niño la
-- primera vez que entra a Ajustes/Perfil, y `racha` no la escribe ningún
-- código todavía (el motor `MotorProgreso.actualizarRacha` existe pero aún no
-- está conectado a la base) — por eso ninguna de las dos tiene semilla aquí.

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
-- Los 27 retos, 3 por isla (fácil/medio/difícil). `variableIndependiente` es
-- la variable que hay que cambiar para responder la corazonada del texto —
-- el motor de prueba justa (`MotorPruebaJusta`) la usa para verificar que el
-- montaje de prueba difiere del de control en esa única variable.
-- `direccionEsperada` ("SUBE", "BAJA" o "NO_CAMBIA") es la meta direccional
-- que se ve antes de tocar nada, nunca un número exacto que cazar.
-- `datoCientifico` es el dato real que se gana como tarjeta "¿Sabías que...?"
-- al cerrar el reto en una prueba justa — propio de cada reto, no una
-- recompensa genérica.
-- ---------------------------------------------------------------------------
INSERT INTO reto (idReto, idIsla, dificultad, textoCorazonada, variableIndependiente, direccionEsperada, datoCientifico, completado) VALUES
  -- Isla de la Marea — la sal sube la flotación; el volumen de agua es el señuelo
  ('reto_marea_facil', 'isla_marea', 'FACIL',
    'Chirimbolo dice: "este huevo no se hunde igual en todos los charcos. ¿Será la sal?"',
    'sal', 'SUBE',
    'Un huevo fresco se hunde en agua dulce, pero flota en agua con suficiente sal: el Mar Muerto tiene tanta sal que una persona flota sola, sin nadar.',
    0),
  ('reto_marea_medio', 'isla_marea', 'MEDIO',
    'Una isleña pregunta si el volumen de agua también cambia si el huevo flota.',
    'sal', 'SUBE',
    'La densidad del agua salada no depende de cuánta agua haya: un charco chico y una piscina con la misma sal por litro flotan el huevo exactamente igual.',
    0),
  ('reto_marea_dificil', 'isla_marea', 'DIFICIL',
    'Prueba con 1, 3 y 6 cucharadas de sal y arma la tendencia completa de cuánto flota.',
    'sal', 'SUBE',
    'Los barcos aprovechan esto al revés: en agua de mar (más densa) flotan con más carga que en un río de agua dulce.',
    0),

  -- Isla del Viento — el paracaídas frena la caída; la altura es la segunda variable real
  ('reto_viento_facil', 'isla_viento', 'FACIL',
    'Chirimbolo quiere saber si su aleta lo ayuda a caer más despacio, como un paracaídas.',
    'paracaidas', 'BAJA',
    'Un paracaídas real no detiene la caída: aumenta tanto el roce con el aire que la velocidad deja de crecer mucho antes de tocar el suelo.',
    0),
  ('reto_viento_medio', 'isla_viento', 'MEDIO',
    'Un isleño duda si la altura también cambia cuánto tarda en llegar al suelo.',
    'paracaidas', 'BAJA',
    'Sin aire (como en la Luna), una pluma y un martillo caen exactamente a la misma velocidad — lo probó un astronauta del Apolo 15 en vivo.',
    0),
  ('reto_viento_dificil', 'isla_viento', 'DIFICIL',
    'Prueba desde tres alturas distintas y arma la tendencia del tiempo de caída.',
    'altura', 'SUBE',
    'El tiempo de caída no crece en línea recta con la altura: para caer el doble de alto no se tarda el doble, se tarda ese doble multiplicado por 1.4 aproximadamente.',
    0),

  -- Isla del Jardín — la luz apura la germinación; el agua es el señuelo de este reto
  ('reto_jardin_facil', 'isla_jardin', 'FACIL',
    'Chirimbolo tiene una hojita que le creció sola. ¿Necesita luz para crecer más?',
    'luz', 'SUBE',
    'Las primeras hojas de una semilla usan la reserva de energía guardada en ella misma — recién con luz empiezan a fabricar su propio alimento con fotosíntesis.',
    0),
  ('reto_jardin_medio', 'isla_jardin', 'MEDIO',
    'Una isleña quiere saber si sin agua la hojita crece igual, con o sin luz.',
    'luz', 'SUBE',
    'Sin agua ninguna semilla germina, tenga luz o no: el agua ablanda la cáscara y activa las reacciones internas que empiezan a formar la raíz.',
    0),
  ('reto_jardin_dificil', 'isla_jardin', 'DIFICIL',
    'Prueba el crecimiento a los 2, 5 y 8 días y arma la tendencia completa.',
    'dias', 'SUBE',
    'El crecimiento de una planta joven no es parejo día a día: casi siempre arranca lento, después acelera de golpe cuando la raíz ya se afirmó.',
    0),

  -- Isla de la Cueva — más distancia debilita el eco; el material es el señuelo
  ('reto_cueva_facil', 'isla_cueva', 'FACIL',
    'Chirimbolo no está seguro de si su tímpano de lata escucha bien de lejos.',
    'distancia', 'BAJA',
    'El eco existe porque el sonido tarda en volver: se necesitan al menos unos 17 metros hasta la pared para que el oído humano distinga el eco del sonido original.',
    0),
  ('reto_cueva_medio', 'isla_cueva', 'MEDIO',
    'Un isleño pregunta si una tela puesta en la pared cambia el eco.',
    'distancia', 'BAJA',
    'Los estudios de grabación cubren las paredes con espuma justamente para absorber el sonido y que no rebote — lo mismo que un material blando le hace al eco de una cueva.',
    0),
  ('reto_cueva_dificil', 'isla_cueva', 'DIFICIL',
    'Prueba a tres distancias distintas y arma la tendencia de la intensidad del eco.',
    'distancia', 'BAJA',
    'El sonido pierde fuerza con la distancia mucho más rápido de lo que parece: al doblar la distancia, la intensidad que llega cae a una cuarta parte, no a la mitad.',
    0),

  -- Isla del Faro — más frotadas atraen más papelitos; la distancia es la segunda variable real
  ('reto_faro_facil', 'isla_faro', 'FACIL',
    'Chirimbolo frotó su antena contra su propio brazo y ahora atrae cosas. ¿Frotar más ayuda?',
    'frotadas', 'SUBE',
    'Frotar dos materiales distintos transfiere electrones de uno a otro: el que se queda con más carga es el que después atrae los papelitos.',
    0),
  ('reto_faro_medio', 'isla_faro', 'MEDIO',
    'Una isleña quiere saber si la distancia a los papelitos también importa.',
    'frotadas', 'SUBE',
    'La fuerza de atracción eléctrica cae rápido con la distancia — es la misma razón por la que un globo con estática solo atrae el pelo si lo acercas bastante.',
    0),
  ('reto_faro_dificil', 'isla_faro', 'DIFICIL',
    'Prueba con 3, 9 y 15 frotadas y arma la tendencia de cuántos papelitos atrae.',
    'frotadas', 'SUBE',
    'La carga estática se escapa sola con el tiempo, sobre todo en días húmedos: el aire con más agua ayuda a que los electrones de más se dispersen.',
    0),

  -- Isla de las Olas — más temperatura disuelve más rápido; el azúcar es la segunda variable real
  ('reto_olas_facil', 'isla_olas', 'FACIL',
    'Chirimbolo tiene una junta oxidada. ¿El agua tibia la afloja más rápido que la fría?',
    'temperatura', 'BAJA',
    'El agua caliente tiene sus moléculas moviéndose más rápido, así que chocan más veces por segundo contra el sólido y lo disuelven antes.',
    0),
  ('reto_olas_medio', 'isla_olas', 'MEDIO',
    'Un isleño pregunta si la cantidad de azúcar también cambia el tiempo.',
    'temperatura', 'BAJA',
    'Un vaso de agua tiene un límite de cuánta azúcar puede disolver — pasado ese punto, el azúcar de más se queda entera en el fondo por más que se espere.',
    0),
  ('reto_olas_dificil', 'isla_olas', 'DIFICIL',
    'Prueba con agua a 5, 20 y 40 grados y arma la tendencia del tiempo de disolución.',
    'temperatura', 'BAJA',
    'Revolver el agua no cambia cuánto se disuelve al final, pero sí acelera cuánto tarda: acerca azúcar nueva a la superficie del sólido en vez de esperar a que llegue sola.',
    0),

  -- Isla del Risco — la superficie rugosa frena más el carrito; la altura es la segunda variable real
  ('reto_risco_facil', 'isla_risco', 'FACIL',
    'Chirimbolo quiere saber si sus patas agarran mejor una rampa lisa o una rugosa.',
    'superficie', 'BAJA',
    'La fricción no depende de cuánta superficie toca el objeto, sino de qué tan ásperas son las dos caras que se rozan — por eso un carrito chico y uno grande frenan casi igual en la misma rampa.',
    0),
  ('reto_risco_medio', 'isla_risco', 'MEDIO',
    'Una isleña pregunta si la altura de la rampa también cambia la distancia.',
    'superficie', 'BAJA',
    'Una rampa más alta no cambia la fricción de la superficie: lo que cambia es la velocidad con la que el carrito llega abajo, y por eso recorre más distancia.',
    0),
  ('reto_risco_dificil', 'isla_risco', 'DIFICIL',
    'Prueba la misma superficie en tres alturas y arma la tendencia de la distancia recorrida.',
    'altura', 'SUBE',
    'Los ingenieros de pistas de patinaje calculan la fricción del material antes de construir, porque muy poca fricción hace la pista peligrosa e imposible de frenar.',
    0),

  -- Isla del Imán — más grosor de obstáculo debilita el imán; el material es el señuelo
  ('reto_iman_facil', 'isla_iman', 'FACIL',
    'Chirimbolo no sabe si su imán interno sigue funcionando bajo un poco de arena.',
    'grosor', 'BAJA',
    'El campo magnético de un imán se debilita con la distancia al objeto, así que entre más grueso el obstáculo, más lejos queda el imán del clip en los hechos.',
    0),
  ('reto_iman_medio', 'isla_iman', 'MEDIO',
    'Un isleño duda si el tipo de material entre el imán y el clip también importa.',
    'grosor', 'BAJA',
    'El magnetismo atraviesa el papel, la tela o el plástico casi sin perder fuerza — pero no atraviesa el metal grueso, que además puede desviar el campo.',
    0),
  ('reto_iman_dificil', 'isla_iman', 'DIFICIL',
    'Prueba con tres grosores distintos y arma la tendencia de si el clip se mueve.',
    'grosor', 'BAJA',
    'Los imanes de neodimio, mucho más fuertes que uno común, se usan justo donde hace falta atravesar materiales gruesos, como en discos duros de computadora.',
    0),

  -- Isla del Reflejo — el color oscuro absorbe más calor; los minutos son la segunda variable real
  ('reto_reflejo_facil', 'isla_reflejo', 'FACIL',
    'Chirimbolo se preocupa de que su placa oscura se calienta más que la clara.',
    'color', 'SUBE',
    'Un auto oscuro estacionado al sol puede quedar bastante más caliente por dentro que uno claro, en el mismo lugar y a la misma hora.',
    0),
  ('reto_reflejo_medio', 'isla_reflejo', 'MEDIO',
    'Una isleña pregunta si el tiempo al sol también cambia cuánto se calienta.',
    'color', 'SUBE',
    'La ropa de color claro se usa en climas calurosos justamente porque refleja más luz solar y absorbe menos calor que la ropa oscura.',
    0),
  ('reto_reflejo_dificil', 'isla_reflejo', 'DIFICIL',
    'Prueba a los 5, 10 y 20 minutos al sol y arma la tendencia de la temperatura.',
    'minutos', 'SUBE',
    'Los techos blancos que se usan en algunas ciudades calurosas reflejan tanta luz solar que bajan varios grados la temperatura dentro del edificio, sin usar electricidad.',
    0);

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
