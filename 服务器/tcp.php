<?php
use Workerman\Worker;
require_once '/root/Workerman/Autoloader.php';
// 创建一个Worker监听2347端口，不使用任何应用层协议
$tcp_worker = new Worker("tcp://0.0.0.0:2348");

// 启动4个进程对外提供服务
$tcp_worker->count = 4;

$i=0;

// 当客户端发来数据时
$tcp_worker->onMessage = function($connection, $data)
{
	
	echo $data;
	try{
		$dbh = new PDO('mysql:dbname=iot;host=localhost', 'root', '96015');
	}catch(PDOException $e){
		echo '数据库连接失败：'.$e->getMessage();
		exit;
	}
	//热释电存储
	if($data=="youren"){
		$query = "UPDATE huanjin SET status=? where id=1"; 
		$stmt = $dbh->prepare($query);    	//调用PDO对象中的prepare()方法准备查询
		$stmt->bindParam(1, $status);
		$status = $data;
	    $stmt->execute(); 
	}elseif($data=="meiren"){
		$query = "UPDATE huanjin SET status=? where id=1"; 
		$stmt = $dbh->prepare($query);    	//调用PDO对象中的prepare()方法准备查询
		$stmt->bindParam(1, $status);
		$status = $data;
		$stmt->execute(); 
	}
	elseif($data=="kpwm"){
		$query = "UPDATE motor SET status=? where id=2"; 
		$stmt = $dbh->prepare($query);    	//调用PDO对象中的prepare()方法准备查询
		$stmt->bindParam(1, $status);
		$status = "1";
		$stmt->execute(); 
	}elseif($data=="gpwm"){
		$query = "UPDATE motor SET status=? where id=2"; 
		$stmt = $dbh->prepare($query);    	//调用PDO对象中的prepare()方法准备查询
		$stmt->bindParam(1, $status);
		$status = "0";
		$stmt->execute(); 
	}
	elseif($data=="kmotor"){
		$query = "UPDATE motor SET status=? where id=1"; 
		$stmt = $dbh->prepare($query);    	//调用PDO对象中的prepare()方法准备查询
		$stmt->bindParam(1, $status);
		$status = "1";
		$stmt->execute(); 
	}elseif($data=="gmotor"){
		$query = "UPDATE motor SET status=? where id=1"; 
		$stmt = $dbh->prepare($query);    	//调用PDO对象中的prepare()方法准备查询
		$stmt->bindParam(1, $status);
		$status = "0";
		$stmt->execute(); 
	}
	
	else{
	//温湿度光照字符串分割
	$arr = explode("/",$data); 
		$query = "UPDATE data SET wendu=?,shidu=?,guangzhao=? where id='1'"; 
		$stmtt = $dbh->prepare($query);    	//调用PDO对象中的prepare()方法准备查询
		$stmtt->bindParam(1, $wen);
		$stmtt->bindParam(2, $shi);
		$stmtt->bindParam(3, $guang);
		$wen = $arr[0];
		$shi = $arr[1];
		$guang = $arr[2];
	    $stmtt->execute(); 
	}
	
	
	 // 向客户端发送hello $data
     //$connection->send('hello ' . $data);
	 
	 //向客户端发送电机信息
	 $stmt = $dbh->query("SELECT id,status FROM motor WHERE id=1");
	 while(list($id,$status)=$stmt->fetch(PDO::FETCH_NUM)){
		        if($status == "1")
		        $connection->send("dianjikai\n");
			    elseif($status == "0")
				$connection->send("dianjiguan\n");
				//$connection->send('dianji' . $status);
				
			}
	//向客户端发送pwm信息
	$stmt = $dbh->query("SELECT id,status FROM motor WHERE id=2");
	 while(list($id,$status)=$stmt->fetch(PDO::FETCH_NUM)){
		        if($status == "1")
		        $connection->send("pwmkai\n");
			    elseif($status == "0")
				$connection->send("pwmguan\n");
				
			}
};

     

// 运行worker
Worker::runAll();
