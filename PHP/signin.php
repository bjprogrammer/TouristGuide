<?php
require_once 'config.php';
$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$username=$_POST["username"];
$password=$_POST["password"];


   $res=array("logged in","Check your username/password");
  $mysqliselect="Select username from bdsignup where username='$username' and password='$password'";
  $result=mysqli_query($conn,$mysqliselect);
  if(mysqli_num_rows($result)>0)
 { 
              echo ($res[0]);
               
 }
else {
              echo ($res[1]);
     }    
  mysqli_close($conn);
?>