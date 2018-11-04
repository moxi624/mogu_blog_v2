<?php

$dir=$_POST['dir'];
$name=$_POST['new_name'];
$myfile = fopen($dir.$name, "w") or die("Unable to create file!".$name);
fclose($myfile);
echo "File ".$name." was created in Directory ".$dir;
