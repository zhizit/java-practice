package com.staff.dto;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

/**
 * 本クラスは社員情報をリストとして管理するクラス
 *
 * @author zhizit
 *
 */
@ApplicationScoped
public class StaffInfoBook {

	// DBの代わりとなる社員情報リスト
	private List<StaffInfo> staffInfoGlobalList;

	public StaffInfoBook() {
		if (null == staffInfoGlobalList) {
			staffInfoGlobalList = new ArrayList<StaffInfo>();
		}
	}

	public List<StaffInfo> getStaffInfoGlobalList() {
		return staffInfoGlobalList;
	}
}
