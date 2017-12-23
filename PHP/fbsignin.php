<?php
require_once 'config.php';
$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$fbname=$_POST["fbname"];
$fbid=$_POST["fbid"];
$fbpic=$_POST["fbpic"];
$fbemail=$_POST["fbemail"];
$fbgender=$_POST["fbgender"];
 
  $response=array("Data inserted","Error");
  $mysqlinsert="Insert into bdfbsignup(fbname,fbid,fbpic,fbemail,fbgender,created_at) values('$fbname','$fbid','$fbpic','$fbemail','$fbgender',Now())" ;
  if(mysqli_query($conn,$mysqlinsert))
{
              echo json_encode($response[0]);
            } 
            else 
            {
              echo json_encode($response[1]);
        
            }
  mysqli_close($conn);
?>