package com.eternie.android.twicca.uxnu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eternie.common.uxnuIF.UxnuInterface; //UxnuInterfaceは別packageにしました。他でも使うかもしれないので。

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class UxnuTwicca extends Activity {
	/**
	 * デバッグログの出力有無
	 */
	private static final boolean LOGD = true;
	/**
	 * デバッグログ用タグ
	 */
	private static final String TAG = "UxnuTwicca";
	
	/**
	 * URL検出パターン
	 */
	protected static final Pattern convURLLinkPtn = 
        Pattern.compile
        ("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+",
        Pattern.CASE_INSENSITIVE);
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); //とりあえずメッセージは出す
        
        /*
         * Intent入力
         * Action判別はIntentフィルターかけてるので不要
         */
        Intent in = getIntent();
        
        //入力テキスト
        String text = in.getStringExtra(Intent.EXTRA_TEXT);
        
        if (LOGD) Log.d(TAG, "Before: " + text);
        
        //コンバート
        String convertedText = urlDetectAndConvert(text);
        if (LOGD) Log.d(TAG, "After: " + convertedText);
        
        //返信Intent作成
        Intent repIn = new Intent();
        repIn.putExtra(Intent.EXTRA_TEXT, convertedText);
        
        //Intent転送
        setResult(RESULT_OK, repIn);
        finish(); //←終了忘れない
    }
    
    /**
     * URLの存在を判別して、ux.nuによる短縮を行わせる。
     * @param basetext 元のテキスト
     * @return basetext内のURLをux.nuにより変換したもの
     */
    public String urlDetectAndConvert (String basetext) {
    	if (basetext == null) return ""; //NullPointerException回避
    	
    	StringBuffer result = new StringBuffer();
    	int base = 0;
    	
    	Matcher matcher = convURLLinkPtn.matcher(basetext);
    	while (matcher.find()) {
    		int beginning = matcher.start();
    		int end = matcher.end();
    		result.append(basetext.substring(base, beginning));
    		String target = basetext.substring(beginning, end);
    		String shortenResult = UxnuInterface.shortenURL(target);
    		result.append(shortenResult);
    		base = end;
    	}
    	return result.toString();
    }
}