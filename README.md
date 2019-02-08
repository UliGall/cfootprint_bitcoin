# The Carbon Footprint of Bitcoin
###### Christian Stoll, Lena Klaaßen, Ulrich Gallersdörfer 

This project provides additional information (source code, database scheme) for the article **The Carbon Footprint of Bitcoin** by *Christian Stoll, Lena Klaaßen, and Ulrich Gallersdörfer*. This project consists out of two applications:
  - SlushPool data generation (can be found in /SlushPool)
  - BlockCypher data generation (can be found in /BlockCypher)

## SlushPool
This tool crawls the hash rate per location and hash rate distribution data from Slushpool and saves it into a local folder as .csv files. The UML-model can be viewed [here](https://github.com/UliGall/cfootprint_bitcoin/blob/master/SlushPool/UML.pdf).
#### Prerequisites


#### Installation


#### Execution


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