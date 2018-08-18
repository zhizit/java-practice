package com.staff.service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.SessionScoped;

import com.staff.Constants;
import com.staff.dto.StaffInfo;
import com.staff.dto.StaffInfoBook;

/**
 * 社員情報のServiceクラス
 *
 * @author zhizit
 *
 */
@SessionScoped
public class StaffInfoService implements Serializable {
	//テスト時のみ使用
	StaffInfoBook staffInfoBook;

	public StaffInfoService() {
		if (null==staffInfoBook) {
			staffInfoBook = new StaffInfoBook();
		}
	}
	public List<StaffInfo> getStaffInfoGlobalList() {
		return staffInfoBook.getStaffInfoGlobalList();
	}

	/**
	 * Stringの値がnullと空文字であるか
	 *
	 * @param staffName
	 *            社員名
	 * @param postalCode
	 *            郵便番号
	 * @param streetAd
	 *            住所
	 * @return nullまたは空文字の場合は、trueを返す
	 */
	public boolean isNullEmpty(String workString) {
		if (null == workString || workString.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 新規登録
	 *
	 * @param staffId
	 *            社員情報
	 * @param staffName
	 *            社員名
	 * @param postalCode
	 *            郵便番号
	 * @param streetAd
	 *            住所
	 */
	public int insertStaffInfo(String staffName, String postalCode, String streetAd) {
		StaffInfo staffInfo = new StaffInfo();
		int staffId = findRegisterStaffId();
		staffInfo.setStaffId(staffId);
		staffInfo.setStaffName(staffName);
		staffInfo.setPostalCode(postalCode);
		staffInfo.setStreetAddress(streetAd);
		List<StaffInfo> staffInfoGlobalList = staffInfoBook.getStaffInfoGlobalList();
		staffInfoGlobalList.add(staffInfo);
		return staffId;
	}

	/**
	 * 登録可能な情報かを判定する (登録条件：社員名、郵便番号、住所がすべて一致する情報が存在しないこと)
	 *
	 * @param staffName
	 *            社員名
	 * @param postalCode
	 *            郵便番号
	 * @param streetAd
	 *            住所
	 * @return 登録可能ならtrue、不可ならfalse
	 */
	public boolean canInsertStaffInfo(String staffName, String postalCode, String streetAd) {
		// 郵便番号に紐づく社員情報を取得
		List<StaffInfo> staffInfoList = findStaffInfoByPostalCode(postalCode);
		// 社員名、郵便番号、住所がすべて一致する情報が存在しなければtrue
		return staffInfoList.stream().filter(staffInfo -> staffName.equals(staffInfo.getStaffName()))
				.filter(staffInfo -> postalCode.equals(staffInfo.getPostalCode()))
				.noneMatch(staffInfo -> streetAd.equals(staffInfo.getStreetAddress()));
	}

	/**
	 * 社員IDが一致する情報が存在しているかを判定する
	 *
	 * @param staffId
	 *            社員ID
	 * @return 存在している場合はtrue、していない場合はfalse
	 */
	public boolean hasStaffInfoByStaffId(int staffId) {
		return findStaffInfoByStaffId(staffId) != null;
	}

	/**
	 * 登録可能な社員IDを取得
	 *
	 * @return int 社員ID
	 */
	private int findRegisterStaffId() {
		List<StaffInfo> staffGlobalList = staffInfoBook.getStaffInfoGlobalList();
		// 1件も登録されていない場合は初番を設定する
		if (staffGlobalList.isEmpty()) {
			return Constants.STAFFINFO_FIRST_ID;
		}
		// 最大値の社員IDに1を足した番号を返す
		return (staffGlobalList.get(staffGlobalList.size() - 1).getStaffId()) + 1;
	}

	/**
	 * 1件更新（社員IDの一致する社員情報)
	 */
	public void updateStaffInfo(int staffId, String staffName, String postalCode, String streetAd) {
		StaffInfo staffInfo = findStaffInfoByStaffId(staffId);
		if (null != staffInfo) {
			staffInfo.setStaffId(staffId);
			staffInfo.setStaffName(staffName);
			staffInfo.setPostalCode(postalCode);
			staffInfo.setStreetAddress(streetAd);
		}
	}

	/**
	 * 更新可能な情報かを判定する (登録条件：社員ID、社員名、郵便番号、住所がすべて一致する情報が存在しないこと)
	 *
	 * @param staffId
	 *            社員情報
	 * @param staffName
	 *            社員名
	 * @param postalCode
	 *            郵便番号
	 * @param streetAd
	 *            住所
	 * @return 登録可能ならtrue、不可ならfalse
	 */
	public boolean canUpdateStaffInfo(int staffId, String staffName, String postalCode, String streetAd) {
		StaffInfo staffInfo = findStaffInfoByStaffId(staffId);
		// IDの一致する情報と、更新予定の情報が全く同じ場合はfalse
		if (staffName.equals(staffInfo.getStaffName()) && postalCode.equals(staffInfo.getPostalCode())
				&& streetAd.equals(staffInfo.getStreetAddress())) {
			return false;
		}
		return true;
	}

	/**
	 * 社員情報全件検索
	 *
	 * @return 社員情報全件
	 */
	public List<StaffInfo> findStaffInfoAll() {
		return staffInfoBook.getStaffInfoGlobalList();
	}

	/**
	 * 全件削除
	 */
	public void deleteStaffInfoAll() {
		List<StaffInfo> staffList = staffInfoBook.getStaffInfoGlobalList();
		staffList.removeAll(staffList);
	}

	/**
	 * 1件検索 (社員IDと一致する社員情報)
	 *
	 * @param staffId
	 *            会員ID
	 * @return 社員IDに紐づく会員情報
	 */
	public StaffInfo findStaffInfoByStaffId(int staffId) {
		List<StaffInfo> staffList = staffInfoBook.getStaffInfoGlobalList().stream()
				.filter(staffInfo -> staffId == staffInfo.getStaffId()).collect(Collectors.toList());
		if (staffList.isEmpty()) {
			return null;
		}
		return staffList.get(0);
	}

	/**
	 * 1件削除 (社員IDが一致する情報を削除)
	 *
	 * @param staffId
	 *            会員ID
	 */
	public void deleteStaffInfoByStaffId(int staffId) {
		staffInfoBook.getStaffInfoGlobalList().removeIf(staffInfo -> staffId == staffInfo.getStaffId());
	}

	/**
	 * 1件検索（郵便番号の一致情報)
	 *
	 * @param postalCode
	 *            郵便番号
	 * @return 郵便番号に紐づく会員情報
	 */
	public List<StaffInfo> findStaffInfoByPostalCode(String postalCode) {
		// 一度全件検索
		List<StaffInfo> staffList = staffInfoBook.getStaffInfoGlobalList();
		// あいまい検索
		return staffList.stream().filter(staffInfo -> staffInfo.getPostalCode().indexOf(postalCode) >= 0)
				.collect(Collectors.toList());
	}
}
