package com.checker.yousef.mytext;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.checker.yousef.mytext.library.Url;
import com.checker.yousef.mytext.util.IabHelper;
import com.checker.yousef.mytext.util.IabResult;
import com.checker.yousef.mytext.util.Inventory;
import com.checker.yousef.mytext.util.Purchase;

import org.json.JSONObject;

import java.util.ArrayList;

public class DonateActivity extends AppCompatActivity {

    private static final String SKU_PREMIUM_120 = "mytext120";
    private static final String SKU_PREMIUM_360 = "mytext360";
    private static final String SKU_PREMIUM_720 = "mytext720";

    private static final int REQUEST_CODE_PURCHASE_PREMIUM = 0;
    private static final String BILLING_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzZWW5sqY+yHLga/+Kau47gqfNUCLqH5rsOkdGPLLNky1kT04kMJSk0zc6h+DNPrMpFDqUvQGyw7i+P9fMl/EA/l+gB/TSEu7sl0WmEqxii64MG+LZyeC2QT/t8vqudViKHki4DO9RtLOspunHeUAzTnA85pCkHHZI3GRnsYB6Y/F5yzPRkXxJVb0iUUSoTr09AbaxzIXLKR2G11O0E99BUguZCux3t3tRZxxAibVSoC9I1yWzKYTUcHoHu7ZU0QwybhecB6ERJZ2dviOsnEAqKnkhvzj5Rh/aNw/DPloakGbo792X/HO0vLTqfJUvDOrTzXocGr26eHC13kYKQ0+MQIDAQAB";

    private IabHelper mBillingHelper;
    private boolean mIsPremium = false;
    private boolean mIsSubscriber = false;

    private Integer donateType = 2;

    //   IabHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);



        TextView donateExplation = (TextView)findViewById(R.id.donateExplanation);
        donateExplation.setText("To make batter this app, i need your support.\n No ads, no notification. use more safety and clean.");

        TextView newFeature = (TextView)findViewById(R.id.newFeature);
        newFeature.setText("New features in the next version\n 1.Batter UI.\n" +
                " 2.More stable.\n" +
                " 3.Multi language support.\n 4.Read text from photo and save as text");

        findViewById(R.id.aboutme).setOnClickListener(new DonateActivity.clickListener());
        findViewById(R.id.oneDollar).setOnClickListener(new DonateActivity.clickListener());
        findViewById(R.id.threeDollar).setOnClickListener(new DonateActivity.clickListener());
        findViewById(R.id.fiveDollar).setOnClickListener(new DonateActivity.clickListener());
        findViewById(R.id.donate).setOnClickListener(new DonateActivity.clickListener());
        setupBilling();
    }


    private void setupBilling() {
        mBillingHelper = new IabHelper(this, BILLING_PUBLIC_KEY);
        mBillingHelper.enableDebugLogging(true); // Remove before release
        mBillingHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) throws IabHelper.IabAsyncInProgressException {
                Log.d("billing", "Setup finished.");
                if (result.isFailure()) return;
                Log.d("billing", "Setup successful. Querying inventory.");
                mBillingHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    private void tearDownBilling() throws IabHelper.IabAsyncInProgressException {
        if (mBillingHelper != null){
            mBillingHelper.dispose();
        }
        mBillingHelper = null;
    }

    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d("billing", "Query inventory finished.");
            if (result.isFailure()) return;
            Log.d("billing", "Query inventory was successful.");
            mIsPremium = inventory.hasPurchase(SKU_PREMIUM_120);
            Log.d("billing", "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
        }
    };

    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d("billing", "Purchase finished: " + result + ", purchase: " + purchase);
            if (result.isFailure()) return;
            Log.d("billing", "Purchase successful.");
            if (purchase.getSku().equals(SKU_PREMIUM_120)) {
                Log.d("billing", "Purchase is premium upgrade. Congratulating user.");
                mIsPremium = true;
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mBillingHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected boolean isPremiumUser() {
        return this.mIsPremium;
    }

    protected boolean isSubscriber() {
        return this.mIsSubscriber;
    }

    protected void requestBilling_low() {
        try {
            mBillingHelper.launchPurchaseFlow(this, SKU_PREMIUM_120, REQUEST_CODE_PURCHASE_PREMIUM, mPurchaseFinishedListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    protected void requestBilling_medium() {
        if (mBillingHelper.subscriptionsSupported()) {
            try {
                mBillingHelper.launchSubscriptionPurchaseFlow(this, SKU_PREMIUM_360, REQUEST_CODE_PURCHASE_PREMIUM, mPurchaseFinishedListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    }

    protected void requestBilling_height() {
        if (mBillingHelper.subscriptionsSupported()) {
            try {
                mBillingHelper.launchSubscriptionPurchaseFlow(this, SKU_PREMIUM_720, REQUEST_CODE_PURCHASE_PREMIUM, mPurchaseFinishedListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
    }

    protected void cancelSubscription() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
    }

    public class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 押下時の処理
            switch (v.getId()){

                case R.id.aboutme:

                    Intent aboutMe = new Intent(DonateActivity.this, BrowserActivity.class);
                    aboutMe.putExtra("Url", Url.aboutme);
                    aboutMe.putExtra("Title","About Me");
                    startActivity(aboutMe);
                    break;
                case  R.id.oneDollar:
                    donateType = 1;
                    setSelected(v.getId());
                    break;
                case  R.id.threeDollar:
                    donateType = 2;
                    setSelected(v.getId());
                    break;
                case  R.id.fiveDollar:
                    donateType = 3;
                    setSelected(v.getId());
                    break;
                case R.id.donate:
                    pay();
                    break;
            }
        }
    }

    public void pay(){
        if (donateType == 1){
            requestBilling_low();
        }
        if (donateType == 2){
            requestBilling_medium();
        }
        if (donateType == 3){
            requestBilling_height();
        }
    }

    public void setSelected(Integer id){
        LinearLayout donaList = (LinearLayout)findViewById(R.id.donaList);

        int count = donaList.getChildCount();
        TextView v = null;
        for(int i=0; i<count; i++) {
            v = (TextView)donaList.getChildAt(i);
            v.setBackgroundColor(Color.parseColor("#eeeeee"));
            v.setTextColor(Color.parseColor("#000000"));
            if (v.getId() == id){
                v.setBackgroundColor(Color.parseColor("#FFCC80"));
                v.setTextColor(Color.parseColor("#ffffff"));
            }
            //do something with your child element
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            tearDownBilling();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }
}
