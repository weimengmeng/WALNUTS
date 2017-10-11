package com.njjd.walnuts;

import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.njjd.adapter.GridViewAddImgesAdpter;
import com.njjd.utils.CommonUtils;
import com.njjd.utils.CustomEditText;
import com.njjd.utils.PhotoUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;

/**
 * Created by mrwim on 17/7/14.
 */

public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.gw)
    GridView gw;
    @BindView(R.id.et_question)
    CustomEditText etQuestion;
    @BindView(R.id.txt_length)
    TextView txtLength;
    @BindView(R.id.et_contact)
    EditText etContact;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;

    @Override
    public int bindLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView(View view) {
        txtTitle.setText("意见反馈");
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gridViewAddImgesAdpter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                txtLength.setText(gridViewAddImgesAdpter.getDatasSize()+"/4");
            }
        });
        gw.setAdapter(gridViewAddImgesAdpter);
        gw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Picker.from(FeedBackActivity.this)
                        .count(5 - gridViewAddImgesAdpter.getCount())
                        .enableCamera(true)
                        .setEngine(new GlideEngine())
                        .forResult(REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back,R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_submit:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
            for (int i = 0; i < mSelected.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                String imgpath = PhotoUtil.saveMyBitmapWH(CommonUtils.getRealPathFromUri(this, mSelected.get(i)), 480, 800);
                map.put("path", imgpath);
                datas.add(map);
            }
            gridViewAddImgesAdpter.setMaxImages(4);
            gridViewAddImgesAdpter.notifyDataSetChanged();
        }
    }
}
