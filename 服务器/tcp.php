<?php
use Workerman\Worker;
require_once '/root/Workerman/Autoloader.php';
// ����һ��Worker����2347�˿ڣ���ʹ���κ�Ӧ�ò�Э��
$tcp_worker = new Worker("tcp://0.0.0.0:2348");

// ����4�����̶����ṩ����
$tcp_worker->count = 4;

$i=0;

// ���ͻ��˷�������ʱ
$tcp_worker->onMessage = function($connection, $data)
{
	
	echo $data;
	try{
		$dbh = new PDO('mysql:dbname=iot;host=localhost', 'root', '96015');
	}catch(PDOException $e){
		echo '���ݿ�����ʧ�ܣ�'.$e->getMessage();
		exit;
	}
	//���͵�洢
	if($data=="youren"){
		$query = "UPDATE huanjin SET status=? where id=1"; 
		$stmt = $dbh->prepare($query);    	//����PDO�����е�prepare()����׼����ѯ
		$stmt->bindParam(1, $status);
		$status = $data;
	    $stmt->execute(); 
	}elseif($data=="meiren"){
		$query = "UPDATE huanjin SET status=? where id=1"; 
		$stmt = $dbh->prepare($query);    	//����PDO�����е�prepare()����׼����ѯ
		$stmt->bindParam(1, $status);
		$status = $data;
		$stmt->execute(); 
	}
	elseif($data=="kpwm"){
		$query = "UPDATE motor SET status=? where id=2"; 
		$stmt = $dbh->prepare($query);    	//����PDO�����е�prepare()����׼����ѯ
		$stmt->bindParam(1, $status);
		$status = "1";
		$stmt->execute(); 
	}elseif($data=="gpwm"){
		$query = "UPDATE motor SET status=? where id=2"; 
		$stmt = $dbh->prepare($query);    	//����PDO�����е�prepare()����׼����ѯ
		$stmt->bindParam(1, $status);
		$status = "0";
		$stmt->execute(); 
	}
	elseif($data=="kmotor"){
		$query = "UPDATE motor SET status=? where id=1"; 
		$stmt = $dbh->prepare($query);    	//����PDO�����е�prepare()����׼����ѯ
		$stmt->bindParam(1, $status);
		$status = "1";
		$stmt->execute(); 
	}elseif($data=="gmotor"){
		$query = "UPDATE motor SET status=? where id=1"; 
		$stmt = $dbh->prepare($query);    	//����PDO�����е�prepare()����׼����ѯ
		$stmt->bindParam(1, $status);
		$status = "0";
		$stmt->execute(); 
	}
	
	else{
	//��ʪ�ȹ����ַ����ָ�
	$arr = explode("/",$data); 
		$query = "UPDATE data SET wendu=?,shidu=?,guangzhao=? where id='1'"; 
		$stmtt = $dbh->prepare($query);    	//����PDO�����е�prepare()����׼����ѯ
		$stmtt->bindParam(1, $wen);
		$stmtt->bindParam(2, $shi);
		$stmtt->bindParam(3, $guang);
		$wen = $arr[0];
		$shi = $arr[1];
		$guang = $arr[2];
	    $stmtt->execute(); 
	}
	
	
	 // ��ͻ��˷���hello $data
     //$connection->send('hello ' . $data);
	 
	 //��ͻ��˷��͵����Ϣ
	 $stmt = $dbh->query("SELECT id,status FROM motor WHERE id=1");
	 while(list($id,$status)=$stmt->fetch(PDO::FETCH_NUM)){
		        if($status == "1")
		        $connection->send("dianjikai\n");
			    elseif($status == "0")
				$connection->send("dianjiguan\n");
				//$connection->send('dianji' . $status);
				
			}
	//��ͻ��˷���pwm��Ϣ
	$stmt = $dbh->query("SELECT id,status FROM motor WHERE id=2");
	 while(list($id,$status)=$stmt->fetch(PDO::FETCH_NUM)){
		        if($status == "1")
		        $connection->send("pwmkai\n");
			    elseif($status == "0")
				$connection->send("pwmguan\n");
				
			}
};

     

// ����worker
Worker::runAll();
