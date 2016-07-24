<?php
    try{
			$dbh = new PDO('mysql:dbname=iot;host=localhost', 'root', '96015');
			}catch(PDOException $e){
			echo '数据库连接失败：'.$e->getMessage();
			exit;
			}

	
?>

<html>
	<head>
	<title>iot</title>
	<meta http-equiv="refresh" content="10">
	</head>

	<body background="background.jpg" >
		
		<h1 align="center">环境监测</h1> 

		<center>
		<!-- 在HTML中使用以下标记嵌入PHP脚本 -->
		<?php
			
			//使用query方式执行SELECT语句，建议使用prepare()和execute()形式执行语句
			$stmt = $dbh->query("SELECT wendu,shidu,guangzhao FROM data WHERE id=1"); 
			echo "服务器时间".date("Y-m-d H:i:s")."<br><br>";
			while(list($wendu,$shidu,$guangzhao)=$stmt->fetch(PDO::FETCH_NUM)){
				echo "温度 $wendu &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 
				湿度 $shidu &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 
				光照 $guangzhao<br><br>";
			}
			//显示热释电
			$stmt = $dbh->query("SELECT status FROM huanjin WHERE id=1"); 
			while(list($ren)=$stmt->fetch(PDO::FETCH_NUM)){
				if($ren=="youren"){
					echo "检测到有人<br>";
				}elseif($ren=="meiren"){
					echo "检测到无人<br>";
				}
			}
		?>
		</center>
		
		<h1 align="center">控制管理</h1>
		<h2><font color="#0000CD">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		通风系统：</h2>
		<center>
		<form action="test.php" method="post">
			<input type="submit" name="kai" value="打开">
			<input type="submit" name="guan" value="关闭">
		</form>
		</center>
		
		<h2><font color="gold">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
		补光系统：</h2>
		<center>
		<form action="test.php" method="post">
			<input type="submit" name="jia" value="补光">
			<input type="submit" name="jian" value="关闭">
		</form>
		</center>
		
		<?php
		    if(isset($_POST['kai'])) {
			echo "start<br>";
			$affected = $dbh->exec("UPDATE motor SET status=1 WHERE id=1");
			}
			if(isset($_POST['guan'])) {
			echo "stop<br>";
			$affected = $dbh->exec("UPDATE motor SET status=0 WHERE id=1");
			}
			
			if(isset($_POST['jia'])) {
			echo "startpwm<br>";
			$affected = $dbh->exec("UPDATE motor SET status=1 WHERE id=2");
			}
			if(isset($_POST['jian'])) {
			echo "stoppwm<br>";
			$affected = $dbh->exec("UPDATE motor SET status=0 WHERE id=2");
			}
	    ?>
		
	</body>
</html>

