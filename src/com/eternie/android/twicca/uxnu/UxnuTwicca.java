package com.eternie.android.twicca.uxnu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eternie.common.uxnuIF.UxnuInterface; //UxnuInterfaceは別packageにしました。他でも使うかもしれないので。
import com.eternie.common.uxnuIF.UxnuShortenedSiteDetail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class UxnuTwicca extends Activity {
	/**
	 * デバッグログの出力有無
	 */
	private static final boolean LOGD = false;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //待ち表示ダイアログ
        ProgressDialog dialog = ProgressDialog.show(this, getText(R.string.command_name), 
                getText(R.string.hello), true);
        dialog.show();
        
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
    	
    	StringBuffer result = new StringBuffer(); //出力バッファ
    	int base = 0;
    	
    	Matcher matcher = convURLLinkPtn.matcher(basetext);
    	while (matcher.find()) {
    		int beginning = matcher.start();
    		int end = matcher.end();
    		result.append(basetext.substring(base, beginning)); //URLと無関係な部分をバッファへ
    		String target = basetext.substring(beginning, end); //URL部分を切り出す
    		
    		UxnuShortenedSiteDetail detail = UxnuInterface.shortenURLWithDetail(target);
    		String shortenResult = detail.getUrl();
    		//String shortenResult = UxnuInterface.shortenURL(target);
    		result.append(shortenResult);
    		base = end;
    	}
    	return result.toString();
    }
}