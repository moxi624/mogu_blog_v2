<?php

$dir =$_POST['dir'];
$new_name=$_POST['new_name'];
$old_name=$_POST['old_name'];
if (!file_exists($dir)) {echo "File does NOT exists"; exit;}
 $fileHand=fopen($dir,'r');
fclose( $fileHand);
$new_name=str_replace($old_name,$new_name,$dir);
if (!rename($dir,$new_name)) echo "you haven't perrmission to RENAME this  file";
else echo "File was renamed";
