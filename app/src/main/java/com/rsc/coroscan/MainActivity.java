package com.rsc.coroscan;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.rsc.coroscan.databinding.ActivityMainBinding;
import com.rsc.coroscan.databinding.DialogDetailsBinding;
import com.rsc.coroscan.models.CasesDiscover;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //global declare
    ActivityMainBinding bdx;
    Context ctx;
    private List<CasesDiscover> discovers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        bdx = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.ctx = this;
        this.main();
    }

    //main sub()
    private void main() {
        //auto start
        start_covid();
        //make bg db breath
        bdx.bgDrop.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.pulse_slow));
        //scan button
        bdx.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_covid();
            }
        });
        //discover btn
        bdx.btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (discovers != null) {
                    Intent intent = new Intent(MainActivity.this, Discover.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ctx, "No Discovers List", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //add button
        bdx.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddReport.class);
                startActivity(intent);
            }
        });
        //load from network
        loadFromNetwork();
    }

    //fire scanning method
    private void start_covid() {
        if (isScanning) {
            stop_scanning();
        } else {
            start_scanning();
        }
    }

    //start scanning
    boolean isScanning = false;
    Handler handler;

    private void start_scanning() {
        loadFromNetwork();
        bdx.vScanner.startRippleAnimation();
        bdx.centerImage.startAnimation(AnimationUtils.loadAnimation(ctx, R.anim.pulse));
        bdx.infoText.setText("Scanning...");
        bdx.btnScan.setText("Stop");
        isScanning = true;
    }

    //stop scanning
    private void stop_scanning() {
        bdx.vScanner.stopRippleAnimation();
        bdx.infoText.setText("Stopped Scanning");
        bdx.btnScan.setText("Start");
        bdx.centerImage.clearAnimation();
        isScanning = false;
        handler = null;
    }

    //result seen
    private void result_scanning() {
        playSound();
        //assign data from network
        final CasesDiscover ds1 = discovers.get(doRandom(0, discovers.size() - 1));
        bdx.c1name.setText(ds1.getState());
        bdx.c1no.setBadgeValue(Integer.parseInt(ds1.getNcase()));
        bdx.c1nod.setBadgeValue(Integer.parseInt(ds1.getNdischarged()));
        //show the first
        bdx.scanB1.setVisibility(View.VISIBLE);
        bdx.scanB1.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.popani));
        //add onclick_listener
        bdx.scanB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_pop(ds1);
            }
        });
        //another fetch
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playSound();
                final CasesDiscover ds2 = discovers.get(doRandom(0, discovers.size() - 1));
                bdx.c2name.setText(ds2.getState());
                bdx.c2no.setBadgeValue(Integer.parseInt(ds2.getNcase()));
                ///set visible now
                bdx.scanB2.setVisibility(View.VISIBLE);
                bdx.scanB2.setAnimation(AnimationUtils.loadAnimation(ctx, R.anim.popani));
                //add onclick_listener
                bdx.scanB2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show_pop(ds2);
                    }
                });
            }
        }, 2000);
    }

    //play sound effect
    private void playSound() {
        final AssetFileDescriptor afd = ctx.getResources().openRawResourceFd(R.raw.pop_effect);
        final FileDescriptor fileDescriptor = afd.getFileDescriptor();
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(fileDescriptor, afd.getStartOffset(),
                    afd.getLength());
            player.setLooping(false);
            player.prepare();
            player.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //network loader
    static protected String BASE_URL = "https://eos.reedax.com/api";

    public void loadFromNetwork() {
        AndroidNetworking.get(BASE_URL)
                .addQueryParameter("gar", "corona")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(CasesDiscover.class, new ParsedRequestListener<List<CasesDiscover>>() {
                    @Override
                    public void onResponse(List<CasesDiscover> response) {
                        //on success
                        if (!response.isEmpty()) {
                            discovers = response;
                            Toast.makeText(ctx, "Scanning nearby states...", Toast.LENGTH_SHORT).show();
                            //start result scanning
                            //wait for 5 sec
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //show success
                                    result_scanning();
                                    //play sound
                                }
                            }, 5000);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ctx, "No Discovery List", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    int doRandom(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
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
}
