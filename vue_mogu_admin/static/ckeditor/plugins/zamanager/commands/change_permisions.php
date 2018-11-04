<?php

$dir =$_POST['dir'];
$new_perm=$_POST['new_perm'];

if (!file_exists($dir)) {echo "This directory do NOT exists"; exit;}
if (!chmod(file,$new_perm)) {echo "You havent right permision to do this action"; exit;}
else
echo "Permision was changed to: ".$new_perm;
