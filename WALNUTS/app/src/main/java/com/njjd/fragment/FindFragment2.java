package com.njjd.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.njjd.adapter.FindAnswerAdapter;
import com.njjd.domain.ColumnEntity;
import com.njjd.domain.SelectedAnswerEntity;
import com.njjd.domain.SpecialEntity;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.walnuts.ColumnDetailActivity;
import com.njjd.walnuts.R;
import com.njjd.walnuts.SelectAnswerDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mrwim on 17/9/14.
 */

public class FindFragment2 extends BaseFragment {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.list_find)
    XRecyclerView listFind;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.top)
    LinearLayout top;
    private Context context;
    private List<SpecialEntity> specialEntities=new ArrayList<>();
    private FindAnswerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_find2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void initAfterSetContentView(Activity activity,
                                               View titleViewGroup) {
        if (activity == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            // 透明状态栏
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (titleViewGroup == null)
                return;
            // 设置头部控件ViewGroup的PaddingTop,防止界面与状态栏重叠
            int statusBarHeight = ImmersedStatusbarUtils.getStatusBarHeight(activity);
            titleViewGroup.setPadding(0, statusBarHeight, 0,0);
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAfterSetContentView(getActivity(), top);
        back.setVisibility(View.GONE);
        txtTitle.setText("精选");
        adapter=new FindAnswerAdapter(context,specialEntities);
        specialEntities.add(new SpecialEntity(new ColumnEntity("1","http://p.3761.com/pic/231432169575.jpg","核桃小编","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"),null));
        specialEntities.add(new SpecialEntity(new ColumnEntity("1","http://img3.imgtn.bdimg.com/it/u=3553261757,602330486&fm=214&gp=0.jpg","核桃小编","超级大美女","我被客户说服了怎么办？","http://up.qqjia.com/z/16/tu17317_45.png"),null));
        specialEntities.add(new SpecialEntity(null,new SelectedAnswerEntity("1","1","1","http://up.qqjia.com/z/16/tu17317_45.png","丽丽","超级大美女","我被客户说服了怎么办？","0","http://up.qqjia.com/z/16/tu17317_45.png","想办法改变自己的说话方式，让客户相信你")));
        specialEntities.add(new SpecialEntity(null,new SelectedAnswerEntity("1","1","1","http://img2.touxiang.cn/file/20160310/0bf65797064bd8990e2438664347c3de.jpg","小美","超级大美女","我被客户说服了怎么办？","1","","想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你")));
        specialEntities.add(new SpecialEntity(null,new SelectedAnswerEntity("1","1","1","http://www.qqpk.cn/Article/UploadFiles/201011/20101128035132850.jpg","丽丽","超级大美女","我被客户说服了怎么办？","0","http://up.qqjia.com/z/16/tu17317_45.png","想办法改变自己的说话方式，让客户相信你")));
        specialEntities.add(new SpecialEntity(null,new SelectedAnswerEntity("1","1","1","http://www.qq745.com/uploads/allimg/141015/1-1410150T344.jpg","丽丽","超级大美女","我被客户说服了怎么办？","1","","想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你想办法改变自己的说话方式，让客户相信你")));
        listFind.setLayoutManager(new LinearLayoutManager(context));
        listFind.setAdapter(adapter);
        listFind.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        listFind.setRefreshProgressStyle(ProgressStyle.BallPulse);
        adapter.setOnItemClickListener(new FindAnswerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position<2){
                    startActivity(new Intent(context, ColumnDetailActivity.class));
                }else{
                    startActivity(new Intent(context, SelectAnswerDetailActivity.class));
                }
            }
        });
    }

    @Override
    public void lazyInitData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
