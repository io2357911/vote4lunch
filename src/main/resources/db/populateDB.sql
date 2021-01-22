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
VALUES ('User', 'user@mail.com', '{noop}user', 'USER'),
       ('Admin', 'admin@mail.com', '{noop}admin', 'ADMIN');

INSERT INTO restaurants (name)
VALUES ('McDonald''s'),
       ('KFC');

INSERT INTO dishes (name, created, price, restaurant_id)
VALUES ('Big Mac', now(), 199, 100002),
       ('Chicken Nuggets', '2020-01-31', 99, 100002),
       ('Original bucket', now(), 79, 100003);

INSERT INTO votes (created, user_id, restaurant_id)
VALUES (now(), 100000, 100002),
       ('2020-01-31', 100000, 100003),
       ('2020-01-31', 100001, 100002);
