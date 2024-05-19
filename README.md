# Bank Management System App

## Overview
This project is a Bank Management System that allows users to create bank accounts, perform transactions, and manage funds. It supports storing data in a MySQL database with three tables: `Bank`, `Account`, and `Transaction`

## Features
- Create bank accounts with unique IDs.
- Deposit and withdraw funds from accounts.
- Perform transactions between accounts with fee management.
- Track cumulative transaction fees and transfer amounts.
- Display summary of bank's transaction fees and transfer amounts.

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- MySQL Server
- JDBC driver for MySQL

## Database Setup

1. **Create the database and tables**

   - Run the provided `bankapplication.sql` script to create the database and tables.
     ```bash
     mysql -u yourusername -p < schema.sql
     ```

2. **Update the `DbConn.java` class with your MySQL database connection details**

   - Edit the `DbConn.java` file to include your MySQL connection details:
     ```java
     public class DbConn {
         private static Connection con;
         private static  String URL = "jdbc:mysql://localhost:3306/bankapplication";
         private static  String USER = "yourusername";
         private static  String PASSWORD = "yourpassword";
         private static  String DRIVER = "com.mysql.cj.jdbc.Driver"

         public static Connection getConnection() throws SQLException {
             try{
                 Class.forName(DRIVER);
                 con= DriverManager.getConnection(URL,USER,PASSWORD);
             }
             catch(Exception e){
                 System.out.println(e.getMessage());
             }
             return con;
         }
     }
     ```

4. **Compile and run the application**
