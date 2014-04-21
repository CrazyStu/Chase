
package com.stu.crazychase;

import android.graphics.Bitmap;

public class Target{

	private Integer id;
	private Bitmap bmp;
	private int X;
	private int Y;
	private int mX;
	private int mY;
	private int speed;
	private int maxSpeed;
	
	public Target(int newId, int w, int h){
			id = newId;
			X = w/2;
			Y = (int)(w*0.8);
			speed = 2;
			maxSpeed = 15;
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
	public void setX(int x){
		X=x;
	}
	public void setY(int y){
		Y=y;
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
