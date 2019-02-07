# The Carbon Footprint of Bitcoin
###### Christian Stoll, Lena Klaaßen, Ulrich Gallersdörfer 


## Overview
This project provides additional information (source code, database scheme) for the article **The Carbon Footprint of Bitcoin** by *Christian Stoll, Lena Klaaßen, and Ulrich Gallersdörfer* . This project consists out of two applications:
  - SlushPool data generation (can be found in /SlushPool)
  - BlockCypher data generation (can be found in /BlockCypher)

## SlushPool
### Overview
You can check the model [here](https://github.com/UliGall/cfootprint_bitcoin/blob/master/SlushPool/UML.pdf).
#### Prerequisites


#### Installation


#### Execution


## BlockCypher
### Overview
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