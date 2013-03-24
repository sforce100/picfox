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
		private float curX,curY;//��ǰ��������
        private Paint paint;
        private Path path;
        private PathEffect effect;
        private Bitmap myBitmap,bgBitmap,tempBitmap;
		private GestureDetector testGestureDetector;
        private Canvas canvas,tempcanvas;
       
		private ArrayList<Path> paths;
        private ArrayList<Paint> paints;

        
        
        //�Ŵ��ƶ������õ�
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
    	
    	   this.initBiamap(bmp);//���ñ���
           this.setOnTouchListener(this);
           this.setLongClickable(true);

    	}
        //���»���
        public void UpPenStyle(PenStyle penSet,String style){
        	System.out.println("UpPenStyle��"+style);
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
        		
               
            	paths=new ArrayList<Path>();//����·�����б�
        		paints=new ArrayList<Paint>();//����·����ԵĻ���
                //������ʱ����
                tempBitmap=Bitmap.createBitmap(600,800,Config.ARGB_4444);
                tempcanvas=new Canvas(tempBitmap);
                tempcanvas.drawColor(Color.TRANSPARENT);//����Ϊ͸������
                canvas = new Canvas(); 
                path = new Path();
                paint = new Paint();                
                paint.setColor(Color.BLUE);                       
                paint.setStyle(Paint.Style.STROKE);                
                paint.setStrokeWidth(3);
                paint.setAntiAlias(true);                        //�򿪿����
                effect = new CornerPathEffect(10);                //��Բ��Ч��
                testGestureDetector = new GestureDetector(this);
                //����ͼƬ
                bgBitmap=bmp;
                //����
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
//                     	tempcanvas.drawPath(paths.get(index), paints.get(index));//����·��
//                     }
//                     canvas.drawBitmap(tempBitmap, 0, 0, null);
//                     invalidate();                                        //ˢ�����,����ʾ��ǰЧ��
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
                    	tempcanvas.drawPath(paths.get(index), paints.get(index));//����·��
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
        		 //��ȡ�ʻ�����
        		 float x= event.getX();
                 float y = event.getY();
                 
                 if((event.getAction()) == (MotionEvent.ACTION_DOWN)){                        //��touchNum = 0ʱ����Ϊһ���ʻ�����㣬��˽�path����ƶ����˵�
                 		 path = new Path();               		
                         path.moveTo(x,y);
                          
                 }
                 else if((event.getAction()) == (MotionEvent.ACTION_MOVE)){
               
                 	  path.lineTo(x,y);
                 }
                             
                 if((event.getAction()) == (MotionEvent.ACTION_UP))                //��ָ�뿪��Ļʱ������һ��Ϊ�±ʻ���㣬������������0
                 {
                          paths.add(path);
                          paints.add(paint);
                 }
        	}
        	/////////////////////////////////////////
        	/////////////�����ƶ���///////////////////
            if(state==EditActivity.ACTION_NORMAL){
            	 
            	 switch (event.getAction() & MotionEvent.ACTION_MASK) {
	         		case MotionEvent.ACTION_DOWN:
	
	         			matrix.set(this.canvas.getMatrix());
	         			savedMatrix.set(matrix);
	         			start.set(event.getX(), event.getY());
	         			
	         			mode = DRAG;
	
	         			break;
	         		case MotionEvent.ACTION_POINTER_DOWN:
	         			
	         			oldDist = spacing(event);//��������������Ϊ���Ŵ�С
	
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
	         				// �ƶ�����
	         				matrix.set(savedMatrix);
	         				matrix.postTranslate(event.getX() - start.x, event.getY()
	         						- start.y);//����λ��
	         				
	         				curX = event.getX();
		         			curY = event.getY();
	         			} else if (mode == ZOOM) {
	         				//��������
	         				float newDist = spacing(event);
	         				if (newDist > 10f) {
	         					matrix.set(savedMatrix);
	         					float scale = newDist / oldDist;
	         					matrix.postScale(scale, scale, mid.x, mid.y);//���÷���
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