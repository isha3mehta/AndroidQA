package com.ibm.hondaconnect.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ibm.hondaconnect.R;

public final class MeterGauge extends View {

	private static final String TAG = MeterGauge.class.getSimpleName();

	// drawing tools
	private RectF rimRect;
	private Paint rimPaint;
	private Paint rimCirclePaint;

	private RectF faceRect;
	private Bitmap faceTexture;
	private Paint facePaint;
	private Paint rimShadowPaint;

	private Paint scalePaint;
	private RectF scaleRect;

	private RectF valueRect;
	private RectF rangeRect;

	private Paint rangeRedPaint;
	private Paint rangeOrangePaint;
	private Paint rangeYellowPaint;
	private Paint rangeParrotPaint;
	private Paint rangeGreenPaint;
	private Paint rangeAllPaint;

	private Paint valueRedPaint;
	private Paint valueOrangePaint;
	private Paint valueYellowPaint;
	private Paint valueParrotPaint;
	private Paint valueGreenPaint;
	private Paint valueAllPaint;

	private Paint unitPaint;
	private Path  unitPath;
	private RectF unitRect;

	private Paint lowerTitlePaint;
	private Paint upperTitlePaint;
	private Path  lowerTitlePath;
	private Path  upperTitlePath;
	private RectF titleRect;

	private Paint handPaint;
	private Path handPath;

	private Paint handScrewPaint;

	private Paint backgroundPaint;
	// end drawing tools

	private Bitmap background; // holds the cached static part

	// scale configuration
	// Values passed as property. Defaults are set here.
	private boolean showHand                 = true;
	private boolean showGauge                = true;
	private boolean showRange                = false;

	private int totalNotches                 = 120; // Total number of notches on the scale.
	private int incrementPerLargeNotch       = 10;
	private int incrementPerSmallNotch       = 2;

	private int scaleColor                   = 0x9f004d0f;
	private int scaleCenterValue             = -90; // the one in the top center (12 o'clock), this corresponds with -90 degrees
	private int scaleMinValue                = 0;
	private int scaleMaxValue                = 90;
	private float degreeMinValue             = 0;
	private float degreeMaxValue             = 0;

	private int rangeRedColor                 = 0x9f00ff00;
	private int rangeRedMinValue              = scaleMinValue;
	private int rangeRedMaxValue              = -50;
	private float degreeRedMinValue           = 0;
	private float degreeRedMaxValue           = 0;

	private int rangeOrangeColor            = 0x9fFF8500;
	private int rangeOrangeMinValue         = rangeRedMaxValue;
	private int rangeOrangeMaxValue         = -10;
	private float degreeOrangeMinValue      = 0;
	private float degreeOrangeMaxValue      = 0;

	private int rangeYellowColor              = 0x9fff0000;
	private int rangeYellowMinValue           = rangeOrangeMaxValue;
	private int rangeYellowMaxValue           = 30;
	private float degreeYellowMinValue        = 0;
	private float degreeYellowMaxValue        = 0;

	private int rangeParrotColor            = 0x9fff0000;
	private int rangeParrotMinValue         = rangeYellowMinValue;
	private int rangeParrotMaxValue         = 70;
	private float degreeParrotMinValue      = 0;
	private float degreeParrotMaxValue      = 0;

	private int rangeGreenColor              = 0x9f17AF4B;
	private int rangeGreenMinValue           = rangeParrotMinValue;
	private int rangeGreenMaxValue           = 110;
	private float degreeGreenMinValue        = 0;
	private float degreeGreenMaxValue        = 0;

	private String lowerTitle                = "www.ats-global.com";
	private String upperTitle                = "Visit http://atstechlab.wordpress.com";
	private String unitTitle                 = "\u2103";

	// Fixed values.
	private static final float scalePosition = 0.10f;  // The distance from the rim to the scale
	private static final float valuePosition = 0.285f; // The distance from the rim to the ranges
	private static final float rangePosition = 0.122f; // The distance from the rim to the ranges
	private static final float titlePosition = 0.145f; // The Distance from the rim to the titles
	private static final float unitPosition  = 0.300f; // The distance from the rim to the unit
	private static final float rimSize       = 0.02f;

