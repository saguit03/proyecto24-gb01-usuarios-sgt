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

Se puede definir un nombre para el flujo de trabajo (en este caso: Workflow) o no. Si no se define, se pondrá un nombre por

```yml
name: Workflow

on: # Eventos
  push:
  pull_request:
  workflow_dispatch:

jobs: # Trabajos
  trabajo1:
    runs-on: ubuntu-latest
    steps:
        - name: Paso 1
        run: echo "Trabajo 1. Paso 1"
        - name: Paso 2
        run: echo "Trabajo 1. Paso 2"
  trabajo2:
    runs-on: ubuntu-latest
    steps:
        - name: Paso 1
        run: echo "Trabajo 2. Paso 1"
        - name: Paso 2
        run: echo "Trabajo 2. Paso 2"

```


## Eventos



```yml
on:
  push:
  pull_request:
  workflow_dispatch:

```