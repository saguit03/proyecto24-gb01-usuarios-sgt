# Entornos de GitHub

GitHub Actions permite gestionar implementaciones mediante la configuración de **entornos** personalizados, como `production`, `testing` o `develop`. Estos entornos ayudan a controlar el despliegue de los flujos de trabajo (*workflows*) y a proteger secretos específicos de cada entorno.

## Introducción a los entornos

### Creación y configuración de entornos

- **Definición de entornos**: Se pueden crear entornos que representen diferentes etapas del ciclo de vida de la aplicación, como `production`, `testing` o `develop`.

- **Reglas de protección**: Cada entorno puede tener reglas de protección que deben cumplirse antes de que un trabajo acceda al entorno o a sus secretos. Estas reglas pueden incluir:
  - Revisores requeridos.
  - Restricciones de rama.
  - Temporizadores de espera.

- **Secretos de entorno**: Se pueden definir secretos específicos para cada entorno, garantizando que solo los trabajos que cumplan con las reglas de protección accedan a ellos.

### Uso de entornos en flujos de trabajo

- **Referencia a entornos**: En los archivos de flujos de trabajo, se pueden especificar el entorno al que se debe desplegar un trabajo utilizando la propiedad `environment`.

- **Despliegue condicionado**: Los trabajos que hacen referencia a un entorno no se ejecutarán hasta que se cumplan todas las reglas de protección definidas para ese entorno.

### Disponibilidad

- **Repositorios públicos**: Las funciones de entornos, secretos de entorno y reglas de protección de despliegue están disponibles para todos los planes actuales de GitHub en repositorios públicos.

- **Repositorios privados**: Para acceder a estas funciones en repositorios privados o internos, es necesario contar con GitHub Pro, GitHub Team o GitHub Enterprise.

