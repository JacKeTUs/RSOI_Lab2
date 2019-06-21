# RSOI_Lab2
2я лабораторная работа по РСОИ

Микросервисы:
1. Песни (Books)
2. Пользователи (Users)
3. Покупки (Purchases)
4. Шлюз (Gateway)

Суть сервиса: Покупка песен пользователями. Хранение песен в аккаунтах пользователей. Оценка песен пользователями.
 

Публичные запросы:
* GET /api/books?page=X&size=Y - получение списка песен.
* GET /api/books/{id} - получение информации об одной песне.
* GET /api/users/{id} - получение информации о пользователе.
* GET /api/users/{id}/books - получение списка песен одного пользователя. Получение данных с Users и Purchases.
* GET /api/users/{id}/books/{id} - информация о купленной пользователем песне, о её рейтинге.
* GET /api/purchase/{id} - получение информации об оплате. 
* POST /api/purchase - покупка песни. Обновление данных сервиса Books, User, Purchases. В теле - информация о пользователе, информация о песне.
* POST /api/users - создать пользователя. Обновление данных сервиса Users. В теле - пользователь.
* POST /api/users/{id}/books/{id}/rate - оценка песни. Обновление данных сервисов Books и Purchases.
* POST /api/books - добавление песни. Обновление Books. В теле - песня.



Подготовка - создание БД и пользователя программы:

```
psql -c "create database books_db" -U postgres
psql -c "create database users_db" -U postgres
psql -c "create database purchases_db" -U postgres
psql -c "create role program WITH password 'test'" -U postgres
psql -c "grant all privileges ON database books_db TO program" -U postgres
psql -c "grant all privileges ON database users_db TO program" -U postgres
psql -c "grant all privileges ON database purchases_db TO program" -U postgres
psql -c "alter role program WITH login" -U postgres
```

Добавление песни:

```curl -X POST -i --header "content-type: application/json" -d '{"artist":"Sample Artist", "name":"Sample name", "link":"Sample Link"}' "localhost:8088/api/books"```


Добавление пользователя:

```curl -X POST -i --header "content-type: application/json" -d '{"login":"Sample_Login", "name":"Sample_username"}' "localhost:8088/api/users"```


Покупка песни:

```curl -X POST -i --header "content-type: application/json" -d '{"userID":1, "bookID":1}' "localhost:8088/api/purchase"```


Оценка песни:

```curl -X POST "localhost:8088/api/users/1/books/1/rate?rating=4"```
