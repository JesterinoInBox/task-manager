# 📋 Task Manager

Учебный проект — RESTful-сервис для управления задачами. Реализован на **Java 21** с использованием **Spring Boot**, **Hibernate** и **PostgreSQL**. Проект полностью контейнеризирован с помощью **Docker**, миграции БД выполняются через **Liquibase**, а документация API автоматически генерируется через **Swagger**.

> Проект создан в рамках учебной практики. Авторизация пока отсутствует, планируется дальнейшее расширение функционала.

---

## 🛠 Технологии

- **Java 21**
- **Spring Boot 4.1.0**
- **Spring Data JPA (Hibernate)**
- **PostgreSQL**
- **Liquibase** (миграции)
- **Docker** + **Docker Compose**
- **Swagger (OpenAPI 3)** — `springdoc-openapi-starter-webmvc-ui`
- **Lombok**
- **Maven**

  ## В Планах
  
- **Redis** для кеширования
- **Actuator** (мониторинг)

---

## ✨ Возможности

- **CRUD** для сущностей:
  - `User` (пользователь)
  - `Task` (задача)
  - `Category` (категория: Home, Work, Education и т.д.)
- Фильтрация задач по:
  - статусу выполнения (`done`, `todo`, `in_progress` и др.)
  - категории
- **DTO** и мапперы (MapStruct / вручную) — разделение слоёв
- Кастомные исключения и **глобальный обработчик ошибок** (`@RestControllerAdvice`)
- Логирование ключевых операций (через `@Slf4j`)
- Полная документация всех эндпоинтов через **Swagger UI**
- **Liquibase** — управление схемой БД (changelog)
- **Docker Compose** — запуск приложения и PostgreSQL в изолированных контейнерах

---

## 🚀 Запуск проекта

## Требования

  - Docker и Docker Compose
  - Либо Java 21 и Maven (для локальной сборки)

### Вариант 1: Запуск через Docker (рекомендуемый)

1. Клонируйте репозиторий:
   ```sh
   git clone https://github.com/your-username/task-manager.git
   cd task-manager
   ```
   
   Запустите контейнеры:
    ```bash
    docker-compose up -d
    ```
    Будет поднято два контейнера:
   
    postgres — база данных
   
    application — Spring Boot приложение

    Приложение станет доступно по адресу:
    http://localhost:8080

    Swagger UI:
    http://localhost:8080/swagger-ui/index.html

Вариант 2: Локальный запуск (без Docker)

  Убедитесь, что PostgreSQL запущен локально (настройки в application.yml).

  Выполните сборку:
  ```sh
  mvn clean package
  ```
    
   Запустите JAR:
    
  ```java
  java -jar target/task-manager-0.0.1-SNAPSHOT.jar
  ```
    
📚 Документация API

После запуска проекта документация доступна в формате OpenAPI по адресу:

- Swagger UI: /swagger-ui/index.html
    
- JSON спецификация: /v3/api-docs

Основные эндпоинты (примеры):

Метод	Путь	Описание

GET	/api/tasks	Получить все задачи

GET	/api/tasks/status/{status}	Получить задачи по статусу

GET	/api/tasks/category/{id}	Получить задачи по категории

POST	/api/tasks	Создать новую задачу

PUT	/api/tasks/{id}	Обновить задачу

DELETE	/api/tasks/{id}	Удалить задачу

...	...	... (см. Swagger)

📁 Структура проекта (основные пакеты)
```text

src/
├── main/
│   ├── java/com/jesterino/task_manager/
│   │   ├── controller/       # REST-контроллеры
│   │   ├── service/          # Бизнес-логика
│   │   ├── repository/       # JPA-репозитории
│   │   ├── model/            # Сущности (Entity)
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── mapper/           # Мапперы (Entity ↔ DTO)
│   │   ├── exception/        # Кастомные исключения и глобальный обработчик
│   └── resources/
│       ├── application.yml   # Конфигурация приложения
└── test/                     # Тесты (JUnit, Mockito)
```

🧪 Планы по развитию

  ✅ Интеграция Redis для кеширования запросов и сравнение производительности

  ✅ Добавление полноценной авторизации (Spring Security + JWT)

  ✅ Реализация пагинации и сортировки

  ✅ Написание юнит- и интеграционных тестов

  ✅ Подключение мониторинга (Prometheus + Grafana)
