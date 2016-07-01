package progressbar.example.aric.customprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by aric on 16/6/30.
 */
public class RoundProgress extends customedProgressBar {

    private int mRadius=dp2px(30);
    private boolean mModel=true;// true 和 false 分表代表两种画图的方式.
    private  int mMaxPaintWidth;//用于计算宽度,代表的是圆环的最大宽度,最大宽度加上半  径就是整个view的宽度


    public RoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mReachHeight=(int)(mUnreachHeight*2.5f);

        TypedArray ta = getContext().obtainStyledAttributes(attrs,R.styleable.RoundProgress);
        mRadius= (int) ta.getDimension(R.styleable.RoundProgress_radius,mRadius);
        mModel=(boolean)ta.getBoolean(R.styleable.RoundProgress_model,mModel);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);


    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String text = getProgress()+"%";
        float textWidth=  mPaint.measureText(text);
        float textHeight= (mPaint.descent()+ mPaint.ascent())/2;

        canvas.save();

        canvas.translate(getPaddingLeft()+mMaxPaintWidth/2,getPaddingRight()+mMaxPaintWidth/2);
        mPaint.setStyle(Paint.Style.STROKE);
        //draw Unreach 部分
        mPaint.setColor(mUnreachColor);
        mPaint.setStrokeWidth(mUnreachHeight);
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);
        float sweepAngle = getProgress()*1.0f/getMax()*360;


        //draw reach 部分:使用两种模式去绘画这部分
       RectF rectF = new RectF(0,0,mRadius*2,mRadius*2);
        mPaint.setStrokeWidth(mReachHeight);
        mPaint.setColor(mReachColor);
        if(mModel==true){
            canvas.drawArc(rectF,0,sweepAngle,false,mPaint);
        }else{

            if (sweepAngle>180){
                canvas.drawArc(rectF,0,90,false,mPaint );
                canvas.drawArc(rectF,0,360-(sweepAngle-180)/2,false,mPaint );

            }else{
                canvas.drawArc(rectF,90-sweepAngle/2,90,false,mPaint);
            }
            canvas.drawArc(rectF,90,sweepAngle/2,false,mPaint);

        }


        //draw 文字部分

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mTextSize);
        canvas.drawText(text,mRadius-textWidth/2,mRadius-textHeight,mPaint);




        canvas.restore();



    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMaxPaintWidth= Math.max(mReachHeight,mUnreachHeight);

        int expect = mRadius*2 + mMaxPaintWidth+getPaddingLeft()+getPaddingRight();
        //默认四个padding 的值是一样的
        int width = resolveSize(expect,widthMeasureSpec);
        int height = resolveSize(expect,heightMeasureSpec);
        int realWidth = Math.min(width,height);
        //首先我们得给radius的值,但是给的这个值只是参考,需要进行测量,测量之后根据实际的情况在进行重新赋值

        mRadius= (realWidth-getPaddingLeft()-getPaddingRight()-mMaxPaintWidth)/2;
        setMeasuredDimension(width,height);
    }

    public RoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundProgress(Context context) {
        this(context,null);
    }
}
