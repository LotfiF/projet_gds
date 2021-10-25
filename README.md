# proje: GESTION_DE_STOCK


_Backend_:
  * **INSTALL**:
	``` go
    cd gestiondestock
    mvn clean install
	```
  * **START**:
	``` go
    cd gestiondestock
    mvn spring-boot:run
	```
  * **URL API REST**:
	``` go
    * on Localhost: http://localhost:8081/gestiondestock/v0
    * on Gitpod: https://8081-coffee-echidna-37t0zian.ws-eu17.gitpod.io/gestiondestock/v0
	```

  * **API_REST_DOCUMENTATION**:
	``` go
    * on Localhost: http://localhost:8081/swagger-ui.html
    * on Gitpod: https://8081-coffee-echidna-37t0zian.ws-eu17.gitpod.io/swagger-ui.html
	```
	
_Frontend_: 
  * **INSTALL**:
	``` go
    cd frontend-gestion-de-stock
    npm install
	```
  * **START**:
	``` go
      cd frontend-gestion-de-stock
      npm start
	```

  * **URL_ANGULAR_APPLICATION**:
	``` go
    * on Localhost: http://localhost:4200/login
    * on Gitpod: https://4200-coffee-echidna-37t0zian.ws-eu17.gitpod.io/login
	```

**ng-swagger-gen**
``` go
rm -rf tools/ && mkdir -p tools/swagger/dist/ && mkdir -p tools/swagger/src/ && cp /workspace/projet_gds/gestiondestock/target/gestiondestock-0.0.1-SNAPSHOT.jar /workspace/projet_gds/frontend-gestion-de-stock/tools/swagger/dist/ && cp /workspace/projet_gds/gestiondestock/target/swagger.json /workspace/projet_gds/frontend-gestion-de-stock/tools/swagger/src/ && node ./tools/swagger/src/swagger.json 0.0.1-SNAPSHOT && ./node_modules/.bin/ng-swagger-gen -i ./tools/swagger/src/swagger.json -o src/gs-api/src
```
