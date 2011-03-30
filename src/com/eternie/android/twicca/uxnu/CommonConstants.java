package com.eternie.android.twicca.uxnu;

import java.util.regex.Pattern;

/**
 * Ux.nu Twicca プラグイン内での共通定数
 * @author eternie
 */
public class CommonConstants {

	/**
	 * デバッグログの出力有無
	 */
	public static final boolean LOGD = false;
	/**
	 * デバッグログ用タグ
	 */
	public static final String TAG = "UxnuTwicca";
	
	/**
	 * URL検出パターン
	 */
	public static final Pattern convURLLinkPtn = 
        Pattern.compile
        ("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+",
        Pattern.CASE_INSENSITIVE);
	
}
