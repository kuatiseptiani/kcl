<?php
	require_once ('koneksi.php');
		$status = $_POST['status'];
		$nominal = $_POST['nominal'];
		$catatan = $_POST['catatan'];
		$tanggal = $_POST['tanggal'];
		
		//insert comment
		$sql = mysqli_query($conn, "INSERT INTO transaksi (status, 
		nominal, catatan, tanggal) VALUES ('$status', '$nominal', 
		'$catatan', '$tanggal')");
	
	if($query) {
		echo json_encode(array('response'=>'succes'));
	} else {
		echo json_encode(array('response'=>'failed'));
	}
?>