	private final float degreesPerNotch      = 360.0f/totalNotches;
	private static final int centerDegrees   =  -90; // the one in the top center (12 o'clock), this corresponds with -90 degrees

	// hand dynamics
	private boolean dialInitialized         = true;
	private float currentValue              = scaleCenterValue;
	private float targetValue               = scaleCenterValue;
	private float dialVelocity              = 0.0f;
	private float dialAcceleration          = 0.0f;
	private long lastDialMoveTime           = -1L;


	public MeterGauge(Context context) {
		super(context);
		init(context, null);
	}

	public MeterGauge(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public MeterGauge(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle bundle = (Bundle) state;
		Parcelable superState = bundle.getParcelable("superState");
		super.onRestoreInstanceState(superState);
		
		dialInitialized  = bundle.getBoolean("dialInitialized");
		currentValue     = bundle.getFloat("currentValue");
		targetValue       = bundle.getFloat("targetValue");
		dialVelocity     = bundle.getFloat("dialVelocity");
		dialAcceleration = bundle.getFloat("dialAcceleration");
		lastDialMoveTime = bundle.getLong("lastDialMoveTime");
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		
		Bundle state = new Bundle();
		state.putParcelable("superState", superState);
		state.putBoolean("dialInitialized", dialInitialized);
		state.putFloat("currentValue", currentValue);
		state.putFloat("targetValue", targetValue);
		state.putFloat("dialVelocity", dialVelocity);
		state.putFloat("dialAcceleration", dialAcceleration);
		state.putLong("lastDialMoveTime", lastDialMoveTime);
		return state;
	}

	private void init(Context context, AttributeSet attrs) {
		// Get the properties from the resource file.
		if (context != null && attrs != null){
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MeterGauge);
			showRange              = a.getBoolean(R.styleable.MeterGauge_showRange,          showRange);
			showGauge              = a.getBoolean(R.styleable.MeterGauge_showGauge,          showGauge);
			showHand               = a.getBoolean(R.styleable.MeterGauge_showHand,           showHand);

			totalNotches           = a.getInt(R.styleable.MeterGauge_totalNotches,           totalNotches);
			incrementPerLargeNotch = a.getInt(R.styleable.MeterGauge_incrementPerLargeNotch, incrementPerLargeNotch);
			incrementPerSmallNotch = a.getInt(R.styleable.MeterGauge_incrementPerSmallNotch, incrementPerSmallNotch);
			scaleCenterValue       = a.getInt(R.styleable.MeterGauge_scaleCenterValue,       scaleCenterValue);
			scaleColor             = a.getInt(R.styleable.MeterGauge_scaleColor,             scaleColor);
			scaleMinValue          = a.getInt(R.styleable.MeterGauge_scaleMinValue,          scaleMinValue);
			scaleMaxValue          = a.getInt(R.styleable.MeterGauge_scaleMaxValue,          scaleMaxValue);
			rangeRedColor           = a.getInt(R.styleable.MeterGauge_rangeRedColor,           rangeRedColor);
			rangeRedMinValue        = a.getInt(R.styleable.MeterGauge_rangeRedMinValue,        rangeRedMinValue);
			rangeRedMaxValue        = a.getInt(R.styleable.MeterGauge_rangeRedMaxValue,        rangeRedMaxValue);
			rangeOrangeColor      = a.getInt(R.styleable.MeterGauge_rangeOrangeColor,      rangeOrangeColor);
			rangeOrangeMinValue   = a.getInt(R.styleable.MeterGauge_rangeOrangeMinValue,   rangeOrangeMinValue);
			rangeOrangeMaxValue   = a.getInt(R.styleable.MeterGauge_rangeOrangeMaxValue,   rangeOrangeMaxValue);
			rangeYellowColor        = a.getInt(R.styleable.MeterGauge_rangeYellowColor,        rangeYellowColor);
			rangeYellowMinValue     = a.getInt(R.styleable.MeterGauge_rangeYellowMinValue,     rangeYellowMinValue);
			rangeYellowMaxValue     = a.getInt(R.styleable.MeterGauge_rangeYellowMaxValue,     rangeYellowMaxValue);
			rangeParrotColor        = a.getInt(R.styleable.MeterGauge_rangeParrotColor,        rangeParrotColor);
			rangeParrotMinValue     = a.getInt(R.styleable.MeterGauge_rangeParrotMinValue,     rangeParrotMinValue);
			rangeParrotMaxValue     = a.getInt(R.styleable.MeterGauge_rangeParrotMaxValue,     rangeParrotMaxValue);
			rangeGreenColor        = a.getInt(R.styleable.MeterGauge_rangeGreenColor,        rangeGreenColor);
			rangeGreenMinValue     = a.getInt(R.styleable.MeterGauge_rangeGreenMinValue,     rangeGreenMinValue);
			rangeGreenMaxValue     = a.getInt(R.styleable.MeterGauge_rangeGreenMaxValue,     rangeGreenMaxValue);
			String unitTitle       = a.getString(R.styleable.MeterGauge_unitTitle);
			String lowerTitle      = a.getString(R.styleable.MeterGauge_lowerTitle);
			String upperTitle      = a.getString(R.styleable.MeterGauge_upperTitle);
			if (unitTitle != null) this.unitTitle = unitTitle;
			if (lowerTitle != null) this.lowerTitle = lowerTitle;
			if (upperTitle != null) this.upperTitle = upperTitle;
		}
		degreeMinValue        = valueToAngle(scaleMinValue)        + centerDegrees;
		degreeMaxValue        = valueToAngle(scaleMaxValue)        + centerDegrees;
		degreeRedMinValue      = valueToAngle(rangeRedMinValue)      + centerDegrees;
		degreeRedMaxValue      = valueToAngle(rangeRedMaxValue)      + centerDegrees;
		degreeOrangeMinValue = valueToAngle(rangeOrangeMinValue) + centerDegrees;
		degreeOrangeMaxValue = valueToAngle(rangeOrangeMaxValue) + centerDegrees;
		degreeYellowMinValue   = valueToAngle(rangeYellowMinValue)   + centerDegrees;
		degreeYellowMaxValue   = valueToAngle(rangeYellowMaxValue)   + centerDegrees;
		degreeParrotMinValue   = valueToAngle(rangeParrotMinValue) + centerDegrees;
		degreeParrotMaxValue   = valueToAngle(rangeParrotMaxValue) + centerDegrees;
		degreeGreenMinValue   = valueToAngle(rangeGreenMinValue) + centerDegrees;
		degreeGreenMaxValue   = valueToAngle(rangeGreenMaxValue) + centerDegrees;
		
		initDrawingTools();
	}

