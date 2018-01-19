package com.njjd.walnuts;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.example.retrofit.entity.SubjectPost;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.sunflower.FlowerCollector;
import com.njjd.application.AppAplication;
import com.njjd.application.ConstantsVal;
import com.njjd.domain.AnswerEntity;
import com.njjd.http.HttpManager;
import com.njjd.utils.AndroidBug5497Workaround;
import com.njjd.utils.FolderTextView;
import com.njjd.utils.JsonParser;
import com.njjd.utils.KeybordS;
import com.njjd.utils.LogUtils;
import com.njjd.utils.SPUtils;
import com.njjd.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mrwim on 17/7/12.
 */

public class AnswerActivity extends BaseActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.btn_voice)
    Button btnVoice;
    @BindView(R.id.lv_voice)
    LinearLayout lvVoice;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.et_answer)
    EditText etAnswer;
    @BindView(R.id.voicLine)
    VoiceLineView voiceLineView;
    @BindView(R.id.txt_content)
    FolderTextView txtContent;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private RecognizerListener recognizerListener;
    private String temp = "";
    private AnswerEntity answerEntity=null;
    @Override
    public int bindLayout() {
        return R.layout.activity_answer;
    }

    @Override
    public void initView(View view) {
        back.setText("详情");
        txtTitle.setText("回答");
        txtName.setText(getIntent().getStringExtra("quesTitle"));
        txtContent.setFoldLine(2);
        txtContent.setText(getIntent().getStringExtra("content"));
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        AndroidBug5497Workaround.assistActivity(this);
        recognizerListener = new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {
                voiceLineView.setVolume(i * 5);
            }

            @Override
            public void onBeginOfSpeech() {
                btnVoice.setText("结束");
            }

            @Override
            public void onEndOfSpeech() {
                btnVoice.setText("开始");
                lvVoice.setVisibility(View.GONE);
            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }

            @Override
            public void onError(SpeechError speechError) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        };
        etAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvVoice.setVisibility(View.GONE);
                btnVoice.setText("开始");
                mIat.stopListening();
            }
        });
        if("2".equals(getIntent().getStringExtra("type"))){
            answerEntity=(AnswerEntity) getIntent().getBundleExtra("answer").get("answer");
            etAnswer.setText(answerEntity.getContent());
            etAnswer.setSelection(answerEntity.getContent().length());
        }
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
//                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.back, R.id.btn_submit, R.id.txt_voice, R.id.btn_voice, R.id.btn_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_cancle:
                lvVoice.setVisibility(View.GONE);
                btnVoice.setText("开始");
                mIat.stopListening();
                break;
            case R.id.btn_voice:
                if (btnVoice.getText().toString().equals("开始")) {
                    temp = etAnswer.getText().toString();
                    mIat.startListening(recognizerListener);
                    btnVoice.setText("结束");
                } else {
                    mIat.stopListening();
                    btnVoice.setText("开始");
                    lvVoice.setVisibility(View.GONE);
                }
                break;
            case R.id.txt_voice:
                if (!AppAplication.selfPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                    return;
                }
                KeybordS.closeBoard(this);
                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(this, "iat_recognize");
                mIatResults.clear();
                //设置参数
                setParam();
                lvVoice.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_submit:
                if(answerEntity!=null){
                    //修改回答
                    editAnswer();
                    return;
                }
                if (etAnswer.getText().toString().trim().equals("")) {
                    ToastUtils.showShortToast(this, "请输入回答");
                    return;
                }
                MobclickAgent.onEvent(this, ConstantsVal.ANSWERQUESTION);
                pubAnswer();
                break;
        }
    }
    private void editAnswer(){
        Map<String, Object> map = new HashMap<>();
        map.put("comment_id", Float.valueOf(answerEntity.getAnwerId()).intValue());
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", ""));
        map.put("content", etAnswer.getText().toString().trim());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(new HttpOnNextListener() {
            @Override
            public void onNext(Object o) {
                finish();
            }
        }, this, true, false), map);
        HttpManager.getInstance().editComment(postEntity);
    }
    private void pubAnswer() {
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", Float.valueOf(getIntent().getStringExtra("quesId")).intValue());
        map.put("uid", SPUtils.get(this, "userId", ""));
        map.put("token", SPUtils.get(this, "token", ""));
        map.put("content", etAnswer.getText().toString().trim());
        SubjectPost postEntity = new SubjectPost(new ProgressSubscriber(this, this, true, false), map);
        HttpManager.getInstance().pubComment(postEntity);
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, null);
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        etAnswer.setText(temp + resultBuffer.toString());
        etAnswer.setSelection(etAnswer.length());
    }

    @Override
    public void onNext(Object o) {
        super.onNext(o);
        ToastUtils.showShortToast(this, "回答成功");
        MobclickAgent.onEvent(this, ConstantsVal.ANSWERRESULT);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(this);
        FlowerCollector.onPageStart(TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }
}
