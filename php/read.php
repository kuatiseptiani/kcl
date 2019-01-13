<?php
	require_once ('koneksi.php');
	
	$sql = mysqli_query($conn, "SELECT * FROM tb_kencleng ORDER BY id DESC");
	while($baris = mysqli_fetch_array($sql)) {
		array_push($hasil, array(
		'id' => $baris['id'],
		'status' => $baris['status'],
		'nominal' => $baris['nominal'],
		'catatan' => $baris['catatan'],
		'tanggal' => $baris['tanggal'],
		));
	}	
	$sql = mysqli_query($conn, "SELECT (SELECT SUM(Jumlah) FROM tb_kencleng WHERE
	status='pemasukan') AS pemasukan, (SELECT SUM(Jumlah) FROM tb_kencleng WHERE
	status='pengeluaran') AS pengeluaran
	");
	
	while($baris = mysqli_fetch_array($sql)) {
		$pemasukan = $baris['pemasukan'];
		$pengeluaran = $baris['pengeluaran'];
		}
	
	echo json_encode(array(
	'pemasukan' => $pemasukan,
	'pengeluaran' => $pengeluaran,
	'saldo' => ($pemasukan - $pengeluaran),
	'hasil' => $hasil,
	));
?>
