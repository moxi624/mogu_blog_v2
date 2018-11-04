<?php
if (file_exists("../../admin/session.php"))  include_once("../../admin/session.php");
else {echo "I cant find i_goo_site_connector.php file"; exit;}
$session= new Session;
$con=$session->return_con();
