package com.eternie.android.twicca.uxnu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class UxnuTwicca extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        /*
         * Intent入力
         * Action判別はIntentフィルターかけてるので不要
         */
        Intent in = getIntent();
        
        ShortenProcess sp = new ShortenProcess(in, this);
        new Thread(sp).start();
    }
    
}