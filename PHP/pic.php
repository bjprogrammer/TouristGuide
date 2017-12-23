<?php
require_once 'config.php';
$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$username=$_POST["username"];
$password=$_POST["password"];

  $response=array();
  $mysqliselect="Select * from bdsignup where username='$username' and password='$password'";
  $result=mysqli_query($conn,$mysqliselect);
              while($row=mysqli_fetch_array($result))
              {
               array_push($response,array("email"=>$row[3],"state"=>$row[5],"mobile"=>$row[1],"pic"=>$row[6],"gender"=>$row[8],"rating"=>$row[9]));
  }
  echo json_encode(array("server_response"=>$response));     
  mysqli_close($conn);
?>