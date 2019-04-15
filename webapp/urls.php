<?php

define('__PROJECT_ROOT__', dirname(__FILE__));
//require_once(__PROJECT_ROOT__.'/connector.php');
require_once('connector.php');
require_once('utils.php');


function success($message){
  $json = array(
    'success' => $message
  );
  echo(json_encode($json));
}


$parametros = json_decode(file_get_contents('php://input'), true);

if (!array_key_exists('action', $parametros)) {
  exit;
}

$con = '';

$action = $parametros["action"];

try {
   $con = connect();
   if ($action == "checkIfUserCanBeLoggedIn") {
     // TODO
     // Checks if a username exists with that password
     $resultado = execute($con, "SELECT username
                           FROM Users
                           WHERE username='".POST("username")."'
                                  AND password='".POST("password")."'");

   }elseif ($action == "signup") {
     /*----  Sign Up ----*/
     // check if username exists
     $hash = hashPassword($parametros["password"]);
     $resultado = execute($con, "SELECT username FROM Users WHERE username='".$parametros["username"]."' AND password='".$hash."'");

     if (select_is_empty($resultado)) {
        // save the new user
        execute($con, "INSERT INTO Users(username, password, accessToken)
                 VALUES ('".$parametros["username"]."', '".$hash."', '".$parametros["accessToken"]."')");
        success("ok");
     }else{
       throw new Exception('username_exists');
     }
   }elseif ($action == "login") {
     /*---- Log In ----*/
     $resultado = execute($con, "SELECT password, accessToken FROM Users WHERE username='".$parametros["username"]."'");
     if (!select_is_empty($resultado)) {
       // the user exists, check password
       $row = mysqli_fetch_assoc($resultado);
       $hashedPassword = $row["password"];

       if (password_verify($parametros["password"], $hashedPassword)) {
          // good credentials
          $accessToken = $row["accessToken"];
          success($accessToken);
       }else{
         throw new Exception('wrong_credentials');
       }
     }else{
       throw new Exception('wrong_credentials');
     }
   }
} catch (Exception $e) {
  // error
  $json = array(
    'error' => $e->getMessage()
  );
  echo(json_encode($json));
}

close($con);

?>
