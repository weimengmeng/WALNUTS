package com.njjd.walnuts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.adapter.GridViewAddImgesAdpter;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.ToastUtils;
import com.yongchun.library.view.ImageSelectorActivity;

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
    private GridView gw;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    @BindView(R.id.lv_back)
    SwipeBackLayout lvBack;
    private  ArrayList<String> images;
    public static Activity activity;
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
//                if (Build.VERSION.SDK_INT >= 23) {
//                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(AskActivity.this,Manifest.permission_group.STORAGE);
//                    if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
//                        ActivityCompat.requestPermissions(AskActivity.this,new String[]{Manifest.permission_group.STORAGE},REQUEST_CODE_ASK_CAMER);
//                        return;
//                    }else{
                        ImageSelectorActivity.start(AskActivity.this,10-gridViewAddImgesAdpter.getCount(), 1, true, true, true);
//                    }
//                } else {
//                    ImageSelectorActivity.start(AskActivity.this,10-gridViewAddImgesAdpter.getCount(), 1, true, true, true);
//                }

            }
        });
        lvBack.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CAMER:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Ok", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(this, "Denied", Toast.LENGTH_SHORT)
                            .show();
                    ActivityCompat.requestPermissions(AskActivity.this,new String[]{Manifest.permission_group.STORAGE},REQUEST_CODE_ASK_CAMER);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
    }
    @OnClick({R.id.back,R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_submit:
                if(etTitle.getText().toString().equals("")){
                    ToastUtils.showShortToast(this,"请输入问题标题");
                    return;
                }
                if(etContent.getText().toString().equals("")){
                    ToastUtils.showShortToast(this,"请输入问题描述");
                    return;
                }
                Intent intent=new Intent(this,AskKindActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("title",etTitle.getText().toString().trim());
                bundle.putString("content",etContent.getText().toString().trim());
                bundle.putStringArrayList("imgs",images);
                intent.putExtra("question",bundle);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            images= (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
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
