package com.tckkj.medicinebox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.tckkj.medicinebox.R;
import com.tckkj.medicinebox.util.ConvertUtils;

import java.util.List;

public class CustomBarChart extends View {
    private Context context;

    // 坐标单位
    private String[] xLabel;
    private String[] yLabel;
    // 曲线数据
    private List<int[]> dataList;
    private List<Integer> colorList;
    // 默认边距
    private int margin;
    // 距离左边偏移量
    private int marginX;
    // 原点坐标
    private int xPoint;
    private int yPoint;
    // X,Y轴的单位长度
    private int xScale;
    private int yScale;
    //刻度标注字体大小
    private int scaleTextSize;
    // 画笔
    private Paint paintAxes;
    private Paint paintXCoordinate;
    private Paint paintYCoordinate;
    private Paint paintRectF;
    private Paint paintValue;
    private Paint paintYScale;  //Y轴刻度
    private float scale;

    public CustomBarChart(Context context, String[] xLabel, String[] yLabel,
                          List<int[]> dataList, List<Integer> colorList) {
        super(context);
        this.context = context;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.dataList = dataList;
        this.colorList = colorList;
        margin = ConvertUtils.dp2px(context,27);
        marginX = ConvertUtils.dp2px(context,10);
        scaleTextSize = ConvertUtils.sp2px(context, 10);
    }

    public CustomBarChart(Context context) {
        super(context);
    }

