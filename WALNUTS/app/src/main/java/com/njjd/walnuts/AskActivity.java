package com.njjd.walnuts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.adapter.GridViewAddImgesAdpter;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.ToastUtils;
import com.yongchun.library.view.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class AskActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_content2)
    EditText etContent;
    private File file;
    private GridView gw;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    @BindView(R.id.lv_back)
    SwipeBackLayout lvBack;
    @Override
    public int bindLayout() {
        return R.layout.activity_ask;
    }

    @Override
    public void initView(View view) {
        ImmersedStatusbarUtils.initAfterSetContentView(this, top);
        back.setText("发布");
        txtTitle.setText("提问");
        gw = (GridView) findViewById(R.id.gw);
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gw.setAdapter(gridViewAddImgesAdpter);
        gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ImageSelectorActivity.start(AskActivity.this,10-gridViewAddImgesAdpter.getCount(), 1, true, true, true);
            }
        });
        lvBack.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back,R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_submit:
              startActivity(new Intent(this,AskKindActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            file = new File(images.get(0));
            for (int i = 0; i < images.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("path", images.get(i));
                datas.add(map);
            }
            gridViewAddImgesAdpter.setMaxImages(9);
            gridViewAddImgesAdpter.notifyDataSetChanged();

        }
    }
}