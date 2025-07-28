CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255),
    password VARCHAR(255),
    kakao_access_token VARCHAR(500),
    kakao_id BIGINT UNIQUE
);

CREATE TABLE IF NOT EXISTS wishes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    UNIQUE (member_id, product_id),
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS product_option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    option_type VARCHAR(255) NOT NULL,
    option_value VARCHAR(255) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    UNIQUE (product_id, option_type, option_value),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    option_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    orderDateTime DATETIME NOT NULL,
    message VARCHAR(500),
    FOREIGN KEY (member_id) REFERENCES members(id)
);

INSERT INTO products (name, price, image_url) VALUES ('스타벅스 아메리카노 Tall', 4500, 'image.com');
INSERT INTO products (name, price, image_url) VALUES ('배스킨라빈스 파인트', 8900, 'image.com');
INSERT INTO products (name, price, image_url) VALUES ('교촌치킨 허니콤보', 20000, 'image.com');
INSERT INTO product_option (product_id, option_type, option_value, quantity) VALUES (1, 'HOT/ICE', 'HOT', 2000);
INSERT INTO product_option (product_id, option_type, option_value, quantity) VALUES (1, 'HOT/ICE', 'ICE', 2000);
INSERT INTO product_option (product_id, option_type, option_value, quantity) VALUES (1, '샷', '샷 추가', 500);
INSERT INTO product_option (product_id, option_type, option_value, quantity) VALUES (2, 'size', '싱글레귤러', 500);
INSERT INTO product_option (product_id, option_type, option_value, quantity) VALUES (2, 'size', '더블주니어', 2000);
INSERT INTO product_option (product_id, option_type, option_value, quantity) VALUES (3, 'size', '순살 변경', 3000);
