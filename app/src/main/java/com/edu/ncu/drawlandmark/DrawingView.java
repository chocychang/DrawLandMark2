package com.edu.ncu.drawlandmark;

import android.view.View;
import android.view.MotionEvent;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import java.util.ArrayList;

public class DrawingView extends View {
    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    private float brushSize, lastBrushSize;  //第一個變量作為畫筆大小，第二個變量用於追蹤用戶最後一個畫筆大小
    private boolean erase=false; //設置橡皮擦功能

    // strokes drawn so far (the last MAX strokes)
    private ArrayList<Stroke> undoStrokes = new ArrayList<>();

    // strokes that were undone.
    private ArrayList<Stroke> undoneStrokes = new ArrayList<>();
    static final int MAX = 50;
    private Stroke currentStroke;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }
    private void setupDrawing(){
//get drawing area setup for interaction
        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();     //實例化繪圖Path和Paint對象
        drawPaint.setColor(paintColor);  //設置初始顏色
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);  //在實體化後可用此行直接設置筆刷寬度
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);    //設置初始路徑屬性

        canvasPaint = new Paint(Paint.DITHER_FLAG);  //setupDrawing通過實例​​化畫布Paint對象
        currentStroke = new Stroke();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);   //使用width和height值實例化繪圖畫布和位圖
//view given size 在DrawingView類中，重寫onSizeChanged方法，當為自定義視圖分配大小時將調用該方法
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);  //繪製畫布和繪圖路徑
        invalidate();
//draw view 允許Class作為自定義繪圖View運行
    }

    // 點擊undo時觸發
    protected boolean onClickUndo() {
        boolean ret = false;

        // remove from undoStrokes list, add it to undoneStrokes list
        if (undoStrokes.size() > 0) {
            Stroke theStroke = undoStrokes.remove(undoStrokes.size()-1);
            drawPaint = new Paint();
            undoneStrokes.add(theStroke);

            // clear the canvas
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

            // draw the strokes that we wanted.
            for (int i = 0; i < undoStrokes.size() ; i++) {
                undoStrokes.get(i).paint.setXfermode(null);
                undoStrokes.get(i).paint.setColor(undoStrokes.get(i).color);
                undoStrokes.get(i).paint.setStrokeWidth(undoStrokes.get(i).brushSize);
                drawCanvas.drawPath(undoStrokes.get(i).path, undoStrokes.get(i).paint);
                invalidate();
            }

            ret = true;
        }
        return ret;
    }

    protected boolean onClickRedo() {
        Boolean ret = false;
        if (undoneStrokes.size() > 0) {
            Stroke theStroke = undoneStrokes.remove(undoneStrokes.size()-1);
            undoStrokes.add(theStroke);

            theStroke.paint.setColor(theStroke.color);
            theStroke.paint.setStrokeWidth(theStroke.brushSize);
            // draw it!
            drawCanvas.drawPath(theStroke.path, theStroke.paint);
            invalidate();
            ret = true;
        }
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch MotionEvent參數onTouchEvent將響應特定的觸摸事件
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                invalidate();

                // keep track of the last MAX strokes if we're not erasing.
                if (erase == false) {
                    currentStroke.setPaint(drawPaint);
                    currentStroke.setColor(paintColor);
                    currentStroke.setPath(drawPath);
                    currentStroke.setBrushSize(brushSize);

                    if (undoStrokes.size() < MAX) {
                        undoStrokes.add(currentStroke);
                    } else {
                        // shift the first stroke off the undoList. We only keep track of MAX strokes.
                        ArrayList<Stroke> tmps = new ArrayList<>(undoStrokes.subList(1, MAX));
                        undoStrokes = tmps;
                        undoStrokes.add(currentStroke);
                    }
                }
                drawPath = new Path();
                drawPath.reset();

                break;
            default:
                return false;  //觸摸View時，我們移動到該位置以開始繪製。當他們在視圖上移動手指時，我們繪製路徑及其觸摸。當他們從View上抬起手指時，我們繪製路徑並重置它以進行下一個繪圖操作。
        }
        invalidate();
        // invalidate將導致該onDraw方法執行  通過使View無效並返回true值來完成該方法
        return true;
    }

    public void setBrushSize(float newSize){
//update size 設置筆刷大小
        brushSize= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        drawPaint.setStrokeWidth(brushSize);  //使用傳遞值更新筆刷大小
    }
    //獲取和設置我們創建的其他大小變量:
    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setColor(String newColor){
//set color
        invalidate();  //首先使視圖無效
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setErase(boolean isErase){
//set erase true or false  假設用戶最初正在繪畫
        erase=isErase; //更新flag變量
        if(erase) {//drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); 原本為切換回橡皮擦模式但會有顏色BUG
            drawPaint.setColor(Color.WHITE);}
        else{drawPaint.setXfermode(null);  //更改Paint對像為擦除或切換回繪圖
            drawPaint.setColor(paintColor);}  //回到原本畫筆的顏色
    }

    public void startNew(String path){
//清除畫布並更新顯示
        undoneStrokes = new ArrayList<Stroke>();
        undoneStrokes.clear();

        undoStrokes = new ArrayList<Stroke>();
        undoStrokes.clear();

        if (path == null) {
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        } else {
            Bitmap workingBitmap = 	BitmapFactory.decodeFile(path);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
            drawCanvas.drawBitmap(mutableBitmap, 0, 0, canvasPaint);
            //setImageBitmap(mutableBitmap);

        }
        invalidate();
        setErase(false);
    }
}