package async.tom.com.chingpiaocup_all;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.w3c.dom.Text;


public class MainLendActivity extends AppCompatActivity {
    int min=0, max=1000;
    int step = 1, REQUEST_CAMERA = 0;
    String Url = ""; // Use your own Google sheet URL
    String postParamDlgBound = "", postParamDlgNotBound = "";
    boolean isFinish = true;
    boolean isStayScreen = true;

    private View mView;
    private TextView mTxtInstruct, mTxtResult, mTxtInstruct2, mTxtLogCat, mTxtSpace, mTxtCup, mTxtBox, mTxtSpoon, mTxtFork, mTxtChopstick;
    private EditText mEdtPhone, mEdtID;
    private NumberPicker mNumPicCup, mNumPicBox, mNumPicSpoon, mNumPicFork, mNumPicChopstick;
    private Button mBtnOK, mBtnSend, mBtnCancel;
    BottomNavigationView navigation;
    private Dialog dlgBuilder;


    private static class StaticHandler extends Handler {
        private final WeakReference<MainLendActivity> mActivity;
        private StaticHandler(MainLendActivity activity) {
            mActivity = new WeakReference<MainLendActivity>(activity);
        }
    }
    private final MainLendActivity.StaticHandler mHandler = new MainLendActivity.StaticHandler(this);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(getApplicationContext(), MainReturnActivity.class));
                    onPause();
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(getApplicationContext(), MainLendLogActivity.class));
                    onPause();
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lend);
        step = 1;

        mView = (View) findViewById(android.R.id.content);
        mTxtInstruct = (TextView) findViewById(R.id.txtInstruct);
        mTxtResult = (TextView) findViewById(R.id.txtResult);
        mTxtInstruct2 = (TextView) findViewById(R.id.txtInstruct2);
        mTxtLogCat = (TextView) findViewById(R.id.logCat);
        mTxtSpace = (TextView) findViewById(R.id.txtSpace);
        mTxtCup = (TextView) findViewById(R.id.txtCup);
        mTxtBox = (TextView) findViewById(R.id.txtBox);
        mTxtSpoon = (TextView) findViewById(R.id.txtSpoon);
        mTxtFork = (TextView) findViewById(R.id.txtFork);
        mTxtChopstick = (TextView) findViewById(R.id.txtChopstick);
        mEdtPhone = (EditText) findViewById(R.id.edtPhoneNum);
        mEdtID = (EditText) findViewById(R.id.edtID);
        mNumPicCup = (NumberPicker) findViewById(R.id.numPicCup);
        mNumPicBox = (NumberPicker) findViewById(R.id.numPicBox);
        mNumPicSpoon = (NumberPicker) findViewById(R.id.numPicSpoon);
        mNumPicFork = (NumberPicker) findViewById(R.id.numPicFork);
        mNumPicChopstick = (NumberPicker) findViewById(R.id.numPicChopstick);
        mBtnOK = (Button) findViewById(R.id.btnOK);
        mBtnSend = (Button) findViewById(R.id.btnSend);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);


        mTxtInstruct2.setEnabled(false);
        mTxtCup.setEnabled(false);
        mTxtBox.setEnabled(false);
        mTxtSpoon.setEnabled(false);
        mTxtFork.setEnabled(false);
        mTxtChopstick.setEnabled(false);
        mTxtLogCat.setVisibility(8);

        mNumPicCup.setMinValue(min);
        mNumPicCup.setMaxValue(300);
        mNumPicBox.setMinValue(min);
        mNumPicBox.setMaxValue(200);
        mNumPicSpoon.setMinValue(min);
        mNumPicSpoon.setMaxValue(100);
        mNumPicFork.setMinValue(min);
        mNumPicFork.setMaxValue(100);
        mNumPicChopstick.setMinValue(min);
        mNumPicChopstick.setMaxValue(100);

        mNumPicCup.setEnabled(false);
        mNumPicBox.setEnabled(false);
        mNumPicSpoon.setEnabled(false);
        mNumPicFork.setEnabled(false);
        mNumPicChopstick.setEnabled(false);

        mEdtID.setEnabled(false);
        mEdtPhone.requestFocus();

        mBtnSend.setEnabled(false);

        if (ActivityCompat.checkSelfPermission(MainLendActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( MainLendActivity.this, new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA );
        }

        InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(mEdtPhone, 0);
        mEdtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(mEdtPhone.isFocused() && mEdtPhone.isEnabled()){
                    int l = s.toString().length();
                    if(l==9) {mTxtInstruct.setText("輸入 \'市話\'"); mBtnOK.setEnabled(true);}
                    else if(l==10) {mTxtInstruct.setText("輸入 \'手機\'"); mBtnOK.setEnabled(true);}
                    else if(l==0) {mBtnOK.setEnabled(true);}
                    else {mTxtInstruct.setText("請輸入電話號碼"); mBtnOK.setEnabled(false);}
                }
            }
        });
        mEdtID.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(mEdtID.isEnabled() && mEdtID.isFocused()){
                    int l = s.toString().length();
                    if(mEdtPhone.getText().toString().equals("")){
                        if(l==0) {
                            mBtnOK.setEnabled(false);
                            Snackbar.make(mView,"電話和QR Code不能同時為空",Snackbar.LENGTH_LONG).show();
                        }else{
                            mBtnOK.setEnabled(true);
                        }
                    }else{
                        mBtnOK.setEnabled(true);
                    }
                }
            }
        });
        mBtnOK.setOnClickListener(btnOKClick);
        mBtnSend.setOnClickListener(btnSendClick);
        mBtnCancel.setOnClickListener(btnCancelClick);
        mTxtSpace.setOnLongClickListener(txtSpaceLongClk);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Repeat Thread
        //RepeatLend RL = new RepeatLend();
        //RL.setHandler(mHandler);
        //RL.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStayScreen = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStayScreen = true;
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected void onPause(){
        super.onPause();
        isStayScreen = false;
    }

    protected void onStop(){
        super.onStop();
        isStayScreen = false;
        //finish();
    }

    private View.OnClickListener btnOKClick = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if(mBtnOK.getText().equals("確認")){
                InputMethodManager kb = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                kb.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                if(mEdtPhone.getText().toString().equals("")) {
                    mTxtInstruct.setText("僅使用QR Code借用");
                    Snackbar.make(v, "僅使用QR Code借用", Snackbar.LENGTH_LONG).show();
                }
                mTxtResult.setText("電話: " + mEdtPhone.getText().toString());

                mTxtInstruct2.setEnabled(true);
                mTxtCup.setEnabled(true);
                mTxtBox.setEnabled(true);
                mTxtSpoon.setEnabled(true);
                mTxtFork.setEnabled(true);
                mTxtChopstick.setEnabled(true);

                mNumPicCup.setEnabled(true);
                mNumPicCup.setVisibility(View.VISIBLE);
                mNumPicBox.setEnabled(true);
                mNumPicBox.setVisibility(View.VISIBLE);
                mNumPicSpoon.setEnabled(true);
                mNumPicSpoon.setVisibility(View.VISIBLE);
                mNumPicFork.setEnabled(true);
                mNumPicFork.setVisibility(View.VISIBLE);
                mNumPicChopstick.setEnabled(true);
                mNumPicChopstick.setVisibility(View.VISIBLE);

                mEdtPhone.setEnabled(false);

                mBtnOK.setText("送出借用資料");
                mBtnOK.setEnabled(false);
                mBtnSend.setEnabled(true);
                mBtnSend.setVisibility(View.VISIBLE);
                step = 2;
            }
            else if(mBtnOK.getText().equals("送出借用資料")){
                final AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(MainLendActivity.this);
                altDlgBuilder.setTitle("借用確認");
                if(mEdtID.getText().toString().equals("")){ altDlgBuilder.setMessage("電話: " + mEdtPhone.getText().toString() + "  ID: " + mEdtID.getText().toString() + "\n杯子: " + mNumPicCup.getValue() + "  餐盒: " + mNumPicBox.getValue() + "  湯匙: " + mNumPicSpoon.getValue() + "  叉子: " + mNumPicFork.getValue() + "  筷子: " + mNumPicChopstick.getValue() +"\n無綁定QR Code，確認借用?"); }
                else{ altDlgBuilder.setMessage("電話: " + mEdtPhone.getText().toString() + "  ID: " + mEdtID.getText().toString() + "\n杯子: " + mNumPicCup.getValue() + "  餐盒: " + mNumPicBox.getValue() + "  湯匙: " + mNumPicSpoon.getValue() + "  叉子: " + mNumPicFork.getValue() + "  筷子: " + mNumPicChopstick.getValue() +"\n確認借用?"); }
                altDlgBuilder.setCancelable(false);
                altDlgBuilder.setPositiveButton("是", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        mEdtPhone.setEnabled(false);
                        mEdtID.setEnabled(false);
                        mNumPicCup.setEnabled(false);
                        mNumPicBox.setEnabled(false);
                        mNumPicSpoon.setEnabled(false);
                        mNumPicFork.setEnabled(false);
                        mNumPicChopstick.setEnabled(false);
                        new Lend().execute();
                        Snackbar.make(v, "送出借用資料...", Snackbar.LENGTH_LONG).show();
                        mTxtInstruct.setText("請輸入電話號碼");
                        mTxtResult.setText("初次借用，請輸入電話並掃描QR Code(不得同時為空)\n若為二次借用，請擇一填寫");
                        mTxtInstruct2.setEnabled(false);
                        mTxtCup.setEnabled(false);
                        mTxtBox.setEnabled(false);
                        mTxtSpoon.setEnabled(false);
                        mTxtFork.setEnabled(false);
                        mTxtChopstick.setEnabled(false);

                        mEdtID.setText("");
                        mEdtPhone.setText("");
                        mEdtPhone.setEnabled(true);
                        mEdtPhone.setVisibility(View.VISIBLE);
                        mEdtPhone.requestFocus();

                        mNumPicCup.setValue(0);
                        mNumPicBox.setValue(0);
                        mNumPicSpoon.setValue(0);
                        mNumPicFork.setValue(0);
                        mNumPicChopstick.setValue(0);
                        step = 1;

                        mBtnOK.setEnabled(true);
                        mBtnOK.setVisibility(View.VISIBLE);
                        mBtnOK.setText("確認");
                        mBtnSend.setEnabled(false);

                        InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                        keyboard.showSoftInput(mEdtPhone, 0);
                    }
                });
                altDlgBuilder.setNeutralButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                altDlgBuilder.show();

            }
        }
    };

    private View.OnClickListener btnCancelClick = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if(step == 1){
                mEdtPhone.setText("");
            }else if(step == 2){
                mEdtPhone.setEnabled(true);
                mEdtPhone.setText("");
                mEdtPhone.requestFocus();
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mEdtPhone, 0);

                mTxtInstruct.setEnabled(true);
                mTxtInstruct.setText("請輸入電話號碼");
                mTxtInstruct2.setEnabled(false);
                mTxtResult.setText("初次借用，請輸入電話並掃描QR Code(不得同時為空)\n若為二次借用，請擇一填寫");
                mTxtCup.setEnabled(false);
                mTxtBox.setEnabled(false);
                mTxtSpoon.setEnabled(false);
                mTxtFork.setEnabled(false);
                mTxtChopstick.setEnabled(false);

                mNumPicCup.setValue(0);
                mNumPicBox.setValue(0);
                mNumPicSpoon.setValue(0);
                mNumPicFork.setValue(0);
                mNumPicChopstick.setValue(0);
                mNumPicCup.setEnabled(false);
                mNumPicBox.setEnabled(false);
                mNumPicSpoon.setEnabled(false);
                mNumPicFork.setEnabled(false);
                mNumPicChopstick.setEnabled(false);

                mBtnOK.setEnabled(true);
                mBtnOK.setText("確認");
                mBtnSend.setEnabled(false);
                step = 1;
            }else if(step == 3){
                mEdtID.setEnabled(false);
                mEdtID.setText("");

                if(mEdtPhone.getText().toString().equals("")){
                    mTxtInstruct.setEnabled(false);
                    mTxtInstruct.setText("僅使用QR Code借用");
                }else {
                    mTxtInstruct.setEnabled(false);
                    mTxtInstruct.setText("請感掃描QR Code");
                }

                mTxtInstruct2.setEnabled(true);
                mTxtCup.setEnabled(true);
                mTxtBox.setEnabled(true);
                mTxtSpoon.setEnabled(true);
                mTxtFork.setEnabled(true);
                mTxtChopstick.setEnabled(true);

                mNumPicCup.setEnabled(true);
                mNumPicCup.setVisibility(View.VISIBLE);
                mNumPicBox.setEnabled(true);
                mNumPicBox.setVisibility(View.VISIBLE);
                mNumPicSpoon.setEnabled(true);
                mNumPicSpoon.setVisibility(View.VISIBLE);
                mNumPicFork.setEnabled(true);
                mNumPicFork.setVisibility(View.VISIBLE);
                mNumPicChopstick.setEnabled(true);
                mNumPicChopstick.setVisibility(View.VISIBLE);

                mBtnOK.setEnabled(false);
                mBtnSend.setEnabled(true);
                step =2;
            }
        }
    };

    private View.OnClickListener btnSendClick = new View.OnClickListener(){

        @SuppressLint("WrongConstant")
        @Override
        public void onClick(View v) {
            mTxtResult.setText("借用資料 - 電話: " + mEdtPhone.getText() + "\n杯子: " + mNumPicCup.getValue() + "  餐盒: " + mNumPicBox.getValue() + "  湯匙: " + mNumPicSpoon.getValue() + "  叉子: " + mNumPicFork.getValue() + "  筷子: " + mNumPicChopstick.getValue());
            mTxtInstruct2.setEnabled(false);
            mTxtCup.setEnabled(false);
            mTxtBox.setEnabled(false);
            mTxtSpoon.setEnabled(false);
            mTxtFork.setEnabled(false);
            mTxtChopstick.setEnabled(false);

            mNumPicCup.setEnabled(false);
            mNumPicBox.setEnabled(false);
            mNumPicSpoon.setEnabled(false);
            mNumPicFork.setEnabled(false);
            mNumPicChopstick.setEnabled(false);
            if(mEdtPhone.getText().toString().equals("")){
                mTxtInstruct.setText("僅使用QR Code 借用\n請掃描QR Code");
            }else mTxtInstruct.setText("請掃描QR Code");

            mEdtPhone.setEnabled(false);
            mEdtID.setEnabled(true);
            mEdtID.setVisibility(1);
            mEdtID.requestFocus();
            InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

            mBtnSend.setEnabled(false);
            if(mEdtPhone.getText().toString().equals("")) mBtnOK.setEnabled(false);
            else mBtnOK.setEnabled(true);

            step = 3;

            scanBarcode(v);
        }
    };

    private View.OnLongClickListener txtSpaceLongClk = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
            if(mTxtLogCat.getVisibility() == View.VISIBLE){
                mTxtLogCat.setVisibility(View.GONE);;
                Snackbar.make(mView, "已關閉偵錯模式，再次長按以開啟", Snackbar.LENGTH_LONG).show();
            }
            else{
                mTxtLogCat.setVisibility(View.VISIBLE);
                Snackbar.make(mView, "已啟動偵錯模式，再次長按以取消", Snackbar.LENGTH_LONG).show();
            }
            return false;
        }
    };

    private View.OnClickListener btnDlgBoundClk = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            new LendDialog().execute(postParamDlgBound);
            Snackbar.make(mView, "送出借用資料...", Snackbar.LENGTH_LONG).show();
            postParamDlgBound = "";
            dlgBuilder.dismiss();
        }
    };

    private View.OnClickListener btnDlgNotBoundClk = new View.OnClickListener(){
      @Override
      public void onClick(View v){
          new LendDialog().execute(postParamDlgNotBound);
          Snackbar.make(mView, "送出借用資料", Snackbar.LENGTH_LONG);
          postParamDlgNotBound = "";
          dlgBuilder.dismiss();
      }
    };

    private View.OnClickListener btnDlgCancelClk = new View.OnClickListener(){
      @Override
      public void onClick(View v){
          dlgBuilder.dismiss();
          postParamDlgBound = "";
          postParamDlgNotBound = "";
      }
    };

    public void scanBarcode(View v){
        Intent intent = new Intent(this, MainScanActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==0) {
            if(requestCode== CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    mEdtID.setText(barcode.displayValue);
                    Toast.makeText(MainLendActivity.this,"QR Code 結果 : " + barcode.displayValue, Toast.LENGTH_LONG).show();
                    final AlertDialog.Builder altDlgBuilder = new AlertDialog.Builder(MainLendActivity.this);
                    altDlgBuilder.setTitle("借用確認");
                    if(mEdtID.getText().toString().equals("")){ altDlgBuilder.setMessage("電話: " + mEdtPhone.getText().toString() + "  ID: " + mEdtID.getText().toString() + "\n杯子: " + mNumPicCup.getValue() + "  餐盒: " + mNumPicBox.getValue() + "  湯匙: " + mNumPicSpoon.getValue() + "  叉子: " + mNumPicFork.getValue() + "  筷子: " + mNumPicChopstick.getValue() +"\n無綁定QR Code，確認借用?"); }
                    else{ altDlgBuilder.setMessage("電話: " + mEdtPhone.getText().toString() + "  ID: " + mEdtID.getText().toString() + "\n杯子: " + mNumPicCup.getValue() + "  餐盒: " + mNumPicBox.getValue() + "  湯匙: " + mNumPicSpoon.getValue() + "  叉子: " + mNumPicFork.getValue() + "  筷子: " + mNumPicChopstick.getValue() +"\n確認借用?"); }
                    altDlgBuilder.setCancelable(false);
                    altDlgBuilder.setPositiveButton("是", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            mEdtPhone.setEnabled(false);
                            mEdtID.setEnabled(false);
                            mNumPicCup.setEnabled(false);
                            mNumPicBox.setEnabled(false);
                            mNumPicSpoon.setEnabled(false);
                            mNumPicFork.setEnabled(false);
                            mNumPicChopstick.setEnabled(false);
                            new Lend().execute();
                            Snackbar.make(mView, "送出借用資料...", Snackbar.LENGTH_LONG).show();
                            mTxtInstruct.setText("請輸入電話號碼");
                            mTxtResult.setText("初次借用，請輸入電話並掃描QR Code(不得同時為空)\n若為二次借用，請擇一填寫");
                            mTxtInstruct2.setEnabled(false);
                            mTxtCup.setEnabled(false);
                            mTxtBox.setEnabled(false);
                            mTxtSpoon.setEnabled(false);
                            mTxtFork.setEnabled(false);
                            mTxtChopstick.setEnabled(false);

                            mEdtID.setText("");
                            mEdtPhone.setText("");
                            mEdtPhone.setEnabled(true);
                            mEdtPhone.setVisibility(View.VISIBLE);
                            mEdtPhone.requestFocus();

                            mNumPicCup.setValue(0);
                            mNumPicBox.setValue(0);
                            mNumPicSpoon.setValue(0);
                            mNumPicFork.setValue(0);
                            mNumPicChopstick.setValue(0);
                            step = 1;

                            mBtnOK.setEnabled(true);
                            mBtnOK.setVisibility(View.VISIBLE);
                            mBtnOK.setText("確認");
                            mBtnSend.setEnabled(false);

                            InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                            keyboard.showSoftInput(mEdtPhone, 0);
                        }
                    });
                    altDlgBuilder.setNeutralButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    altDlgBuilder.show();

                } else {
                    Toast.makeText(MainLendActivity.this, "掃不到QR Code", Toast.LENGTH_LONG).show();
                    mBtnSend.setEnabled(true);
                }
            }
        } else {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class Lend extends AsyncTask<Void, Void, Void>{
        String re = "";
        String result = "";
        String phone = mEdtPhone.getText().toString();
        String id = mEdtID.getText().toString();
        int nCup = mNumPicCup.getValue();
        int nBox = mNumPicBox.getValue();
        int nSpoon = mNumPicSpoon.getValue();
        int nFork = mNumPicFork.getValue();
        int nChopstick = mNumPicChopstick.getValue();
        String postParam = "action=Lend&phone=" + phone + "&id=" + id + "&nCup=" + nCup + "&nBox=" + nBox + "&nSpoon=" + nSpoon + "&nFork=" + nFork + "&nChopstick=" + nChopstick;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            result = result + "PostExecute_Done\n\n\n\n\n\n";
            Toast.makeText(MainLendActivity.this, re, Toast.LENGTH_LONG).show();
            mTxtLogCat.setText(result);
            if(re.equals("ID Not Found") || re.equals("Phone Number Not Found")){
                dlgBuilder = new Dialog(MainLendActivity.this);
                dlgBuilder.setCancelable(false);
                dlgBuilder.setContentView(R.layout.dialog);

                TextView mTxtDlgTitle = (TextView) dlgBuilder.findViewById(R.id.txtDlgTitle);
                TextView mTxtDlgMessage = (TextView) dlgBuilder.findViewById(R.id.txtDlgMessage);
                final TextView mTxtDlgResult = (TextView) dlgBuilder.findViewById(R.id.txtDlgResult);
                final Button mBtnDlgBound = (Button) dlgBuilder.findViewById(R.id.btnDlgBound);
                final Button mBtnDlgNotBound = (Button) dlgBuilder.findViewById(R.id.btnDlgNotBound);
                final Button mBtnDlgCancel = (Button) dlgBuilder.findViewById(R.id.btnDlgCancel);
                final EditText mEdtDlgPhone = (EditText) dlgBuilder.findViewById(R.id.edtDlgPhone);

                mTxtDlgMessage.setText("借用人資料 -\n電話: " + phone + "  ID: " + id + "\n借用內容 -\n杯子: " + nCup + "  餐盒: " + nBox + "  湯匙: " + nSpoon + "  叉子: " + nFork + "  筷子: " + nChopstick);

                mBtnDlgBound.setEnabled(false);
                mBtnDlgNotBound.setEnabled(false);

                mBtnDlgBound.setOnClickListener(btnDlgBoundClk);
                mBtnDlgNotBound.setOnClickListener(btnDlgNotBoundClk);
                mBtnDlgCancel.setOnClickListener(btnDlgCancelClk);
                mEdtDlgPhone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    @Override
                    public void afterTextChanged(Editable s) {
                        int l = s.toString().length();
                        if(l==9) {
                            postParamDlgBound = "action=Lend&phone=" + mEdtDlgPhone.getText().toString() + "&id=" + id + "&nCup=" + nCup + "&nBox=" + nBox + "&nSpoon=" + nSpoon + "&nFork=" + nFork + "&nChopstick=" + nChopstick;
                            postParamDlgNotBound = "action=Lend&phone=" + mEdtDlgPhone.getText().toString() + "&id=" + "&nCup=" + nCup + "&nBox=" + nBox + "&nSpoon=" + nSpoon + "&nFork=" + nFork + "&nChopstick=" + nChopstick;
                            mTxtDlgResult.setText("輸入 \'市話\'");
                            mBtnDlgBound.setEnabled(true);
                            mBtnDlgNotBound.setEnabled(true);
                        }else if(l==10) {
                            postParamDlgBound = "action=Lend&phone=" + mEdtDlgPhone.getText().toString() + "&id=" + id + "&nCup=" + nCup + "&nBox=" + nBox + "&nSpoon=" + nSpoon + "&nFork=" + nFork + "&nChopstick=" + nChopstick;
                            postParamDlgNotBound = "action=Lend&phone=" + mEdtDlgPhone.getText().toString() + "&id=" + "&nCup=" + nCup + "&nBox=" + nBox + "&nSpoon=" + nSpoon + "&nFork=" + nFork + "&nChopstick=" + nChopstick;
                            mTxtDlgResult.setText("輸入 \'手機\'");
                            mBtnDlgBound.setEnabled(true);
                            mBtnDlgNotBound.setEnabled(true);
                            InputMethodManager kb = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            kb.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                        }else {
                            mTxtDlgResult.setText("長度不足");
                            mBtnDlgBound.setEnabled(false);
                            mBtnDlgNotBound.setEnabled(false);
                        }
                    }
                });
                dlgBuilder.show();
                mEdtDlgPhone.requestFocus();
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mEdtDlgPhone, 0);
            }
            isFinish = true;
        }
        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            HttpURLConnection urlConn = null;
            try {
                url = new URL(Url);
                result = result + "NewURL_Done\n";
                urlConn = (HttpURLConnection) url.openConnection();
                result = result + "OpenURL_Done\n";
                urlConn.setRequestMethod("POST");
                result = result + "SetPostMethod_Done\n";
                urlConn.setRequestProperty("Content-Length", "" + Integer.toString(postParam.getBytes().length));
                result = result + "SetRequestProperty_Done\n";
                urlConn.setDoInput(true);
                result = result + "SetInput_Done\n";
                urlConn.setDoOutput(true);
                result = result + "SetOutput_Done\n";
                DataOutputStream wr = new DataOutputStream(urlConn.getOutputStream());
                result = result + "NewDataOutputStream_Done\n";
                wr.writeBytes(postParam);
                wr.flush();
                wr.close();
                InputStream is = urlConn.getInputStream();
                int responseCode = urlConn.getResponseCode();
                if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                    result = result + "HTTP_OK_Done\n";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "utf-8"));
                    result = result + "NewBufferedReader_Done\n";
                    String str = "";
                    result = result + "StartReading_Done\n";
                    while((str=reader.readLine()) != null){
                        re = re + str;
                        result = result + str + "\n";
                        result = result + "Line_Done" ;
                    }
                    reader.close();
                    result = result + "\nHTTP_OK: " + responseCode + "\ndoInBackGround_Done\n";
                }else { result = result + "ERROR: " + responseCode + "\n"; }
                result = result + "DisConnect_Done\n";
            } catch (IOException e) {
                e.printStackTrace();
                result = result + "CatchException\n";
            } finally {
                if(urlConn != null){
                    urlConn.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isFinish = false;
        }
    }

    class LendDialog extends AsyncTask<String, Void, Void>{
        String re = "";
        String result = "";
        String postParam = "";
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            result = result + "PostExecute_Done\n\n\n\n\n\n";
            mTxtLogCat.setText(result);
            Toast.makeText(MainLendActivity.this, re, Toast.LENGTH_LONG).show();
            isFinish = true;
        }
        @Override
        protected Void doInBackground(String... params) {
            postParam = params[0];
            URL url = null;
            HttpURLConnection urlConn = null;
            try {
                url = new URL(Url);
                result = result + "NewURL_Done\n";
                urlConn = (HttpURLConnection) url.openConnection();
                result = result + "OpenURL_Done\n";
                urlConn.setRequestMethod("POST");
                result = result + "SetPostMethod_Done\n";
                urlConn.setRequestProperty("Content-Length", "" + Integer.toString(postParam.getBytes().length));
                result = result + "SetRequestProperty_Done\n";
                urlConn.setDoInput(true);
                result = result + "SetInput_Done\n";
                urlConn.setDoOutput(true);
                result = result + "SetOutput_Done\n";
                DataOutputStream wr = new DataOutputStream(urlConn.getOutputStream());
                result = result + "NewDataOutputStream_Done\n";
                wr.writeBytes(postParam);
                wr.flush();
                wr.close();
                InputStream is = urlConn.getInputStream();
                int responseCode = urlConn.getResponseCode();
                if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                    result = result + "HTTP_OK_Done\n";
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "utf-8"));
                    result = result + "NewBufferedReader_Done\n";
                    String str = "";
                    result = result + "StartReading_Done\n";
                    while((str=reader.readLine()) != null){
                        re = re + str;
                        result = result + str + "\n";
                        result = result + "Line_Done" ;
                    }
                    reader.close();
                    result = result + "\nHTTP_OK: " + responseCode + "\ndoInBackGround_Done\n";
                }else { result = result + "ERROR: " + responseCode + "\n"; }
                result = result + "DisConnect_Done\n";
            } catch (IOException e) {
                e.printStackTrace();
                result = result + "CatchException\n";
            } finally {
                if(urlConn != null){
                    urlConn.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isFinish = false;
        }
    }

    public class RepeatLend extends Thread{
        private Handler mHandler;
        public void run(){
            while(isStayScreen){
                if(isFinish) {
                    try {
                        Thread.sleep(1000);
                        new Lend().execute();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        void setHandler(Handler h){
            mHandler = h;
        }
    }
}