	private void initDrawingTools() {

		valueRect = new RectF();
		valueRect.set(0.2f, 0.2f, 0.8f, 0.8f);


		scalePaint = new Paint();
		scalePaint.setStyle(Paint.Style.STROKE);
		scalePaint.setColor(rangeRedColor);
		scalePaint.setStrokeWidth(0.005f);
		scalePaint.setAntiAlias(true);
		scalePaint.setTextSize(0.045f);
		scalePaint.setTypeface(Typeface.SANS_SERIF);
		scalePaint.setTextScaleX(0.8f);
		scalePaint.setTextAlign(Paint.Align.CENTER);		
		
		rangeRedPaint = new Paint();
		rangeRedPaint.setStyle(Paint.Style.STROKE);
		rangeRedPaint.setColor(rangeRedColor);
		rangeRedPaint.setStrokeWidth(0.012f);
		rangeRedPaint.setAntiAlias(true);

		rangeOrangePaint = new Paint();
		rangeOrangePaint.setStyle(Paint.Style.STROKE);
		rangeOrangePaint.setColor(rangeOrangeColor);
		rangeOrangePaint.setStrokeWidth(0.012f);
		rangeOrangePaint.setAntiAlias(true);

		rangeYellowPaint = new Paint();
		rangeYellowPaint.setStyle(Paint.Style.STROKE);
		rangeYellowPaint.setColor(rangeYellowColor);
		rangeYellowPaint.setStrokeWidth(0.012f);
		rangeYellowPaint.setAntiAlias(true);

		rangeParrotPaint = new Paint();
		rangeParrotPaint.setStyle(Paint.Style.STROKE);
		rangeParrotPaint.setColor(rangeParrotColor);
		rangeParrotPaint.setStrokeWidth(0.012f);
		rangeParrotPaint.setAntiAlias(true);

		rangeGreenPaint = new Paint();
		rangeGreenPaint.setStyle(Paint.Style.STROKE);
		rangeGreenPaint.setColor(rangeGreenColor);
		rangeGreenPaint.setStrokeWidth(0.012f);
		rangeGreenPaint.setAntiAlias(true);

		rangeAllPaint = new Paint();
		rangeAllPaint.setStyle(Paint.Style.STROKE);
		rangeAllPaint.setColor(0xcfffffff);
		rangeAllPaint.setStrokeWidth(0.012f);
		rangeAllPaint.setAntiAlias(true);
		rangeAllPaint.setShadowLayer(0.005f, -0.002f, -0.002f, 0x7f000000);

		valueRedPaint = new Paint();
		valueRedPaint.setStyle(Paint.Style.STROKE);
		valueRedPaint.setColor(rangeRedColor);
		valueRedPaint.setStrokeWidth(0.30f);
		valueRedPaint.setAntiAlias(true);

		valueOrangePaint = new Paint();
		valueOrangePaint.setStyle(Paint.Style.STROKE);
		valueOrangePaint.setColor(rangeOrangeColor);
		valueOrangePaint.setStrokeWidth(0.30f);
		valueOrangePaint.setAntiAlias(true);

		valueYellowPaint = new Paint();
		valueYellowPaint.setStyle(Paint.Style.STROKE);
		valueYellowPaint.setColor(rangeYellowColor);
		valueYellowPaint.setStrokeWidth(0.30f);
		valueYellowPaint.setAntiAlias(true);

		valueParrotPaint = new Paint();
		valueParrotPaint.setStyle(Paint.Style.STROKE);
		valueParrotPaint.setColor(rangeParrotColor);
		valueParrotPaint.setStrokeWidth(0.30f);
		valueParrotPaint.setAntiAlias(true);

		valueGreenPaint = new Paint();
		valueGreenPaint.setStyle(Paint.Style.STROKE);
		valueGreenPaint.setColor(rangeGreenColor);
		valueGreenPaint.setStrokeWidth(0.30f);
		valueGreenPaint.setAntiAlias(true);

		valueAllPaint = new Paint();
		valueAllPaint.setStyle(Paint.Style.STROKE);
		valueAllPaint.setColor(0xcfffffff);
		valueAllPaint.setStrokeWidth(0.30f);
		valueAllPaint.setAntiAlias(true);
		valueAllPaint.setShadowLayer(0.005f, -0.002f, -0.002f, 0x7f000000);


		handPaint = new Paint();
		handPaint.setAntiAlias(true);
		handPaint.setColor(0xff392f2c);
		handPaint.setShadowLayer(0.01f, -0.005f, -0.005f, 0x7f000000);
		handPaint.setStyle(Paint.Style.FILL);	

		handScrewPaint = new Paint();
		handScrewPaint.setAntiAlias(true);
		//handScrewPaint.setColor(0xff493f3c);
		handScrewPaint.setColor(0xffff3f3c);
		handScrewPaint.setStyle(Paint.Style.FILL);
		
		backgroundPaint = new Paint();

		backgroundPaint.setColor(0xffffffff);

		backgroundPaint.setShadowLayer(0.01f, -0.005f, -0.005f, 0xffffffff);
		backgroundPaint.setFilterBitmap(true);

		// The hand is drawn with the tip facing up. That means when the image is not rotated, the tip 
		// faces north. When the the image is rotated -90 degrees, the tip is facing west and so on.
		handPath = new Path();                                              //   X      Y
		handPath.moveTo(0.5f, 0.5f);                                 // 0.500, 0.700
		handPath.lineTo(0.5f - 0.010f, 0.5f - 0.007f);               // 0.490, 0.630
		handPath.lineTo(0.5f - 0.002f, 0.5f - 0.40f);                       // 0.498, 0.100
		handPath.lineTo(0.5f + 0.002f, 0.5f - 0.40f);                       // 0.502, 0.100
		handPath.lineTo(0.5f + 0.010f, 0.5f - 0.007f);               // 0.510, 0.630
		handPath.lineTo(0.5f, 0.5f);                                 // 0.500, 0.700
		handPath.addCircle(0.5f, 0.5f, 0.025f, Path.Direction.CW);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
		Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);
		
