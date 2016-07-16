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

	private static final String TAG = "SerialPort";//�ַ�����ǩ=�����ڡ�//˽�о�̬

	/*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;

	public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {

		/* Check access permission *///������Ȩ��
		if (!device.canRead() || !device.canWrite()) //����豸�ɶ�����д
		{
			try {
				/* Missing read/write permission, trying to chmod the file *///��ʧ��дȨ�ޣ���ͼchmod�ļ�
				Process su;//����
				su = Runtime.getRuntime().exec("/system/bin/su");//����ʱ�õ�����ʱ  ִ��  ϵͳ/��/��
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
						+ "exit\n";//�ַ�������=��chmod 666��+�豸��ȡ����·������+��\n��+���˳�\n��     �ļ�/Ŀ¼Ȩ���������chmod 
				Log.d("SerialPort", "cmd:" + cmd);
				
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					throw new SecurityException();//�׳��µİ�ȫ�쳣
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}
		}

		
		
		mFd = open(device.getAbsolutePath(), baudrate, flags);
		if (mFd == null) {
			Log.e(TAG, "native open returns null");//��־��ǩ   ���ش򿪷���null
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}

	
	
	
	
	// Getters and setters//��ȡ������
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
	private native static FileDescriptor open(String path, int baudrate, int flags);//˽�˱��ؾ�̬�ļ��������Ŀ��ҵ�·���������ʣ�int������
	public native void close();
	static {
		System.loadLibrary("serial_port");//���ؿ�
	}
}
