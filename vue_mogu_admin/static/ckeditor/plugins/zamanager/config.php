<?php
$browse_url= "/"                      ;  //directory from where is browsing enabled... in case you are in other folder than root write some as /yourFolder/
$zsmanager_path="vit/skladiste/zsmanager/";
 // Path where you put zsmanager folder.
$disalowed_ext =    'ini,dat'                    ;                                                // here you can add ANY extension
$permisions[]=null;                                                                       // for this I have plan for future version. now, leave as is.
$config_database_file="";                                // or any other file where script can read database settings.
$site_connector_file=""  ;                        // enter another connector script. or run without them, leave blank ""
$default_lang= "en" ;                                                             // write here default manager language
                                                                                              // if you use CKEditor , editor will send language in request.
//i_goo_site_connector.php
/* You can use this settings below to initialy disable some, or all button of ZmajSoft File Manager
To do that simple write between bracket word disabled  so wholw linw will look ass:
$sw_SAVE="disabled";
Buttons with EMPTY will be enabled initialy.
You can use those names to disable this buttons thry site connector script or similar_text trap.
This is default settings ( all enabled), but, just as say, latter you can change that,,,
See i_goo_site_connector.php to get idea how.
*/
 $sw_SAVE='';
$sw_DELETE='';
$sw_MOVE='';
$sw_CREATE='';
$sw_RENAME='';
$sw_PERMISSION='';
$sw_QUERY='';
$sw_QUERY_PREPARE='';
//Below was database stuff..
// You will need database connection if you wana run queries from editor.
//Of course, if you make connection thry connector file live this unchanged;
//Also if you do not have database, leave this unchanged;
$dbServer="";
$dbUser="";
$dbPass="";
$dbDatabase="";
$dbExt="";
//Most free servers force their premark for names ( eg A4574_ then come your name ) ...This pre name I was call $dbExt
$ver="1.2";                  // ZSManager current version
