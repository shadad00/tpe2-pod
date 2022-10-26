# Trabajo Práctico Especial 2: Peatones 🚶‍♂️


## Instrucciones de Compilación 🛠️
Generación de los .tar.gz a la carpeta target/
```
mvn clean install
```

## Ejecución 🚀
### Server
El servidor puede ejecutarse de dos maneras distintas
**(Ubicado en la raíz del proyecto en el comienzo para ambos casos)**:
1. Utilizando un Shell Script:
```
./scripts/server.sh
```
2. Mediante comandos por terminal:


```
cd server/target/
tar -xvf tpe2-g14-server-1.0-SNAPSHOT-bin.tar.gz
cd tpe2-g14-server-1.0-SNAPSHOT/
chmod u+x *
./run-server.sh
```
### Queries
Las queries pueden ejecutarse mediante:
1. **Shell Script** 
2. **Comandos por terminal**

    Todas las queries necesitan de la ejecución de los siguientes comandos si se decide no utilizar los scripts. 👀
**(Ubicado en la raíz del proyecto en el comienzo)**
    ```
    cd client/target/
    tar -xvf tpe2-g14-client-1.0-SNAPSHOT-bin.tar.gz
    cd tpe2-g14-client-1.0-SNAPSHOT/
    chmod u+x *
    ```

> También está la opción de cargar los sensores en ram en vez de en el cluster. Si se quiere usar esa opción se debe incluir el flag: 
1. Con Shell Script: "*-r true*".
2. Comandos por terminal: "*-Dram=true*".

#### Query 1
1. Utilizando el script: **(Ubicado en la raíz del proyecto)**
```
./scripts/query1.sh -a 'xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -i <inPath> -o <outPath> [-r true]
```
2. Mediante comandos por terminal: **(Recordar ejecutar los comandos mencionados previamente 👀)**
```
./query1.sh -DinPath=<inPath> -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DoutPath=<outPath> [-Dram=true]
```
#### Query 2
1. Utilizando el script: **(Ubicado en la raíz del proyecto)**
```
./scripts/query2.sh -a 'xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -i <inPath> -o <outPath> [-r true]
```
2. Mediante comandos por terminal: **(Recordar ejecutar los comandos mencionados previamente 👀)**
```
./query2.sh -DinPath=<inPath> -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DoutPath=<outPath> [-Dram=true]
```
#### Query 3
> El parámetro *-m* o *-Dmin* hace referencia a la cantidad de peatones mínima que debe haber medido un sensor para figurar.
1. Utilizando el script: **(Ubicado en la raíz del proyecto)**
```
./scripts/query3.sh -a 'xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -i <inPath> -o <outPath> -m <min> [-r true]
```
2. Mediante comandos por terminal: **(Recordar ejecutar los comandos mencionados previamente 👀)**
```
./query3.sh -DinPath=<inPath> -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DoutPath=<outPath> -Dmin=<min> [-Dram=true]
```
#### Query 4
> El parámetro *-n* o *-Dn* hace referencia al límite de sensores con mayor promedio mensual de peatones a mostrar.

> El parámetro *-y* o *-Dyear* hace referencia al año en cuestión para realizar el promedio mensual.
1. Utilizando el script: **(Ubicado en la raíz del proyecto)**
```
./scripts/query4.sh -a 'xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -i <inPath> -o <outPath> -n <n> -y <year> [-r true]
```
2. Mediante comandos por terminal: **(Recordar ejecutar los comandos mencionados previamente 👀)**
```
./query4.sh -DinPath=<inPath> -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DoutPath=<outPath> -Dyear=<year> -Dn=<n> [-Dram=true]
```
#### Query 5
1. Utilizando el script: **(Ubicado en la raíz del proyecto)**
```
./scripts/query5.sh -a 'xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -i <inPath> -o <outPath> [-r true]
```
2. Mediante comandos por terminal: **(Recordar ejecutar los comandos mencionados previamente 👀)**
```
./query5.sh -DinPath=<inPath> -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DoutPath=<outPath> [-Dram=true]
```


## Autores 💭
* **Gaspar Budó Berra**
* **Bruno Squillari**
* **Facundo Zimbimbakis**
* **Santiago Hadad**
* **Marcos Dedeu**