package progressbar.example.aric.customprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by aric on 16/6/30.
 */
public class customedProgressBar extends ProgressBar {
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //super.onDraw(canvas); 这里完全由我们自己进行绘制,所以删除父类中的onDraw()方法;

        canvas.save();

        canvas.translate(getPaddingLeft(),getHeight()/2);
        /**
         * 线性的prograssbar 的绘制分为三个部分:unreachprograss Text reachprograss
         *
         */
        boolean noNeedUnreach=false;//是否需要画Unreach 部分
        String text = getProgress()+"%";
        float radio = getProgress()*1.0f/getMax();//进度的百分比=当前进度/总进度
        int textWidth= (int) mPaint.measureText(text);//获取中间文字部分的宽度
        float progressX=radio*mRealWidth+mTextOffset/2-textWidth;//reach部分的长度
        //判断是否需要继续画UnReach部分
        if(progressX+textWidth>mRealWidth){noNeedUnreach=true;
           progressX=mRealWidth-textWidth;
        }

        /**
         * 1.画Reach部分
         */

        float endX = radio*mRealWidth-mTextOffset/2-textWidth;//reach 部分的终点位置
        if(endX>0){
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }
        /**
         * 2.画文字部分
         */
        mPaint.setColor(mTextColor);
        /**
         * 计算文字的基准线: Paint
         * 概念理解:
         * baseline 相当于英语音标四线中第三条线
         * ascent   相当于四线中有f的时候 他是固定在f的最上方
         * descent  相当于四线中有g的时候他是固定在g的最下方
         * top      相当于baseline到四线最上面线的位置
         * bottom   相当于baseline到四线最下面线的位置
         *
         * 测量文字的高度只需要 ascent and descent
         * 测量文字的宽度 paint.measureText();
         *
         * paint.getTextBounds(Rect)---可以根据这个Rect去获取高度 宽度,缺点是得到的int值 其精度不高.
         */
        int y= (int) (-(mPaint.descent()+mPaint.ascent())/2);
        canvas.drawText(text,progressX,y,mPaint);

        /**
         *3.画unReach 部分
         */

         if(!noNeedUnreach){
             float start = progressX+mTextOffset/2 + textWidth;
             mPaint.setColor(mUnreachColor);
             mPaint.setStrokeWidth(mUnreachHeight);
             canvas.drawLine(start,0,mRealWidth,0,mPaint);
         }


        canvas.restore();

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height=measureHeight(heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        //define the heigth and width for the custom view.
        setMeasuredDimension(width,height);
        mRealWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode==MeasureSpec.EXACTLY){
            result = size;
        }else{

            result=getPaddingBottom()+getPaddingTop()+Math.max(Math.max(mUnreachHeight,mReachHeight),Math.abs((int)(mPaint.descent()-mPaint.ascent())));

            if (mode==MeasureSpec.AT_MOST){
                result=Math.min(size,result);
            }
        }



        return result;
    }


    /**
     * define default value for each attribute.
     */
    private  static final  int DEFAULT_TEXT_SIZE=10;
    private  static final  int DEFAULT_TEXT_COLOR=0xFFFC00D1;
    private  static final  int DEFAULT_UNREACH_COLOR=0xFFD3D6DA;
    private  static final  int DEFAULT_UNREACH_HEIGHT=2;
    private  static final  int DEFAULT_REACH_COLOR=0xFFFC00D1;
    private  static final  int DEFAULT_REACH_HEIGHT=2;
    private  static final  int DEFAULT_TEXT_OFFSET=10;

    /**
     * define each attribute.
     */
    protected int mTextSize=sp2dp(DEFAULT_TEXT_SIZE);
    protected int mTextColor=DEFAULT_TEXT_COLOR;
    protected int mUnreachColor=DEFAULT_UNREACH_COLOR;
    protected int mUnreachHeight=dp2px(DEFAULT_UNREACH_HEIGHT);
    protected int mReachColor=DEFAULT_REACH_COLOR;
    protected int mReachHeight=dp2px(DEFAULT_REACH_HEIGHT);
    protected int mTextOffset=dp2px(DEFAULT_TEXT_OFFSET);

    protected Paint mPaint= new Paint();
    protected int mRealWidth;

    public customedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.customedProgressBar);
        mTextSize= (int) ta.getDimension(R.styleable.customedProgressBar_progress_text_size,mTextSize);
        mUnreachHeight= (int) ta.getDimension(R.styleable.customedProgressBar_progress_unreach_height,mUnreachHeight);
        mReachHeight= (int) ta.getDimension(R.styleable.customedProgressBar_progress_reach_height,mReachHeight);
        mTextOffset= (int) ta.getDimension(R.styleable.customedProgressBar_progress_text_offset,mTextOffset);
        mTextColor=ta.getColor(R.styleable.customedProgressBar_progress_text_color,mTextColor);
        mUnreachColor=ta.getColor(R.styleable.customedProgressBar_progress_unreach_color,mUnreachColor);
        mReachColor=ta.getColor(R.styleable.customedProgressBar_progress_reach_color,mReachColor);
        ta.recycle();

        mPaint.setTextSize(mTextSize);
    }

    public customedProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public customedProgressBar(Context context) {
        this(context,null);
    }



    protected int dp2px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }

    protected int sp2dp(int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }
}
