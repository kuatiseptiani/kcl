<?php
	require_once ('koneksi.php');
		$id = $_POST['id'];
				
		$sql = mysqli_query($conn, "DELETE FROM tb_kencleng WHERE id='$id'");
	
	if($sql) {
		echo json_encode(array('response'=>'succes'));
	} else {
		echo json_encode(array('response'=>'failed'));
	}
?>
