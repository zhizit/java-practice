package com.staff.dto;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

import com.staff.Constants;

/**
 * 本クラスは会員情報のクラス
 *
 * @author zhizit
 */
@XmlRootElement
public class StaffInfo {

	// JSON用：引数なしコンストラクタ
	public StaffInfo() {
	}

	// JSON出力用：引数ありコンストラクタ
	public StaffInfo(int staffId, String staffName, String postalCode, String streetAddress) {
		this.staffId = staffId;
		this.staffName = staffName;
		this.postalCode = postalCode;
		this.streetAddress = streetAddress;
	}

	// 社員ID
	private int staffId;
	// 社員名
	@NotNull(message=Constants.NOTNULL_MESSAGE)
	@FormParam("staffName")
	private String staffName;
	// 郵便番号
	@NotNull(message=Constants.NOTNULL_MESSAGE)
	@FormParam("postalCode")
	private String postalCode;
	// 住所
	@NotNull(message=Constants.NOTNULL_MESSAGE)
	@FormParam("streetAddress")
	private String streetAddress;

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postCode) {
		this.postalCode = postCode;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
}
