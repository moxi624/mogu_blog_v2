<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
 if (session_status() === PHP_SESSION_NONE){
 if ($rot_dir."/".$config_database_file)  {
       include_once( $rot_dir."/".$config_database_file);
       global $session;
       $session= new Session;
      $con= $session->return_con();
       $session = new Session;
        $conn=array( "server" => dbServer,"user" => dbExt.dbUser,"pass" => dbPass,"base" => dbExt.dbDatabase) ;

        }}
      ?>
  <script>
echo session_status();
</script>
<?php
if (file_exists(dirname(__FILE__). "/language/".LANG.".php")) include_once( dirname(__FILE__)."/language/".LANG.".php");
else {include_once(dirname(__FILE__)."/language/en.php"); $lang_defined=1;}



switch($_SESSION['role']){
case 1: {
 $sw_SAVE='';
$sw_DELETE='';
$sw_MOVE='';
$sw_CREATE='';
$sw_RENAME='';
$sw_PERMISSION='';
$sw_QUERY_PREPARE='';
      } break;

case 2: {
 $sw_SAVE='';
$sw_DELETE='disabled';
$sw_MOVE='disabled';
$sw_CREATE='';
$sw_RENAME='disabled';
$sw_PERMISSION='';
$sw_QUERY='';
$sw_QUERY_PREPARE='disabled';
      } break;

case 3: {
$sw_SAVE='disabled';
$sw_DELETE='disabled';
$sw_MOVE='disabled';
$sw_CREATE='disabled';
$sw_RENAME='disabled';
$sw_PERMISSION='disabled';
$sw_QUERY='disabled';
$sw_QUERY_PREPARE='disabled';
      } break;

case 4: {
$sw_SAVE='disabled';
$sw_DELETE='disabled';
$sw_MOVE='disabled';
$sw_CREATE='disabled';
$sw_RENAME='disabled';
$sw_PERMISSION='disabled';
$sw_QUERY='disabled';
$sw_QUERY_PREPARE='disabled';
      } break;

case 5: {
$sw_SAVE='disabled';
$sw_DELETE='disabled';
$sw_MOVE='disabled';
$sw_CREATE='disabled';
$sw_RENAME='disabled';
$sw_PERMISSION='disabled';
$sw_QUERY='disabled';
$sw_QUERY_PREPARE='disabled';
      } break;

case 6:{
$sw_SAVE='disabled';
$sw_DELETE='disabled';
$sw_MOVE='disabled';
$sw_CREATE='disabled';
$sw_RENAME='disabled';
$sw_PERMISSION='disabled';
$sw_QUERY='disabled';
$sw_QUERY_PREPARE='disabled';
      } break;

case 7: {
$sw_SAVE='disabled';
$sw_DELETE='disabled';
$sw_MOVE='disabled';
$sw_CREATE='disabled';
$sw_RENAME='disabled';
$sw_PERMISSION='disabled';
$sw_QUERY='disabled';
$sw_QUERY_PREPARE='disabled';
      } break;
default: {
$sw_SAVE='disabled';
$sw_DELETE='disabled';
$sw_MOVE='disabled';
$sw_CREATE='disabled';
$sw_RENAME='disabled';
$sw_PERMISSION='disabled';
$sw_QUERY='disabled';
$sw_QUERY_PREPARE='disabled';
      }
      break;
}

// database query
