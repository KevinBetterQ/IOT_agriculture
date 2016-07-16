
package com.example.skzh;

import java.io.IOException;


import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

public class SmartVideo extends Activity{
	Camera camera;
    boolean preview = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smartvideo);
		//SurfaceView�ǿ���ֱ�Ӵ��ڴ����DMA��Ӳ���ӿ�ȡ��ͼ������,�Ǹ��ǳ���Ҫ�Ļ�ͼ����
		SurfaceView surfaceView =  (SurfaceView) findViewById(R.id.surfaceView);  
	       surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
	       surfaceView.getHolder().setFixedSize(200, 200);
	        
	       surfaceView.getHolder().addCallback(new SurfaceViewCallback());  
	    // ��SurfaceView��ǰ�ĳ�����һ���ص�����
	}

	private final class SurfaceViewCallback implements Callback {  
        /** 
         * surfaceView �������ɹ�����ô˷��� 
         */  
        @Override  
        public void surfaceCreated(SurfaceHolder holder) {  
          //  Log.d(TAG,"surfaceCreated");  
            /*  
             * ��SurfaceView������֮�� ������ͷ 
             * ע���� android.hardware.Camera 
             */  
            camera = Camera.open();  
            /* 
             * This method must be called before startPreview(). otherwise surfaceviewû��ͼ�� 
             */  
            try {  
                camera.setPreviewDisplay(holder);  
            } catch (IOException e) {  
                // TODO Auto-generated catch block   
                e.printStackTrace();  
            }  
              
            Camera.Parameters parameters = camera.getParameters();  
            /* ����Ԥ����Ƭ�Ĵ�С���˴�����Ϊȫ�� */  
//          WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); // ��ȡ��ǰ��Ļ����������   
//          Display display = wm.getDefaultDisplay();                        // ��ȡ��Ļ��Ϣ��������   
//          parameters.setPreviewSize(display.getWidth(), display.getHeight());          // ����   
            parameters.setPreviewSize(200, 200); 
            
            
            /* ÿ�������ͷ����5֡���棬 */  
            parameters.setPreviewFrameRate(5);  
            
            //parameters.(rotation)
            /* ������Ƭ�������ʽ:jpg */  
            parameters.setPictureFormat(PixelFormat.JPEG);  
            /* ��Ƭ���� */  
            parameters.set("jpeg-quality", 85);  
            /* ������Ƭ�Ĵ�С���˴���Ƭ��С������Ļ��С */  
//          parameters.setPictureSize(display.getWidth(), display.getHeight());   
            
            
            parameters.setPictureSize(200, 200);  
            /* �����������赽 camera ������ */  
//          camera.setParameters(parameters);   
          //  mSeekBar.setMax(100);  
            camera.setDisplayOrientation(180);
            try{
            	camera.startPreview();
            }catch(Exception e){
            	Toast.makeText(SmartVideo.this, "����ͷ��ʼ��ʧ��", Toast.LENGTH_SHORT).show();
            }
           // camera.startPreview();  
            /** 
             * Installs a callback to be invoked for every preview frame in addition to displaying them on the screen.  
             * The callback will be repeatedly called for as long as preview is active. This method can be called at  
             * any time, even while preview is live. Any other preview callbacks are overridden. 
             * a callback object that receives a copy of each preview frame, or null to stop receiving  
             */  
            camera.setPreviewCallback(new Camera.PreviewCallback(){  
  
                @Override  
                public void onPreviewFrame(byte[] data, Camera camera) {  
                    // TODO Auto-generated method stub   
                    //����Ƶ�����У����ﴫ�ͱ���frame���ݸ�remote��   
                   // Log.d(TAG, "camera:"+camera);  
                   // Log.d(TAG, "byte:"+data);  
                }  
                  
            });  
            preview = true;  
        }  
        @Override  
        public void surfaceChanged(SurfaceHolder holder, int format, int width,  
                int height) {  
           // Log.d(TAG,"surfaceChanged");  
        }  
        /** 
         * SurfaceView ������ʱ�ͷŵ� ����ͷ 
         */  
        @Override  
        public void surfaceDestroyed(SurfaceHolder holder) {  
            if(camera != null) {  
                /* ������ͷ���ڹ�������ֹͣ�� */  
                if(preview) {  
                    camera.stopPreview();  
                    preview = false;  
                }  
                //���ע���˴˻ص�����release֮ǰ���ã�����release֮�󻹻ص���crash   
                camera.setPreviewCallback(null);  
                camera.release();  
            }  
        }  
          
    }  
      
    /** 
     * ������Ƭ������֮����¼� 
     */  
    private final class TakePictureCallback implements PictureCallback {  
        @Override  
        public void onPictureTaken(byte[] data, Camera camera) {  
        }  
    }  
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
