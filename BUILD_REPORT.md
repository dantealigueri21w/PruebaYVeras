# Bitácora de compilación — Prueba y Verás

## 25/08/2026 — Inicio del proyecto (Parte 1: dominio y datos)

Scaffold creado siguiendo `01-PROMPT-MAESTRO.md` sección 7 y 7.1. Versiones
copiadas de `numeropolis/` y `fabrica-de-historias/` (ya verificadas
compilando en este mismo entorno, ver sus propios `BUILD_REPORT.md`):
Gradle 9.3.1, AGP 9.1.1, Kotlin 2.4.0, KSP 2.3.10, Compose BOM 2026.08.00,
Room 2.8.4, JDK 17, compileSdk/targetSdk 37, minSdk 24.

## 25/08/2026 — Parte 1 cerrada: dominio y datos, 100 % verificados

Sigue el plan `.superpowers/plans/2026-08-25-pruebayveras-dominio-y-datos.md`.
Esta parte no incluye ninguna pantalla todavía (`MainActivity` es un
marcador de posición) — cubre `domain/` y `data/` completos, tal como los
describe `fichas/30-CIENCIALAB.md`.

### `./gradlew clean testDebugUnitTest` — 84 tests, 0 fallos, 0 errores

```
BUILD SUCCESSFUL in 27s
30 actionable tasks: 30 executed
```

| Suite | Qué prueba | Tests |
|---|---|---|
| `MotorPruebaJustaTest` | dos montajes difieren en exactamente una variable | 8 |
| `MotorFlotabilidadTest` | densidad del agua salada vs. densidad del huevo (Isla de la Marea) | 6 |
| `MotorCaidaTest` | caída libre y factor de paracaídas (Isla del Viento) | 5 |
| `MotorGerminacionTest` | crecimiento según agua y luz (Isla del Jardín) | 6 |
| `MotorEcoTest` | retardo e intensidad del eco (Isla de la Cueva) | 5 |
| `MotorEstaticaTest` | papelitos atraídos según frotadas (Isla del Faro) | 5 |
| `MotorDisolucionTest` | tiempo de disolución según temperatura (Isla de las Olas) | 4 |
| `MotorFriccionTest` | distancia según superficie de rampa (Isla del Risco) | 6 |
| `MotorMagnetismoTest` | grosor de obstáculo vs. tipo de material (Isla del Imán) | 5 |
| `MotorAbsorcionLuzTest` | temperatura ganada según color (Isla del Reflejo) | 5 |
| `MotorCuadernoDatosTest` | tendencia real desde los datos guardados | 6 |
| `MotorProgresoTest` | desbloqueos, Chirimbolo completo, racha | 8 |
| `AppDatabaseTest` | Room en memoria con Robolectric | 3 |
| `SemillaContractTest` | cantidades semilla contra la ficha (9 islas, 27 retos, 9 piezas, 12 insignias, 30 frases) | 8 |
| `CienciaLabRepositoryTest` | sembrado idempotente, tendencia real, confirmar pieza | 4 |

### `./gradlew lintDebug` — 0 errores, 13 avisos

Todos los avisos son esperables en esta etapa: versiones de librería ancladas a
propósito (igual que `numeropolis` y `fabrica-de-historias`, para no arriesgar
una combinación no probada), `MissingApplicationIcon` porque el icono real
se genera en la Parte 2, y `allowBackup` deprecado (cosmético, no afecta
la regla de privacidad de la sección 9: la app no declara `INTERNET` ni ningún
otro permiso).

### Un error real encontrado y corregido durante la construcción

**Room rechaza una clase `Converters` vacía.** El plan original dejaba un
`Converters.kt` sin funciones "por si una migración futura lo necesita". KSP
lo rechazó de inmediato: *"Class is referenced as a converter but it does not
have any converter functions."* Ninguna de las 8 entidades usa un tipo que
Room no soporte de forma nativa, así que se eliminó el archivo y la anotación
`@TypeConverters` de `AppDatabase` en vez de rellenarlo con una función que no
se usa — el mismo criterio de "no simular una función que no existe" que pide
la sección 1 del prompt maestro, aplicado a una clase de infraestructura.

## 25/08/2026 — Parte 2 cerrada: las 14 pantallas, arte integrado, APK real

Sigue el plan `.superpowers/plans/2026-08-25-pruebayveras-ui-pantallas.md`.
Tema con paleta verificada por contraste WCAG, tres formas propias
(`GenericShape`), tres componentes manipuladores que reemplazan
`Slider`/`Switch` de Material, `MesaDoblePrueba` reutilizable en las nueve
islas vía `AdaptadorIsla`, navegación diegética en el Archipiélago, y las
83 ilustraciones de la Parte de arte ya integradas.

### `./gradlew testDebugUnitTest` — 118 tests, 0 fallos, 0 errores

```
BUILD SUCCESSFUL in 1m 7s
29 actionable tasks: 29 executed
```

34 tests nuevos sobre los 84 de la Parte 1: `ContrasteTest` (3),
`FormasPropiasTest` (3), `MesaDoblePruebaTest` (2), `AdaptadoresIslasTest`
(9, uno por isla), `IslaScreenTest` (9, uno por isla, con contenido real
verificado — no solo "no revienta"), `IslaScreenInteraccionTest` (1),
`ArchipielagoScreenTest` (1), `CobertizoScreenTest` (2), `CuadernoScreenTest`
(2), `PerfilScreenTest` (1), `AjustesScreenTest` (1).

