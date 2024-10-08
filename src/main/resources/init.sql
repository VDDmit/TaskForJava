-- Создание таблицы для кофе
CREATE TABLE IF NOT EXISTS coffee
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(255),
    price DECIMAL
);

-- Создание таблицы для клиентов
CREATE TABLE IF NOT EXISTS customer
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(255),
    email VARCHAR(255)
);

-- Создание таблицы для заказов
CREATE TABLE IF NOT EXISTS orders
(
    id          SERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customer (id)
);

-- Создание таблицы для элементов заказа
CREATE TABLE IF NOT EXISTS order_item
(
    id        SERIAL PRIMARY KEY,
    order_id  BIGINT REFERENCES orders (id),
    coffee_id BIGINT REFERENCES coffee (id),
    quantity  INT
);

-- Вставка данных в таблицу кофе
INSERT INTO coffee (name, price)
VALUES ('Latte', 3.50);

INSERT INTO coffee (name, price)
VALUES ('Espresso', 2.00);

-- Вставка данных в таблицу клиентов
INSERT INTO customer (name, email)
VALUES ('John Doe', 'john@example.com');

INSERT INTO customer (name, email)
VALUES ('Jane Doe', 'jane@example.com');

-- Заказ для клиента с id = 1 (John Doe)
INSERT INTO orders (customer_id)
VALUES (1);

-- Заказ для клиента с id = 2 (Jane Doe)
INSERT INTO orders (customer_id)
VALUES (2);

--ссылаемся на существующие заказы и кофе
-- Элементы заказа для заказа с id = 1 (John Doe)
INSERT INTO order_item (order_id, coffee_id, quantity)
VALUES (1, 1, 2); -- 2 порции Латте для John Doe

INSERT INTO order_item (order_id, coffee_id, quantity)
VALUES (1, 2, 1);
-- 1 порция Эспрессо для John Doe

-- Элементы заказа для заказа с id = 2 (Jane Doe)
INSERT INTO order_item (order_id, coffee_id, quantity)
VALUES (2, 1, 1); -- 1 порция Латте для Jane Doe

INSERT INTO order_item (order_id, coffee_id, quantity)
VALUES (2, 2, 3); -- 3 порции Эспрессо для Jane Doe
