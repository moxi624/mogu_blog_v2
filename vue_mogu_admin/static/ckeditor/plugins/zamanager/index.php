<?php
/*This is ZmajSoft file manager
autor: Jocic Ivica zmmaj
from ZmajSoft.com
Email: zmajsoft@zmajsoft.com
or       zmmaj2000@gmail.com
happy programing
*/
ob_start();
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

 ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<title>ZmajSoft File MANAGER</title>
<?php

$lang_defined="en";
 $disalowed_extensions=array();
//$conn=array();
 include_once dirname(__FILE__)."/config.php";
require_once dirname(__FILE__)."/logic.php";
//if (defined('DB_HOST')) $dbServer=DB_HOST;
//if (defined('DB_NAME')) $dbUser=DB_NAME;
//if (defined('DB_PASS')) $dbPass=DB_PASS;
//if (defined('DB_USER')) $dbDatabase=DB_USER;
$base_dir = __DIR__; // Absolute path to your installation, ex: /var/www/mywebsite
$doc_root = preg_replace("!{$_SERVER['SCRIPT_NAME']}$!", '', $_SERVER['SCRIPT_FILENAME']); # ex: /var/www
$base_url = preg_replace("!^{$doc_root}!", '', $base_dir); # ex: '' or '/mywebsite'
$protokol = empty($_SERVER['HTTPS']) ? 'http' : 'https';
$port = $_SERVER['SERVER_PORT'];
$disp_port = ($protokol == 'http' && $port == 80 || $protokol == 'https' && $port == 443) ? '' : ":$port";
$domain = $_SERVER['SERVER_NAME'];
$full_url = "$protokol://{$domain}{$disp_port}{$base_url}"; # Ex: 'http://example.com', 'https://example.com/mywebsite', etc

 $rot_dir=  $_SERVER["DOCUMENT_ROOT"];
 $site_name=  $_SERVER["SERVER_NAME"];
chdir($rot_dir."/".$zsmanager_path);


 /*
 echo"Manager patrh: ". $zsmanager_path."<br>";

  echo "rot_dir: ".$rot_dir."<br>";
 echo "site_name: ".$site_name."<br>";
 echo "base url: ".$base_url."<br>";
 echo "docRoot: ".$doc_root."<br>";
 echo "Current working dir:" .getcwd()."<br>";
echo "Relative path:".$rel_path."<br>";
*/
$rel_path=realpath(getcwd());
//$rel_path=realpath(getcwd());
 //connect to database if exists on your site;
 //This connection you will need to perform MYsqli queries.
 if (strlen($dbServer)){
    //   echo" no i goo";
     $conn=array( "'".$dbServer."'", "'".$dbExt.$dbUser."'","'".$dbPass."'", "'".$dbExt.$dbDatabase."'") ;
 } ;

//Or if you on I-Goo let's do this automatically :)
//if (!is_dir("/".$zsmanager_path."/".$site_connector_file)){
 //      include_once(getcwd()."/".$site_connector_file);
// }


$link_path="/".$zsmanager_path;
$search_dir=$browse_url;
 $uploader= "/".$zsmanager_path."/upload_back.php";

// echo $uploader;
?>
            <script src="/<?php echo $zsmanager_path;?>/jquery-1.8.0.js"  type="text/javascript"></script>
            <script src="/<?php echo $zsmanager_path;?>/logic.js"  type="text/javascript"></script>
		<script src="/<?php echo $zsmanager_path;?>/jquery.easing.js"  type="text/javascript"></script>
		<script src="/<?php echo $zsmanager_path?>/jqueryFileTree.js"  type="text/javascript"></script>
		<link href="/<?php echo $zsmanager_path;?>/jqueryFileTree.css"  rel="stylesheet" type="text/css" media="screen" />
            <?php



$root_path= $rot_dir.$browse_url;
 //CONNECTION WITH TEXT EDITORS
 $editor = "";
 $WhatEditor="";
 if(isset($_REQUEST['CKEditor'])){$editor=$_REQUEST['CKEditor']; $WhatEditor="CKEditor";}
 If(isset($_REQUEST['act'])) $act= $_REQUEST['act'];
$CKEditorFuncNum=0;
  If(isset($_REQUEST['CKEditorFuncNum'])) $CKEditorFuncNum= $_REQUEST['CKEditorFuncNum'];

  $Ftype="Images";
If(isset($_REQUEST['type'])) $type= $_REQUEST['type'];
$langCode="en";
  If(isset($_REQUEST['langCode'])) $langCode= $_REQUEST['langCode'];