### `./gradlew lintDebug` — 0 errores, 29 avisos

20 son `UnusedResources`: 13 objetos manipulables (`objeto_huevo`,
`objeto_sal`, etc.) que todavía no se muestran en pantalla — los
manipuladores reales (perilla, interruptor, selector) ya funcionan con
gestos y datos reales, pero usan arte genérico de relleno en vez del
objeto específico de cada isla (ver "Qué sigue simplificado" abajo) — y 7
strings de encabezados/descripciones para pulido futuro (`archipielago_titulo`,
`cuaderno_titulo`, `cd_isla_bloqueada`, `cd_isla_completada`,
`isla_resultado_control`, `isla_resultado_prueba`). El resto son versiones
de librería ancladas a propósito, igual que en la Parte 1.

### `./gradlew assembleDebug` — APK real generado y verificado

| Dato | Valor |
|---|---|
| Archivo | `app/build/outputs/apk/debug/app-debug.apk` |
| Tamaño | 23 MB (código + las 83 ilustraciones) |
| `applicationId` | `pe.appmobile.pruebayveras` |
| `versionName` / `versionCode` | `1.0.0` / `1` |
| Permisos | Ninguno de red. El único presente es `pe.appmobile.pruebayveras.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION`, un permiso propio y benigno que AndroidX agrega automáticamente para receptores dinámicos en Android 13+ — no otorga ninguna capacidad de red ni de datos; confirmado con `grep -i internet` sobre el `aapt2 dump badging`, sin resultados |
| Icono | Conectado: `icono_lanzador.webp`, visible en `aapt2 dump badging` |

### Dos errores reales encontrados y corregidos durante la construcción

**1. Tres retos "difícil" tenían `variableIndependiente` inconsistente con su
propio texto.** `reto_viento_dificil` decía `"paracaidas"` pero el texto
describe variar la altura; `reto_jardin_dificil` decía `"luz"` pero describe
variar los días; `reto_reflejo_dificil` decía `"color"` pero describe variar
los minutos al sol. Encontrado al diseñar los adaptadores de isla, antes de
escribir código — se corrigió `SemillaRetos.kt` en las tres líneas.

**2. Los tests de pantalla interferían entre sí al correr la suite completa.**
Cada `IslaViewModel`/`ArchipielagoViewModel`/etc. lanza una corrutina en su
`init` (siembra la base, carga datos) usando `viewModelScope` — pero un test
unitario que instancia el ViewModel a mano, sin pasar por un `ViewModelStore`
real, nunca cancela esa corrutina. Corriendo cada test de pantalla solo, todos
pasaban; corriendo la suite completa junta, 3 tests fallaban con
`IllegalStateException` de SQLite o con una excepción de un test anterior
atribuida al siguiente — la corrutina de un test seguía viva y tocaba una
base de datos en memoria que ya no existía. Se corrigió creando cada
ViewModel de test dentro de un `ViewModelStore` real (`viewModelDeTest()`,
en `ui/testutil/`) y cerrándolo en `@After` con `store.clear()` — el mismo
mecanismo que usa Android en producción para destruir un ViewModel cuando su
dueño desaparece. Además, `compose.waitForIdle()` no siempre alcanza a que
termine una corrutina que usa el executor propio de Room (un hilo de fondo
real, no el reloj de Compose); donde hace falta el resultado de esa
corrutina antes de continuar, se sondea con `Thread.sleep` real en vez de
confiar en una sola pasada de idle.

## Qué sigue simplificado — dicho, no escondido

- **Los 13 objetos manipulables** (huevo, sal, paracaídas, maceta, campana,
  globo, azúcar, telas, termómetro, clip) están procesados e integrados en
  `drawable-nodpi/`, pero `IslaScreen` todavía no los muestra: la perilla usa
  un arco de progreso dibujado en `Canvas`, y el interruptor/selector
  reutilizan arte de Chirimbolo o de otro objeto como marcador. La mecánica
  es real (gestos reales, motor real, dato persistido real) — lo que falta
  es la piel visual específica de cada objeto, no la interacción.
- **`posicionesIslas` en `ArchipielagoScreen`** son coordenadas relativas
  estimadas a mano sobre `mapa_archipielago.webp`, no medidas por análisis de
  imagen — revisar visualmente contra el mapa real antes de la entrega final.
- **Sin sonido ni háptica.** Los interruptores de Ajustes existen y cambian
  de estado, pero no hay ningún efecto conectado detrás.
- **Sin capturas del manual de usuario ni memoria descriptiva.** Empiezan en
  la Fase 2, después de instalar el APK real que genere GitHub Actions — no
  el de esta compilación local (sección 15 del prompt maestro: mismo código,
  hash distinto).

Nada de esto afecta la regla central: **el mecanismo es el contenido** en
las nueve islas — se manipula una perilla, un interruptor o un selector con
gestos reales, el resultado sale de un motor real verificado contra una
fuente real, y cada intento se guarda en Room de verdad.
