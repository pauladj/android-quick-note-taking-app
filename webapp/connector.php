<?php


/*define("DB_SERVER", "localhost"); #la dirección del servidor
define("DB_USER", "Xpdejaime001"); #el usuario para esa base de datos
define("DB_PASS", "8WwMyYf4jz"); #la clave para ese usuario
define("DB_DATABASE", "Xpdejaime001_"); #la base de datos a la que hay que conectarse
*/
// Webcindario
define("DB_SERVER", "mysql.webcindario.com"); #la dirección del servidor
define("DB_USER", "dawepauladj"); #el usuario para esa base de datos
define("DB_PASS", "92749274"); #la clave para ese usuario
define("DB_DATABASE", "dawepauladj");
/*
define("DB_SERVER", "localhost"); #la dirección del servidor
define("DB_USER", "root"); #el usuario para esa base de datos
define("DB_PASS", ""); #la clave para ese usuario
define("DB_DATABASE", "das");*/


function connect(){
  # Se establece la conexión:
  $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASS, DB_DATABASE);

  #Comprobamos conexión
  if (mysqli_connect_errno($con)) {
    echo 'Error de conexion: ' . mysqli_connect_error();
    throw new Exception('external_error');
  }

  return $con;
}


function execute($con, $sentence){
  # Ejecutar la sentencia SQL
  $resultado = mysqli_query($con, $sentence);
  # Comprobar si se ha ejecutado correctamente
  if (!$resultado) {
    echo 'Ha ocurrido algún error: ' . mysqli_error($con);
    exit;
    throw new Exception('external_error');
  }
  return $resultado;

}

function select_is_empty($resultado){
  if (mysqli_num_rows($resultado) > 0) {
    return true;
  }else{
    return false;
  }
}

function close($con){
  try {
    if (isset($con)){
      mysqli_close($con);
    }
  } catch (Exception $e) {
    //
  }
}

?>
