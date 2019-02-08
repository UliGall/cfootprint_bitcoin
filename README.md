# The Carbon Footprint of Bitcoin
###### Christian Stoll, Lena Klaaßen, Ulrich Gallersdörfer 

This project provides additional information (source code, database scheme) for the article **The Carbon Footprint of Bitcoin** by *Christian Stoll, Lena Klaaßen, and Ulrich Gallersdörfer*. This project consists out of two applications:
  - SlushPool data generation (can be found in /SlushPool)
  - BlockCypher data generation (can be found in /BlockCypher)

## SlushPool
This tool crawls the hash rate per location and hash rate distribution data from Slushpool and saves it into a local folder as .csv files. The UML-model can be viewed [here](https://github.com/UliGall/cfootprint_bitcoin/blob/master/SlushPool/UML.pdf).
#### Prerequisites
- Java 8 JDK
- JavaFX (Oracle JDK has JavaFX included)
- Google GSON

#### Installation
- Install the Google GSON library from [here](https://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.8.5/gson-2.8.5.jar)
	```sh
	cd /cfootprint_bitcoin/SlushPool/
	```
	```sh
	wget -O gson-2.8.5.jar https://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.8.5/gson-2.8.5.jar
	```
- Compile files
	```sh
	javac -cp gson-2.8.5.jar src/model/distribution/*.java src/model/location/*.java src/controller/*.java
	```

#### Execution

	```sh
	cd src/
	```
	```sh
	java -cp ../gson-2.8.5.jar:. controller.App
	```



## BlockCypher
This tool crawls blockdata from BlockCypher and stores it into a MySQL-database. It later queries IPInfo for location information about the miners.

#### Prerequisites
- PHP 7
- *any* MySQL version

#### Installation
- import `scheme.sql` into your database
- adapt settings in `config.php` 

#### Execution
- use CLI (e.g. `php`) to execute
    - `php getBlocks.php` to generate block data
    - `php getLocation.php` to generate location data