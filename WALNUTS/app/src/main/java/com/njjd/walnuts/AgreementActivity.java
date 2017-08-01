package com.njjd.walnuts;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.njjd.utils.ImmersedStatusbarUtils;

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
    @BindView(R.id.lv_back)
    SwipeBackLayout lvBack;

    @Override
    public int bindLayout() {
        return R.layout.activity_aggrement;
    }

    @Override
    public void initView(View view) {
        back.setText("设置");
        txtTitle.setText("协议正文");
        pdfView.fromAsset("agreement.pdf")
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                .load();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lvBack.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
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