$php_connector_path="/".$zsmanager_path."/connectors/jqueryFileTree.php";
$find_content_address="/".$zsmanager_path."/find_content.php";

 $logo_path="/".$zsmanager_path."/logo.jpg";

//$server=$conn["server"];
//$user=$conn["user"];
//$pass=$conn["pass"];
//$base=$conn["base"];

 // Lets perform initialy translation reading
 // $default_lang was set in config.php, but if you omit that, English will be default language
 if ($lang_defined){
 if (getcwd()."/language/".$default_lang.".php") { include_once (getcwd()."/language/".$default_lang.".php"); }
}
else {include_once (getcwd()."/language/sr.php"); }
?>

		<style type="text/css">
             td { border: 1px solid black; width: 200px; text-left: center; margin:10; padding:0;}
  #span1 { background-color: #DDD; width: 200px; float: right; height:150px; margin:10; }
  #span2 { background-color: #EEE; width: 200px; float: left; height:40px; margin:10; }
 #span3 { background-color: black; width: 200px; float: top;  margin:10;}
  .t { border: 3px solid black; }
  .r { border: 3px solid black;}
  .edi {text-left; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px; color:black; }
  .vid {text-left; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px; color:black; }


			.example {
				float: left;
				margin: 10px;
			}

			.browse {
				width: 250px;
				height: 300px;
				border-top: solid 1px black;
				border-left: solid 1px black;
				border-bottom: solid 1px red;
				border-right: solid 1px black;
				background: #FFF;
				overflow: scroll;
	                  position:top;
            }
            	.tableBrowser {
				width: 500px;
				height: 300px;
				border-top: solid 1px #BBB;
				border-left: solid 1px #BBB;
				border-bottom: solid 1px #FFF;
				border-right: solid 1px #FFF;
				background: #FFF;


            }
            	#progressbar{
