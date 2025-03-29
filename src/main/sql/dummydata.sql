-- Inserisci dati nella tabella PaymentMethod
INSERT INTO PaymentMethod (cardNumber, expirationDate, cvv, ownerName, ownerSurname, withheld)
VALUES
    ('1234567812345678', '05/26', '123', 'Tommaso', 'Ticci', 0.1),
    ('8765432187654321', '06/28', '456', 'Guillermo', 'Cognome', 0.2),
    ('1111222233334444', '05/25', '830', 'Alexandru', 'Burciu', 0.1);

-- Inserisci dati nella tabella Users
INSERT INTO Users (name, surname, username, age, email, password, sex, paymentMethod)
VALUES
    ('Tommaso', 'Ticci', 'TonkyTc', 21, 'tonkytc@email.com', 'myPassword', 'M', '1234567812345678'),
    ('Guillermo', 'Cognome', 'guillermo', 22, 'guillermo123@email.com', 'pollo.KFC', 'M', '8765432187654321'),
    ('Mattia', 'Lombardo', 'Gurkuz', 20, 'lombardomattia@email.com', 'palestra', 'M', NULL), -- Nessun metodo di pagamento
    ('Alexandru', 'Burciu', 'ale30', 20, 'bublinadga@email.com', 'bublinadga@email.com', 'M', '1111222233334444');

-- Inserisci dati nella tabella Orders
INSERT INTO Orders (userId, status, date)
VALUES
    (1, 'Received', '2025-03-24 10:30:00'),
    (2, 'In Progress', '2025-03-23 14:15:00'),
    (1, 'Completed', '2025-03-22 18:45:00'),
    (4, 'Received', '2025-03-22 19:45:00');

-- Inserisci dati nella tabella Item
INSERT INTO Item (name, description, type, price, discount)
VALUES
    ('The Standard', 'Hamburger with beef', 'Food', 5.00, 10.00),
    ('The Big One', 'Cheeseburger with beef, salad and tomatoes', 'Food', 7.50, 00.00),
    ('Pollo Hermano', 'Chicken burger with spicy sauce', 'Food', 9.00, 20.00),
    ('Water', 'Water (500ml)', 'Drink', 0.50, 0.00),
    ('Coke', 'Coke (500ml)', 'Drink', 1.20, 0.00),
    ('Cheesecake', 'A slice of cheesecake covered in berry jam', 'Dessert', 6.00, 10.00);

-- Inserisci dati nella tabella OrdersItem (associazione tra ordini e prodotti)
INSERT INTO OrdersItem (orderId, itemId, quantity)
VALUES
    (1, 3, 1),
    (1, 4, 1),
    (1, 6, 2),
    (2, 2, 3),
    (2, 4, 1),
    (3, 5, 2),
    (4, 6, 1);