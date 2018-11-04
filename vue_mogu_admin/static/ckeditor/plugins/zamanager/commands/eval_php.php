<?php
$str= $_REQUEST['string'];
$server=$_REQUEST['server'];
$user=$_REQUEST['user'];
$pass=$_REQUEST['pass'];
$base=$_REQUEST['base'];
$str=str_replace("~","+",$str);

     $myFile = "testFile.php";
$fh = fopen($myFile, 'w');
//check if is query or not
if (strpos($str,"query"))
   {
fwrite($fh,"<?php \n$"."con =mysqli_connect( '".$server."','".$user."','".$pass."','".$base."') or die (mysqli_error($"."con));");
fwrite($fh, "\n");

//fwrite($fh, $no."  \n");
fwrite($fh, $str. " ?>");
fclose($fh);
}

else{
$myFile = "testFile.php";
$fh = fopen($myFile, 'w');
fwrite($fh, "<?php \n".$str. " ?>");
fclose($fh);
}

chmod("testFile.php", 0777);
$fil="testFile.php";
include "testFile.php";
unlink($fil);
