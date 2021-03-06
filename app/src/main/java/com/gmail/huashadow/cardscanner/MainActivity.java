package com.gmail.huashadow.cardscanner;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * Created by Wolf Xu on 2016/3/1.
 */
public class MainActivity extends Activity {

    private static final int SCAN_REQUEST_CODE = 1;

    private TextView mTvCardNumber;
    private Button mBtnCopyToClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mTvCardNumber = (TextView) findViewById(R.id.tv_cardNumber);
        mBtnCopyToClipboard = (Button) findViewById(R.id.btn_copy_to_clipboard);
        handleScanBtnClick();
        handleClipboardBtnClick();
    }

    private void handleScanBtnClick() {
        Button btnScan = (Button) findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performScan();
            }
        });
    }

    private void handleClipboardBtnClick() {
        findViewById(R.id.btn_copy_to_clipboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipBoard();
            }
        });
    }

    private void performScan() {
        Intent scanIntent = new Intent(this, CardIOActivity.class);

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    }

    private void copyToClipBoard() {
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", mTvCardNumber.getText()));
        Toast.makeText(this, R.string.has_copy_to_clip_board, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                mTvCardNumber.setText(scanResult.getFormattedCardNumber());
                mBtnCopyToClipboard.setVisibility(View.VISIBLE);
            }
        }
    }
}
