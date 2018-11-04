<?php

$dir=$_POST['dir'];
if(!file_exists($dir)) {echo "File do not exists, or you probable choose directory. For Directory deletion use Dellete button below browser"; exit;}
unlink($dir) or die("Unable to delete file!".$name);
echo "File ".$name." was DELETED";