    public void setScaleTextSize(int scaleTextSize) {
        this.scaleTextSize = scaleTextSize;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * 初始化数据值和画笔
     */
    public void init() {
        xPoint = margin + marginX;
        yPoint = this.getHeight() - margin;
        xScale = (this.getWidth() - 2 * margin - marginX) / (xLabel.length - 1);
        yScale = (this.getHeight() - 2 * margin) / (yLabel.length - 1);

        paintAxes = new Paint();
        paintAxes.setStyle(Paint.Style.STROKE);
        paintAxes.setAntiAlias(true);
        paintAxes.setDither(true);
        paintAxes.setColor(ContextCompat.getColor(getContext(), R.color.barDiagramLine));
        paintAxes.setStrokeWidth(ConvertUtils.dp2px(context, 1));        //X、Y轴宽度

        paintXCoordinate = new Paint();
        paintXCoordinate.setStyle(Paint.Style.STROKE);
        paintXCoordinate.setDither(true);
        paintXCoordinate.setAntiAlias(true);
        paintXCoordinate.setColor(ContextCompat.getColor(getContext(), R.color.normalText));
        paintXCoordinate.setTextSize(ConvertUtils.sp2px(context, 10));

        paintYCoordinate = new Paint();
        paintYCoordinate.setStyle(Paint.Style.STROKE);
        paintYCoordinate.setDither(true);
        paintYCoordinate.setAntiAlias(true);
        paintYCoordinate.setColor(ContextCompat.getColor(getContext(), R.color.hintColor));
        paintYCoordinate.setTextSize(scaleTextSize);

        paintYScale = new Paint();
        paintYScale.setStyle(Paint.Style.STROKE);
        paintYScale.setAntiAlias(true);
        paintYScale.setDither(true);
        paintYScale.setColor(ContextCompat.getColor(getContext(), R.color.alphaBarDiagramLine));
        paintYScale.setStrokeWidth(ConvertUtils.dp2px(context, 1));

        paintRectF = new Paint();
        paintRectF.setStyle(Paint.Style.FILL);
        paintRectF.setDither(true);
        paintRectF.setAntiAlias(true);
        paintRectF.setStrokeWidth(ConvertUtils.dp2px(context, 10));

        paintValue = new Paint();
        paintValue.setStyle(Paint.Style.STROKE);
        paintValue.setAntiAlias(true);
        paintValue.setDither(true);
        paintValue.setTextAlign(Paint.Align.CENTER);
        paintValue.setTextSize(ConvertUtils.sp2px(context, 10));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(ContextCompat.getColor(getContext(), R.color.white));  //柱状图背景色
        init();
        drawAxesLine(canvas, paintAxes);
        drawCoordinate(canvas, paintXCoordinate, paintYCoordinate, paintYScale);
        if (dataList.size() == 1) {
            drawBar(canvas, paintRectF, dataList.get(0), colorList);
            drawValue(canvas, paintValue, dataList.get(0), colorList.get(2));
        } else if (dataList.size() == 2) {
            drawBars(canvas, paintRectF, dataList, colorList);
            drawValues(canvas, paintValue, dataList, colorList.get(2));
        }
    }

    /**
     * 绘制坐标轴
     */
    private void drawAxesLine(Canvas canvas, Paint paint) {
        // X
        canvas.drawLine(xPoint, yPoint, this.getWidth() - margin / 6, yPoint, paint);
//        canvas.drawLine(this.getWidth() - margin / 6, yPoint, this.getWidth() - margin / 2, yPoint - margin / 3, paint);
//        canvas.drawLine(this.getWidth() - margin / 6, yPoint, this.getWidth() - margin / 2, yPoint + margin / 3, paint);

        // Y
        canvas.drawLine(xPoint, yPoint, xPoint, margin / 6, paint);
//        canvas.drawLine(xPoint, margin / 6, xPoint - margin / 3, margin / 2, paint);
//        canvas.drawLine(xPoint, margin / 6, xPoint + margin / 3, margin / 2, paint);
    }

    /**
     * 绘制刻度
     */
    private void drawCoordinate(Canvas canvas, Paint xPaint, Paint yPaint, Paint paintYScale) {
        // X轴坐标
        for (int i = 0; i <= (xLabel.length - 1); i++) {
            xPaint.setTextAlign(Paint.Align.CENTER);
            int startX = xPoint + i * xScale;
            canvas.drawText(xLabel[i], startX, this.getHeight() - margin / 2, xPaint);
        }

        // Y轴坐标
        for (int i = 0; i <= (yLabel.length - 1); i++) {
            xPaint.setTextAlign(Paint.Align.LEFT);
            int startY = yPoint - i * yScale;
            int offsetX;
            switch (yLabel[i].length()) {
                case 1:
                    offsetX = 28;
                    break;

                case 2:
                    offsetX = 20;
                    break;

                case 3:
                    offsetX = 12;
                    break;

                case 4:
                    offsetX = 5;
                    break;

                default:
                    offsetX = 0;
                    break;
            }
            int offsetY;
            if (i == 0) {
                offsetY = 0;
            } else {
                offsetY = margin / 5;
            }
            canvas.drawText(yLabel[i], margin / 4 + ConvertUtils.sp2px(context, offsetX/3), startY + offsetY, yPaint);

            canvas.drawLine(xPoint, startY, this.getWidth() - margin / 6, startY, paintYScale);
        }
    }

    /**
     * 绘制单柱形
     */
    private void drawBar(Canvas canvas, Paint paint, int data[], List<Integer> colorList) {
        for (int i = 1; i <= (xLabel.length - 1); i++) {
            int startX = xPoint + i * xScale;
            RectF rect = new RectF(startX - ConvertUtils.dp2px(context, 16), toY(data[i - 1]), startX + ConvertUtils.dp2px(context, 16), this.getHeight() - margin - 2);
            Log.i("111111", "drawBar: " + ConvertUtils.dp2px(context, 16));
            if (i % 2 == 1) {
                paint.setColor(ContextCompat.getColor(getContext(), colorList.get(1)));
            } else {
                paint.setColor(ContextCompat.getColor(getContext(), colorList.get(1)));
            }
            canvas.drawRect(rect, paint);
        }
    }

    /**
     * 绘制双柱形
     */
    private void drawBars(Canvas canvas, Paint paint, List<int[]> dataList, List<Integer> colorList) {
        for (int i = 1; i <= (xLabel.length - 1); i++) {
            int startX = xPoint + i * xScale;
            paint.setColor(ContextCompat.getColor(getContext(), colorList.get(0)));
            RectF rect1 = new RectF(startX - 20, toY(dataList.get(0)[i - 1]), startX - 10,
                    this.getHeight() - margin - 2);
            canvas.drawRect(rect1, paint);

            paint.setColor(ContextCompat.getColor(getContext(), colorList.get(1)));
            RectF rect2 = new RectF(startX - 5, toY(dataList.get(1)[i - 1]), startX + 5,
                    this.getHeight() - margin - 2);
            canvas.drawRect(rect2, paint);
        }
    }

    /**
     * 绘制单数值
     */
    private void drawValue(Canvas canvas, Paint paint, int data[], int color) {
        paint.setColor(ContextCompat.getColor(getContext(), color));
        for (int i = 1; i <= (xLabel.length - 1); i++) {
            canvas.drawText(data[i - 1] + "", xPoint + i * xScale, toY(data[i - 1]) - 5, paintValue);
        }
    }

    /**
     * 绘制双数值
     */
    private void drawValues(Canvas canvas, Paint paint, List<int[]> dataList, int color) {
        paint.setColor(ContextCompat.getColor(getContext(), color));
        for (int i = 1; i <= (xLabel.length - 1); i++) {
            int startX = xPoint + i * xScale;
            int offsetY1 = 5;
            int offsetY2 = 5;
            if (dataList.get(0)[i - 1] == dataList.get(1)[i - 1]) {
                offsetY2 += 10;
            }
            if (i > 1) {
                if ((dataList.get(1)[i - 2] == dataList.get(0)[i - 1])) {
                    offsetY1 += 10;
                }
            }
            canvas.drawText(dataList.get(0)[i - 1] + "", startX - 18,  //后缀
                    toY(dataList.get(0)[i - 1]) - offsetY1, paintValue);
            canvas.drawText(dataList.get(1)[i - 1] + "", startX + 3,
                    toY(dataList.get(1)[i - 1]) - offsetY2, paintValue);
        }
    }

    /**
     * 数据按比例转坐标
     */
    private float toY(int num) {
        float y;
        try {
            float a = (float) num / scale;
            y = yPoint - a * yScale;
        } catch (Exception e) {
            return 0;
        }
        return y;
    }

}
