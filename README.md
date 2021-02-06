This repository is a graduation project of the [topjava internship](https://javaops.ru/view/topjava).

# Technologies used

* SpringMVC
* Spring Data JPA + Hibernate + HSQLDB
* Spring Security
* Spirng Test, JUnit5
* Bean validation + Binding
* Ehcache
* logback

# The task

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it.

-----------------------------
P.S.: Make sure everything works with latest version that is on github :)

P.P.S.: Asume that your API will be used by a frontend developer to build frontend on top of that.

# Build and run

1. Build an image with docker
```sh
docker build -t vote4lunch .
```

2. Run the docker image
```sh
docker run -p 8080:8080 vote4lunch
```

3. Swagger documentation will be available at http://localhost:8080/vote4lunch/swagger-ui.html

Credentials
- User:
    - login: user@mail.com
    - password: user
- Admin:
    - login: admin@mail.com
    - password: admin

# REST API curl examples

## Restaurants API

### Get all
```sh
curl --location \
    --request GET 'http://localhost:8080/vote4lunch/rest/restaurants' \
    --user user@mail.com:user
```

### Get
```sh
curl --location \
    --request GET 'http://localhost:8080/vote4lunch/rest/restaurants/100002' \
    --user user@mail.com:user
```

### Get with dishes
```sh
curl --location \
    --request GET 'http://localhost:8080/vote4lunch/rest/restaurants/with-dishes' \
    --user user@mail.com:user
```

### Create
```sh
curl --location \
    --request POST 'http://localhost:8080/vote4lunch/rest/restaurants' \
    --header 'Content-Type: application/json' \
    --user admin@mail.com:admin \
    --data-raw '{
        "name": "Hot pizza"
    }
    '
```

### Update
```sh
curl --location \
    --request PUT 'http://localhost:8080/vote4lunch/rest/restaurants/100002' \
    --header 'Content-Type: application/json' \
    --user admin@mail.com:admin \
    --data-raw '{
        "id": 100002,
        "name": "Burger King"
    }
    '
```

### Delete
```sh
curl --location \
    --request DELETE 'http://localhost:8080/vote4lunch/rest/restaurants/100002' \
    --user admin@mail.com:admin
```

### Access denied error
```sh
curl --location \
    --request POST 'http://localhost:8080/vote4lunch/rest/restaurants' \
    --header 'Content-Type: application/json' \
    --user user@mail.com:user \
    --data-raw '{
        "name": "Hot pizza"
    }
    '
```

## Dishes API

### Get all
```sh
curl --location \
    --request GET 'http://localhost:8080/vote4lunch/rest/dishes?restaurantId=100002' \
    --user user@mail.com:user
```

### Get
```sh
curl --location \
    --request GET 'http://localhost:8080/vote4lunch/rest/dishes/100004' \
    --user user@mail.com:user
```

### Create
```sh
curl --location \
    --request POST 'http://localhost:8080/vote4lunch/rest/dishes' \
    --header 'Content-Type: application/json' \
    --user admin@mail.com:admin \
    --data-raw '{
        "restaurantId": 100002,
        "name": "Super hot pizza",
        "price": 15
    }
    '
```

### Update
```sh
curl --location \
    --request PUT 'http://localhost:8080/vote4lunch/rest/dishes/100004' \
    --header 'Content-Type: application/json' \
    --user admin@mail.com:admin \
    --data-raw '{
        "restaurantId": 100002,
        "name": "Tasty nuggets",
        "price": 20
    }
    '
```

### Delete
```sh
curl --location \
    --request DELETE 'http://localhost:8080/vote4lunch/rest/dishes/100004' \
    --user admin@mail.com:admin
```

### Validation error
```sh
curl --location \
    --request POST 'http://localhost:8080/vote4lunch/rest/dishes' \
    --header 'Content-Type: application/json' \
    --user admin@mail.com:admin \
    --data-raw '{
        "restaurantId": 100002,
        "name": "",
        "price": 0
    }
    '
```

## Votes API

### Get by date
```sh
curl --location \
    --request GET 'http://localhost:8080/vote4lunch/rest/votes' \
    --user user@mail.com:user
```

### Get
```sh
curl --location \
    --request GET 'http://localhost:8080/vote4lunch/rest/votes/100007' \
    --user user@mail.com:user
```

### Create
```sh
curl --location \
    --request POST 'http://localhost:8080/vote4lunch/rest/votes?restaurantId=100003' \
    --user user@mail.com:user
```

### Update
```sh
curl --location \
    --request PUT 'http://localhost:8080/vote4lunch/rest/votes?restaurantId=100003' \
    --user user@mail.com:user \
```

### Delete
```sh
curl --location \
    --request DELETE 'http://localhost:8080/vote4lunch/rest/votes/100007' \
    --user user@mail.com:user
```
