<?php



function phpconsole($label='var',$x){
 ?>
 <script type="text/javascript">
    console.log('<?php echo ($label)?>');
    console.log('<?php echo json_encode($x)?>');
    </script>
 <?php
}


function Read($file){
echo file_get_contents($file);
};

function findStringFromArray($phrases, $string, &$position) {
    // Reverse sort phrases according to length.
    // This ensures that 'taxi' isn't found when 'taxi cab' exists in the string.
    usort($phrases, create_function('$a,$b',
                                    '$diff=strlen($b)-strlen($a);
                                     return $diff<0?-1:($diff>0?1:0);'));

    // Pad-out the string and convert it to lower-case
    $string = ' '.strtolower($string).' ';

    // Find the phrase
    foreach ($phrases as $key => $value) {
        if (($position = strpos($string, ' '.strtolower($value).' ')) !== FALSE) {
            return $phrases[$key];
        }
    }
    // Not found
    return FALSE;
}

  function getRelativePath($basePath, $targetPath)
    {
        if ($basePath === $targetPath) {
            return '';
        }

        $sourceDirs = explode('/', isset($basePath[0]) && '/' === $basePath[0] ? substr($basePath, 1) : $basePath);
        $targetDirs = explode('/', isset($targetPath[0]) && '/' === $targetPath[0] ? substr($targetPath, 1) : $targetPath);
        array_pop($sourceDirs);
        $targetFile = array_pop($targetDirs);

        foreach ($sourceDirs as $i => $dir) {
            if (isset($targetDirs[$i]) && $dir === $targetDirs[$i]) {
                unset($sourceDirs[$i], $targetDirs[$i]);
            } else {
                break;
            }
        }

        $targetDirs[] = $targetFile;
        $path = str_repeat('../', count($sourceDirs)).implode('/', $targetDirs);

        // A reference to the same base directory or an empty subdirectory must be prefixed with "./".
        // This also applies to a segment with a colon character (e.g., "file:colon") that cannot be used
        // as the first segment of a relative-path reference, as it would be mistaken for a scheme name
        // (see http://tools.ietf.org/html/rfc3986#section-4.2).
        return '' === $path || '/' === $path[0]
            || false !== ($colonPos = strpos($path, ':')) && ($colonPos < ($slashPos = strpos($path, '/')) || false === $slashPos)
            ? "./$path" : $path;
    }


?>
