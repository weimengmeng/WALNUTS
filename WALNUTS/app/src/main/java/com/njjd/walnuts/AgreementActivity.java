package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.pdfview.PDFView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class AgreementActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.pdf_view)
    PDFView pdfView;

    @Override
    public int bindLayout() {
        return R.layout.activity_aggrement;
    }

    @Override
    public void initView(View view) {
        back.setText("设置");
        txtTitle.setText("协议正文");
        pdfView.fromAsset("aggrement.pdf")
                .enableSwipe(true) // allows to block changing pages using swipe
                .defaultPage(0)
                .load();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
