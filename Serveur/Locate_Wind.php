<?php
require_once "conf.class.php";
class Locate_Wind{

private $latitude;
private $longitude;
private $force;
private $orientation;
private $date;

public function __construct($latitude,$longitude,$force,$orientation){
	$this->latitude=$latitude;
	$this->longitude=$longitude;
	$this->force=$force;
	$this->orientation=$orientation; 
	
}

public final static function connect(){
	$lien=mysql_connect(Conf::$server,Conf::$user,Conf::$passwd);
	$db=mysql_select_db(Conf::$base,$lien);
}

public function insere(){
	Locate_Wind::connect();
	$this->latitude=floatval($this->latitude);
	$this->longitude=floatval($this->longitude);
	$this->force=floatval($this->force);
	$query = "INSERT INTO LOCATE VALUES (NULL, ".$this->latitude.", ".$this->longitude.", ".$this->force.", '".$this->orientation."', NOW())";
	$result=mysql_query($query);
	if (!$result) {
   echo 'Impossible d\'exécuter la requête : ' . mysql_error();
   exit;
}
	//mysql_free_result($result);
}
	 
public final static function check_locate(){
	Locate_Wind::connect();
	$query="SELECT * FROM LOCATE ORDER BY date DESC";
	$result=mysql_query($query);
	
	
	while($row=mysql_fetch_assoc($result)) $output[]=$row;
	print(json_encode($output));
	mysql_close();
	//mysql_free_result($result);
}
}
?> 
