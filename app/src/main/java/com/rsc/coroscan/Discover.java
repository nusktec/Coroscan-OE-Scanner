package com.rsc.coroscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.rsc.coroscan.adabters.DiscoverAdp;
import com.rsc.coroscan.databinding.ActivityDiscoverBinding;
import com.rsc.coroscan.databinding.DialogDetailsBinding;
import com.rsc.coroscan.models.CasesDiscover;

import java.util.List;
import java.util.Objects;

public class Discover extends AppCompatActivity {
    private List<CasesDiscover> discovers;
    ActivityDiscoverBinding bdx;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bdx = DataBindingUtil.setContentView(this, R.layout.activity_discover);
        this.ctx = this;
        //set tool
        setToolBar();
        main();
    }

    //set toolbar
    public void setToolBar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void main() {
        AndroidNetworking.get(MainActivity.BASE_URL)
                .addQueryParameter("gar", "okay")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(CasesDiscover.class, new ParsedRequestListener<List<CasesDiscover>>() {
                    @Override
                    public void onResponse(List<CasesDiscover> response) {
                        //on success
                        if (!response.isEmpty()) {
                            bdx.descTxt.setVisibility(View.GONE);
                            discovers = response;
                            bdx.descList.setAdapter(new DiscoverAdp(discovers, new DiscoverAdp.CasemoreListener() {
                                @Override
                                public void viewDetail(CasesDiscover discover) {
                                    //Show dialog
                                    show_pop(discover);
                                }
                            }));
                        } else {
                            //try again
                            Toast.makeText(ctx, "No Discovery List...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    //show dialog
    void show_pop(CasesDiscover discover) {
        Dialog d = new Dialog(this);
        Objects.requireNonNull(d.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        DialogDetailsBinding db = DataBindingUtil.inflate(LayoutInflater.from(d.getContext()), R.layout.dialog_details, null, false);
        d.setContentView(db.getRoot());
        //set attribute
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = 600;
        //pass data
        db.dcaseno.setText(discover.getNcase());
        db.ddeano.setText(discover.getNdeath());
        db.ddisno.setText(discover.getNdischarged());
        db.nstate.setText(discover.getState().toUpperCase());
        //prepare to show
        d.show();
        d.getWindow().setAttributes(lp);
    }

    //on option menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
