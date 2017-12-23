<?php
require_once 'config.php';
$conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

$name=$_POST["username"];
$mobile=$_POST["mobile"];
$email=$_POST["email"];
$password=$_POST["password"];
$pic=$_POST["pic"];
$state=$_POST["state"];
$gender=$_POST["gender"];
$confirm_code=md5(uniqid(rand()));
 
  $response=array("Successfully registered","Please try registering using different username or email");
  $mysqlinsert="Insert into tmp_bdsignup(username,mobile,password,email,state,gender,pic,created_at,confirm_code) values('$name','$mobile','$password','$email','$state','$gender','$pic',Now(),'$confirm_code')" ;
  if(mysqli_query($conn,$mysqlinsert))
{
              echo json_encode($response[0]);
			  $to=$email;
              $subject="Your Tourist Guide app confirmation link here";
              $header = "From:Touristguide@cloudwaysapp.com \r\n";
              $header .= "Cc:".$email." \r\n";
              $header .= "MIME-Version: 1.0\r\n";
              $header .= "Content-type: text/html\r\n";
              $message="Your Comfirmation link \r\n";
              $message.="Click on this link to activate your account \r\n";
              $message.="http://www.touristguide.gq/confirmation.php?passkey=$confirm_code";
              $sentmail = mail($to,$subject,$message,$header);
            } 
            else 
            {
              echo json_encode($response[1]);
            }
  mysqli_close($conn);
?>