		int chosenDimension = Math.min(chosenWidth, chosenHeight);

		setMeasuredDimension(chosenDimension, chosenDimension);
	}
	
	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else { // (mode == MeasureSpec.UNSPECIFIED)
			return getPreferredSize();
		} 
	}
	
	// in case there is no size specified
	private int getPreferredSize() {
		return 250;
	}

	private void drawBackground(Canvas canvas) {
		if (background == null) {
			Log.w(TAG, "Background not created");
		} else {
			canvas.drawBitmap(background, 0, 0, backgroundPaint);
		}
	}

	private void drawScaleRanges(Canvas canvas) {
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.drawArc(rangeRect, degreeMinValue,        degreeMaxValue -        degreeMinValue,        false, rangeAllPaint);
		canvas.drawArc(rangeRect, degreeRedMinValue,      degreeRedMaxValue      - degreeRedMinValue,      false, rangeRedPaint);
		canvas.drawArc(rangeRect, degreeOrangeMinValue, degreeOrangeMaxValue - degreeOrangeMinValue, false, rangeOrangePaint);
		canvas.drawArc(rangeRect, degreeYellowMinValue,   degreeYellowMaxValue   - degreeYellowMinValue,   false, rangeYellowPaint);
		canvas.drawArc(rangeRect, degreeParrotMinValue,   degreeParrotMaxValue   - degreeParrotMinValue,   false, rangeParrotPaint);
		canvas.drawArc(rangeRect, degreeGreenMinValue,   degreeGreenMaxValue   - degreeGreenMinValue,   false, rangeGreenPaint);
		canvas.restore();		
	}

	private void drawHand(Canvas canvas) {
		if (dialInitialized) {
			float angle = valueToAngle(currentValue);
			canvas.save(Canvas.MATRIX_SAVE_FLAG);
			canvas.rotate(angle, 0.5f, 0.5f);
			canvas.drawPath(handPath, handPaint);
			canvas.restore();
			
			canvas.drawCircle(0.5f, 0.5f, 0.01f, handScrewPaint);
		}
	}
	
	private void drawGauge(Canvas canvas) {
		if (dialInitialized) {
			// When currentValue is not rotated, the tip of the hand points
			// to n -90 degrees.
		//	float angle = valueToAngle(currentValue) - 90;
	
			/*if(targetValue <= rangeOkMaxValue){
				canvas.drawArc(valueRect, degreeMinValue, degreeMaxValue, false, valueOkPaint);
			}
			if((targetValue > rangeOkMaxValue) && (targetValue <= rangeOrangeMaxValue)){
				canvas.drawArc(valueRect, degreeMinValue, degreeOkMaxValue, false, valueOkPaint);
				canvas.drawArc(valueRect, degreeOrangeMinValue, degreeOrangeMaxValue, false, valueOrangePaint);
			}
			if((targetValue > rangeOrangeMaxValue) && (targetValue <= rangeYellowMaxValue)){
				canvas.drawArc(valueRect, degreeMinValue, degreeOkMaxValue , false, valueOkPaint);
				canvas.drawArc(valueRect, degreeOrangeMinValue, degreeOrangeMaxValue, false, valueOrangePaint);
				canvas.drawArc(valueRect, degreeYellowMinValue, degreeYellowMaxValue, false, valueYellowPaint);
			}

			if((targetValue > rangeYellowMaxValue) && (targetValue <= rangeParrotMaxValue)){
				canvas.drawArc(valueRect, degreeMinValue, degreeOkMaxValue, false, valueOkPaint);
				canvas.drawArc(valueRect, degreeOrangeMinValue, degreeOrangeMaxValue, false, valueOrangePaint);
				canvas.drawArc(valueRect, degreeYellowMinValue, degreeYellowMaxValue, false, valueYellowPaint);
				canvas.drawArc(valueRect, degreeParrotMinValue, degreeParrotMaxValue, false, valueParrotPaint);
			}*/
				canvas.drawArc(valueRect, degreeMinValue, degreeRedMaxValue - degreeMinValue, false, valueRedPaint);
				canvas.drawArc(valueRect, degreeOrangeMinValue, degreeOrangeMaxValue - degreeOrangeMinValue, false, valueOrangePaint);
				canvas.drawArc(valueRect, degreeYellowMinValue, degreeYellowMaxValue - degreeYellowMinValue, false, valueYellowPaint);
				canvas.drawArc(valueRect, degreeParrotMinValue, degreeParrotMaxValue - degreeParrotMinValue, false, valueParrotPaint);
				canvas.drawArc(valueRect, degreeGreenMinValue, degreeGreenMaxValue - degreeGreenMinValue, false, valueGreenPaint);
		}
	}

	private void drawBezel(Canvas canvas) {
		// Draw the bevel in which the value is draw.
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.drawArc(valueRect, degreeMinValue, degreeMaxValue - degreeMinValue, false, valueAllPaint);
		canvas.restore();		
	}

	/* Translate a notch to a value for the scale.
	 * The notches are evenly spread across the scale, half of the notches on the left hand side
	 * and the other half on the right hand side.
	 * The raw value calculation uses a constant so that each notch represents a value n + 2.
	 */
	private int notchToValue(int notch) {
		int rawValue = ((notch < totalNotches / 2) ? notch : (notch - totalNotches)) * incrementPerSmallNotch;
		int shiftedValue = rawValue + scaleCenterValue;
		return shiftedValue;
	}
	
	private float valueToAngle(float value) {
		// scaleCenterValue represents an angle of -90 degrees.
		return (value - scaleCenterValue) / 2.0f * degreesPerNotch;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//drawBackground(canvas);

		float scale = (float) getWidth();		
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(scale, scale);
		// Draw the needle using the updated value
		if (showGauge){
			drawGauge(canvas);
		}

		// Draw the needle using the updated value
		if (showHand){
			drawHand(canvas);
		}

		canvas.restore();
	
		// Calculate a new current value.
		calculateCurrentValue();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d(TAG, "Size changed to " + w + "x" + h);
		regenerateBackground();
	}
	
	private void regenerateBackground() {
		// free the old bitmap
		if (background != null) {
			background.recycle();
		}
		
		background = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas backgroundCanvas = new Canvas(background);
		float scale = (float) getWidth();
		backgroundCanvas.scale(scale, scale);
		
		//drawRim(backgroundCanvas);
		//drawFace(backgroundCanvas);
		//drawScale(backgroundCanvas);
		if (showRange){
			drawScaleRanges(backgroundCanvas);
		}
		if (showGauge){
			drawBezel(backgroundCanvas);
		}
		//drawTitle(backgroundCanvas);
	}

	// Move the hand slowly to the new position.
	private void calculateCurrentValue() {
		if (!(Math.abs(currentValue - targetValue) > 0.01f)) {
			return;
		}
		
		if (lastDialMoveTime != -1L) {
			long currentTime = System.currentTimeMillis();
			float delta = (currentTime - lastDialMoveTime) / 1000.0f;

			float direction = Math.signum(dialVelocity);
			if (Math.abs(dialVelocity) < 90.0f) {
				dialAcceleration = 5.0f * (targetValue - currentValue);
			} else {
				dialAcceleration = 0.0f;
			}
			currentValue += dialVelocity * delta;
			dialVelocity += dialAcceleration * delta;
			if ((targetValue - currentValue) * direction < 0.01f * direction) {
				currentValue = targetValue;
				dialVelocity = 0.0f;
				dialAcceleration = 0.0f;
				lastDialMoveTime = -1L;
			} else {
				lastDialMoveTime = System.currentTimeMillis();				
			}
			invalidate();
		} else {
			lastDialMoveTime = System.currentTimeMillis();
			calculateCurrentValue();
		}
	}
	
	public void setValue(float value) {
		if      (value < scaleMinValue) value = scaleMinValue;
		else if (value > scaleMaxValue) value = scaleMaxValue;

		targetValue = value;
		dialInitialized = true;
		
		invalidate(); // forces onDraw() to be called.
	}

	public float getValue() {
		return targetValue;
	}

}
