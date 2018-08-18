package com.staff;

public class Constants {
	/** 社員IDの初番号 */
	public static final int STAFFINFO_FIRST_ID = 300;
	/** 数値バリデーションの正規表現 */
	public static final String NUMERIC_REGEX = "^[0-9]*$";
	/** 画面に表示するエラーメッセージ */
	public static final String PARAM_ERROR_MESSAGE = "\"正しい値を入力してください\"";
	/** 画面に表示するエラーメッセージ */
	public static final String DUPLICATION_ERR_MESSAGE = "\"すでに同じデータが存在しています\"";
	/** 画面に表示するエラーメッセージ */
	public static final String NODATA_MESSAGE = "\"社員情報が存在しません\"";
	/** 画面に表示するエラーメッセージ */
	public static final String NUMERIC_VALID_MESSAGE = "\"数値を入力してください\"";
	/** 画面に表示するエラーメッセージ */
	public static final String NOTNULL_MESSAGE = "\"nullは許可されていません\"";


	private Constants() {
	}
}
