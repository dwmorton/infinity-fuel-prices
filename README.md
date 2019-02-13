# Web Service Code Test - Fuel Prices

Implementation of the "Fuel Prices" coding test.
Fully working, tested & hosted as requested.

## Authors
* [David Morton](mailto:david@infinitycontracting.com) (Infinity Contracting Ltd.)

## Main Technologies Used
* Java 8
* Spring Boot 2.1.2
* Maven
* JUnit 4
* Mockito
* Hamcrest
* JetBrains IntelliJ IDEA Ultimate 2018.2
* Swagger / OpenAPI

## Build & Test
```
./mvnw clean install
```
## Run
```
./mvnw spring-boot:run
```
## Usage
N.B. The input parameter "fuelType" accepts the following values:
* "ULSP" = Petrol
* "ULSD" = Diesel
### Running locally
[http://localhost:8080/fuelcost?date=2019-01-01&fuelType=ULSP&milesPerGallon=25&mileage=100](http://localhost:8080/fuelcost?date=2019-01-01&fuelType=ULSP&milesPerGallon=25&mileage=100)
```
{"fuelCost":3887,"dutyPaid":1264,"deltaToday":-22}
```
### Publically Hosted at Heroku
[https://infinity-fuel-prices.herokuapp.com/fuelcost?date=2010-01-01&fuelType=ULSP&milesPerGallon=25&mileage=200](https://infinity-fuel-prices.herokuapp.com/fuelcost?date=2010-01-01&fuelType=ULSP&milesPerGallon=25&mileage=200)
```
{"fuelCost":3887,"dutyPaid":1264,"deltaToday":-22}
```

## ToDo / Implementation Comments
* Fuel prices are stored in a TreeMap of HashMaps, which was chosen for code readability and simplicity of price lookup.
* Prices are stored internally as BigDecimal to avoid currency rounding errors associated with floats, at the expense of execution speed and code readability.
* The wrong priceData.csv file is injected in the unit tests (the src/main/resources version is used), due to an unresolved issue with the SpringBoot application.properties. 
* Add Spring Boot integration tests against REST endpoint (with mocked dependencies)
* Enable endpoint security
* Implement HATEOAS
* Implement REST endpoint versioning


## License
This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details

## OpenAPI
See [openapi.yamp](openapi.yaml)
```
swagger: '2.0'
info:
  description: Fuel Cost API
  version: 1.0.0
  title: Fuel Cost API
  contact:
    email: david@infinitycontracting.com
  license:
    name: MIT License
    url: http://opensource.org/licenses/MIT
host: "infinity-fuel-prices.herokuapp.com"
paths:
  /fuelcost:
    get:
      summary: calculates fuel cost, duty paid and delta today
      operationId: fuelcost
      produces:
      - application/json
      parameters:
      - name: date
        in: query
        description: date on which journey was made
        required: true
        type: string
        format: date
      - name: fuelType
        in: query
        description: Fuel type used
        required: true
        type: string
        enum: [ULSP, ULSD]
      - name: milesPerGallon
        in: query
        description: Vehicle fuel efficiency in miles per UK gallon
        required: true
        type: number
      - name: mileage
        in: query
        description: Distance travelled in miles
        required: true
        type: number       
      responses:
        200:
          description: Fuel cost details
          schema:
            type: object
            example: {"fuelCost":3887,"dutyPaid":1264,"deltaToday":-22}
            items:
              $ref: '#/definitions/FuelCost'
        400:
          description: bad input parameter
definitions:
  FuelCost:
    type: object
    required:
    - fuelCost
    - dutyPaid
    - deltaToday
    properties:
      fuelCost:
        type: integer
        description: Total fuel cost in UK pence (£0.01)
        example: 1234
      dutyPaid:
        type: integer
        description: Total UK duty paid in UK pence (£0.01)
        example: 1234
      deltaToday:
        type: integer
        description: How much cheaper/more expensive would the journey have been today?
        example: -200

```

