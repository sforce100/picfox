package com.fox.local.editimage;

import android.graphics.Paint;

public class PenStyle {
	private  int pColor;
	private  int pSize=3;
	
	private Paint paint;
	public Paint getPaint(){
		paint=new Paint();
		paint.setColor(getpColor());                        
        paint.setStyle(Paint.Style.STROKE);                
        paint.setStrokeWidth(getpSize());
        paint.setAntiAlias(true);                        //´ò¿ª¿¹¾â³Ý
		return paint;
	}

	public  void setpColor(int color) {
		pColor=color;
	}
	public  int getpSize() {
		return pSize;
	}
	public  void setpSize(int pSize) {
		this.pSize = pSize;
	}
	
	public  int getpColor() {
		return pColor;
	}


	
}
