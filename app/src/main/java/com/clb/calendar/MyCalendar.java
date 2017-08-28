package com.clb.calendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017.08.28.0028.
 */

public class MyCalendar extends LinearLayout {

    private ImageView pre;
    private ImageView next;
    private TextView txtData;
    private GridView calendar_grid;

    //设置单例
    private Calendar curDate = Calendar.getInstance();


    public MyCalendar(Context context) {
        this(context, null);
    }

    public MyCalendar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initControl(context);
    }

    private void initControl(Context context) {

        bindControl(context);
        bindControlEvent();
        renderCalendar();
    }

    /**
     * 绑定事件
     */
    private void bindControlEvent() {

        /**
         * 给上一个日历设置点击事件
         */
        pre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                curDate.add(Calendar.MONTH, -1);
                renderCalendar();
            }
        });

        /**
         * 给下一月设置点击事件
         */
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                curDate.add(Calendar.MONTH, 1);
                renderCalendar();

            }
        });
    }

    /**
     * 渲染界面
     */
    private void renderCalendar() {

        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        txtData.setText(sdf.format(curDate.getTime()));

        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curDate.clone();

        //计算当月有多少天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -prevDays);

        int maxCellCount = 6 * 7;
        while (cells.size() < maxCellCount) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendar_grid.setAdapter(new CalendarAdapter(getContext(), cells));
    }

    /**
     * 绑定控件
     *
     * @param context
     */
    private void bindControl(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view, this);

        pre = findViewById(R.id.btn_pre);
        next = findViewById(R.id.btn_next);
        txtData = findViewById(R.id.txt_data);
        calendar_grid = findViewById(R.id.calendar_grid);

    }

    private class CalendarAdapter extends ArrayAdapter<Date> {

        private LayoutInflater inflater;

        public CalendarAdapter(@NonNull Context context, @NonNull ArrayList<Date> dates) {
            super(context, R.layout.canlendar_text_day, dates);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Date date = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.canlendar_text_day, null);
            }
            int day = date.getDate();
            ((TextView) convertView).setText(String.valueOf(day));


            Boolean isSameMouth = false;
            Date now = new Date();
            if (date.getMonth()==now.getMonth()){
                isSameMouth = true;
            }else {
                isSameMouth = false;
            }
            if (isSameMouth){

                //如果是当月的日期，那就设置一种颜色
                ((TextView)convertView).setTextColor(Color.parseColor("#000000"));
            }else {
                ((TextView)convertView).setTextColor(Color.parseColor("#666666"));
            }


            if (now.getDate() == date.getDate() && now.getMonth() == date.getMonth() && now.getYear() == date.getYear()) {

                //如果是当前的日期
                ((TextView)convertView).setTextColor(Color.parseColor("#ff0000"));
            }

            return convertView;

        }
    }

}