width: 100%;
height: 7px;
 background: yellow;
 padding: 3px;
 border-radius: 3px;
 box-shadow: inset 0px 1px 3px rgba(0,0,0,0.2);
	}
	#progress{
		height: 5px;
 background: cornflowerblue;
 display: block;
 border-radius: 2px;
 -webkit-transition: width 0.8s ease;
 -moz-transition: width 0.8s ease;
 transition: width 0.8s ease;
	}

		</style>


		<script type="text/javascript">

			$(document).ready( function() {

                        var path="<?php echo $link_path;?>";
                        var root_dir="<?php echo $root_path;?>";
                        var php_connector_path="<?php echo $php_connector_path;?>";
                        var find_content="<?php echo $find_content_address;?>";
                        var site_name= "<?php echo   $site_name;?>";
                        var site_root= "<?php echo   $rot_dir;?>";
                        var protokol="<?php echo   $protokol;?>";

                        document.getElementById("full").value=root_dir;
                        document.getElementById("fullForm").value=root_dir;

				$('#fileTreeDemo_4').fileTree({ root: root_dir, script: php_connector_path, folderEvent: 'click', expandSpeed: 1, collapseSpeed: 1 }, function(file) {
		 var type=0;
                        var fileExt = (file).split('.').pop();
                  var str=file;

                  document.getElementById("slika").src="";
                  document.getElementById("aud").src="";
                  document.getElementById("movie").src="";
                  document.getElementById('editor').innerHTML = "";
                  document.getElementById("audio_name").value="";
                  document.getElementById("movie_name").value="";
                  document.getElementById("text_name").value="";
                  document.getElementById("full").value="";
                  document.getElementById("fullForm").value=str;
                  document.getElementById("op_txt").value="";
                  str= pathStrip(file);
                              var ext=(fileExt).toLowerCase() ;
                              if ((ext=="jpg") || (ext=="png") || (ext=="gif")  || (ext=="jpeg") || (ext=="tiff") || (ext=="tif") || (ext=="ico")) type=1;
                              if ((ext=="php") || (ext=="html") || (ext=="css") || (ext=="js") || (ext=="txt") || (ext=="htm")
                               || (ext=="asp") || (ext=="aspx") || (ext=="bat") || (ext=="log") || (ext=="cgi") || (ext=="ini")
                                || (ext=="py") || (ext=="sql") || (ext=="xls") || (ext=="xml") || (ext=="pl") || (ext=="md") || (ext=="gpl")
                                || (ext=="lgpl") || (ext=="htaccess")) type=2;
                              if ((ext=="mp3") || (ext=="ogg") || (ext=="wav")) type=3;
                              if ((ext=="webm") || (ext=="ogv") || (ext=="mp4") || (ext=="mpg") || (ext=="mpeg") || (ext=="3gp") || (ext=="swf")) type=4;
                              if ((ext=="swf") || (ext=="fla")) type=5;
                              if ((ext=="pdf")) type=6;
                              switch(type){
                         case 1:  {
                         stra=protokol+"://"+str.replace(site_root, site_name);

                        document.getElementById("slika").src=stra;
                        document.getElementById("full").value=str;
                        document.getElementById("fullForm").value=root_dir+str;
                        document.getElementById("op_txt").value=str.split('\\').pop().split('/').pop();
                        }
                            break;
                          case 2:  {

        $.post(find_content, { url: str }, function(data) {
              var ed =document.getElementById('editor');
             ed.innerHTML = data;
             document.getElementById("text_name").value=str.split('\\').pop().split('/').pop();
    var fullpath = path.substring(0, Math.max(path.lastIndexOf("/"), path.lastIndexOf("\\\\")));

                        document.getElementById("full").value=str;
                        document.getElementById("fullForm").value=str;
                        document.getElementById("op_txt").value=str.split('\\').pop().split('/').pop();

});
                              }
                              break;

                  case 3: {
                         stra=protokol+"://"+str.replace(site_root, site_name);

               document.getElementById("aud").src=stra;
                document.getElementById("audio_name").value=str.split('\\').pop().split('/').pop();

                        document.getElementById("full").value=str;
                        document.getElementById("fullForm").value=str;
                        document.getElementById("op_txt").value=str.split('\\').pop().split('/').pop();

                        }
                        break
                  case 4: {
                         stra=protokol+"://"+str.replace(site_root, site_name);

                         document.getElementById("movie").src=stra;
                          document.getElementById("movie_name").value=str.split('\\').pop().split('/').pop();

                        document.getElementById("full").value=root_dir+str;
                        document.getElementById("fullForm").value=root_dir+str;
                        document.getElementById("op_txt").value=str.split('\\').pop().split('/').pop();
                  }
                  break;
                        default: {
                        document.getElementById("full").value="";
                        document.getElementById("fullForm").value="";
                          document.getElementById("op_txt").value="";
                        document.getElementById("full").value=str;
                        document.getElementById("fullForm").value=str;
                         document.getElementById("op_txt").value="Unknown file or Directory: " + str.split('\\').pop().split('/').pop(); }
                   break;
                        }

				});





      	var ajax = new XMLHttpRequest();
      var uploader ="<?php echo  $uploader; ?>";
	function ids(id){
		return document.getElementById(id);
	}
	function upload(e){
		e.preventDefault();
		var file = ids("file1").files[0];
            var path=ids('fullForm').value;
		var formdata = new FormData();
		formdata.append('file1', file);
            formdata.append('path', path);

		ajax.upload.addEventListener('progress', progressHandler, false);
		ajax.addEventListener('load', completeHandler, false);
		ajax.addEventListener('abort', abortHandler, false);
		ajax.addEventListener('error', errorHandler, false);
		ajax.open('POST', uploader);
		ajax.send(formdata);
	}

	function progressHandler(e){
		var percent = (e.loaded / e.total) * 100;
		percent = Math.round(percent);
		ids('progress').style.width = Math.round(percent)+'%';
		ids('status').innerHTML = percent + '% uploaded plzz wait.....';
	}
	 function completeHandler(){
	 	ids('status').innerHTML = ajax.responseText;
	 	ids('progress').style.width = '0%';
	 }

	 function abortHandler(){
	 	alert('file upload aborted');
	 }

	 function errorHandler(){
	 	alert('file upload has an error');
	 }
	ids('forma').addEventListener('submit', upload, false);


			});
		</script>

<div id="cols" class="box">
 <div id="main_browser" >

            <table border="3px">
 <tr><td style="border: 2px solid black;  text-left: margin:1; padding:1;" >

		<div class="example">
			<div id="fileTreeDemo_4" class="browse"></div><label style="halign:right";>
		</div>	<?php echo "Version: ".$ver; ?></label></div>
</td>
<td>
<div id="span1" class="t">

 <img id="slika" src="<?php echo $logo_path;?>" alt="IMAGE" border="3px" style="max-width:200px;max-height:150px;" ondblclick="send_to_editor('<?php echo $CKEditorFuncNum;?>',this.src,'Images','<?php echo $Ftype;?>');"/></div>

<div id="span2" class="t">
<audio id= "aud" controls>
  <source src="" type="audio/ogg">
  <source src="" type="audio/mpeg">
  <source src="" type="audio/wav">
