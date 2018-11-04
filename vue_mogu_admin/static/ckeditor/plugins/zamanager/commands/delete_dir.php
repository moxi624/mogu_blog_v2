<?php

$dir =$_POST['dir'];
if (!is_dir($dir)) {echo "Please use FILE delete button. Choosen item is NOT Directory. probably  you chose file for deletion"; exit;}

$it = new RecursiveDirectoryIterator($dir, RecursiveDirectoryIterator::SKIP_DOTS);
$files = new RecursiveIteratorIterator($it,
             RecursiveIteratorIterator::CHILD_FIRST);
foreach($files as $file) {
    if ($file->getFilename() === '.' || $file->getFilename() === '..') {
        continue;
    }
    if ($file->isDir()){
       if (! rmdir($file->getRealPath()))echo "you haven't perrmission to delete this directory";
    } else {
        if(!unlink($file->getRealPath())) echo "you haven't perrmission to delete this files";
    }
}
if (!rmdir($dir))echo "you haven't perrmission to delete this directory, or it's still have items inside.Please delete items first";
else echo "Directory was deleted";
