<?php
	require_once ('koneksi.php');
		$id = $_POST['id'];
		$status = $_POST['status'];
		$nominal = $_POST['nominal'];
		$catatan = $_POST['catatan'];
		$tanggal = $_POST['tanggal'];
		
		$sql = mysqli_query($conn, "UPDATE tb_kencleng SET status='$status', 
		nominal='$nominal', catatan='$catatan', tanggal='$tanggal' WHERE id='$id'");
	
	if($sql) {
		echo json_encode(array('response'=>'succes'));
	} else {
		echo json_encode(array('response'=>'failed'));
	}
?>
