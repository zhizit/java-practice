package com.staff.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.staff.Constants;
import com.staff.dto.StaffInfo;
import com.staff.service.StaffInfoService;

/**
 * 本クラスは社員情報の登録、検索、更新、削除を管理するクラス
 *
 * @author zhizit
 *
 */
@Path("staffers")
@RequestScoped
public class StaffInfoController  {

	@Inject
	StaffInfoService service;

	/**
	 * 検索 QueryParamなし→全件検索、郵便番号指定あり→郵便番号検索
	 *
	 * @param postalCode
	 *            郵便番号
	 * @return Response ステータスコードと検索結果
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findStaffInfo(@QueryParam("post") String postalCode) {
		List<StaffInfo> staffInfoList = new ArrayList<StaffInfo>();
		// 任意の郵便番号が入力されているか
		if (service.isNullEmpty(postalCode)) {
			// 全件検索
			staffInfoList = service.findStaffInfoAll();
			if (staffInfoList.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		} else {
			// 郵便番号検索
			staffInfoList = service.findStaffInfoByPostalCode(postalCode);
			if (staffInfoList.isEmpty()) {
				return Response.status(Response.Status.NO_CONTENT).build();
			}
		}
		// 検索結果を返す
		return Response.status(Response.Status.OK).entity(staffInfoList).build();
	}

	/**
	 * 社員情報を全件削除
	 *
	 * @return Response ステータスコード
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteStaffInfoList() {
		service.deleteStaffInfoAll();
		return Response.status(Response.Status.OK).build();
	}

	/**
	 * 社員情報を1件 登録
	 *
	 * 社員名、郵便番号、住所が全く同じ情報の場合は登録を行わない
	 *
	 * @param staffName
	 *            社員名
	 * @param postalCode
	 *            郵便番号
	 * @param streetAd
	 *            住所
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertStaffInfo(@Valid StaffInfo staffInfo) {
		String staffName = staffInfo.getStaffName();
		String postalCode = staffInfo.getPostalCode();
		String streetAd = staffInfo.getStreetAddress();

		// 登録可能な情報かチェック
		if (!service.canInsertStaffInfo(staffName, postalCode, streetAd)) {
			return Response.status(Response.Status.CONFLICT).entity(Constants.DUPLICATION_ERR_MESSAGE).build();
		}
		// 登録後、採番された社員IDを取得
		int staffId = service.insertStaffInfo(staffName, postalCode, streetAd);
		// 登録情報とステータスコードを返す
		return Response.status(Response.Status.CREATED).entity(service.findStaffInfoByStaffId(staffId)).build();
	}

	/**
	 * 社員情報を1件 更新
	 *
	 * @param staffId
	 *            社員ID
	 * @param staffName
	 *            社員名
	 * @param postalCode
	 *            郵便番号
	 * @param streetAd
	 *            住所
	 * @return Response ステータスコードとエンティティ
	 */
	@PUT
	@Path("/{staffId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStaffInfo(
			@PathParam("staffId") @Pattern(regexp = Constants.NUMERIC_REGEX, message = Constants.NUMERIC_VALID_MESSAGE) String staffIdWord,
			@Valid StaffInfo staffInfo) {
		int staffId = Integer.parseInt(staffIdWord);
		String staffName = staffInfo.getStaffName();
		String postalCode = staffInfo.getPostalCode();
		String streetAd = staffInfo.getStreetAddress();
		// 社員IDが存在しているかチェック
		if (!service.hasStaffInfoByStaffId(staffId)) {
			return Response.status(Response.Status.BAD_REQUEST).entity(Constants.NODATA_MESSAGE).build();
		}
		// 更新可能な情報かをチェック
		if (!service.canUpdateStaffInfo(staffId, staffName, postalCode, streetAd)) {
			return Response.status(Response.Status.CONFLICT).entity(Constants.DUPLICATION_ERR_MESSAGE).build();
		}
		// 更新
		service.updateStaffInfo(staffId, staffName, postalCode, streetAd);
		return Response.status(Response.Status.OK).entity(service.findStaffInfoByStaffId(staffId)).build();
	}

	/**
	 * 1件検索（社員IDと一致する社員情報）
	 *
	 * @param staffId
	 *            社員ID
	 * @return Response ステータスコードとエンティティ
	 */
	@GET
	@Path("/{staffId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findStaffInfoByStaffId(
			@PathParam("staffId") @Pattern(regexp = Constants.NUMERIC_REGEX, message = Constants.NUMERIC_VALID_MESSAGE) String staffIdWords) {
		int staffId = Integer.parseInt(staffIdWords);
		// 社員IDが存在しているかチェック
		if (!service.hasStaffInfoByStaffId(staffId)) {
			return Response.status(Response.Status.BAD_REQUEST).entity(Constants.NODATA_MESSAGE).build();
		}
		// 検索
		StaffInfo staffInfo = service.findStaffInfoByStaffId(staffId);
		return Response.status(Response.Status.OK).entity(staffInfo).build();
	}

	/**
	 * 1件削除（社員IDと一致する社員情報）
	 *
	 * @param staffId
	 *            会員ID
	 * @return Response ステータスコードとエンティティ
	 */
	@DELETE
	@Path("/{staffId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteStaffInfoByStaffId(
			@PathParam("staffId") @Pattern(regexp = Constants.NUMERIC_REGEX, message = Constants.NUMERIC_VALID_MESSAGE) String staffIdWords) {
		int staffId = Integer.parseInt(staffIdWords);
		// 社員IDが存在しているか確認
		if (service.hasStaffInfoByStaffId(staffId)) {
			// 1件削除
			service.deleteStaffInfoByStaffId(staffId);
		}
		return Response.status(Response.Status.OK).build();
	}
}