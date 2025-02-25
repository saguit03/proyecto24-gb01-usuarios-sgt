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

Se puede definir un nombre para el flujo de trabajo (en este caso: Workflow) o no. Si no se define, se pondrá por defecto algún nombre relacionado con el evento que lo ha desencadenado.  

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
- `workflow_dispatch`. Permite la ejecución manual de un flujo de trabajo. Se pueden definir variables de entrada, que se utilizarán cuando se ejecute el flujo.
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
```yml
jobs:
  deploy:
    environment: produccion
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

    # Optional: Uploads the full dependency graph to GitHub to improve the quality of Dependabot alerts this repository can receive
    - name: Update dependency graph
      uses: advanced-security/maven-dependency-submission-action@571e99aab1055c2e71a1e2309b9691de18d6b7d6

  rollback:
      needs: deploy
      if: failure()
      runs-on: ubuntu-latest
      steps:
        - name: Revertir cambios en el código
          run: |
            git revert HEAD --no-edit
            git push origin main
      
```