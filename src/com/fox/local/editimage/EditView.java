package com.fox.local.editimage;

import java.util.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class EditView extends View implements OnTouchListener,OnGestureListener
{
       
		private int state;  
		private float curX,curY;//当前画布坐标
        private Paint paint;
        private Path path;
        private PathEffect effect;
        private Bitmap myBitmap,bgBitmap,tempBitmap;
		private GestureDetector testGestureDetector;
        private Canvas canvas,tempcanvas;
       
		private ArrayList<Path> paths;
        private ArrayList<Paint> paints;

        
        
        //放大移动功能用到
    	// These matrices will be used to move and zoom image
    	private Matrix matrix = new Matrix();
    	private Matrix savedMatrix = new Matrix();

    	// We can be in one of these 3 states
    	private static final int NONE = 0;
    	private static final int DRAG = 1;
    	private static final int ZOOM = 2;
    	private int mode = NONE;

    	// Remember some things for zooming
    	private PointF start = new PointF();
    	private PointF mid = new PointF();
    	private float oldDist = 1f;
    	
    	
        public EditView(Context context,Bitmap bmp) {
    		super(context);
    		// TODO Auto-generated constructor stub
    	
    	   this.initBiamap(bmp);//设置背景
           this.setOnTouchListener(this);
           this.setLongClickable(true);

    	}
        //更新画笔
        public void UpPenStyle(PenStyle penSet,String style){
        	System.out.println("UpPenStyle："+style);
        	if(style.equalsIgnoreCase("eraser")){
        		paint=penSet.getPaint();
        		paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        	}
        	
        	if(style.equalsIgnoreCase("pen")){
        		//paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        		paint=penSet.getPaint();
        		paint.setColor(penSet.getpColor());
        	}
        	paint.setStrokeWidth(penSet.getpSize());
        }
      
        public void initBiamap(Bitmap bmp)
        {
        		state=EditActivity.ACTION_NORMAL;
        		
               
            	paths=new ArrayList<Path>();//保存路径的列表
        		paints=new ArrayList<Paint>();//保存路径相对的画笔
                //设置临时画板
                tempBitmap=Bitmap.createBitmap(600,800,Config.ARGB_4444);
                tempcanvas=new Canvas(tempBitmap);
                tempcanvas.drawColor(Color.TRANSPARENT);//设置为透明背景
                canvas = new Canvas(); 
                path = new Path();
                paint = new Paint();                
                paint.setColor(Color.BLUE);                       
                paint.setStyle(Paint.Style.STROKE);                
                paint.setStrokeWidth(3);
                paint.setAntiAlias(true);                        //打开抗锯齿
                effect = new CornerPathEffect(10);                //打开圆角效果
                testGestureDetector = new GestureDetector(this);
                //背景图片
                bgBitmap=bmp;
                //清屏
                paths.clear();
                
                curX=0;curY=50;
                invalidate();
               
        }
        @Override
        protected void onDraw(Canvas canvas)
        {
        	
                super.onDraw(canvas);
//                if(state==EditActivity.ACTION_DRAW){
//                	 paint.setAntiAlias(true);
//                     paint.setPathEffect(effect);
//                     canvas.drawBitmap(bgBitmap, 0, 0, null);
//                     tempcanvas.drawPath(path, paint);
//                     for(int index=0;index<paths.size();index++){
//                     	tempcanvas.drawPath(paths.get(index), paints.get(index));//画出路径
//                     }
//                     canvas.drawBitmap(tempBitmap, 0, 0, null);
//                     invalidate();                                        //刷新组件,以显示当前效果
//                }
//                if(state==EditActivity.ACTION_NORMAL){
                	this.canvas=canvas;
                	paint.setAntiAlias(true);
                    paint.setPathEffect(effect);
                    
                    //if(mode != NONE)
                    canvas.setMatrix(matrix);                   
                    
                    canvas.drawBitmap(bgBitmap, 0, 0, null);
                    
                    tempcanvas.drawPath(path, paint);
                    for(int index=0;index<paths.size();index++){
                    	tempcanvas.drawPath(paths.get(index), paints.get(index));//画出路径
                    }
                    canvas.drawBitmap(tempBitmap, 0, 0, null);
                	invalidate(); 
//                }
                return;
        }
        public boolean onTouch(View v, MotionEvent event) 
        {
                
                // TODO Auto-generated method stub
               
        	if(state==EditActivity.ACTION_DRAW){
        		 //获取笔画坐标
        		 float x= event.getX();
                 float y = event.getY();
                 
                 if((event.getAction()) == (MotionEvent.ACTION_DOWN)){                        //当touchNum = 0时，则为一个笔画的起点，因此将path起点移动到此点
                 		 path = new Path();               		
                         path.moveTo(x,y);
                          
                 }
                 else if((event.getAction()) == (MotionEvent.ACTION_MOVE)){
               
                 	  path.lineTo(x,y);
                 }
                             
                 if((event.getAction()) == (MotionEvent.ACTION_UP))                //手指离开屏幕时，则下一笔为新笔画起点，因此相关数据置0
                 {
                          paths.add(path);
                          paints.add(paint);
                 }
        	}
        	/////////////////////////////////////////
        	/////////////画板移动的///////////////////
            if(state==EditActivity.ACTION_NORMAL){
            	 
            	 switch (event.getAction() & MotionEvent.ACTION_MASK) {
	         		case MotionEvent.ACTION_DOWN:
	
	         			matrix.set(this.canvas.getMatrix());
	         			savedMatrix.set(matrix);
	         			start.set(event.getX(), event.getY());
	         			
	         			mode = DRAG;
	
	         			break;
	         		case MotionEvent.ACTION_POINTER_DOWN:
	         			
	         			oldDist = spacing(event);//计算两点间距离设为缩放大小
	
	         			if (oldDist > 10f) {
	         				savedMatrix.set(matrix);
	         				midPoint(mid, event);
	         				
	         				mode = ZOOM;
	         		
	         			}
	         			break;
	         		case MotionEvent.ACTION_UP:
	         		case MotionEvent.ACTION_POINTER_UP:
	         			mode = NONE;
	
	         			break;
	         		case MotionEvent.ACTION_MOVE:
	         			if (mode == DRAG) {
	         				// 移动画板
	         				matrix.set(savedMatrix);
	         				matrix.postTranslate(event.getX() - start.x, event.getY()
	         						- start.y);//设置位置
	         				
	         				curX = event.getX();
		         			curY = event.getY();
	         			} else if (mode == ZOOM) {
	         				//画板缩放
	         				float newDist = spacing(event);
	         				if (newDist > 10f) {
	         					matrix.set(savedMatrix);
	         					float scale = newDist / oldDist;
	         					matrix.postScale(scale, scale, mid.x, mid.y);//设置放缩
	         				}
	         			}
	         			
	         			break;
	         		 }
            	 
               }    
           
           return true;
                
        }
        public boolean onDown(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
        }
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) 
        {
                // TODO Auto-generated method stub
                return false;
        }
        public void onLongPress(MotionEvent e) {
                // TODO Auto-generated method stub
                
        }
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                        float distanceY) {
                // TODO Auto-generated method stub
                return false;
        }
        public void onShowPress(MotionEvent e) {
                // TODO Auto-generated method stub
        }
        public boolean onSingleTapUp(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
        }
        public Bitmap getMyBitmap() {
			return myBitmap;
		}
		public void setMyBitmap(Bitmap myBitmap) {
			this.myBitmap = myBitmap;
		}

		public Bitmap getTempBitmap() {
			return tempBitmap;
		}
		public void setTempBitmap(Bitmap tempBitmap) {
			this.tempBitmap = tempBitmap;
		}
		public Bitmap getBgBitmap() {
			return bgBitmap;
		}
		public void setBgBitmap(Bitmap bgBitmap) {
			this.bgBitmap = bgBitmap;
		}
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		
		
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}

		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}

}