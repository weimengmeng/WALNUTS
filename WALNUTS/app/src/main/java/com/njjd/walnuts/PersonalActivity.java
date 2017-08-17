package com.njjd.walnuts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.utils.ImmersedStatusbarUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/13.
 */

public class PersonalActivity extends BaseActivity implements OnItemClickListener, OnDismissListener{
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_sex)
    TextView txtSex;
    @BindView(R.id.txt_message)
    TextView txtMessage;
    @BindView(R.id.txt_vocation)
    TextView txtVocation;
    @BindView(R.id.txt_province)
    TextView txtProvince;
    @BindView(R.id.txt_city)
    TextView txtCity;
    @BindView(R.id.txt_position)
    TextView txtPosition;
    @BindView(R.id.txt_company)
    TextView txtCompany;
    @BindView(R.id.btn_add_help)
    TextView btnAddHelp;
    @BindView(R.id.lv_back)
    SwipeBackLayout lvBack;
    private AlertView mAlertViewExt;//窗口拓展例子
    private EditText etName;//拓展View内容
    private InputMethodManager imm;
    ViewGroup extView;
    private int flag=0;
    @Override
    public int bindLayout() {
        return R.layout.activity_personal;
    }

    @Override
    public void initView(View view) {
        back.setText("我的");
        txtTitle.setText("个人资料");
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        txtName.setText(SPUtils.get(this,"name","").toString());
        txtMessage.setText(SPUtils.get(this,"message","未完善").toString());
        txtPosition.setText(SPUtils.get(this,"position","未完善").toString());
        txtProvince.setText(SPUtils.get(this,"province","").toString());
        txtCity.setText(SPUtils.get(this,"city","").toString());
        txtVocation.setText(SPUtils.get(this,"industry","").toString());
        if(SPUtils.get(this,"sex","0").toString().equals("1.0")){
            txtSex.setText("女");
        }else{
            txtSex.setText("男");
        }
        txtCompany.setText(SPUtils.get(this,"company","待完善").toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersedStatusbarUtils.initAfterSetContentView(this, top);
        btnAddHelp.setVisibility(View.VISIBLE);
        lvBack.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }

    @OnClick({R.id.back,R.id.btn_add_help,R.id.txt_name,R.id.txt_message,R.id.txt_company})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.txt_name:
                flag=0;
                mAlertViewExt = new AlertView("提示", "请填写姓名", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
                extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_et,null);
                etName = (EditText) extView.findViewById(R.id.etName);
                etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focus) {
                        //输入框出来则往上移动
                        boolean isOpen=imm.isActive();
                        mAlertViewExt.setMarginBottom(isOpen&&focus ? 120 :0);
                        System.out.println(isOpen);
                    }
                });
                mAlertViewExt.addExtView(extView);
                mAlertViewExt.show();
                break;
            case R.id.txt_message:
                flag=1;
                mAlertViewExt = new AlertView("提示", "请填写签名(简介)", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
                extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_et,null);
                etName = (EditText) extView.findViewById(R.id.etName);
                etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focus) {
                        //输入框出来则往上移动
                        boolean isOpen=imm.isActive();
                        mAlertViewExt.setMarginBottom(isOpen&&focus ? 120 :0);
                        System.out.println(isOpen);
                    }
                });
                mAlertViewExt.addExtView(extView);
                mAlertViewExt.show();
                break;
            case R.id.txt_company:
                flag=2;
                mAlertViewExt = new AlertView("提示", "请输入公司名称", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
                extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_et,null);
                etName = (EditText) extView.findViewById(R.id.etName);
                etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focus) {
                        //输入框出来则往上移动
                        boolean isOpen=imm.isActive();
                        mAlertViewExt.setMarginBottom(isOpen&&focus ? 120 :0);
                        System.out.println(isOpen);
                    }
                });
                mAlertViewExt.addExtView(extView);
                mAlertViewExt.show();
                break;
            case R.id.btn_add_help:
                ToastUtils.showShortToast(this,"修改信息");
                break;
        }
    }
    private void closeKeyboard() {
        //关闭软键盘
        imm.hideSoftInputFromWindow(etName.getWindowToken(),0);
        //恢复位置
        mAlertViewExt.setMarginBottom(0);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDismiss(Object o) {
        closeKeyboard();
    }

    @Override
    public void onItemClick(Object o, int position) {
        closeKeyboard();
        //判断是否是拓展窗口View，而且点击的是非取消按钮
        if(o == mAlertViewExt && position != AlertView.CANCELPOSITION){
            String content = etName.getText().toString();
            if(content.isEmpty()){
                ToastUtils.showShortToast(this, "啥都没填呢");
            }
            else{
                switch (flag){
                    case 0:
                        txtName.setText(content);
                        break;
                    case 1:
                        if(content.trim().length()>49){
                            ToastUtils.showShortToast(this,"签名最多50字哦");
                            return;
                        }
                        txtMessage.setText(content);
                        break;
                    case 2:
                        txtCompany.setText(content);
                        break;
                }
            }
            return;
        }

    }
}
