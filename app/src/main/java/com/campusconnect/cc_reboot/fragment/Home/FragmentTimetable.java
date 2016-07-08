package com.campusconnect.cc_reboot.fragment.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.campusconnect.cc_reboot.POJO.SubscribedCourseList;
import com.campusconnect.cc_reboot.R;
import com.campusconnect.cc_reboot.auxiliary.ObservableScrollView;
import com.campusconnect.cc_reboot.auxiliary.ScrollViewListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by RK on 05/06/2016.
 */
public class FragmentTimetable extends Fragment implements ScrollViewListener{

    @Bind(R.id.scroll_horizontal_header)
    ObservableScrollView header_scroll_horizontal;
    @Bind(R.id.scroll_horizontal_body)
    ObservableScrollView body_scroll_horizontal;

    @Bind(R.id.table_body)
    TableLayout content_table;

    @Bind(R.id.row_1)
    TableRow row_1;
    @Bind(R.id.row_2)
    TableRow row_2;
    @Bind(R.id.row_3)
    TableRow row_3;
    @Bind(R.id.row_4)
    TableRow row_4;
    @Bind(R.id.row_5)
    TableRow row_5;
    @Bind(R.id.row_6)
    TableRow row_6;
    @Bind(R.id.row_7)
    TableRow row_7;
    @Bind(R.id.row_8)
    TableRow row_8;
    @Bind(R.id.row_9)
    TableRow row_9;
    @Bind(R.id.row_10)
    TableRow row_10;
    @Bind(R.id.row_11)
    TableRow row_11;

    String courseCode[] = {"ECO"};
    String date[] = {"1","2"};
    String startTime[]={"13:00","14:00"};
    String endTime[]={"14:00","15:00"};
    List<SubscribedCourseList> subscribedCourseListList;

    TableRow row;
    LinearLayout cell_container;

    HashMap<Integer, TableRow> map = new HashMap<Integer, TableRow>();
    public static View v;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this,v);
        map.put(1,row_1);
        map.put(2,row_2);
        map.put(3,row_3);
        map.put(4,row_4);
        map.put(5,row_5);
        map.put(6,row_6);
        map.put(7,row_7);
        map.put(8,row_8);
        map.put(9,row_9);
        map.put(10,row_10);
        map.put(11,row_11);
        setUpTable();
        header_scroll_horizontal.setScrollViewListener(this);
        body_scroll_horizontal.setScrollViewListener(this);

        return v;
    }

    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if(scrollView == header_scroll_horizontal) {
            body_scroll_horizontal.scrollTo(x, y);
        } else if(scrollView == body_scroll_horizontal) {
            header_scroll_horizontal.scrollTo(x, y);
        }
    }

    public void setUpTable(){
        for(int i=1;i<=6;i++)
            for(int j=1;j<=11;j++){
                cell_container = new LinearLayout(getContext());
                TableRow.LayoutParams trlp1 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                trlp1.span=1;
                trlp1.setMargins(1,1,1,1);
                trlp1.column=i;
                cell_container.setLayoutParams(trlp1);
                cell_container.setBackgroundColor(Color.rgb(223,223,223));
                cell_container.setPadding(0,16,0,16);
                cell_container.setOrientation(LinearLayout.VERTICAL);
                cell_container.setId(Integer.parseInt(i+""+j));

                map.get(j).addView(cell_container);
            }

    }


}
