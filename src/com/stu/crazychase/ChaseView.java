package com.stu.crazychase;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;




public class ChaseView extends SurfaceView implements SurfaceHolder.Callback {

	private Context myContext;
	private SurfaceHolder mySurfaceHolder;
	private boolean running = false;
	private boolean pause = true;
	private ChaseThread thread;
	private ArrayList<Icon> iconArray = new ArrayList<Icon>();
	private Paint whitePaint;
	private Paint redPaint;
	private Target myTarget;
	private Bitmap press, press2;
//	private int molesWhacked=0;
	private int fingerX, fingerY;
	private int touchX, touchY;
	private int score;
	protected ArrayList<Integer> fxArray = new ArrayList<Integer>(10);
	private ArrayList<Integer> fyArray = new ArrayList<Integer>(10);
//	private int targetX, targetY;
	private int screenH;
	private int screenW;
	private int O = 25;
	private int T = 10;
	private int Q =4;
	
	 @Override
	    public void onSizeChanged (int w, int h, int oldw, int oldh){
	        super.onSizeChanged(w, h, oldw, oldh);
	        screenW = w;
	        screenH = h;
	 }
	
	
	@SuppressLint("HandlerLeak")
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
		}
		public void setSurfaceSize(int width, int height){
			synchronized (mySurfaceHolder){
				screenH = height;
				screenW = width;
				fingerX = width/2;
				fingerY = height/2;
				press = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.ic_finger);
				press = Bitmap.createScaledBitmap(press, O*6, O*6, false);
				press2 = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.ic_finger2);
				press2 = Bitmap.createScaledBitmap(press2, O*4, O*4, false);
				createIcons(Q);
				createTarget(1);
				whitePaint = new Paint();
				whitePaint.setAntiAlias(true);
				whitePaint.setColor(Color.WHITE);
				whitePaint.setStyle(Paint.Style.FILL);
				whitePaint.setTextAlign(Paint.Align.LEFT);
				whitePaint.setTextSize(30);
				redPaint = new Paint();
				redPaint.setAntiAlias(true);
				redPaint.setColor(Color.RED);
				redPaint.setStyle(Paint.Style.FILL);
				redPaint.setTextAlign(Paint.Align.LEFT);
				redPaint.setTextSize(40);
			}
		}
		public void createIcons(int b){
			int a=0;
			while(a<b){
				Icon tempIcon = new Icon(a, screenW, screenH);
				int resourceId = getResources().getIdentifier("icon2", "drawable", myContext.getPackageName());
				Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(), resourceId);
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempBitmap, O*2, O*2, false);
				tempIcon.setBitmap(scaledBitmap);
				iconArray.add(tempIcon);
				a++;
			}
		}
		private void createTarget(int id) {
			Target tempT = new Target(id, screenW, screenH);
			Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(), R.drawable.target2);
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempBitmap, O*2, O*2, false);
			tempT.setBitmap(scaledBitmap);
			myTarget = tempT;			
		}
		@Override
		public void run(){
			while(running){
				Canvas c = null;
				try{
					c=mySurfaceHolder.lockCanvas(null);
					synchronized (mySurfaceHolder){
						inputControl();
						if(!pause){
							animateIcons();
							animateTargetControl();
							score++;
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
					canvas.drawPaint(whitePaint);	
					canvas.drawText(screenW+" + "+screenH, 100,100, redPaint);
					canvas.drawText(myTarget.getX()+" "+myTarget.getY(), 100,150, redPaint);
					canvas.drawText("Score>>"+score, 100,50, redPaint);
					canvas.drawBitmap(myTarget.getBitmap(), myTarget.getX(),myTarget.getY(),null);
					if(!pause){
					canvas.drawBitmap(press, touchX-O*3,touchY-O*3,null);
					canvas.drawBitmap(press2, fingerX-O*2,fingerY-O*2,null);
					}
						for (int i=0;i<iconArray.size();i++){
							canvas.drawBitmap(iconArray.get(i).getBitmap(), iconArray.get(i).getX(),iconArray.get(i).getY(),null);
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
					pause =false;
					Log.i("DOWN", X+" + "+Y+ "  "+running);
					myTarget.updatePosition(X, Y, 0, 0);
					score = 0;
//						fingerX=X-O;
//						fingerY=Y-O;
//						touchX = X;
//						touchY = Y;
					break;
					case MotionEvent.ACTION_MOVE:
//						fingerX=X-O;
//						fingerY=Y-O;
						fxArray.add(0, X);
						fyArray.add(0, Y);
						if(fxArray.size()==10){
							fxArray.remove(fxArray.size()-1);
							fyArray.remove(fyArray.size()-1);
						}
					break;
				case MotionEvent.ACTION_UP:
					pause =true;
					fxArray.clear();
					fyArray.clear();
					
					break;
				}
			}
			return true;
		}
		public void inputControl(){
			if(!fxArray.isEmpty()){
				fingerX= fxArray.get(0);
				fingerY= fyArray.get(0);
				touchX = fxArray.get(fxArray.size()-1);
				touchY = fyArray.get(fyArray.size()-1);
			}
		}
		public void animateTargetControl(){
				int[] tempList = myTarget.getAll();
				int targetX = tempList[0];
				int targetY = tempList[1];
				int targetMX = tempList[2];
				int targetMY = tempList[3];
				int speed = tempList[4];
				int maxS = tempList[5];
				if(touchX<fingerX-O& targetMX<maxS&targetX<screenW+T){
					targetMX+=speed;
				}
				if(touchX>fingerX+O& targetMX>-maxS&targetX>0-T){
					targetMX-=speed;
				}
				if(touchY<fingerY-O& targetMY<maxS&targetY<screenH+T){
					targetMY+=speed;
				}
				if(touchY>fingerY+O& targetMY>-maxS&targetY>0-T){
					targetMY-=speed;
				}
					targetX+=targetMX;
					targetY+=targetMY;
				if(targetX<0){
					targetX=0;
				}else if(targetX+O*2>screenW){
					targetX=screenW-O*2;
				}
				if(targetY<0){
					targetY=0;
				}else if(targetY+O*2>screenH){
					targetY=screenH-O*2;
				}
				myTarget.updatePosition(targetX, targetY,targetMX, targetMY);
		}
		public void animateTarget(){
			int[] tempList = myTarget.getAll();
			int targetX = tempList[0];
			int targetY = tempList[1];
			int targetMX = tempList[2];
			int targetMY = tempList[3];
			int speed = tempList[4];
			if(targetX<fingerX+T*2){
				targetX+=speed;
			}
			if(targetX>fingerX-T*2){
				targetX-=speed;
			}
			if(targetY<fingerY+T*2){
				targetY+=speed;
			}
			if(targetY>fingerY-T*2){
				targetY-=speed;
			}
			myTarget.updatePosition(targetX, targetY,targetMX, targetMY);
	}
		public void animateIcons(){
			int tX = myTarget.getX();
			int tY = myTarget.getY();
			for (int i=0;i<iconArray.size();i++){
				int[] tempList = iconArray.get(i).getAll();
				int iconX = tempList[0];
				int iconY= tempList[1];
				int iconMX = tempList[2];
				int iconMY = tempList[3];
				int speed = tempList[4];
				int maxS = tempList[5];
				if(iconX<tX+(O*2)-T && iconX+(O*2)>tX+T && iconY<tY+(O*2)-T && iconY+(O*2)>tY+T){
					pause=true;
				}			
				if(iconX<tX+T*2& iconMX<maxS){
					iconMX+=speed;
				}
				if(iconX>tX-T*2& iconMX>-maxS){
					iconMX-=speed;
				}
				if(iconY<tY+T*2& iconMY<maxS){
					iconMY+=speed;
				}
				if(iconY>tY-T*2& iconMY>-maxS){
					iconMY-=speed;
				}
				iconX+=iconMX;
				iconY+=iconMY;
				iconArray.get(i).updatePosition(iconX, iconY,iconMX, iconMY);
			}
		}
		public void setRunning(boolean b){
			running = b;
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
			Log.i("thread", "thread started");
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		thread.setRunning(false);
	}
}
