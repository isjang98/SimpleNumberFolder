package simple.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NumberFolderView  extends  RelativeLayout {

	private Paint mTextPaint;
	private String mText = "0";

	private int tsize = 16;
	
	private int tx = 0;
	private int ty = 0;
	
	private static final int RANGE_MIN = 0;
	private static final int RANGE_MAX = 9;
	private int curvalue = 0;
	private String[] textrange = {
			"0","1","2","3","4","5","6","7","8","9"
	};
	
	RelativeLayout.LayoutParams lpbk = null;
	RelativeLayout.LayoutParams lpup = null;
	RelativeLayout.LayoutParams lpbtm = null;
	RelativeLayout.LayoutParams lpup2 = null;
	RelativeLayout.LayoutParams lpbtm2 = null;
	
	NumberLabelView nlvbk = null;
	NumberLabelView nlvup = null;
	NumberLabelView nlvbtm = null;
	NumberLabelView nlvup2 = null;
	NumberLabelView nlvbtm2 = null;
	
	private int width = 0;
	private int height = 0;
	private int x = 0;
	private int y =0;
	
	public NumberFolderView(Context context,  int tsize, int x, int y, int w, int h) {
		super(context);
		this.tsize = tsize;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		
		this.setMeasuredDimension(width, height);
		nlvbk = new NumberLabelView(context,  NumberLabelView.STYLE_NORMAL);
		nlvup = new NumberLabelView(context,  NumberLabelView.STYLE_UPPER);
		nlvbtm = new NumberLabelView(context,  NumberLabelView.STYLE_BOTTOM);
		nlvup2 = new NumberLabelView(context,  NumberLabelView.STYLE_UPPER2);
		nlvbtm2 = new NumberLabelView(context,  NumberLabelView.STYLE_BOTTOM2);
		
		initView();
	}
	
	private void initView(){
		
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(tsize);
		mTextPaint.setColor(Color.WHITE);
		setNumberText(curvalue);
		setTextAncher();
		downASet(curvalue);
		
		
		lpbk = new RelativeLayout.LayoutParams(width, height);
		lpup = new RelativeLayout.LayoutParams(width, height/2);
		lpbtm = new RelativeLayout.LayoutParams(width, height/2);
		lpup2 = new RelativeLayout.LayoutParams(width, height/2);
		lpbtm2 = new RelativeLayout.LayoutParams(width, height/2);

		nlvbk.setBackgroundColor(Color.BLACK);
		/*
		nlvup.setBackgroundColor(Color.DKGRAY);
		nlvbtm.setBackgroundColor(Color.GRAY);
		nlvup2.setBackgroundColor(Color.DKGRAY);
		nlvbtm2.setBackgroundColor(Color.GRAY);
		*/
		
		nlvup.setBackgroundColor(Color.BLACK);
		nlvbtm.setBackgroundColor(Color.BLACK);
		nlvup2.setBackgroundColor(Color.BLACK);
		nlvbtm2.setBackgroundColor(Color.BLACK);

		lpbk.setMargins(x, y, x+width, y+height);
		lpup.setMargins(x, y, x+width, y+height/2);
		lpbtm.setMargins(x, y+height/2, x+width, y+height);
		lpup2.setMargins(x, y+height/2, x+width, y+height);
		lpbtm2.setMargins(x, y, x+width, y+height/2);
				
		addView(nlvbk, lpbk);
		addView(nlvup, lpup);
		addView(nlvbtm, lpbtm);
		addView(nlvup2, lpup2);
		addView(nlvbtm2, lpbtm2);
		
		nlvup.setVisibility(View.INVISIBLE);
		nlvup2.setVisibility(View.INVISIBLE);
		nlvbtm2.setVisibility(View.INVISIBLE);
		nlvbtm.setVisibility(View.INVISIBLE);
		
		
	}
	
	private void setTextAncher(){
		
		tx = (width - (int) mTextPaint.measureText(mText));
		if(tx <= 0){
			tx = 0;
		}else{
			tx = tx/2;
		}
		
		int mAscent = (int)mTextPaint.ascent();
		int mDescent = (int)mTextPaint.descent();
		
		ty = (height+mAscent)/2 - mAscent - (mDescent/2);
		
		if (ty <= 0 ){
			ty=0;						
		}
		
	}
	
	private void downASet(int nextval){
		
		nlvbk.setText(textrange[nextval]);
		nlvup.setText(textrange[curvalue]);
		nlvbtm.setText(textrange[curvalue]);
		nlvup2.setText(textrange[nextval]);
		nlvbtm2.setText(textrange[curvalue]);
		
		if(nlvbtm.getVisibility() == View.INVISIBLE){
			nlvbtm.setVisibility(View.VISIBLE);
		}
		
		requestLayout();
		reflash();
		
	}
	
	private void upASet(int nextval){
		
		nlvbk.setText(textrange[nextval]);
		
		nlvup.setText(textrange[curvalue]);
		nlvbtm.setText(textrange[curvalue]);
		nlvup2.setText(textrange[curvalue]);
		nlvbtm2.setText(textrange[nextval]);
		
		if(nlvup.getVisibility() == View.INVISIBLE){
			nlvup.setVisibility(View.VISIBLE);
		}
		
		requestLayout();
		reflash();
		
	}
	
	public void setNumberText(int curval){
		
		if(curval <  RANGE_MIN){curval = RANGE_MAX;}
		if(curval >  RANGE_MAX){curval = RANGE_MIN;}
		
		Log.i("setNumberText", "current["+curvalue+"], next["+curval+"]");
		
		if((curvalue == RANGE_MAX) &&  (curval == RANGE_MIN )){
			//dwa
			downASet(curval);
			nlvup.startAnim();
			nlvup2.startAnim();
		}else if((curvalue == RANGE_MIN) &&  (curval == RANGE_MAX )){
			//upa
			upASet(curval);
			//Log.i("setNumberText2", ">>>>>>>>>>>>");
			
			nlvbtm.startAnim();
			nlvbtm2.startAnim();
			//downASet(curval);
			//nlvup.startAnim();
			//nlvup2.startAnim();
		}else if(curval > curvalue ){
			//dwa
			downASet(curval);
			
			nlvup.startAnim();
			nlvup2.startAnim();
		}else if(curval < curvalue){
			//upa
			upASet(curval);
			nlvbtm.startAnim();
			nlvbtm2.startAnim();
		}else if(curval == curvalue){
			return;
		}
		
		this.curvalue = curval;
		mText = textrange[curvalue];
	}
	
	public int getCurrentNumber(){
		return this.curvalue;
	}
	
	public String getText(){
		return mText;
	}
	
	public void setTextSize(int size){
		this.tsize = size;
		mTextPaint.setTextSize(size);
		setTextAncher();
		
		nlvbk.setTextSize(size);
		nlvup.setTextSize(size);
		nlvbtm.setTextSize(size);
		nlvup2.setTextSize(size);
		nlvbtm2.setTextSize(size);
		
		requestLayout();
		reflash();
	}
	
	public void setTextColor(int color){
		mTextPaint.setColor(color);
		
		nlvbk.setTextColor(color);
		nlvup.setTextColor(Color.RED);
		nlvbtm.setTextColor(Color.YELLOW);
		
		reflash();
	}
	
	public void reflash(){
		nlvbk.invalidate();
		nlvup.invalidate();
		nlvbtm.invalidate();
		nlvup2.invalidate();
		nlvbtm2.invalidate();
	}
	
	
	class NumberLabelView extends TextView implements Animation.AnimationListener{

		public static final int STYLE_NORMAL  = 0;
		public static final int STYLE_UPPER  = 1;
		public static final int STYLE_BOTTOM  = 2;
		public static final int STYLE_UPPER2  = 3;
		public static final int STYLE_BOTTOM2  = 4;
		private int style = 0;
		private Rect rect;
		
		float start = 0;
		float end = 90;
		float end2 = 180;
		
		long speed = 1000;
		long hspeed = speed/2;
		long hssoffest = hspeed - (hspeed/2);
		
		AnimationSet animset;
		Rot3D rotanim;
		AlphaAnimation alphaanim;
		
		public NumberLabelView(Context context, int style){
			super(context);
			this.style = style;
			
			rect = new Rect();
			
			animset = new AnimationSet(true);
			
			switch(style){
			case STYLE_NORMAL:
				rect.set(x,y,x+width,y+height);
				break;
			case STYLE_UPPER:
			{
				rotanim = new Rot3D(start, -end, width/2, height/2, 0.0f, true ,STYLE_UPPER);
				rotanim.setFillAfter(true);
				rotanim.setDuration(hspeed);
				animset.addAnimation(rotanim);
				/*
				alphaanim = new AlphaAnimation(0.8f, 0.5f);
				alphaanim.setFillAfter(true);
				alphaanim.setDuration(1000);
				alphaanim.setStartOffset(1000);
				animset.addAnimation(alphaanim);
				*/
				animset.setRepeatMode(Animation.RESTART);
				animset.setRepeatMode(Animation.INFINITE);
				animset.setAnimationListener(this);
			}
				break;
			case STYLE_BOTTOM:
			{
				rotanim = new Rot3D(start, end, width/2, 0.0f, 0.0f, true, STYLE_BOTTOM);
				rotanim.setFillAfter(true);
				rotanim.setDuration(hspeed);
				animset.addAnimation(rotanim);
				/*
				alphaanim = new AlphaAnimation(0.8f, 0.5f);
				alphaanim.setFillAfter(true);
				alphaanim.setDuration(1000);
				alphaanim.setStartOffset(1000);
				animset.addAnimation(alphaanim);
				*/
				animset.setRepeatMode(Animation.RESTART);
				animset.setRepeatMode(Animation.INFINITE);
				animset.setAnimationListener(this);
			}
				break;
			case STYLE_UPPER2:
			{
				rotanim = new Rot3D(end,0.0f, width/2, 0.0f, 0.0f, true, STYLE_UPPER2);

				rotanim.setFillAfter(true);
				rotanim.setDuration(hspeed);
				rotanim.setStartOffset(hssoffest);
				animset.addAnimation(rotanim);
				
				/*
				alphaanim = new AlphaAnimation(0.8f, 0.5f);
				alphaanim.setFillAfter(true);
				alphaanim.setDuration(1000);
				alphaanim.setStartOffset(1000);
				animset.addAnimation(alphaanim);
				*/
				animset.setRepeatMode(Animation.RESTART);
				animset.setRepeatMode(Animation.INFINITE);
				animset.setAnimationListener(this);
			}
				break;
			case STYLE_BOTTOM2:
			{
				rotanim = new Rot3D(end, 0.0f, width/2, height/2, 0.0f, true, STYLE_BOTTOM2);
				rotanim.setFillAfter(true);
				rotanim.setDuration(hspeed);
				rotanim.setStartOffset(hssoffest);
				animset.addAnimation(rotanim);
				
				/*
				alphaanim = new AlphaAnimation(0.8f, 0.5f);
				alphaanim.setFillAfter(true);
				alphaanim.setDuration(1000);
				alphaanim.setStartOffset(1000);
				animset.addAnimation(alphaanim);
				*/
				animset.setRepeatMode(Animation.RESTART);
				animset.setRepeatMode(Animation.INFINITE);
				animset.setAnimationListener(this);
			}
				break;
			
			}

		}
		
		public void startAnim(){
			if(this.style != STYLE_NORMAL){
				if(this.getVisibility() == View.INVISIBLE){
					this.setVisibility(View.VISIBLE);
				}
				this.startAnimation(animset);
			}
		}
		
		
		public void onDraw(Canvas canvas){

			//canvas.save();
			switch(style){
			case STYLE_NORMAL:
				//mTextPaint.setColor(Color.WHITE);
				canvas.drawText(this.getText().toString(), tx, ty, mTextPaint);
				break;
			case STYLE_UPPER:
				//mTextPaint.setColor(Color.GREEN);
				canvas.drawText(this.getText().toString(), tx, ty, mTextPaint);
				break;
			case STYLE_BOTTOM:
				//mTextPaint.setColor(Color.YELLOW);
				canvas.drawText(this.getText().toString(), tx, ty-(height/2), mTextPaint);
				break;
			case STYLE_UPPER2:
				//mTextPaint.setColor(Color.CYAN);
				canvas.drawText(this.getText().toString(), tx, ty-(height/2), mTextPaint);
				break;
			case STYLE_BOTTOM2:
				//mTextPaint.setColor(Color.MAGENTA);
				canvas.drawText(this.getText().toString(), tx, ty, mTextPaint);
				break;
			}
			//canvas.restore();
			//Log.i("onDraw", "painted..");
		}
		

		public void onAnimationEnd(Animation animation) {
			Log.i("onAnimationEnd", animation.toString());
			if(this.getVisibility() == View.VISIBLE){
				this.setVisibility(View.INVISIBLE);
			}
			
			if(nlvup.getVisibility() == View.VISIBLE){
				nlvup.setVisibility(View.INVISIBLE);
			}
			if(nlvbtm.getVisibility() == View.VISIBLE){
				nlvbtm.setVisibility(View.INVISIBLE);
			}
		}

		public void onAnimationRepeat(Animation animation) {
			Log.i("onAnimationRepeat", animation.toString());
		}

		public void onAnimationStart(Animation animation) {
			Log.i("onAnimationStart", animation.toString());
			
			
			
		}
		
	}
	
	class Rot3D extends Animation {
	    private final float mFromDegrees;
	    private final float mToDegrees;
	    private final float mCenterX;
	    private final float mCenterY;
	    private final float mDepthZ;
	    private final boolean mReverse;
	    private Camera mCamera;
	    private int style;

	    public Rot3D(float fromDegrees, float toDegrees,
	            float centerX, float centerY, float depthZ, boolean reverse, int style) {
	        mFromDegrees = fromDegrees;
	        mToDegrees = toDegrees;
	        mCenterX = centerX;
	        mCenterY = centerY;
	        mDepthZ = depthZ;
	        mReverse = reverse;
	        this.style = style;
	    }

	    public void initialize(int width, int height, int parentWidth, int parentHeight) {
	        super.initialize(width, height, parentWidth, parentHeight);
	        mCamera = new Camera();
	    }

	    protected void applyTransformation(float interpolatedTime, Transformation t) {
	        final float fromDegrees = mFromDegrees;
	        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

	        final float centerX = mCenterX;
	        final float centerY = mCenterY;
	        final Camera camera = mCamera;

	        final Matrix matrix = t.getMatrix();

	        camera.save();
	        

	        if (mReverse) {
	            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
	        } else {
	            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
	        }

	        
	        //camera.rotateY(degrees);
	        camera.rotateX(degrees);
	        camera.getMatrix(matrix);
	        
	        camera.restore();

	        matrix.preTranslate(-centerX, -centerY);
	        matrix.postTranslate(centerX, centerY);
	        //Log.i("interpolatedTime : ",Float.toString(interpolatedTime));
	    }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
