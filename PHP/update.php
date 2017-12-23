<?php
require_once 'config.php';
$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$username=$_POST["username"];
$password=$_POST["password"];
$pic=$_POST["pic"];
$state=$_POST["state"];
$gender=$_POST["gender"];

  $response=array();
  $mysqliselect="Update bdsignup set pic ='$pic', password ='$password',state ='$state',gender ='$gender' where username='$username' ";
  $result=mysqli_query($conn,$mysqliselect);
   if($result)
   {
     echo json_encode("Profile updated");     
   }
   else
   {
    echo json_encode("error");
   }
   mysqli_close($conn);
?>