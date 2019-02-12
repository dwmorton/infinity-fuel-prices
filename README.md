**Web Service Code Test - Fuel Prices**

**Build & Test**

./mvnw clean install

**Run**

./mvnw spring-boot:run

Example URL:
http://localhost:8080/getFuelCost?date=2019-01-01&fuelType=ULSP&milesPerGallon=25&mileage=100

JSON Output: {"fuelCost":3887,"dutyPaid":1264,"deltaToday":-22}

N.B. Output values are in UK pence (divide by 100 for UKP)
