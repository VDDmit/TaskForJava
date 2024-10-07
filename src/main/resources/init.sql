CREATE TABLE IF NOT EXISTS coffee
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(255),
    price DECIMAL
);

CREATE TABLE IF NOT EXISTS customer
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS orders
(
    id          SERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customer (id)
);

CREATE TABLE IF NOT EXISTS order_item
(
    id        SERIAL PRIMARY KEY,
    order_id  BIGINT REFERENCES orders (id),
    coffee_id BIGINT REFERENCES coffee (id),
    quantity  INT
);

INSERT INTO coffee (name, price)
VALUES ('Latte', 3.50);
INSERT INTO coffee (name, price)
VALUES ('Espresso', 2.00);

INSERT INTO customer (name, email)
VALUES ('John Doe', 'john@example.com');
INSERT INTO customer (name, email)
VALUES ('Jane Doe', 'jane@example.com');
