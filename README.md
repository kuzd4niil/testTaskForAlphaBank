# testTaskForAlphaBank
Test Task For Alpha Bank
## Инструкция по сборке и запуску (используется JAVA 11):
```
user:<project_dir>$ ./gradlew build
user:<project_dir>$ java -jar build/libs/testTaskForAlphaBank-0.0.1-SNAPSHOT.jar
```
### Пример использования:
По адресу [localhost:8080/api/v1/compare_rate?rate_currency=<код валюты>]()

Для рубля будет так: [localhost:8080/api/v1/compare_rate?rate_currency=RUB](localhost:8080/api/v1/compare_rate?rate_currency=RUB)

Если валюты не существует, то придёт картинка [cherry.webp](https://simpl.info/webp/cherry.webp)
### OpenAPI:
Описание конечной точки [OpenAPI](http://localhost:8080/v3/api-docs) (программа должна быть запущена)
## Docker:
Ручная сборка и запуск Dockerfile:
```
user:<project_dir>$ docker build -t <имя образа> .
user:<project_dir>$ docker run -d -p 8080:8080 --name <имя контейнера> <имя образа>
```
Авоматическая сборка Dockerfile и его запуск при помощи docker-compose:
```
user:<project_dir>$ docker-compose up -d
```