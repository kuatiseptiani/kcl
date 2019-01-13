<?php
 //Mendefinisikan Konstanta
 define('HOST','localhost');
 define('USER','root');
 define('PASS','');
 define('DB','android');
 
 //membuat koneksi dengan database
 $conn = mysqli_connect(HOST,USER,PASS,DB) or die('Tidak bisa connect');
 date_default_timezone_set("Asia/Jakarta);
 ?>
