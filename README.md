# Trabajo Práctico Especial 2: Peatones 🚶‍♂️


## Instrucciones de Compilación 🛠️
Generación de los .tar.gz a la carpeta target/
```
mvn clean install
```

## Ejecución 🚀
###Server
Para correr el server utilizando un script:
```
./scripts/server.sh
```
O también se podría mediante los siguientes comandos por terminal
```
cd server/target/
tar -xvf tpe2-g14-server-1.0-SNAPSHOT-bin.tar.gz
cd tpe2-g14-server-1.0-SNAPSHOT/
chmod u+x *
./run-server.sh
```
###Queries
Las queries pueden ejecutarse mediante un script o también mediante determinados comandos por terminal.

Todas las queries necesitan de la ejecución de los siguientes comandos si se decide no utilizar los scripts. 👀
```
cd client/target/
tar -xvf tpe2-g14-client-1.0-SNAPSHOT-bin.tar.gz
cd tpe2-g14-client-1.0-SNAPSHOT/
chmod u+x *
```
####Query 1
Ejecución de query 1 utilizando el script:
```
./scripts/query1.sh -a 'xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' 
-i <inPath> -o <outPath>
```
Ejecución de query 1 sin utilizar el script (recordar ejecutar los comandos mencionados previamente 👀):
```
./query1.sh -DinPath=<inPath> -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DoutPath=<outPath>
```
####Query 2
Ejecución de query 2 utilizando el script:
```
./scripts/query2.sh -a 'xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' 
-i <inPath> -o <outPath>
```
Ejecución de query 2 sin utilizar el script (recordar ejecutar los comandos mencionados previamente 👀):
```
./query2.sh -DinPath=<inPath> -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DoutPath=<outPath>
```
####Query 3
Ejecución de query 3 utilizando el script:
```
./scripts/query3.sh -a 'xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' 
-i <inPath> -o <outPath> -m <min>
```
Ejecución de query 3 sin utilizar el script (recordar ejecutar los comandos mencionados previamente 👀):
```
./query3.sh -DinPath=<inPath> -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DoutPath=<outPath> -Dmin=<min>
```
####Query 4
Ejecución de query 4 utilizando el script:
```
./scripts/query4.sh -a 'xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' 
-i <inPath> -o <outPath> -n <n> -y <year>
```
Ejecución de query 4 sin utilizar el script (recordar ejecutar los comandos mencionados previamente 👀):
```
./query4.sh -DinPath=<inPath> -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DoutPath=<outPath> -Dyear=<year> -Dn=<n>
```
####Query 5
Ejecución de query 5 utilizando el script:
```
./scripts/query5.sh -a 'xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' 
-i <inPath> -o <outPath>
```
Ejecución de query 5 sin utilizar el script (recordar ejecutar los comandos mencionados previamente 👀):
```
./query5.sh -DinPath=<inPath> -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DoutPath=<outPath>
```


## Autores 💭
* **Gaspar Budó Berra**
* **Bruno Squillari**
* **Facundo Zimbimbakis**
* **Santiago Hadad**
* **Marcos Dedeu**