
物联网实训做一个智慧农业项目。  

主要涉及： WSN感知数据、zigbee组网、PHP服务器、Android客户端  
主要任务：  
1、zigbee节点编程和配置  
2、多种传感器的使用，数据获取  
3、传感器数据的传输：   
（zigbee通信协议的熟悉）  
4、串口通信  
只有用串口通信，才能将协调器的数据获取出来。  
5、socket通信  

文件介绍：  
Skzh----Android网关程序，实现串口通信，socket传输，网关控制  
服务器  
tcp.php----PHP服务器，进行socket通信，与数据库存取  
test.php----网页控制端，但没有无闪烁刷新  
iot.html和iot.html---改进的网页控制端，实现无闪烁刷新  
物联网课程设计----文档介绍，包括概要设计、遇到问题和心得体会  
