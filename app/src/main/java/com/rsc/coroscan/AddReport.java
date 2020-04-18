package com.rsc.coroscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rsc.coroscan.databinding.ActivityAddReportBinding;

import java.util.Objects;

public class AddReport extends AppCompatActivity {
    Context ctx;
    ActivityAddReportBinding bdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        this.bdx = DataBindingUtil.setContentView(this, R.layout.activity_add_report);
        main();
    }

    //mobile fomr url
    static String MOBILE_FORM_URL = "https://eos.reedax.com/mobile-form?type=corona";

    //main void
    void main() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //load browser
        bdx.wb.setWebViewClient(new MyWbClient());
        bdx.wb.getSettings().setAllowContentAccess(true);
        bdx.wb.getSettings().setJavaScriptEnabled(true);
        bdx.wb.getSettings().getLoadWithOverviewMode();
        bdx.wb.getSettings().getLoadsImagesAutomatically();
        bdx.wb.loadUrl(MOBILE_FORM_URL);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //override wb client
    public class MyWbClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return true;
        }
    }
}
