<?php

function hashPassword($pass){
  $hash = password_hash($pass, PASSWORD_BCRYPT);
  return $hash;
}

function create_user_group($accessToken, $firebaseToken)
{
  try {
      // crear grupo de usuario de firebase para un único usuario
      $msg = array(
        'operation' => 'create',
        'notification_key_name' => $accessToken,
        'registration_ids' => array($firebaseToken)
      );

      $msgJSON = json_encode($msg);
      $json = enviar_post_fcm($msgJSON, 'https://fcm.googleapis.com/fcm/notification');

      $notification_key = $json["notification_key"];
      return $notification_key;
  } catch (Exception $e) {
    throw new Exception("connection_error");
  }
}


function add_device_to_group($accessToken, $firebaseToken, $groupId)
{
  // el grupo de usuario ya existe, añadir dispositivo
  try {
      // crear grupo de usuario de firebase para un único usuario
      $msg = array(
        'operation' => 'add',
        'notification_key_name' => $accessToken,
        'notification_key' => $groupId,
        'registration_ids' => array($firebaseToken)
      );
      $msgJSON = json_encode($msg);
      $json = enviar_post_fcm($msgJSON, 'https://fcm.googleapis.com/fcm/notification');
      // si se produce un fallo es que la respuesta no ha sido correcta
      $notification_key = $json["notification_key"];
  } catch (Exception $e) {
    throw new Exception("connection_error");
  }
}

function enviar_post_fcm($msgJSON, $url='https://fcm.googleapis.com/fcm/send'){
  // enviar post a fcm
  $cabecera= array(
  'Authorization: key=AIzaSyBUmzG6OtRIyQ1aVJXSpAjP4tnwQQZVyUw',
  'Content-Type: application/json',
  'project_id: 376726478494'
  );
  $ch = curl_init(); #inicializar el handler de curl
  #indicar el destino de la petición, el servicio FCM de google
  curl_setopt( $ch, CURLOPT_URL, $url);
  #indicar que la conexión es de tipo POST
  curl_setopt( $ch, CURLOPT_POST, true );
  #agregar las cabeceras
  curl_setopt( $ch, CURLOPT_HTTPHEADER, $cabecera);
  #Indicar que se desea recibir la respuesta a la conexión en forma de string
  curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
  #agregar los datos de la petición en formato JSON
  curl_setopt( $ch, CURLOPT_POSTFIELDS, $msgJSON);
  #ejecutar la llamada
  $resultado= curl_exec( $ch );
  $responseCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
  #cerrar el handler de curl
  curl_close( $ch );

  if (curl_errno($ch) || $responseCode != "200") {
    throw new Exception("connection_error");
  }
  $json = json_decode($resultado, true);
  return $json;
}


 ?>
