<?php
require_once "Locate_Wind.php";
$locate= new Locate_Wind($_POST['latitude'],$_POST['longitude'],$_POST['force'],$_POST['orientation']);
$locate->insere();
?>