Para más detalles, se puede profundizar en la [documentación oficial de GitHub](https://docs.github.com/es/actions/managing-workflow-runs-and-deployments/managing-deployments/managing-environments-for-deployment#about-environments). 

## Promoción entre entornos

Suponiendo estos entornos:
- `production`. El entorno definitivo de producción. Tiene estas reglas de protección:
  - Revisor requerido.
  - El revisor no puede ser la misma persona que inició el flujo de trabajo.
- `testing`. El entorno para pruebas. Tiene esta regla de protección:
  - Revisor requerido.
- `develop`. El entorno de desarrollo. No tiene reglas de protección.

Se podría hacer la promoción entre entornos de dos maneras diferentes:
- *Caller & Called*
- *Workflow run*

### Promoción con *Caller & Called*

Se podrían tener dos flujos de trabajo: *caller* y *called*. Desde *caller*, se desencadenan los flujos en todos los entornos: `production`, `testing` o `develop`.  

Gracias a las reglas de protección, únicamente se iniciará el flujo en `develop` y los otros dos se quedarán esperando a que un revisor autorice al flujo. Dado que esta autorización se realiza de forma manual, los revisores pueden esperar a que termine la ejecución en `develop` para decidir si autorizar o no a la ejecución de los flujos.

```yml
name: CALLER
on:
  push:
  pull_request:
  workflow_dispatch:

jobs:
  CallerMatrix:
    strategy:
      matrix:
        env-type: [develop, testing, production]
    uses: saguit03/proyecto24-gb01-usuarios-sgt/.github/workflows/learning-called.yml@actions
    with:
      env-type: ${{ matrix.env-type }}
```

```yml
name: CALLED

on:
  workflow_call:
    inputs:
      env-type:
        required: true
        type: string

jobs:
  deploy:
    environment:  ${{ inputs.env-type }}
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    # ...
  rollback:
    # ...
```

Se define la variable `env-type` para poder cambiar entre los tipos de entorno. Utilizando *caller* y *called*, los trabajos que se realizarán en todos los entornos serán los mismos.  

Por ello, se pueden definir trabajos para hacer *rollback* o para mostrar mensajes de error en caso de fallo. Los tipos de trabajo y sus pasos se explican en otra guía.  

### Promoción con *Workflow run*

Se pueden definir varios flujos de trabajo, cada uno para un entorno distinto. A continuación se muestran tres flujos:

1. `Workflow in develop`. Primer flujo que se ejecuta en el entorno de desarrollo, sin restricciones.
2. `Workflow in testing`. Segundo flujo, que se ejecuta en el entorno de pruebas si:
    - `Workflow in develop` se ha completado. Dependiendo del resultado del primer flujo, se realizará un trabajo u otro.
    - Un revisor ha autorizado su ejecución.
3. `Workflow in production`. Tercer y último flujo, que se ejecuta en el entorno de producción si:
    - `Workflow in testing` se ha completado. Dependiendo del resultado del segundo flujo, se realizará un trabajo u otro.
    - Un revisor ha autorizado su ejecución, y este revisor no es quien ha iniciado los flujos.

En estos ejemplos, cada uno de los flujos realiza los mismos trabajos (de forma equivalente al uso de *caller* y *called*). Entonces, si se quisieran realizar trabajos distintos en cada flujo de trabajo, podrían realizarse modificaciones de forma individual. Sin embargo, se recomienda reutilizar los flujos de trabajo con *caller* y *called* si los trabajos serán siempre los mismos.  

```yml
name: Workflow in develop

on:
  push:
  pull_request:
  workflow_dispatch:

jobs:
  deploy:
    uses: saguit03/proyecto24-gb01-usuarios-sgt/.github/workflows/learning-called.yml@actions
    with:
      env-type: 'develop'
```

```yml
name: Workflow in testing

on:
  workflow_run:
    workflows: [Workflow in develop]
    types:
      - completed

jobs:
  on-success:
    if: ${{ github.event.workflow_run.conclusion == 'success' }}
    uses: saguit03/proyecto24-gb01-usuarios-sgt/.github/workflows/learning-called.yml@actions
    with:
      env-type: 'testing'
  on-failure:
    if: ${{ github.event.workflow_run.conclusion == 'failure' }}
    environment: 'testing'
    runs-on: ubuntu-latest
    steps:
      - run: echo 'The triggering workflow failed'
      - run: exit 1
```

```yml
name: Workflow in production

on:
  workflow_run:
    workflows: [Workflow in testing]
    types:
      - completed

jobs:
  deploy:
    if: ${{ github.event.workflow_run.conclusion == 'success' }}
    uses: saguit03/proyecto24-gb01-usuarios-sgt/.github/workflows/learning-called.yml@actions
    with:
      env-type: 'production'

  on-failure:
    if: ${{ github.event.workflow_run.conclusion == 'failure' }}
    environment: 'production'
    runs-on: ubuntu-latest
    steps:
      - run: echo 'The triggering workflow failed'
      - run: exit 1
```

### Comparación de enfoques

La utilización de un enfoque u otro es una decisión que se debe sopesar acorde a las necesidades y recursos de cada proyecto.

Utilizando *caller* y *called*, todos los flujos se desencadenan a la vez, aunque solamente se iniciará la ejecución en `develop`. No obstante, esto pasará solamente si se han definido correctamente las reglas de protección; por lo que, si estas reglas faltan, es posible que se ejecuten flujos cuando no sea el momento adecuado. Esto se debe a que estos flujos se desencadenan de forma concurrente, lo que básicamente quiere decir, al mismo tiempo. La concurrencia y el paralelismo quedan fuera del alcance de esta guía.  

Por otro lado, usando varios flujos con `workflow_run`, se garantiza un orden y se pueden compartir resultados de ejecución de una forma más sencilla.  

| Enfoque                 | Características | Consideraciones |
|-------------------------|----------------|----------------|
| **Uso de `caller` y `called`** | - Todos los flujos se desencadenan a la vez. <br> - Los flujos son independientes entre sí. <br> - Los trabajos de los flujos son idénticos. | - Puede haber ejecuciones no deseadas si las reglas de protección faltan. <br> - Los flujos se desencadenan concurrentemente. <br> - Facilita la mantenibilidad del código. |
| **Uso de `workflow_run`** | - Garantiza un orden en la ejecución de flujos. <br> - Permite compartir resultados de ejecución de manera más sencilla. <br> - Permite que cada flujo defina trabajos diferentes. | - Ejecución secuencial. <br> - Puede aumentar la complejidad de depuración. |

## Referencias

- https://docs.github.com/es/actions/managing-workflow-runs-and-deployments/managing-deployments/managing-environments-for-deployment#about-environments