Your browser does not support the audio element.
</audio>
 <input type="text" id="audio_name" disabled value="" class="vid" readonly><br>
</div>

<div id="span3" class="t" >
 <video id="movie" width="200" height="140" float="top" controls ondblclick="send_to_editor('<?php echo $CKEditorFuncNum;?>',this.src,'Movies','<?php echo $Ftype;?>');">
 <source src="" type=video/webm>
<source src="" type=video/ogg>
<source src="" type=video/mp4>
<source src="" type=video/3gp>
<source src="" type=video/flv>
  Your browser does not support the video tag.
</video>
 <input type="text" id="movie_name" disabled value="" class="vid" readonly><br>
</div>

<td >
<textarea  id="editor" class="edi" style="width:198px; height: 340px; border: 2px;font-size:10px;float:top center;margin:5;" ondblclick="send_to_editor('<?php echo $CKEditorFuncNum;?>',this.src,'Files','<?php echo $Ftype;?>');">
</textarea>
 <input type="text" id="text_name"  style="width:198px;" value="" class="vid" readonly><br>
</td>
</tr><tr class="t"><td colspan="2">

<button id="browse_del"  style="font-size:10px;"  onclick="del_Dir('<?php echo  $link_path;?>');" <?php echo $sw_DELETE;?>><?php echo $BLang_delete;?></button>
<button id="Browse_create" style="font-size:10px;" onclick="create_Dir('<?php echo  $link_path;?>');" <?php echo $sw_CREATE;?>><?php echo $BLang_create;?></button>
<button id="browse_rename" style="font-size:10px;" onclick="rename_Dir('<?php echo  $link_path;?>');" <?php echo $sw_RENAME;?>><?php echo $BLang_rename?></button>
 <button id="browse_move" style="font-size:10px;" onclick="move_Dir('<?php echo  $link_path;?>');" <?php echo $sw_MOVE;?>><?php echo $BLang_move_dir?></button>
 <button id="browse_ch_perm" style="font-size:10px;" onclick="change_dir_permision('<?php echo  $link_path;?>');" <?php echo $sw_PERMISSION;?>><?php echo $BLang_permission?></button>
 <hr style="color:blue;size:4px;">

		<form id="forma" action="<?php echo $uploader;?>" enctype="multipart/form-data" method="post">

<input style="font-size:10px;"  id="file1" type="file" name="file1" >
 <input id="fullForm" type="hidden" name="fullForma" value="am" >


		<input style="font-size:10px;"  type="submit" name="submit" id="submit" value="upload">

		</form>
	<div style="width:400px;">
		<div id="progressbar">
			<div id="progress" style="width:0px;"></div>
		</div>
	</div>
	<div id="status"></div>
</td><td >
<button id="txt_del" type="button" style="font-size:10px;" onclick="delete_file('<?php echo  $link_path;?>');" <?php echo $sw_DELETE;?>><?php echo $BLang_delete;?></button>
<button type="txt_create" style="font-size:10px;" onclick="create_file('<?php echo  $link_path;?>');" <?php echo $sw_CREATE;?>><?php echo $BLang_create;?></button>
<button type="txt_rename" style="font-size:10px;"  onclick="rename_file('<?php echo  $link_path;?>');" <?php echo $sw_RENAME;?>><?php echo $BLang_rename?></button>
<button type="txt_save" style="font-size:10px;" onclick="save_file('<?php echo  $link_path;?>');" <?php echo $sw_SAVE;?>><?php echo $BLang_save;?></button>
<button type="txt_save" style="font-size:10px;" onclick="prepare_query();" <?php echo $sw_QUERY_PREPARE;?>><?php echo $BLang_query_prepare;?></button>
<button type="txt_save" style="font-size:10px;" onclick="run_query('<?php echo  $link_path;?>','<?php echo ($server);?>','<?php echo ($user);?>','<?php echo ($pass);?>','<?php echo ($base);?>');" <?php echo $sw_QUERY;?>><?php echo $BLang_query;?></button>
</td>
</tr><td colspan="3">
<div>
<label for="op_txt">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  <?php echo $BLang_filename?></label>
<input class="vid" id="op_txt" type="text" value="" size="25em" ><br>
<label for="full"><?php echo $BLang_fullPath?> </label>
<input class="vid" id="full" type="text" value="" size="100em" >
<input class="vid" id="fullH" type="hidden" value="" size="100em" >
</div>

</td></tr></table>



</div>
