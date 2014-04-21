
package com.stu.crazychase;

import android.graphics.Bitmap;

public class Icon{

	private Integer id;
	private Bitmap bmp;
	private int X;
	private int Y;
	private int mX;
	private int mY;
	private int speed;
	private int maxSpeed;
	
	public Icon(int newId, int w, int h){
		if (newId==0){
			id = newId;
			X = (int) (Math.random()*800);
			Y = 0;
			speed = 2;
			maxSpeed = 40;
			mY = 0;
			mX = 0;
		}else if (newId==1){
			id = newId;
			X = (int) (Math.random()*800);
			Y = 0;
			speed = 1;
			maxSpeed = 25;
			mY = 0;
			mX = 0;
		}else if (newId==2){
			id = newId;
			X = (int) (Math.random()*800);
			Y = 0;
			speed = 3;
			maxSpeed = 13;
			mY = 0;
			mX = 0;
		}else if (newId==3){
			id = newId;
			X = (int) (Math.random()*800);
			Y = 0;
			speed = 4;
			maxSpeed = 10;
			mY = 0;
			mX = 0;
		}else{
			randomIcon(newId);
		}
	}
	public void randomIcon(int newId){
		id = newId;
		X = (int) (Math.random()*800);
		Y = (int) (Math.random()*200);
		speed = (int) (Math.random()*7+1);
		maxSpeed = (int) (Math.random()*7+2);
		mY = 0;
		mX = 0;
	}
	public void updatePosition(int nX, int nY, int nmX, int nmY){
		X=nX;
		Y=nY;
		mX=nmX;
		mY=nmY;
	}
	public void setBitmap(Bitmap newBitmap){
		bmp = newBitmap;
	}
	public Bitmap getBitmap() {
		return bmp;
	}
	public int getId(){
		return id;
	}
	public int getX() {
		return X;
	}
	public int getY() {
		return Y;
	}
	public int getSpeed() {
		return speed;
	}
	public int[] getAll(){
		int[] temp = new int[]{X,Y,mX,mY,speed,maxSpeed};
		return temp;
	}
}
