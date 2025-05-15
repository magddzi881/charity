-- Schema
CREATE TABLE fundraising_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    amount DECIMAL(19, 4) DEFAULT 0
);

CREATE TABLE collection_box (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assigned BOOLEAN NOT NULL,
    event_id BIGINT,
    CONSTRAINT fk_collection_box_event FOREIGN KEY (event_id) REFERENCES fundraising_event(id)
);

CREATE TABLE collection_box_money (
    collection_box_id BIGINT NOT NULL,
    currency VARCHAR(50) NOT NULL,
    amount DECIMAL(19, 4),
    PRIMARY KEY (collection_box_id, currency),
    CONSTRAINT fk_collection_box_money_collection_box FOREIGN KEY (collection_box_id) REFERENCES collection_box(id)
);

-- Create fundraising events
INSERT INTO fundraising_event (name, currency) VALUES ('Charity One', 'EUR');
INSERT INTO fundraising_event (name, currency) VALUES ('Charity Two', 'USD');
INSERT INTO fundraising_event (name, currency) VALUES ('Charity Three', 'USD');
INSERT INTO fundraising_event (name, currency) VALUES ('Charity Four', 'PLN');

-- Create collection boxes
INSERT INTO collection_box (assigned) VALUES (FALSE);
INSERT INTO collection_box (assigned) VALUES (FALSE);
INSERT INTO collection_box (assigned) VALUES (FALSE);
INSERT INTO collection_box (assigned) VALUES (FALSE);
INSERT INTO collection_box (assigned) VALUES (FALSE);

-- Assign few boxes to events
UPDATE collection_box SET assigned = TRUE, event_id = 1 WHERE id = 1;
UPDATE collection_box SET assigned = TRUE, event_id = 2 WHERE id = 2;
UPDATE collection_box SET assigned = TRUE, event_id = 2 WHERE id = 3;
UPDATE collection_box SET assigned = TRUE, event_id = 3 WHERE id = 4;

-- Add money to collection boxes
INSERT INTO collection_box_money (collection_box_id, currency, amount) VALUES (1, 'EUR', 50.00);
INSERT INTO collection_box_money (collection_box_id, currency, amount) VALUES (2, 'USD', 20.00);
INSERT INTO collection_box_money (collection_box_id, currency, amount) VALUES (2, 'EUR', 10.00);
INSERT INTO collection_box_money (collection_box_id, currency, amount) VALUES (3, 'USD', 5.50);
INSERT INTO collection_box_money (collection_box_id, currency, amount) VALUES (4, 'PLN', 100.00);

-- View all tables
SELECT * FROM FUNDRAISING_EVENT;
SELECT * FROM COLLECTION_BOX ;
SELECT * FROM COLLECTION_BOX_MONEY;