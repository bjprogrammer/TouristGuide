<?php
     require_once 'config.php';
     $conn = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
         
         $email=$_POST["email"];
         $to =$email;
         $subject = "Password reset instructions-Tourist Guide app";
         
         $header = "From:touristguide@cloudwaysapp.com \r\n";
         $header .= "Cc:".$email." \r\n";
         $header .= "MIME-Version: 1.0\r\n";
         $header .= "Content-type: text/html\r\n";
         
         function randomPassword() 
         {
               $alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890';
               $pass = array(); 
               $alphaLength = strlen($alphabet) - 1; 
               for ($i = 0; $i < 8; $i++) 
                {
                    $n = rand(0, $alphaLength);
                    $pass[] = $alphabet[$n];
                 }
                return implode($pass); 
        }

         $mysqliselect1="Select email from bdsignup where email='$email' ";
         $result1=mysqli_query($conn,$mysqliselect1);
         if(mysqli_num_rows($result1)>0)
         {
               $OTP= randomPassword();
               $message = "Use this OTP to login into your account.<br><b>OTP- ".$OTP. "</b><br><br>Please do not reply to this email.This is an auto-generated email.";
           if(mail ($to,$subject,$message,$header))
           {
             
             $mysqliselect2="Select OTP from bdforgetpassword where email='$email' ";
             $result2=mysqli_query($conn,$mysqliselect2);
             if(mysqli_num_rows($result2)>0)
             {
             
               if(mysqli_query($conn, "UPDATE bdforgetpassword SET OTP='$OTP' WHERE email='$email'"))
                {
				  if(mysqli_query($conn, "UPDATE bdsignup SET password='$OTP' WHERE email='$email'"))
                  {
                  echo "OTP sent successfully";
                  } 
                  else 
                  {
                   echo "Error";
                  }
                } 
                else 
                {
                   echo "Error";
                 }
             }
             else
             {
                $mysqlinsert="Insert into bdforgetpassword(email,OTP,created_at) values('$email','$OTP',Now())" ;
                if(mysqli_query($conn,$mysqlinsert))
                {
                  if(mysqli_query($conn, "UPDATE bdsignup SET password='$OTP' WHERE email='$email'"))
                  {
                  echo "OTP sent successfully";
                  } 
                  else 
                  {
                   echo "Error";
                  }
                } 
                else 
                {
                   echo "Error";
                }
             
             }
           }
           else 
           {
              echo "Message could not be sent...";
           }
         
           }
           else 
           {
              echo "email-id not registered";
           }
         
         
           mysqli_close($conn);
      ?>