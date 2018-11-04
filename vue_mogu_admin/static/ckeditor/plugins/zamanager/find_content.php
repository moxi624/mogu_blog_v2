<?php

$url = $_POST['url'];
if (file_exists($url)) echo file_get_contents($url);
else echo " file do not exists at address:".$url;
