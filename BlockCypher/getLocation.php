<?php
include "config.php";

/**
 * getLocation.php
 * 
 * 
 * Generates location information based on ip-addresses.
 * 
 * @author Ulrich Gallersdörfer
 * 
 */

//Query MySQL-db for ip-information
$sql = "Select * FROM blocks WHERE height > 540000 order by height desc";
$result = $db->query($sql);

//Filter for IPv4 addresses
while ($row = $result->fetch_assoc()) {
	$ip = explode(":",$row["ipaddress"]);
	$ipAddr[] = $ip[0];
}

//Remove duplicates
$ipAddr = array_unique($ipAddr);

//Iterate over all ip-addresses
foreach ($ipAddr as $key => $value) {
	//Array can contain empty values, skip if necessary
	if($value == "" || $value == "<nil>") continue;
	
	//Generate json-file
	$json = json_decode((file_get_contents("http://ipinfo.io/".$value."/json?token=".$token_ipinfo)));

	//Export in variables
	$country = $json->country;
	$area = $json->hostname;
	$isp = $json->org;
	$city = $json->city;
	$zip = $json->postal;
	$state = $json->region;
	$metro = "";
	$geo = explode(",", $json->loc);
	$lat = $geo[0];
	$lng = $geo[1];

	//Generate rows
	$sql = "INSERT INTO `ipaddress` (`id`, `ipaddress`, `lat`, `lng`, `country`, `area_code`, `isp`, `city`, `zip`, `state`, `metro`) VALUES (NULL, '".$value."', '".$lat."', '".$lng."', '".$country."', '".$area."', '".$isp."', '".$city."', '".$zip."', '".$state."', '".$metro."');";
	$db->query($sql);

	//CLI output
	echo $value." added.".PHP_EOL;

}
?>