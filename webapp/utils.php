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
      $json = enviar_post_fcm($msgJSON, 'https://fcm.googleapis.com/fcm/notification', true);

      $notification_key = $json["notification_key"];
  } catch (Exception $e) {
    throw new Exception("connection_error");
  }
}

function get_user_group($accessToken)
{
  try {
      // se obtiene el grupo de un usuario si lo tiene
      $json = enviar_post_fcm(NULL, "https://fcm.googleapis.com/fcm/notification?notification_key_name=".$accessToken, false);

      if (isset($json["notification_key"])) {
        // el usuario tiene grupo
        return $json["notification_key"];
      }else{
        // el usuario no tiene grupo
        return NULL;
      }
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
      $json = enviar_post_fcm($msgJSON, 'https://fcm.googleapis.com/fcm/notification', true);
      // si se produce un fallo es que la respuesta no ha sido correcta
      $notification_key = $json["notification_key"];
  } catch (Exception $e) {
    throw new Exception("connection_error");
  }
}

function remove_device_from_group($accessToken, $firebaseToken, $groupId)
{
  // el grupo de usuario ya existe, quitar dispositivo
  try {
      $msg = array(
        'operation' => 'remove',
        'notification_key_name' => $accessToken,
        'notification_key' => $groupId,
        'registration_ids' => array($firebaseToken)
      );
      $msgJSON = json_encode($msg);
      $json = enviar_post_fcm($msgJSON, 'https://fcm.googleapis.com/fcm/notification', true);
  } catch (Exception $e) {
    throw new Exception("connection_error");
  }
}

function send_message_to_group($groupId, $elemento){
  // mandar mensaje a un usuario en todas las aplicaciones en las que haya iniciado sesión
  // que tiene una nota corta nueva, es decir, la ha mandado desde alguna de sus aplicaciones
  try {
      $msg = array(
        'to' => $groupId,
        'data' => array(
          'action' => 'newselfnote',
        ),
        'notification' => array (
            'body' => $elemento,
            'title' => 'Nueva nota breve',
            'icon' => 'ic_stat_ic_notification',
        )
      );
      $msgJSON = json_encode($msg);
      $json = enviar_post_fcm($msgJSON, 'https://fcm.googleapis.com/fcm/send', true);
  } catch (Exception $e) {
    throw new Exception("connection_error");
  }
}

function enviar_post_fcm($msgJSON, $url, $isPost){
  // enviar post a fcm
  $cabecera= array(
  'Authorization: key=AIzaSyBUmzG6OtRIyQ1aVJXSpAjP4tnwQQZVyUw',
  'Content-Type: application/json',
  'project_id: 376726478494'
  );
  $ch = curl_init(); #inicializar el handler de curl
  #indicar el destino de la petición, el servicio FCM de google
  curl_setopt( $ch, CURLOPT_URL, $url);
  if ($isPost == true) {
    #indicar que la conexión es de tipo POST
    curl_setopt( $ch, CURLOPT_POST, true );
    #agregar los datos de la petición en formato JSON
    curl_setopt( $ch, CURLOPT_POSTFIELDS, $msgJSON);
  }

  #agregar las cabeceras
  curl_setopt( $ch, CURLOPT_HTTPHEADER, $cabecera);
  #Indicar que se desea recibir la respuesta a la conexión en forma de string
  curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );

  #ejecutar la llamada
  $resultado= curl_exec( $ch );
  $responseCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
  #cerrar el handler de curl
  curl_close( $ch );

  if (curl_errno($ch) || ($responseCode != "200" && $responseCode != "400") ) {
    throw new Exception("connection_error");
  }
  $json = json_decode($resultado, true);
  return $json;
}


 ?>
