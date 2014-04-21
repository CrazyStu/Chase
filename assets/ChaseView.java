package com.stu.crazychase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;




public class ChaseView extends SurfaceView implements SurfaceHolder.Callback {

	private Context myContext;
	private SurfaceHolder mySurfaceHolder;
	private Bitmap backgroundImg;
	private int screenW = 1;
	private int screenH = 1;
	private boolean running = false;
	private boolean onTitle = true;
	private ChaseThread thread;
	private int backgroundOrigW;
	private int backgroundOrigH;
	private float scaleH;
	private float scaleW;
	private float drawScaleW;
	private float drawScaleH;
	private Bitmap mole;
	private int mole1x;
	private int mole1y;
	private int activeMole=0;
	private boolean moleRising=true;
	private boolean moleSinking=true;
	private int moleRate=3;
	private boolean moleJustHit=false;	
	private Paint blackPaint;
	private Bitmap whack;
	private boolean whacking=false;
	private int molesWhacked=0;
	private int lives =5;
	private int molesMissed=lives;
	private int fingerX, fingerY;
	public int highScore;
	private boolean gameOver=false;

	public static final String PREFERENCES_NAME = "log";
	
	public ChaseView(Context context, AttributeSet attrs){
		super(context, attrs);
		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		thread = new ChaseThread(holder, context, new Handler(){
			@Override
			public void handleMessage(Message m) {
			}
		});
		setFocusable(true);
	}
	
	public ChaseThread getThread(){
		return thread;
	}
	
	class ChaseThread extends Thread{
		public ChaseThread(SurfaceHolder surfaceHolder, Context context, Handler handler){
			mySurfaceHolder = surfaceHolder;
			myContext = context;
//			backgroundImg=BitmapFactory.decodeResource(context.getResources(), R.drawable.title);
//    		backgroundOrigW = backgroundImg.getWidth();
//    		backgroundOrigH = backgroundImg.getHeight();
//    		sounds = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
//    		whackSound = sounds.load(myContext, R.raw.whack, 1);
//    		missSound = sounds.load(myContext, R.raw.miss5, 1);
		}
		@Override
		public void run(){
			while (running){
				Canvas c = null;
				try{
					c=mySurfaceHolder.lockCanvas(null);
					synchronized (mySurfaceHolder){
						if(!gameOver){
						animateMoles();
						}
						draw(c);
					}
				} finally {
					if (c != null){
						mySurfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
		private void draw (Canvas canvas){
			try{
//				canvas.drawBitmap(backgroundImg, 0, 0, null);
//				if(soundOn){
//					canvas.drawBitmap(sound, 10, 100, null);
//				}else {
//					canvas.drawBitmap(noSound, 10, 100, null);
//				}
				if(!onTitle){
					canvas.drawText("Score: "+ Integer.toString(molesWhacked), 10, blackPaint.getTextSize()+10, blackPaint);
					canvas.drawBitmap(mole, mole1x, mole1y, null);
//					if(whacking){
//						canvas.drawBitmap(whack, fingerX-(whack.getWidth()/2),fingerY-(whack.getHeight()/2),null);
//					}
					//canvas.drawText(Boolean.toString(soundOn), screenW/2, 70, blackPaint);
				}
			}catch(Exception e){
			}
		}
		boolean doTouchEvent(MotionEvent event){
			synchronized (mySurfaceHolder){
				int eventaction = event.getAction();
				int X=(int)event.getX();
				int Y=(int)event.getY();
				switch (eventaction){
				case MotionEvent.ACTION_DOWN:
					if(!gameOver){
						fingerX=X;
						fingerY=Y;
					}
					break;
					case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					if(onTitle){
						scaleW = (float)screenW/(float)backgroundOrigW;
						scaleH = (float)screenH/(float)backgroundOrigH;
					}
					whacking=false;
//							WhackAMoleActivity.highScoreSave=molesWhacked;
					break;
				}
			}
			return true;
		}
		public void setSurfaceSize(int width, int height){
			synchronized (mySurfaceHolder){
				screenW=width;
				screenH=height;
				drawScaleW = (float)screenW/800;
				drawScaleH = (float)screenH/600;
				mole1x=(int)(55*drawScaleW);
				blackPaint = new Paint();
				blackPaint.setAntiAlias(true);
				blackPaint.setColor(Color.BLACK);
				blackPaint.setStyle(Paint.Style.STROKE);
				blackPaint.setTextAlign(Paint.Align.LEFT);
				blackPaint.setTextSize(drawScaleW*30);
			}
		}
		public void setRunning(boolean b){
			running = b;
		}
		public void animateMoles(){
			if(activeMole==1){
				if (moleRising){
					mole1y-=moleRate;
				}else if(moleSinking){
					mole1y+=moleRate;
				}
				if(mole1y>(int)(475*drawScaleH)||moleJustHit){
					mole1y=(int)(475*drawScaleH);
				}
				if(mole1y<=(int)(300*drawScaleH)){
					mole1y=(int)(300*drawScaleH);
					moleRising=false;
					moleSinking=true;
				}
			}
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		return thread.doTouchEvent(event);
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder,int format,int width, int height){
		thread.setSurfaceSize(width,height);
	}
	public void surfaceCreated(SurfaceHolder holder){
		thread.setRunning(true);
		if (thread.getState()==Thread.State.NEW){
			thread.start();
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		thread.setRunning(false);
	}
//	private boolean detectMoleContact(){
//		boolean contact = false;
//		if (activeMole==1 &&
//				fingerX>= mole1x &&
//				fingerX<mole1x+(int)(88*drawScaleW)&&
//				fingerY>mole1y&&
//				fingerY<(int)450*drawScaleH){
//			contact=true;
//			moleJustHit = true;
//		}
//		return contact;
//	}
}
