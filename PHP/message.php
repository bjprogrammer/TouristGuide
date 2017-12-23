<?php
require_once 'config.php';
$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$email=$_POST["email"];
$message=$_POST["message"];
 
  $response=array("Message sent","Error");
  $mysqlinsert="Insert into bdmessage(email,message,created_at) values('$email','$message',Now())" ;
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