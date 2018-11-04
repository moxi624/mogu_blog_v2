<?php

$dir=$_POST['dir'];
$text=$_POST['text'];
$myfile = fopen($dir, "w") or die("Unable to open file!");
fwrite($myfile, $text);
fclose($myfile);
echo "File ".$name." was SAVED";
