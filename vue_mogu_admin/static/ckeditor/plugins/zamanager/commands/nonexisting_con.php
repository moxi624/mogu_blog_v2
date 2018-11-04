<?php
if (file_exists("../config.php"))  include_once("../config.php");
else {echo "I cant find config.php file"; exit;}
 $con =mysqli_connect( $dbServer, $dbExt.$dbUser,$dbPass, $dbExt.$dbDatabase) or die ("VARS: Error connecting to mysql server: ".mysqli_error($con));
 if (!$con) {echo(mysqli_error($con)); exit;}
