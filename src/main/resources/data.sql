INSERT INTO products (name, price, image_url)
VALUES ('스타벅스 아메리카노 Tall', 4500, 'image.com');
INSERT INTO products (name, price, image_url)
VALUES ('배스킨라빈스 파인트', 8900, 'image.com');
INSERT INTO products (name, price, image_url)
VALUES ('교촌치킨 허니콤보', 20000, 'image.com');
INSERT INTO product_option (product_id, option_type, option_value, quantity)
VALUES (1, 'HOT/ICE', 'HOT', 2000);
INSERT INTO product_option (product_id, option_type, option_value, quantity)
VALUES (1, 'HOT/ICE', 'ICE', 2000);
INSERT INTO product_option (product_id, option_type, option_value, quantity)
VALUES (1, '샷', '샷 추가', 500);
INSERT INTO product_option (product_id, option_type, option_value, quantity)
VALUES (2, 'size', '싱글레귤러', 500);
INSERT INTO product_option (product_id, option_type, option_value, quantity)
VALUES (2, 'size', '더블주니어', 2000);
INSERT INTO product_option (product_id, option_type, option_value, quantity)
VALUES (3, 'size', '순살 변경', 3000);
