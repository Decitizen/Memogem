##Memogem Program Structure

MemoGem is structured following good design principles. Main higher abstractional responsibilities are separated into three packages: 

1.	Core Application-package (memogem.coreapplication)

2.	Database-package (memogem.carddatabase)

3.	User interface (memogem.ui)


###Core application structure

Consists of three different type of classes:

1.	Card, CardStats, Set, Tag – which hold information about objects that can be created out of them

2.	CardEngine – is responsible for the logic of the program

3.	Other classes which work between and are used by the earlier classes.


###Database structure

Consists of three different type of classes:

1.	Database, holds information about the cards in work-memory and executes commands to other classes in the package

2.	CardDAO, SetDAO, DatabaseLoaderDAO and TagDAO implement the Dao interface and query the SQL database

3.	InitDB is used to initialize new SQL-database if not found on the startup


###User interface structure

Class MainApp is the main-class of the program. It creates the database and launches the MainWindow which is the GUI’s parent-class and takes care of the creation of other GUI-classes such as EdiCardWindow and StudyWindow.
