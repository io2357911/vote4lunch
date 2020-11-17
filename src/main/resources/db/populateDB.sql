DELETE
FROM votes;
DELETE
FROM dishes;
DELETE
FROM restaurants;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password, role)
VALUES ('User', 'user@yandex.ru', 'password', 'USER'),
       ('Admin', 'admin@gmail.com', 'admin', 'ADMIN');

INSERT INTO restaurants (name)
VALUES ('McDonald''s'),
       ('KFC');

INSERT INTO dishes (name, date, price, restaurant_id)
VALUES ('Big Mac', '2020-01-30 00:00:00', 199, 100002),
       ('Chicken Nuggets', '2020-01-31 00:00:00', 99, 100002),
       ('Original bucket', '2020-01-30 00:00:00', 79, 100003);

INSERT INTO votes (date, user_id, restaurant_id)
VALUES ('2020-01-30 00:00:00', 100000, 100002),
       ('2020-01-31 00:00:00', 100000, 100003),
       ('2020-01-30 00:00:00', 100001, 100002);
