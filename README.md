# TodoList API

## Table of contents
- Introduction
- Requirements
- Installation
- Configuration
- Developers
- Maintainers
  
## Introduction

This project is a basic api where you can make request to create, read, update and delete (CRUD) items from a list.

## Requirements

You have to have java and some IDE to run it, and a mysql server to host your local database.
If you dont have these things, you can install them following their docs.

[java]: https://docs.oracle.com/en/java/javase/11/install/installation-jdk-microsoft-windows-platforms.html#GUID-A7E27B90-A28D-4237-9383-A58B416071CA

[mysql]: https://dev.mysql.com/doc/mysql-installation-excerpt/8.0/en/windows-install-archive.html

- Java JDK: [java]
- Mysql: [mysql]

## Installation

1. Clone the repo
   ```sh
   git clone https://github.com/AlanCanalesM/TodoLIstAPI-Spring.git
   ```

[mysql-acces]: https://learn.microsoft.com/en-us/answers/questions/511134/how-to-access-mysql-from-terminal.html
2. Open your mysql terminal (if you have problems with this you should read this) -> [mysql-acces]
   ```sh
   mysql -u root -p
   ```

3. Once you acces to mysql, you have to create a dabatase called "items"
   ```sh
   create database items;
   ```


4. Go to file src/main/resources/application.properties and you have to change the credentials of the database to your own
   ```sh
   spring.datasource.url=jdbc:mysql://localhost:3306/items
   spring.datasource.username=(here your username)
   spring.datasource.password=(here your password)
   spring.jpa.hibernate.ddl-auto=update
   ```
## Test

To test this project you have to run the file "TodolistapiApplication.java" and try to execute the following curl's commands in your terminal





