<?php

function hashPassword($pass){
  $hash = password_hash($pass, PASSWORD_BCRYPT);
  return $hash;
}


 ?>
