# Charity Fundraising Application

A backend application for managing collection boxes during fundraising events for charity organizations. It allows to register boxes and events, assign them to events, add money, transfer funds, and view financial reports.

## Build and Run Instructions

### Clone the Repository
```bash
git clone https://github.com/magddzi881/charity.git
```

### Build the Project
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

The application will run at 🔗 [`http://localhost:8080`](http://localhost:8080)

---

## Accessing H2 Database Console

To access the H2 database console, open the following URL after the application is running:

🔗 [`http://localhost:8080/h2-console`](http://localhost:8080/h2-console)

### Login Credentials

- **JDBC URL**: `jdbc:h2:mem:charitydb`
- **Username**: `sa`
- **Password**: *(leave empty)*

---

## Tests

Controller tests are available in the `src/test/java/pl/sii/charity/controller/CollectionControllerTest.java`

#### Run tests:
```bash
mvn test
```

### REST API Endpoints Covered in Tests


| Endpoint                              | Method   | Description                                                     |
| ------------------------------------- | -------- | --------------------------------------------------------------- |
| `/api/events`                         | `POST`   | Create a new fundraising event                                  |
| `/api/boxes`                          | `POST`   | Register a new collection box                                   |
| `/api/boxes`                          | `GET`    | List all collection boxes (shows if assigned and if empty)      |
| `/api/boxes/{boxId}`                  | `DELETE` | Remove (delete) a collection box                                |
| `/api/boxes/{boxId}/unregister`       | `POST`   | Unregister a collection box (clears money without transferring) |
| `/api/boxes/{boxId}/assign/{eventId}` | `POST`   | Assign a collection box to a fundraising event                  |
| `/api/boxes/{boxId}/money`            | `POST`   | Add money to a collection box                                   |
| `/api/boxes/{boxId}/transfer`         | `POST`   | Transfer money from box to fundraising event account            |
| `/api/report`                         | `GET`    | Generate a financial report (creates `.pdf` and `.txt` files)   |

---

## CURL Sample Queries
Sample queries are included in `resources/curl.txt`. 

``` bash
# -- Create a new fundraising event
curl -X POST "http://localhost:8080/api/events" -H "Content-Type: application/json" -d "{\"currency\":\"USD\",\"name\":\"Charity Five\"}"
curl -X POST "http://localhost:8080/api/events" -H "Content-Type: application/json" -d "{\"currency\":\"EUR\",\"name\":\"Charity Six\"}"
curl -X POST "http://localhost:8080/api/events" -H "Content-Type: application/json" -d "{\"currency\":\"USD\",\"name\":\"Charity Seven\"}"

# -- Register a new empty collection box
curl -X POST "http://localhost:8080/api/boxes"

# -- List all collection boxes
curl -X GET "http://localhost:8080/api/boxes"

# -- Put money inside a box
curl -X POST "http://localhost:8080/api/boxes/2/money" -H "Content-Type: application/json" -d "{\"currency\":\"EUR\",\"amount\":4.00}"
curl -X POST "http://localhost:8080/api/boxes/1/money" -H "Content-Type: application/json" -d "{\"currency\":\"PLN\",\"amount\":11.20}"
curl -X POST "http://localhost:8080/api/boxes/3/money" -H "Content-Type: application/json" -d "{\"currency\":\"EUR\",\"amount\":24.50}"
curl -X POST "http://localhost:8080/api/boxes/3/money" -H "Content-Type: application/json" -d "{\"currency\":\"PLN\",\"amount\":4.20}"
curl -X POST "http://localhost:8080/api/boxes/3/money" -H "Content-Type: application/json" -d "{\"currency\":\"PLN\",\"amount\":9.10}"

# -- Transfer money to charity
curl -X POST "http://localhost:8080/api/boxes/1/transfer"
curl -X POST "http://localhost:8080/api/boxes/2/transfer"
curl -X POST "http://localhost:8080/api/boxes/3/transfer"
curl -X POST "http://localhost:8080/api/boxes/4/transfer"

# -- Assign box to charity
curl -X POST "http://localhost:8080/api/boxes/5/assign/4"

# -- Delete a box
curl -X DELETE "http://localhost:8080/api/boxes/2"

# -- Unregister a box
curl -X POST "http://localhost:8080/api/boxes/3/unregister"

# -- Generate report
curl -X GET "http://localhost:8080/api/report"

```

---

## SQL Script for Data Insertion
The script for inserting data is available in `src/main/resources/data.sql` When the application starts, it will automatically execute SQL to add sample data from the script:
```sql
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
```
---
