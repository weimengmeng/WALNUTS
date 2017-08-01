package com.njjd.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.njjd.adapter.DemoAdapter;
import com.njjd.domain.Demo;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.ToastUtils;
import com.njjd.walnuts.InformActivity;
import com.njjd.walnuts.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mrwim on 17/7/10.
 */

public class MessageFragment extends BaseFragment {
    @BindView(R.id.list_mes)
    SwipeMenuListView listMes;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.txt_date)
    TextView txtDate;
    private Context context;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    private SwipeMenuCreator creator;
    private List<Demo> list;
    private DemoAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), top);
        back.setVisibility(View.GONE);
        txtTitle.setText("消息");
        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem messItem = new SwipeMenuItem(
                        context);
                messItem.setBackground(new ColorDrawable(Color.LTGRAY));
                messItem.setWidth(240);
                messItem.setTitle("置顶");
                messItem.setTitleSize(16);
                messItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(messItem);

                SwipeMenuItem focusItem = new SwipeMenuItem(
                        context);
                focusItem.setBackground(R.color.login);
                focusItem.setWidth(240);
                focusItem.setTitle("删除");
                focusItem.setTitleSize(16);
                focusItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(focusItem);
            }
        };
        listMes.setMenuCreator(creator);
        listMes.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listMes.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ToastUtils.showShortToast(context, "置顶");
                        break;
                    case 1:
                        ToastUtils.showShortToast(context, "删除");
                        break;
                }
                return false;
            }
        });
        list=new ArrayList<>();
        list.add(new Demo());
        list.add(new Demo());
        list.add(new Demo());
        adapter=new DemoAdapter(list,context);
        listMes.setAdapter(adapter);
    }


    @OnClick(R.id.lv_inform)
    public void onViewClicked() {
        startActivity(new Intent(context, InformActivity.class));
    }

    @Override
    public void lazyInitData() {

    }
}
