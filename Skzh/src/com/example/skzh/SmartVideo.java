
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
		//SurfaceView是可以直接从内存或者DMA等硬件接口取得图像数据,是个非常重要的绘图容器
		SurfaceView surfaceView =  (SurfaceView) findViewById(R.id.surfaceView);  
	       surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
	       surfaceView.getHolder().setFixedSize(200, 200);
	        
	       surfaceView.getHolder().addCallback(new SurfaceViewCallback());  
	    // 给SurfaceView当前的持有者一个回调对象。
	}

	private final class SurfaceViewCallback implements Callback {  
        /** 
         * surfaceView 被创建成功后调用此方法 
         */  
        @Override  
        public void surfaceCreated(SurfaceHolder holder) {  
          //  Log.d(TAG,"surfaceCreated");  
            /*  
             * 在SurfaceView创建好之后 打开摄像头 
             * 注意是 android.hardware.Camera 
             */  
            camera = Camera.open();  
            /* 
             * This method must be called before startPreview(). otherwise surfaceview没有图像 
             */  
            try {  
                camera.setPreviewDisplay(holder);  
            } catch (IOException e) {  
                // TODO Auto-generated catch block   
                e.printStackTrace();  
            }  
              
            Camera.Parameters parameters = camera.getParameters();  
            /* 设置预览照片的大小，此处设置为全屏 */  
//          WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); // 获取当前屏幕管理器对象   
//          Display display = wm.getDefaultDisplay();                        // 获取屏幕信息的描述类   
//          parameters.setPreviewSize(display.getWidth(), display.getHeight());          // 设置   
            parameters.setPreviewSize(200, 200); 
            
            
            /* 每秒从摄像头捕获5帧画面， */  
            parameters.setPreviewFrameRate(5);  
            
            //parameters.(rotation)
            /* 设置照片的输出格式:jpg */  
            parameters.setPictureFormat(PixelFormat.JPEG);  
            /* 照片质量 */  
            parameters.set("jpeg-quality", 85);  
            /* 设置照片的大小：此处照片大小等于屏幕大小 */  
//          parameters.setPictureSize(display.getWidth(), display.getHeight());   
            
            
            parameters.setPictureSize(200, 200);  
            /* 将参数对象赋予到 camera 对象上 */  
//          camera.setParameters(parameters);   
          //  mSeekBar.setMax(100);  
            camera.setDisplayOrientation(180);
            try{
            	camera.startPreview();
            }catch(Exception e){
            	Toast.makeText(SmartVideo.this, "摄像头初始化失败", Toast.LENGTH_SHORT).show();
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
                    //在视频聊天中，这里传送本地frame数据给remote端   
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
         * SurfaceView 被销毁时释放掉 摄像头 
         */  
        @Override  
        public void surfaceDestroyed(SurfaceHolder holder) {  
            if(camera != null) {  
                /* 若摄像头正在工作，先停止它 */  
                if(preview) {  
                    camera.stopPreview();  
                    preview = false;  
                }  
                //如果注册了此回调，在release之前调用，否则release之后还回调，crash   
                camera.setPreviewCallback(null);  
                camera.release();  
            }  
        }  
          
    }  
      
    /** 
     * 处理照片被拍摄之后的事件 
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
