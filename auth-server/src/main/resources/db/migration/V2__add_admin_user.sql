INSERT INTO users (email, role, password, activated, phone_number, first_name, last_name, patronymic,
                   profile_image_path, deleted, created_at)
VALUES ('admin', 'ROLE_ADMIN', '$2a$10$ca9nY6S1iSocz9cJ8moT7uLyZEmvh2fQZeSt4MXHyPei7V6Myv2Ka', TRUE, '+996509091625',
        'Нияз', 'Кабылов', 'Саткынович',
        'https://www.thespruce.com/thmb/-SEvcDRp722ZhpMzLjxgR0HkxuU=/6016x0/filters:no_upscale():max_bytes(150000):strip_icc()/plants-with-big-flowers-4138211-hero-b10becb169064cc4b3c7967adc1b22e1.jpg', FALSE, NOW())