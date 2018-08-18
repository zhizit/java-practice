package com.staff.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.staff.Constants;
import com.staff.dto.StaffInfo;

public class StaffInfoServiceTest {
	StaffInfoService service;
	private int firstStaffId = Constants.STAFFINFO_FIRST_ID;

	@Test
	public void 登録が正しく行われていること() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		assertThat("登録されている情報が0件であること", staffInfoList.size(), is(0));
		// 登録
		service.insertStaffInfo("い", "う", "え");
		service.insertStaffInfo("き", "く", "け");
		List<StaffInfo> resultList = service.staffInfoBook.getStaffInfoGlobalList();
		assertThat("2件の登録ができていること", resultList.size(), is(2));
		{
			StaffInfo staffInfo = resultList.get(0);
			assertThat("社員IDが初番であること", staffInfo.getStaffId(), is(firstStaffId));
			assertThat("社員名がいであること", staffInfo.getStaffName(), is("い"));
			assertThat("郵便番号がうであること", staffInfo.getPostalCode(), is("う"));
			assertThat("住所がえであること", staffInfo.getStreetAddress(), is("え"));
		}
		{
			StaffInfo staffInfo = resultList.get(1);
			assertThat("社員IDが初番の次であること", staffInfo.getStaffId(), is(firstStaffId + 1));
			assertThat("社員名がいであること", staffInfo.getStaffName(), is("き"));
			assertThat("郵便番号がうであること", staffInfo.getPostalCode(), is("く"));
			assertThat("住所がえであること", staffInfo.getStreetAddress(), is("け"));
		}
	}

	@Test
	public void 登録可能な情報かが正しく判別されること() {
		service = new StaffInfoService();
		assertTrue("すべて一致する情報が存在しない場合trueであること", service.canInsertStaffInfo("あ", "い", "う"));
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.add(new StaffInfo(firstStaffId, "社員名", "郵便番号", "住所"));
		assertTrue("社員名が一致していても、郵便番号と住所が一致していなければtrueであること", service.canInsertStaffInfo("社員名", "郵便番号_不一致", "住所_不一致"));
		assertTrue("社員名と郵便番号が一致していても、住所が一致していなければtrueであること", service.canInsertStaffInfo("社員名", "郵便番号", "住所_不一致"));
		assertTrue("社員名と住所が一致していても、郵便番号が一致していなければtrueであること", service.canInsertStaffInfo("社員名", "郵便番号_不一致", "住所"));
		assertTrue("郵便と住所が一致していても、社員名が一致していなければtrueであること", service.canInsertStaffInfo("社員名_不一致", "郵便番号", "住所"));

		assertFalse("社員名と郵便番号と住所が一致している場合はfalseであること", service.canInsertStaffInfo("社員名", "郵便番号", "住所"));
	}

	@Test
	public void 社員IDが存在しているかかが正しく判別されること() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		assertFalse("一致する社員IDが存在しない場合falseとなること", service.hasStaffInfoByStaffId(firstStaffId));
		staffInfoList.add(new StaffInfo(firstStaffId, "い", "う", "え"));
		assertTrue("一致する社員IDが存在する場合trueであること", service.hasStaffInfoByStaffId(firstStaffId));

	}

	@Test
	public void 更新が正しく行われていること() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.add(new StaffInfo(firstStaffId, "い", "う", "え"));
		staffInfoList.add(new StaffInfo(firstStaffId + 1, "き", "く", "け"));
		// 登録の確認
		List<StaffInfo> resultList = service.staffInfoBook.getStaffInfoGlobalList();
		{
			StaffInfo staffInfo = resultList.get(0);
			assertThat("社員IDがあであること", staffInfo.getStaffId(), is(firstStaffId));
			assertThat("社員名がいであること", staffInfo.getStaffName(), is("い"));
			assertThat("郵便番号がうであること", staffInfo.getPostalCode(), is("う"));
			assertThat("住所がえであること", staffInfo.getStreetAddress(), is("え"));
		}
		{
			StaffInfo staffInfo = resultList.get(1);
			assertThat("社員IDがかであること", staffInfo.getStaffId(), is(firstStaffId + 1));
			assertThat("社員名がきであること", staffInfo.getStaffName(), is("き"));
			assertThat("郵便番号がくであること", staffInfo.getPostalCode(), is("く"));
			assertThat("住所がけであること", staffInfo.getStreetAddress(), is("け"));
		}
		assertThat("2件登録されていること", resultList.size(), is(2));
		// 更新
		service.updateStaffInfo(firstStaffId + 1, "み", "む", "め");
		// 更新の確認
		List<StaffInfo> updateResultList = service.getStaffInfoGlobalList();
		{
			StaffInfo staffInfo = resultList.get(0);
			assertThat("社員IDがあであること", staffInfo.getStaffId(), is(firstStaffId));
			assertThat("社員名がいであること", staffInfo.getStaffName(), is("い"));
			assertThat("郵便番号がうであること", staffInfo.getPostalCode(), is("う"));
			assertThat("住所がえであること", staffInfo.getStreetAddress(), is("え"));
		}
		{
			StaffInfo staffInfo = updateResultList.get(1);
			assertThat("社員IDがかであること", staffInfo.getStaffId(), is(firstStaffId + 1));
			assertThat("社員名がみであること", staffInfo.getStaffName(), is("み"));
			assertThat("郵便番号がむであること", staffInfo.getPostalCode(), is("む"));
			assertThat("住所がめであること", staffInfo.getStreetAddress(), is("め"));
		}
		assertThat("更新後に件数が変動せず、2件であること", updateResultList.size(), is(2));
	}

	@Test
	public void 全件検索が正しく行われていること() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.add(new StaffInfo(firstStaffId, "い", "う", "え"));
		staffInfoList.add(new StaffInfo(firstStaffId + 1, "き", "く", "け"));
		// 全件検索
		List<StaffInfo> resultList = service.findStaffInfoAll();
		{
			StaffInfo staffInfo = resultList.get(0);
			assertThat("社員IDがあであること", staffInfo.getStaffId(), is(firstStaffId));
			assertThat("社員名がいであること", staffInfo.getStaffName(), is("い"));
			assertThat("郵便番号がうであること", staffInfo.getPostalCode(), is("う"));
			assertThat("住所がえであること", staffInfo.getStreetAddress(), is("え"));
		}
		{
			StaffInfo staffInfo = resultList.get(1);
			assertThat("社員IDがかであること", staffInfo.getStaffId(), is(firstStaffId + 1));
			assertThat("社員名がきであること", staffInfo.getStaffName(), is("き"));
			assertThat("郵便番号がくであること", staffInfo.getPostalCode(), is("く"));
			assertThat("住所がけであること", staffInfo.getStreetAddress(), is("け"));
		}
		assertThat("検索結果が2件であること", resultList.size(), is(2));
	}

	@Test
	public void 社員情報が存在しない場合は全件検索の結果が取得されないこと() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.removeAll(staffInfoList);
		List<StaffInfo> resultList = service.findStaffInfoAll();
		assertTrue(resultList.isEmpty());
	}

	@Test
	public void 全件削除が正しく行われていること() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.add(new StaffInfo(firstStaffId, "い", "う", "え"));
		staffInfoList.add(new StaffInfo(firstStaffId + 1, "き", "く", "け"));
		List<StaffInfo> resultList = service.findStaffInfoAll();
		assertThat("2件登録されていること", resultList.size(), is(2));
		service.deleteStaffInfoAll();
		resultList = service.findStaffInfoAll();
		assertTrue(resultList.isEmpty());
		assertThat("登録された2件が全件削除されていること", resultList.size(), is(0));
	}

	@Test
	public void 社員IＤが一致する社員情報1件の検索が正しく行われていること() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.add(new StaffInfo(firstStaffId, "い", "う", "え"));
		staffInfoList.add(new StaffInfo(firstStaffId + 1, "き", "く", "け"));
		// 検索
		StaffInfo staffInfo = service.findStaffInfoByStaffId(firstStaffId + 1);
		assertThat("社員IDがかであること", staffInfo.getStaffId(), is(firstStaffId + 1));
		assertThat("社員名がきであること", staffInfo.getStaffName(), is("き"));
		assertThat("郵便番号がくであること", staffInfo.getPostalCode(), is("く"));
		assertThat("住所がけであること", staffInfo.getStreetAddress(), is("け"));
	}

	@Test
	public void 社員情報が存在しない場合は社員IDと一致する検索結果が取得されないこと() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.removeAll(staffInfoList);
		StaffInfo staffInfo = service.findStaffInfoByStaffId(firstStaffId + 1);
		assertNull(staffInfo);
	}

	@Test
	public void 社員IDが一致する社員情報の削除が正しく行われていること() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.add(new StaffInfo(firstStaffId, "い", "う", "え"));
		staffInfoList.add(new StaffInfo(firstStaffId + 1, "き", "く", "け"));
		assertNotNull("ユーザーID[か]が存在していること", service.findStaffInfoByStaffId(firstStaffId));
		assertNotNull("ユーザーID[か]が存在していること", service.findStaffInfoByStaffId(firstStaffId + 1));
		// 削除
		service.deleteStaffInfoByStaffId(firstStaffId);
		assertNull("ユーザーID[あ]が存在していないこと", service.findStaffInfoByStaffId(firstStaffId));
		assertNotNull("ユーザーID[か]が存在していること", service.findStaffInfoByStaffId(firstStaffId + 1));
	}

	@Test
	public void 社員IDが一致しない場合削除が行われないこと() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.add(new StaffInfo(firstStaffId, "い", "う", "え"));
		staffInfoList.add(new StaffInfo(firstStaffId + 1, "き", "く", "け"));
		assertNotNull("ユーザーID[か]が存在していること", service.findStaffInfoByStaffId(firstStaffId));
		assertNotNull("ユーザーID[か]が存在していること", service.findStaffInfoByStaffId(firstStaffId + 1));
		// 削除
		service.deleteStaffInfoByStaffId(2222);
		assertNotNull("ユーザーID[か]が存在していること", service.findStaffInfoByStaffId(firstStaffId));
		assertNotNull("ユーザーID[か]が存在していること", service.findStaffInfoByStaffId(firstStaffId + 1));
	}

	@Test
	public void 郵便番号が一致する社員情報の検索ができていること() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		String target = "ね";
		staffInfoList.add(new StaffInfo(firstStaffId, "い", "う", "え"));
		staffInfoList.add(new StaffInfo(firstStaffId + 1, "き", target, "け"));
		staffInfoList.add(new StaffInfo(firstStaffId + 1 + 1, "し", "す", "せ"));
		staffInfoList.add(new StaffInfo(firstStaffId + 1 + 2, "ち", target, "て"));
		assertThat("4件登録されていること", staffInfoList.size(), is(4));
		// 検索
		List<StaffInfo> resultList = service.findStaffInfoByPostalCode(target);
		{
			StaffInfo staffInfo = resultList.get(0);
			assertThat("社員IDがあであること", staffInfo.getStaffId(), is(firstStaffId + 1));
			assertThat("社員名がいであること", staffInfo.getStaffName(), is("き"));
			assertThat("郵便番号がうであること", staffInfo.getPostalCode(), is(target));
			assertThat("住所がえであること", staffInfo.getStreetAddress(), is("け"));
		}
		{
			StaffInfo staffInfo = resultList.get(1);
			assertThat("社員IDがたであること", staffInfo.getStaffId(), is(firstStaffId + 1 + 2));
			assertThat("社員名がちであること", staffInfo.getStaffName(), is("ち"));
			assertThat("郵便番号がねであること", staffInfo.getPostalCode(), is(target));
			assertThat("住所がてであること", staffInfo.getStreetAddress(), is("て"));
		}
		assertThat("検索結果が2件であること", resultList.size(), is(2));
	}

	@Test
	public void 郵便番号のあいまい検索ができること() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.add(new StaffInfo(firstStaffId, "い", "1905678", "え"));
		staffInfoList.add(new StaffInfo(firstStaffId + 1, "き", "1913333", "け"));
		staffInfoList.add(new StaffInfo(firstStaffId + 2, "し", "1905678", "せ"));
		staffInfoList.add(new StaffInfo(firstStaffId + 3, "ち", "1924321", "て"));
		assertThat("4件登録されていること", staffInfoList.size(), is(4));
		// 検索
		List<StaffInfo> resultList = service.findStaffInfoByPostalCode("190");
		{
			StaffInfo staffInfo = resultList.get(0);
			assertThat("前方一致の値が取得されていること", staffInfo.getPostalCode(), is("1905678"));
		}
		{
			StaffInfo staffInfo = resultList.get(1);
			assertThat("前方一致の値が取得されていること", staffInfo.getPostalCode(), is("1905678"));
		}
		assertThat("検索結果が2件であること", resultList.size(), is(2));
	}

	@Test
	public void 社員情報が存在しない場合は郵便番号と一致する検索結果が取得されないこと() {
		service = new StaffInfoService();
		List<StaffInfo> staffInfoList = service.getStaffInfoGlobalList();
		staffInfoList.removeAll(staffInfoList);
		List<StaffInfo> resultList = service.findStaffInfoByPostalCode("あ");
		assertTrue(resultList.isEmpty());
	}
}
