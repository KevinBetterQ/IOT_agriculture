<?php
//设置页面内容是html编码格式是utf-8
header("Content-Type: text/plain;charset=utf-8"); 


	try{
			$dbh = new PDO('mysql:dbname=iot;host=localhost', 'root', '96015');
			}catch(PDOException $e){
			echo '数据库连接失败：'.$e->getMessage();
			exit;
			}
	$stmt = $dbh->query("SELECT wendu,shidu,guangzhao FROM data WHERE id=1"); 
	while(list($wendu,$shidu,$guangzhao)=$stmt->fetch(PDO::FETCH_NUM)){
				//echo "温度 $wendu &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 
				//湿度 $shidu &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 
				//光照 $guangzhao<br><br>";
				$wen=$wendu;
				$shi=$shidu;
				$guang=$guangzhao;
			}
			//显示热释电
			$stmt = $dbh->query("SELECT status FROM huanjin WHERE id=1"); 
			while(list($ren)=$stmt->fetch(PDO::FETCH_NUM)){
				if($ren=="youren"){
				//	echo "检测到有人<br>";
					$reshi= "有人";
				}elseif($ren=="meiren"){
				//	echo "检测到无人<br>";
					$reshi= "无人";
				}
			}

if ($_SERVER["REQUEST_METHOD"] == "GET") {
	search();//显示温湿度
} elseif ($_SERVER["REQUEST_METHOD"] == "POST"){
	echo "post";
	create();//上传控制信息，更新数据库，电机、PWM信息
}

//显示温湿度
function search(){
	
	global $wen;
	global $shi;
	global $guang;
	global $reshi;
	
	$result = '服务器时间' . date("Y-m-d H:i:s") . 
			'<br><br>温度：' . $wen . 
			' &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp湿度：' . $shi . 
			' &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp光照：' . $guang . 
			' &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<br><br>人员检测：' . $reshi ;
    echo $result;
}

//更新数据库，电机、PWM信息
function create(){
	if(($_POST['kmotor'])== "1") {
			echo "start<br>";
			$affected = $dbh->exec("UPDATE motor SET status=1 WHERE id=1");
			echo "success";
			}
	if(isset($_POST["gmotor"])) {
			echo "stop<br>";
			$affected = $dbh->exec("UPDATE motor SET status=0 WHERE id=1");
			echo 'gggg';
			}
			
	if(isset($_POST["kpwm"])) {
			echo "startpwm<br>";
			$affected = $dbh->exec("UPDATE motor SET status=1 WHERE id=2");
			}
	if(isset($_POST["gpwm"])) {
			echo "stoppwm<br>";
			$affected = $dbh->exec("UPDATE motor SET status=0 WHERE id=2");
			}
	}


?>