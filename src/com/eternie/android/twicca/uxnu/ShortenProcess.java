package com.eternie.android.twicca.uxnu;

import java.util.regex.Matcher;

import com.eternie.common.uxnuIF.UxnuInterface;
import com.eternie.common.uxnuIF.UxnuProcessException;
import com.eternie.common.uxnuIF.UxnuSiteDetail;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

/**
 * 短縮プロセス
 * @author eternie
 */
public class ShortenProcess implements Runnable {

	private Intent in;
	private Activity act;
	
	/**
	 * コンストラクタ
	 * @param in 入力Intent
	 * @param act 実行中のActivity
	 */
	public ShortenProcess (Intent in, Activity act) {
		this.in = in;
		this.act = act;
	}
	
	@Override
	public void run() {
		//入力テキスト
		String text = in.getStringExtra(Intent.EXTRA_TEXT);
		
		if (CommonConstants.LOGD) Log.d(CommonConstants.TAG, "Before: " + text);
		
		try {
			//コンバート
			String convertedText = urlDetectAndConvert(text);
			if (CommonConstants.LOGD) Log.d(CommonConstants.TAG, "After: " + convertedText);
			
			//返信Intent作成
			Intent repIn = new Intent();
			repIn.putExtra(Intent.EXTRA_TEXT, convertedText);
			
			//Intent転送
			act.setResult(Activity.RESULT_OK, repIn);
		}catch(Exception e) {
			if (CommonConstants.LOGD) Log.d(CommonConstants.TAG, e.getMessage());
		}
		act.finish(); //←終了忘れない
	}
	
	/**
	 * URLを検出し、都度短縮する。
	 * @param basetext 処理前の文字列
	 * @return 処理後の文字列
	 * @throws IllegalArgumentException URLを含まない文字列が与えられた場合
	 * @throws UxnuProcessException ux.nuの短縮処理中のエラー
	 */
	public String urlDetectAndConvert (String basetext) throws IllegalArgumentException, UxnuProcessException {
		if (basetext == null) return ""; //NullPointerException回避
		
		StringBuffer result = new StringBuffer(); //出力バッファ
		int base = 0;
		
		boolean haveMatched = false; //マッチしたことがあるか記録
		
		Matcher matcher = CommonConstants.convURLLinkPtn.matcher(basetext);
		while (matcher.find()) {
			haveMatched = true;
			int beginning = matcher.start();
			int end = matcher.end();
			result.append(basetext.substring(base, beginning)); //URLと無関係な部分をバッファへ
			String target = basetext.substring(beginning, end); //URL部分を切り出す
			
			UxnuSiteDetail detail = UxnuInterface.shortenURLWithDetail(target);
			String shortenResult = detail.getUrl();
			result.append(shortenResult);
			base = end;
		}
		//URLが含まれていない文字列を変換した場合はnull返答して、intent転送させないようにする
		if (!haveMatched)
			throw new IllegalArgumentException("No URL is included in the argument.");
		return result.toString();
    }

}
