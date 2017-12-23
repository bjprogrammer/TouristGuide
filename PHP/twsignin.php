<?php
require_once 'config.php';
$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$twname=$_POST["twname"];
$twid=$_POST["twid"];
$twpic=$_POST["twpic"];
$twemail=$_POST["twemail"];
 
  $response=array("Data inserted","Error");
  $mysqlinsert="Insert into bdtwsignup(twname,twid,twpic,twemail,created_at) values('$twname','$twid','$twpic','$twemail',Now())" ;
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