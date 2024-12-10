<div align="center">
  <h1>CLOTHES STORE API - VICTOR MANUEL CASTILLO</h1>
</div>
<br />
<div align="center">
<h3 align="center">SPRING BOOT API</h3>
  <p align="center">
   This project is a REST API designed for a fictional company called Clothesstore. Its purpose is to manage and create products while allowing the hosting and resizing of their images using the AWS S3 cloud storage service.
  </p>
</div>

### Built with

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
* ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
* ![Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
* ![Amazon S3](https://img.shields.io/badge/Amazon%20S3-FF9900?style=for-the-badge&logo=amazons3&logoColor=white)
* ![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?&style=for-the-badge&logo=mysql&logoColor=white)

### Project Structure
This project follows a standard layout for a Spring Boot application

* Persistence: Manages the database layer, including repository and entity definitions.
* Presentation: Handles incoming HTTP requests through REST controllers.
* Service: Implements the business logic.
* Tests: Includes unit and integration tests.


### Prerequisites

* JDK 17 https://jdk.java.net/java-se-ri/17
* Maven https://maven.apache.org/install.html
* MySQL https://dev.mysql.com/downloads/installer/

### Recommended Tools
* IntelliJ IDEA https://www.jetbrains.com/idea/download/
* Postman https://www.postman.com/downloads/

### Installation
1. Clone Repository
 ```sh
    https://github.com/CastilloDev4/Clothesstore.git
 ```
2. Create a new Database in MySQL called "clothesstore_db"
3. Create an S3 bucket in your AWS account called "clothesstore-images".
4.  Update the path of previously cloned configurations
 ```sh
    #APPLICACION.PROPERTIES    ---MYSQL CONFIG
    spring.datasource.url=jdbc:mysql://localhost:3306/clothesstore_db
    spring.datasource.username=<<YOUR USERNAME>>
    spring.datasource.password=<<YOUR PASSWORD>
 ```
```sh
   #AWS CONFIG
    aws.access.key=<<YOUR ACCESS KEY>>
    aws.secret.key=<<YOUR SECRET_KEY>>
    aws.bucket.name=clothesstore-images
```

5. Run the API
* The API will be available at http://localhost:8080 by default

### API Documentation
* This project includes OpenAPI documentation (Swagger).
* Once the API is running, you can access the documentation at:
http://localhost:8080/swagger-ui.html




### Tests
Unit tests
- Right-click the test folder and choose Run tests with coverage
