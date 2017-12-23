<?php

require_once 'config.php';
$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$passkey=$_GET['passkey'];
$sql="SELECT * FROM tmp_bdsignup WHERE confirm_code ='$passkey'";
$result1=mysqli_query($conn,$sql);

if(mysqli_num_rows($result1)>0){

$rows=mysqli_fetch_array($result1);
$username=$rows['username'];
$email=$rows['email'];
$password=$rows['password']; 
$gender=$rows['gender'];
$mobile=$rows["mobile"];
$pic=$rows["pic"];
$state=$rows["state"];
 
 
$sql2="INSERT INTO bdsignup(username, email, password,state,gender,mobile,pic,created_at) VALUES('$username', '$email', '$password', '$state','$gender', '$mobile', '$pic',Now())";
$result2=mysqli_query($conn,$sql2);
if($result2){
echo json_encode("Your account has been activated");
}
}
else {
echo json_encode("Already confirmed");

}


?>