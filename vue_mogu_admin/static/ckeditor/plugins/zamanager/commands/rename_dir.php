<?php

$dir =$_POST['dir'];
$new_name=$_POST['new_name'];
if (!file_exists($dir)) {echo "Directory does NOT exists"; exit;}
$old = umask(0);
if (!rename($dir,$new_name)) echo "you haven't perrmission to RENAME this  directory";
else echo "Directory was renamed";
umask($old);
