-- Elimina le tabelle se esistono
DROP TABLE IF EXISTS Item, OrdersItem, Orders, PaymentMethod, Users CASCADE;

-- Crea la tabella User senza la chiave esterna a PaymentMethod
CREATE TABLE Users (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(50),
                        surname VARCHAR(50),
                        username VARCHAR(50) UNIQUE NOT NULL,
                        age INT CHECK (age >= 0),
                        email VARCHAR(100) UNIQUE NOT NULL,
                        password TEXT NOT NULL,
                        sex CHAR(1),
                        paymentMethod VARCHAR(16) -- Colonna senza vincolo di chiave esterna
);

-- Crea la tabella PaymentMethod
CREATE TABLE PaymentMethod (
                               cardNumber VARCHAR(16) PRIMARY KEY,
                               expirationDate VARCHAR(5) NOT NULL,
                               cvv VARCHAR(4) NOT NULL,
                               ownerName VARCHAR(50) NOT NULL,
                               ownerSurname VARCHAR(50) NOT NULL,
                               withheld FLOAT DEFAULT 0.0
);

-- Aggiungi la chiave esterna a User dopo aver creato PaymentMethod
ALTER TABLE Users
    ADD CONSTRAINT paymentMethod_fk
        FOREIGN KEY (paymentMethod)
            REFERENCES PaymentMethod(cardNumber)
            ON DELETE SET NULL;

-- Crea la tabella Orders
CREATE TABLE Orders (
                        id SERIAL PRIMARY KEY,
                        userId INT NOT NULL,
                        status VARCHAR(20),
                        date VARCHAR(25),
                        FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE
);

-- Crea la tabella Item
CREATE TABLE Item (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      description TEXT,
                      type VARCHAR(50),
                      price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
                      discount DECIMAL(5,2) CHECK (discount BETWEEN 0 AND 100) DEFAULT 0
);

-- Crea la tabella OrdersItem
CREATE TABLE OrdersItem (
                            orderId INT NOT NULL,
                            itemId INT NOT NULL,
                            quantity INT NOT NULL,
                            PRIMARY KEY (orderId, itemId),
                            FOREIGN KEY (orderId) REFERENCES Orders(id) ON DELETE CASCADE,
                            FOREIGN KEY (itemId) REFERENCES Item(id) ON DELETE CASCADE

);

--Aggiunge l'Admin
INSERT INTO Users (id, username, email, password) VALUES (0, 'ADMIN', '', 'admin');