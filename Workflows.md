# GitHub Actions y Workflows

***GitHub Actions*** es una plataforma CI/CD (Integración Continua/Despliegue Continuo) que permite automatizar el flujo de desarrollo, testeo y despliegue. Esto se logra a través de flujos de trabajo (*workflows*) que se ejecutan cada vez que se detecta algún evento, como puede ser un `push` , un `merge` o una `pull request`.  

En esta guía, se explicarán algunos de los posibles flujos. Si se desea profundizar en esta plataforma, se recomienda la lectura de la documentación original: [GitHub Actions](https://docs.github.com/es/actions/writing-workflows/quickstart).  

## Conceptos básicos

Un *flujo de trabajo* (*workflow*) es un proceso automatizado configurable que ejecutará uno o más trabajos cuando se produzca un **evento**. Los flujos se definen en el directorio `.github/workflows` dentro de un repositorio. Puede haber varios flujos, pero no deben estar en subdirectorios de `.github/workflows`.  

Un ***trabajo*** (*job*) es un conjunto de pasos de un flujo de trabajo que se ejecutan en un **ejecutor**. Cada paso puede ser un script de shell o una acción que se ejecutarán. 

Una ***acción*** es una aplicación personalizada para la plataforma de GitHub Actions que realiza una tarea compleja pero que se repite frecuentemente.  

Un ***ejecutor*** es un servidor que ejecuta los flujos de trabajo cuando se desencadenan. Cada ejecutor puede ejecutar un solo trabajo a la vez. GitHub proporciona ejecutores de Ubuntu Linux, Microsoft Windows y macOS para ejecutar los flujos de trabajo. Cada ejecución de flujo de trabajo se ejecuta en una máquina virtual recién aprovisionada.  

Un ***evento*** es una actividad específica en un repositorio que desencadena una ejecución de flujo de trabajo. Por ejemplo, un `push`, `pull request` o `merge`.

## Estructura básica

Se puede definir un nombre para el flujo de trabajo (en este caso: Workflow) o no. Si no se define, se pondrá por defecto la ruta del *workflow* (`.github/workflows/nombre.yml`).  

Los eventos son las actividades que disparan o desencadenan los flujos, como los *triggers*. Este ejemplo se ejecutará cada vez que se realice un `push` o `pull request`, aunque se pueden restringir más estos eventos o utilizar otros.

Puede haber varios trabajos (como se ve en `trabajo1` y `trabajo2`), cada uno con sus propios pasos (*steps*). Luego se explicarán mejor.

```yml
name: Workflow

on: # Eventos
  push:
  pull_request:

jobs: # Trabajos
  trabajo1:
    runs-on: ubuntu-latest
    steps:
        - name: Paso 1
        run: echo "Trabajo 1. Paso 1"
        - name: Paso 2
        run: echo "Trabajo 1. Fin"
  trabajo2:
    runs-on: ubuntu-latest
    steps:
        - name: Paso 1
        run: echo "Trabajo 2. Paso 1"
        - name: Paso 2
        run: echo "Trabajo 2. Paso 2"
        - name: Paso 3
        run: echo "Trabajo 2. Fin"
```


## Eventos

Entre los [eventos que desencadenan flujos de trabajo](https://docs.github.com/es/actions/writing-workflows/choosing-when-your-workflow-runs/events-that-trigger-workflows), se destacan los siguientes:

- `push`. Cuando se hace un *push* al repositorio. Se pueden especificar las ramas sobre las que se desencadena el flujo. Si no se indica ninguna rama, se disparará cuando se haga *push* en cualquier rama.
- `pull_request`. Cuando se realiza una *pull request*. Se pueden especificar el tipo de *pull request* y las ramas.
- `workflow_dispatch`. Permite la ejecución manual de un flujo de trabajo. Se pueden definir argumentos de entrada, que se utilizarán cuando se ejecute el flujo.
- `workflow_call`. Permite la ejecución desde otro flujo de trabajo, lo cual resulta especialmente útil para reutilizar *workflows*.
- `workflow_run`. Permite la ejecución cuando se solicita o se completa la ejecución de otro flujo de trabajo.

Cada uno de estos eventos se describen en la [documentación oficial](https://docs.github.com/es/actions/writing-workflows/choosing-when-your-workflow-runs/events-that-trigger-workflows), en caso de que se quiera profundizar en su uso.  

### Ejemplos de eventos

El flujo `PRIMERO` se desencadenará con los siguientes eventos:  
- Se produce un *push* en las ramas `main` o `releases/**`.
- Se abre o se reabre una *pull_request* en las ramas `main` o `releases/**`.
- Se ejecuta manualmente desde GitHub. `Repositorio > Actions > Nombre del workflow > Run workflow`.

```yml
name: PRIMERO
on:
  push:
    branches:
      - 'main'
      - 'releases/**'
  pull_request:
    types: [opened, reopened]
    branches:
      - 'releases/**'
  workflow_dispatch:
    inputs:
      environment:
        description: 'Entorno de GitHub'
        type: environment
        required: true
```

El flujo `SEGUNDO` se desencadenará cuando se haya completado el flujo `PRIMERO`.  

```yml
name: SEGUNDO
on:
  workflow_run:
    workflows: [PRIMERO]
    types:
      - completed
```

El flujo `REUTILIZABLE` se desencadenará cuando otro flujo de trabajo lo llame. Más adelante, se explicará la [reutilización de flujos de trabajo](https://docs.github.com/es/actions/sharing-automations/reusing-workflows). Además, este flujo necesita que se le pase una variable de entrada llamada `nombre`.  

```yml
name: REUTILIZABLE
on:
  workflow_call:
    inputs:
      nombre:
        required: true
        type: string
```

## Trabajos

Los trabajos dependerán de la aplicación que se esté desarrollando. Se mostrarán ejemplos para una aplicación desarrollada en Java con Maven y una desplegada en Docker.  

### Ejemplo Java

En este ejemplo, se define un único trabajo llamado `deploy` que se ejecutará en el entorno `develop` sobre un sistema operativo Ubuntu en su última versión. Para más información de los entornos, se puede consultar [la documentación oficial de entornos de GitHub](https://docs.github.com/es/actions/managing-workflow-runs-and-deployments/managing-deployments/managing-environments-for-deployment#about-environments).  

El trabajo `deploy` consta de varios pasos:
1. Se obtiene el repositorio actual (`actions/checkout@v4`). Como se puede intuir, se debe especificar la versión de cada acción. Da la casualidad de que en este ejemplo son ambas `v4`, pero no tiene por qué ser así.
2. Se define el JDK 17 para Java.
3. Se compila el proyecto con Maven.

```yml
jobs:
  deploy:
    environment: develop
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven
    - name: Build with Maven
      run: mvn -B package --file pom.xml  
```

Se puede apreciar que cada paso puede tener o no un nombre, que se mostrará en GitHub cuando el flujo esté en ejecución.  

### Ejemplo Docker

En este ejemplo, se define un único trabajo llamado `despliegue` que se ejecutará en el entorno `dev` sobre un sistema operativo Ubuntu en su última versión. Para más información de los entornos, se puede consultar [la documentación oficial de entornos de GitHub](https://docs.github.com/es/actions/managing-workflow-runs-and-deployments/managing-deployments/managing-environments-for-deployment#about-environments).


El trabajo `despliegue` consta de varios pasos:
1. Se obtiene el repositorio actual (`actions/checkout@v4`).
2. Se prepara el entorno Docker (`docker/setup-buildx-action@v2`).
3. Se ejecutan los servicios con Docker Compose. Se utiliza `run:`, y no `uses`, porque se trata de un comando que se ejecutará en la consola del ejecutador.
4. Se espera y se comprueba que estén corriendo los contenedores.
5. Se realiza una petición a un *endpoint* definido en uno de los servicios de los contenedores para ver si se han desplegado correctamente. En caso de que se haya producido algún fallo durante el despliegue, esta petición fallará y el trabajo terminará con un código `1`.  

El hecho de que se produzca `exit 1` si falla algo durante el último paso resulta bastante útil si después de este flujo de trabajo se desencadenan otros que dependan de su resultado. Sin esta salida, el flujo terminaría con estado `success` (éxito), cuando en verdad el trabajo ha terminado insatisfactoriamente.  

```yml
jobs:
  despliegue:
    runs-on: ubuntu-latest
    environment: 'dev'

    steps:
      - name: Check out repository code
        uses: actions/checkout@v4
        
      - name: Set up Docker
        uses: docker/setup-buildx-action@v2
          
      - name: Build and start services with Docker Compose
        run: |
          docker compose up -d

      - name: Wait for services to be ready
        run: |
          sleep 10
          docker compose ps
          
      - name: Run health check
        run: |
          curl -f http://localhost:8080/alive || exit 1
```

## Algunos flujos de trabajo específicos

### Caller & Called

Cuando se quiere reutilizar un *Workflow* en distintos lugares, en vez de repetir el código de cada flujo de trabajo, es recomendable utilizar llamadas a *workflows*. Se aconseja leer la documentación oficial sobre la [reutilización de flujos de trabajo](https://docs.github.com/es/actions/sharing-automations/reusing-workflows).

Se distinguen dos roles principales: *called* (llamado o al que se llama) y *caller* (el que llama). Se podría entender como que *called* es un método en cualquier lenguaje de programación y que el *caller* hace uso de la implementación que ha hecho *called*.

A pesar de que los trabajos a ejecutar se definan en el flujo de *called*, se ejecutará en la máquina de *caller*. Al consultar los distintos *workflows* en GitHub, se verá que, a pesar de que se utilice múltiples veces el flujo de *called*, nunca tendrá ejecuciones asociadas, sino que la ejecución aparecerá en los flujos que lo hayan utilizado.   

#### Ejemplo de Caller

En este caso, se utiliza una [estrategia de matriz](https://docs.github.com/es/actions/writing-workflows/choosing-what-your-workflow-does/running-variations-of-jobs-in-a-workflow#about-matrix-strategies), ya que se quiere ejecutar el mismo flujo de trabajo en distintos entornos de GitHub. Como solo se tiene una variable, la matriz es unidimensional (como una lista o vector), pero se podrían utilizar varias versiones. El uso completo de matrices no entra dentro del alcance de esta guía.  

Para hacer la llamada al flujo de trabajo *called*, se utiliza la sintaxis `uses`. En este tipo de trabajos que llaman a otros flujos, no se pueden definir otros pasos.

Para hacer referencia al flujo *called*, se utiliza la siguiente sintaxis:

```yml
<autor>/<repositorio>/.github/workflows/<fichero yml del workflow>@actions
```

Además, en este ejemplo, el flujo *called* recibe una variable de entrada, que se define con `with: nombre-variable: variable`. En este caso, como se utiliza una estrategia de matriz, se utiliza como variable `${{ matrix.env-type }}`, aunque se podría haber definido directamente un valor como `develop`.  

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

#### Ejemplo de Called

Para que sea un flujo de trabajo reutilizable, el evento que lo desesencadene debe ser `on: workflow_call`. Además, se pueden definir argumentos de entrada para aumentar la flexibilidad. Para entender el uso de estos argumentos de entrada, se recomienda la lectura de [reutilización de flujos de trabajo](https://docs.github.com/es/actions/sharing-automations/reusing-workflows#using-inputs-and-secrets-in-a-reusable-workflow).  

En este caso, se define la variable `env-type` para poder cambiar entre tipos de entorno explicados anteriormente. El resto del trabajo es como los vistos en anteriores ejemplos.   

Nótese la sintaxis para utilizar los argumentos de entrada como variables: `${{ inputs.env-type }}`.  

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
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven
    - name: Build with Maven
      run: mvn -B package --file pom.xml
```

### Workflow-run

Se pueden definir varios flujos de trabajo para que se desencadenen secuencialmente, es decir, uno después de otro, según el la actividad del primero de ellos. Los tipos de actividad son los siguientes:

- `completed`. Cuando el flujo desencadenante ha terminado.
- `requested`. Cuando el flujo desencadenante ha sido solicitado. Dependiendo de si el entorno requiere autorización de terceros, es posible que esté solicitado, pero que no se haya iniciado la ejecución.
- `in_progress`. Cuando el flujo desencadenante ha iniciado su ejecución.

La cancelación de un flujo de trabajo cancela también los siguientes flujos que fueran a ser ejecutados a continuación.  

Se proporcionará un ejemplo de dos flujos encadenados con `workflow_run`.  

#### Primer flujo

Este primero flujo denominado `PRIMERO` se ejecuta en el entorno `primero`. El trabajo que realiza es como el ejemplo mostrado anteriormente, y se desencadena con los eventos `push`, `pull` y `workflow_dispatch` (manualmente desde GitHub). No hay mucho más que explicar, pero téngase en cuenta que el nombre (`name: PRIMERO`) se utilizará en el siguiente flujo.  

```yml
name: PRIMERO

on:
  push:
  pull_request:
  workflow_dispatch:

jobs:
  deploy:
    runs-on: ubuntu-latest
    environment: 'primero'

    steps:
      - name: Check out repository code
        uses: actions/checkout@v4
        
      - name: Set up Docker
        uses: docker/setup-buildx-action@v2
          
      - name: Build and start services with Docker Compose
        run: |
          docker compose up -d

      - name: Wait for services to be ready
        run: |
          sleep 10
          docker compose ps
          
      - name: Run health check
        run: |
          curl -f http://localhost:8080/alive || exit 1
```

#### Segundo flujo

Este segundo flujo, denominado `SEGUNDO`, se desencadenará cuando el flujo de nombre `PRIMERO` se haya completado. Un flujo se considerará completado cuando haya terminado su ejecución, sea cual sea el resultado o conclusión.  

En este caso, se quiere realizar un trabajo (el mismo que el ejemplo de Docker) en el entorno `segundo` solamente si el resultado del flujo anterior fue exitoso. Por ello, se indica con la condición:
```yml
    if: ${{ github.event.workflow_run.conclusion == 'success' }}
```

Si la condición de éxito no se cumple, pasará al siguiente trabajo automáticamente. En este caso, simplemente se muestra un mensaje de error y se termina el trabajo; pero más adelante se verá cómo realizar una operación de *rollback* en caso de que algo haya ido mal.  

```yml
name: SEGUNDO

on:
  workflow_run:
    workflows: [PRIMERO]
    types:
      - completed

jobs:
# Los trabajos de SEGUNDO se ejecutarán en un entorno que requiere autorización de terceros
  on-success:
    if: ${{ github.event.workflow_run.conclusion == 'success' }}
    runs-on: ubuntu-latest
    environment: 'segundo'

    steps:
      - name: Check out repository code
        uses: actions/checkout@v4
        
      - name: Set up Docker
        uses: docker/setup-buildx-action@v2
          
      - name: Build and start services with Docker Compose
        run: |
          docker compose up -d

      - name: Check containers are running
        run: |
          docker ps
  on-failure:
    if: ${{ github.event.workflow_run.conclusion == 'failure' }}
    runs-on: ubuntu-latest
    steps:
      - run: echo 'The triggering workflow failed'
      - run: exit 1
```

#### N-flujos

Como se indica en la documentación oficial de [workflow_run](https://docs.github.com/en/actions/writing-workflows/choosing-when-your-workflow-runs/events-that-trigger-workflows#workflow_run), un flujo de trabajo puede ser desencadenado por varios flujos de trabajo. Sin embargo, si se especifica más de uno, solamente se ejecutará una vez cuando se produzca el primer evento. Por ejemplo:

```yml
name: TERCERO
on:
  workflow_run:
    workflows: [PRIMERO, SEGUNDO]
    types:
      - completed
# Los trabajos de TERCERO se ejecutarán en un entorno que requiere autorización de terceros
```

```yml
name: CUARTO
on:
  workflow_run:
    workflows: [SEGUNDO, TERCERO]
    types:
      - in_progress
```

```yml
name: QUINTO
on:
  workflow_run:
    workflows: [CUARTO]
    types:
      - requested
```

Dados estos flujos, `TERCERO` y `CUARTO`, desencadenados por el evento `workflow_run`, y teniendo en cuenta los otros flujos expuestos anteriormente, la secuencia de flujos será la siguiente:
1. Se iniciará la ejecución de `PRIMERO`.
2. Después de que `PRIMERO` se haya completado, desencadenará dos flujos:
   1. `SEGUNDO`, porque su desencadenador es `on: workflow_run: workflows: [PRIMERO]`. 
   2. `TERCERO`, porque su desencadenador es `on: workflow_run: workflows: [PRIMERO, SEGUNDO]`.
   Sin embargo, debido al entorno en el que se ejecutan, no se iniciarán automáticamente, ya que requiere revisión de terceros.
3. Después de un rato, se autoriza el flujo de `TERCERO`, que cambiará su estado a `in_progress`. Por tanto, desencadena `CUARTO`.
4. `CUARTO` pasa a estar solicitado, lo que desencadena el flujo `QUINTO`.
  
Si en vez de autorizar a `TERCERO`, se hubiera autorizado a `SEGUNDO`, se habría desencadenado de igual forma `CUARTO`, que a su vez desencadena `QUINTO`.  

Como son flujos que se ejecutan en distintos ejecutores, desde GitHub se podría ver que algunos flujos habrán acabado antes que otros. Por ejemplo, si no se autorizan `SEGUNDO` o `TERCERO`, ni siquiera aparecerían `CUARTO` o `QUINTO` en el historial de *workflows*.  

Pero si solamente se autoriza a `SEGUNDO`, desencadenaría a `CUARTO`, que desencadena a `QUINTO`; sin que `TERCERO` se tenga que aprobar. Cuando terminen, si `TERCERO` sigue sin autorización, los demás aparecerían como completados y `TERCERO` seguiría esperando a que un revisor le autorice.  

Como se puede notar, estos eventos y la autorización de los entornos pueden aumentar bastante la complejidad de los flujos de trabajo. Es recomendable no abusar de este tipo de flujos para que sean más fáciles de entender y mantener.  

### Rollback

Normalmente, cuando se realiza un `push`, `pull request` o cualquier otra actividad que modifique el repositorio, se ha comprobado previamente que todo funcionase correctamente. Sin embargo, si hubiera algún fallo inesperado, es posible definir un trabajo que se ejecute en caso de error (`if: failure()`).  

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
    # Etc.
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven
    - name: Build with Maven
      run: mvn -B package --file pom.xml

  rollback:
    needs: deploy
    if: failure()
    runs-on: ubuntu-latest
    steps:
      - name: Clonar el repositorio con acceso de escritura
        uses: actions/checkout@v4
        with:
          token: ${{ secrets.GITHUB_TOKEN }}
      
      - name: Configurar Git con identidad
        run: |
          git config --global user.email "github-actions@github.com"
          git config --global user.name "GitHub Actions"

      - name: Asegurar que hay historial de commits
        run: |
          git fetch --unshallow || echo "Repo ya tiene historial completo"

      - name: Revertir el último commit fallido
        run: |
          git fetch origin actions
          git checkout actions
          git reset --hard HEAD~1
          git push --force origin actions 
```

Para hacer el *rollback*, se necesita un *token* secreto que está disponible a través de la variable `${{ secrets.GITHUB_TOKEN }}`. Este *token* es necesario para que GitHub Actions pueda consultar el historial del repositorio y volver a un *commit* anterior al fallo.  

En este ejemplo, se estaban realizando las pruebas con los flujos sobre una rama llamada `actions`, por lo que se deberá cambiar este nombre por la rama predeterminada del repositorio (normalmente `main` o `master`) o aquella sobre la que se produzcan los cambios. **Es muy importante que los nombres de las ramas estén escritos correctamente para que el *rollback* se produzca correctamente**.

### Plantillas de flujos de trabajo

En esta pequeña guía, se han visto varios ejemplos de flujos de trabajo, utilizando trabajos con Java y Docker. Desde la documentación oficial, se ofrecen [plantillas de flujo de trabajo](https://docs.github.com/es/actions/writing-workflows/using-workflow-templates) para distintas necesidades.  

## Referencias 

- https://docs.github.com/es/actions/writing-workflows/quickstart
- https://docs.github.com/es/actions/writing-workflows/using-workflow-templates
- https://docs.github.com/es/actions/writing-workflows/choosing-when-your-workflow-runs/events-that-trigger-workflows
- https://docs.github.com/es/actions/writing-workflows/choosing-what-your-workflow-does/running-variations-of-jobs-in-a-workflow#about-matrix-strategies
- https://docs.github.com/en/actions/writing-workflows/choosing-when-your-workflow-runs/events-that-trigger-workflows#workflow_run
- https://docs.github.com/es/actions/sharing-automations/reusing-workflows
- https://docs.github.com/es/actions/managing-workflow-runs-and-deployments/managing-deployments/managing-environments-for-deployment#about-environments
- https://docs.github.com/es/actions/sharing-automations/reusing-workflows#using-inputs-and-secrets-in-a-reusable-workflow