package com.skzh.iot;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class SerialPort {

	private static final String TAG = "SerialPort";//字符串标签=“串口”//私有静态

	/*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;

	public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {

		/* Check access permission *///检查访问权限
		if (!device.canRead() || !device.canWrite()) //如果设备可读可以写
		{
			try {
				/* Missing read/write permission, trying to chmod the file *///丢失读写权限，试图chmod文件
				Process su;//过程
				su = Runtime.getRuntime().exec("/system/bin/su");//运行时得到运行时  执行  系统/斌/苏
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
						+ "exit\n";//字符串命令=“chmod 666”+设备获取绝对路径（）+“\n”+“退出\n”     文件/目录权限设置命令：chmod 
				Log.d("SerialPort", "cmd:" + cmd);
				
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					throw new SecurityException();//抛出新的安全异常
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}
		}

		
		
		mFd = open(device.getAbsolutePath(), baudrate, flags);
		if (mFd == null) {
			Log.e(TAG, "native open returns null");//日志标签   本地打开返回null
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}

	
	
	
	
	// Getters and setters//获取和设置
	public InputStream getInputStream() {
		return mFileInputStream;
	}
	

	public OutputStream getOutputStream() {
//		if(mFileOutputStream!=null){
//			try {
//				mFileOutputStream.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			mFileOutputStream = new FileOutputStream(mFd);
//		}
		return mFileOutputStream;
	}

	// JNI
	private native static FileDescriptor open(String path, int baudrate, int flags);//私人本地静态文件描述符的开弦的路径，波特率，int的旗帜
	public native void close();
	static {
		System.loadLibrary("serial_port");//加载库
	}
}
