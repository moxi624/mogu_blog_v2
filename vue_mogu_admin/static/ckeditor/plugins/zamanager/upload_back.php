<?php
 error_reporting(E_ALL);
 if(isset($_SERVER['HTTPS']))
{
$protokol="https://";
}
else
{
$protokol="http://";
}

$path=$_POST['path'];
if (!is_dir($path)) $path=(dirname($path));

 $rot_dir=        $_SERVER["DOCUMENT_ROOT"];
 $site_name=        $_SERVER["SERVER_NAME"];
$preview=str_replace($rot_dir,$site_name,$path);

	function getext($img){
		$name = strtolower($img);
		$data = explode(".", $name);
		$ext = count($data) -1;
		return $data[$ext];
	}
	if(isset($_FILES)){
	//	$allowed = array('jpg','JPG','png','gif');
	//	$ext = getext($_FILES['file1']['name']);
		$size = $_FILES['file1']['size'];
	//	if(in_array($ext, $allowed)){
			if($size < 2097152){
				$name = $_FILES['file1']['name'];

				if(move_uploaded_file($_FILES['file1']['tmp_name'], $path.$name)){
					echo '<a href="'.$protokol.$preview.$name.'" target="_blank" >'.$name.' Size:'.$size .'</a>';
                       //       echo $adr.$name;
				}else{
					echo "file upload has an error";
				}
			}else{
				echo "File size more than <strong>2MB<strong>";
			}
      //}
     // else{
	//		echo "file type not allowed";
	//	}
	}else{
		echo "we reach some error";
	}
?>
