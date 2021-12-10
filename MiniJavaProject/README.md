# Star Hotel's Restaurant Manage System

MiniJavaProject - Developed by Luís Felipe Ortolan. 

This project implements a manage system for "Star-Hotel"'s Restaurant. The specifications are set in the "Mini Project - Java.docx" and were provided by Techademy.

This project integrates Java and MySql. To run this project, you need to run the commands on /src/com/java/starhotel/SqlCommands.sql and you must change the variables "url", "username" and "password" on CartDaoSqlImpl.java and MenuItemDaoSqlImpl.java with your own information.

All java files can be found under /src/com/java. Class.java is the main class, where the interface for the program is run, /starhotel/dao is where data is manipulated /starhotel/model is the Classes that implements the classes MenuItem and Cart and /starhotel/util implements a conversor from String to Date.

It's important to understand that the SQL commands will persists after the program is exited, but the collection data will be reset every time the program is exited.