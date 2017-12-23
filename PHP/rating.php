<?php
require_once 'config.php';
$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$username=$_POST["username"];
$password=$_POST["password"];
$rating=$_POST["rating"];

  $response=array();
  $mysqliselect="Update bdsignup set rating ='$rating' where username='$username' and password='$password'";
  $result=mysqli_query($conn,$mysqliselect);
   if($result)
   {
  echo json_encode("data updated");     
  }
  else
  {
  echo json_encode("error");
  }
  mysqli_close($conn);
?>