# Витрина интернет-магазина

Веб-приложение «Витрина интернет-магазина», написанное с использованием Spring Boot на реактивном стеке технологий.

## Использование

На устройстве должен быть установлен и запущен Docker. Для сборки и запуска приложения выполните команду:

```sh
docker compose up -d
```

Приложение будет доступно по адресу:

```
http://localhost:8080/products
```

Для запуска юнит тестов использовать команду:

```sh
./gradlew payments:unit
./gradlew storefront:unit
```

Для запуска интеграционных тестов для витрины нужно запустить сервис платежей:

```sh
docker pull postgres:17.4 && docker run -d --name pg_pmnts -p 5433:5432 -e POSTGRES_USER=sa -e POSTGRES_PASSWORD=password -e POSTGRES_DB=payments postgres:17.4 && ./gradlew :payments:bootRun
```

И затем сами тесты:

```sh
./gradlew :storefront:integration
```