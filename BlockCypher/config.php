<?php
/**
 * config.php
 * 
 * 
 * Enter MySQL-database credentials and your token for blockcypher / ipinfo.
 * 
 * @author Ulrich Gallersdörfer
 * 
 */
error_reporting(E_ALL ^ E_WARNING ^ E_NOTICE);

//Enter your token for blockcypher here
$token_blockcypher = "";

//Enter your token for ipinfo here
$token_ipinfo = "";

//Insert your MySQL credentials here
$host = "";
$usr ="";
$pw ="";
$db_name = "";

//Connect to database
$db = new mysqli($host,$usr,$pw,$db_name);
if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}

//Set Correct UTF-8
$db->query("SET NAMES 'utf8' COLLATE 'utf8_unicode_ci'") or die($db->error);

//Enable db globally
$GLOBALS['db'] = $db;
?>