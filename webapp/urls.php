<?php

define('__PROJECT_ROOT__', dirname(__FILE__));
//require_once(__PROJECT_ROOT__.'/connector.php');
require_once('connector.php');
require_once('utils.php');


function success($message){
  $json = array(
    'success' => $message,
  );
  echo(json_encode($json));
}


$parametros = json_decode(file_get_contents('php://input'), true);

if (!array_key_exists('action', $parametros) && !isset($_POST['action'])) {
  exit;
}

$con = '';

if (isset($_POST['action'])){
  $action = $_POST['action'];
}else{
  $action = $parametros["action"];
}

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
         $accessToken = $row["accessToken"];
         $firebaseToken = $parametros["firebaseToken"];

         if (password_verify($parametros["password"], $hashedPassword)) {
            // good credentials
         }else{
           // bad credentials
           throw new Exception('wrong_credentials');
         }

         $groupId = get_user_group($accessToken);

         if ($groupId == NULL) {
           // se crea el grupo para el usuario
           create_user_group($accessToken, $firebaseToken);
         }else{
           // el grupo de usuario ya existe, añadir dispositivo
           add_device_to_group($accessToken, $firebaseToken, $groupId);
         }
         success($accessToken);
     }else{
       throw new Exception('wrong_credentials');
     }
   }elseif ($action == "logout") {
     /*---- Log out ----*/
     $accessToken = $parametros["username"];
     $firebaseToken = $parametros["firebaseToken"];
     $groupId = get_user_group($accessToken);

     if ($groupId != NULL) {
       // el grupo de usuario existe, quitar dispositivo
       remove_device_from_group($accessToken, $firebaseToken, $groupId);
     }
     success("ok");

   }elseif ($action == "fetchselfnotes") {
      //se obtienen las nuevas selfnotes desde la última vez que se miraron
      $username = $parametros["username"];
      $date = $parametros["date"];
      $timestamp = date('Y-m-d H:i:s', strtotime($date));

      $now = date('Y-m-d H:i:s', strtotime($parametros["now"]));

      if ($timestamp == $now) {
        // first time fetching
        $resultado = execute($con, "SELECT message, imagePath, creationDate FROM SelfNotes WHERE accessToken='".$username."' AND creationDate <= '".$now."'");
      }else{
        $resultado = execute($con, "SELECT message, imagePath, creationDate FROM SelfNotes WHERE accessToken='".$username."' AND creationDate > '".$timestamp."' AND creationDate <= '".$now."'");
      }
      $all_json = array(); // all the results
      if (!select_is_empty($resultado)) {
        // there are new notes, transform then into json object
        while($row = mysqli_fetch_assoc($resultado)) {
          // create json object
          $json_row = array(
            'imagePath' => $row["imagePath"],
            'date' => $row["creationDate"],
            'message'=> $row["message"]
          );
          $all_json[] = $json_row;
        }
      }
      success($all_json);
   }else if($action == "sendselfnotes"){
     // a user creates a new self note with text
     $date = $parametros["date"];
     $timestamp = date('Y-m-d H:i:s', strtotime($date));

     execute($con, "INSERT INTO SelfNotes(accessToken, message, creationDate)
              VALUES ('".$parametros["username"]."', '".$parametros["message"]."', '".$timestamp."')");

      // avisar a sus otros dispositivos de android
      $groupId = get_user_group($parametros["username"]);
      if ($groupId != NULL) {
        send_message_to_group($groupId, $parametros["message"], 'message', $parametros["message"], $timestamp);
      }

     success("ok");
   }else if($action == "sendphoto"){
     // a user creates a new self note with a photo
      $date = $_POST['date'];
      $timestamp = date('Y-m-d H:i:s', strtotime($date));
      $filename = date('Ymd_His', strtotime($date));

      $base = $_POST['image'];
      $binary = base64_decode($base);
      file_put_contents($filename.".jpg", $binary);

      $actual_link = "https://134.209.235.115/pdejaime001/WEB/".$filename.".jpg";

      execute($con, "INSERT INTO SelfNotes(accessToken, imagePath, creationDate)
               VALUES ('".$_POST["username"]."', '".$actual_link."', '".$timestamp."')");

       // avisar a sus otros dispositivos de android
       $groupId = get_user_group($_POST["username"]);
       if ($groupId != NULL) {
         send_message_to_group($groupId, "📷", "image", $actual_link, $timestamp);
       }
      success("ok");
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
