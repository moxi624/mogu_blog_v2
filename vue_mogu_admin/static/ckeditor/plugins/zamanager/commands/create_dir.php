<?php

$dir =$_POST['dir']."/";

if (file_exists($dir)) {echo "Directory with this name allredy exists"; exit;}
$old = umask(0);
if (!mkdir($dir, 0777)) echo "you haven't perrmission to Create this NEW directory";
else echo "Directory was created";
umask($old);
