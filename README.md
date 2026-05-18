# Voucher simple manager

## Prerequisites
Install and start redis.

## Building
```
./gradlew clean build
```

## Starting REST server
```
java -jar voucher-manager-0.0.1-SNAPSHOT.jar
```

## Endpoints
Create voucher: POST http://locashost:8080/api/voucher with appropriate payload
Use voucher: PUT http://locashost:8080/api/voucher with appropriate payload

## Architectural Decision Record
* Project kick-off technologies selected:
** Kotlin as modern, versatile and expressive JVM language.
** Gradle as modern and standard dependency manager.
** Spring Boot as standard library suite to build simple service quickly using well-proven DI, REST, JPA libraries
** JDK24 as the latest JDK supporting Kotlin
* Decided to use 10-character long voucher code giving enough 24^10 (6.4E14) codes
* Decided to use Redis as fast in-memory key-value storage matching structure: code+allowedCountries as key and number of uses as value.  

## Not done and deferred
* IP2Country (GeoIP use)
* Add detailed error message on 400
* OpenAPI specification
* Add one REST test
* Dockerise redis and the service itself
* Add performance tests verifying low-latency & high throughput
* Add feature to allow adding non-generated & human-readable codes
