<?php
include "config.php";

/**
 * getBlocks.php
 * 
 * 
 * crawls block data from BlockCypher
 * 
 * @author Ulrich Gallersdörfer
 * 
 */

// Generates the next block to crawl by getting information from the MySQL database.
function getNextBlockID($db) {
	$sql = "SELECT height FROM blocks ORDER BY HEIGHT DESC LIMIT 1";
	$result = $db->query($sql);
	$return = $result->fetch_assoc();
	return $return["height"]+1;
}

//Returns the JSON-file about a specific block.
//id = BlockHeight
//t = token
function getBlock($id, $t) {
	$url = "https://api.blockcypher.com/v1/btc/main/blocks/".$id."?token=".$t;
	return file_get_contents($url);
} 

//Generate an array which contains all relevant blocks for us
$arr = range(getNextBlockID($db), 551606);

//Iterate over all blocks in our array
foreach($arr as $height) {

	//Generate the json-file
	$raw = getBlock($height, $token_blockcypher);

	//Create an object
	$data = json_decode($raw);

	//check if API returns invalid information
	if($raw == false || $data->time == "")
		{
			exit("Reached end or API error.");
		}

	//Create SQL-statement
	$sql = "INSERT INTO `blocks` (`height`, `ipaddress`, `time`, `received_time`) VALUES ('".$height."', '".$data->relayed_by."', '".$data->time."','".$data->received_time."');";

	//Execute
	$db->query($sql);

	//Output in CLI
	echo "Block #".$height." inserted.".PHP_EOL;
}