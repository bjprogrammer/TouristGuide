<?php

require_once 'config.php';

$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE) or die("Unable to Connect to '$dbhost'");
mysql_select_db($dbname) or die("Could not open the db '$dbname'");
$test_query = "SHOW TABLES FROM $dbname";
$result = mysql_query($test_query);
$tblCnt = 0;
while($tbl = mysql_fetch_array($result)) {
  $tblCnt++;
  #echo $tbl[0]."<br />\n";
}
if (!$tblCnt) {
  echo "There are no tables<br />\n";
} else {
  echo "There are $tblCnt tables<br />\n";
}