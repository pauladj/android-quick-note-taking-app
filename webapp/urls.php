<?php

define('__PROJECT_ROOT__', dirname(__FILE__));
//require_once(__PROJECT_ROOT__.'/connector.php');
require_once('connector.php');


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
   $allGood = false;
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
     $resultado = execute($con, "SELECT username FROM Users WHERE username='".$parametros["username"]."' AND password='".$parametros["password"]."'");
     if (select_is_empty($resultado)) {
        // save the new user
        execute($con, "INSERT INTO Users(username, password)
                 VALUES ('".$parametros["username"]."', '".$parametros["password"]."')");
        success("ok");
     }else{
       throw new Exception('username_exists');
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
