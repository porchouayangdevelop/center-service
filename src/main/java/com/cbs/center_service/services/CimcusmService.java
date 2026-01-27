package com.cbs.center_service.services;

import com.cbs.center_service.models.AccountList;
import com.cbs.center_service.models.CardInfo;
import com.cbs.center_service.models.Cimcusm;
import com.cbs.center_service.utils.LoadEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CimcusmService {


  private final DataSourceManager dbManager;

  @Autowired
  public CimcusmService(DataSourceManager dbManager) {
    this.dbManager = dbManager;
  }

  @Autowired
  private LoadEnvironment environment;


  public CardInfo getCustomers(String cif, String cardType) {
    List<CardInfo> cardInfoList = new ArrayList<>();
    CardInfo card = new CardInfo();

    List<AccountList> accountList = new ArrayList<>();

    Connection conn = null;
    CallableStatement callStmt = null;
    ResultSet rs = null;

    try {

      if (environment.getStrEnvironment("spring.profiles.active").equals("prod")) {
        conn = dbManager.getProdCoreExecuteConnection();
        log.info("Connected to PROD database for fetching customers");
      } else {
        conn = dbManager.getCoreConnection();
        log.info("Connected to UAT database for fetching customers");
      }

      callStmt = conn.prepareCall("{proc_debit_credit_master(?,?)}");
      callStmt.setString(1, cif);
      callStmt.setString(2, cardType);

      rs = callStmt.executeQuery();
      while (rs.next()) {

        //set data to card info
        card.setCiNo(rs.getString("ci_no"));
        card.setCiBranch(rs.getString("ci_branch"));
        card.setCustomerEngName(rs.getString("customer_engname"));
        card.setCustomerTitle(rs.getString("customer_title"));
        card.setCustomerGender(rs.getString("customer_gender"));
        card.setCustomerIdType(rs.getString("customer_id_type"));
        card.setDateBirth(rs.getString("date_birth"));
        card.setCustomerNationalityCode(rs.getString("customer_nationality_code"));
        card.setAddress1(rs.getString("address1"));
        card.setAddress2(rs.getString("address2"));
        card.setAddress3(rs.getString("address3"));
        card.setAddress4(rs.getString("address4"));
        card.setVillageCode(rs.getString("village_code"));
        card.setVillageName(rs.getString("v_eng_name"));
        card.setCityCode(rs.getString("city_code"));
        card.setCityName(rs.getString("d_eng_name"));
        card.setProvinceCode(rs.getString("province_code"));
        card.setProvinceName(rs.getString("p_eng_name"));
        card.setTelNo1(rs.getString("tel_no"));
        card.setTelNo2(rs.getString("tel_no2"));
        card.setEmail(rs.getString("email"));
        card.setFaxNo(rs.getString("fax_no"));

        //set data to account list
        AccountList account = new AccountList();
        account.setAcct(rs.getString("acct"));
        account.setAcctBranchCode(rs.getString("acct_branchcode"));
        account.setProdCode(rs.getString("prod_code"));
        account.setAcctType(rs.getString("acct_type"));
        account.setLocalName(rs.getString("l_acname"));
        account.setEngName(rs.getString("e_acname"));
        account.setCcy(rs.getString("ccy"));
        account.setAcStatus(rs.getString("ac_sts"));

        accountList.add(account);
      }

      card.setAccountList(accountList);
      cardInfoList.add(card);

      return card;
    } catch (SQLException ex) {
      log.error("ERROR getting customers: {}", ex.getMessage());
    } finally {
      dbManager.closeResource(conn, null, callStmt, rs);
    }
    return card;
  }

  public List<Map<String, Object>> fetchingCimcusm(String cif, String cardType) {
    List<Map<String, Object>> responseList = new ArrayList<>();
    Map<String, Object> responseMap = new HashMap<>();
    List<Cimcusm> cimcusmList = new ArrayList<>();

    String caseType = "";

    if(!"D".equals(cardType) && !"C".equals(cardType)) {
      responseMap.put("error", "Invalid card type. Must be 'D' for Debit or 'C' for Credit.");
      responseList.add(responseMap);
      return responseList;
    }

    caseType = "D".equals(caseType) ? "DEBIT" : "CREDIT";
    log.info("Processing CIF: {} with Card Type: {}", cif, caseType);

    if(cif.length() !=12 && cif.length() !=16) {
      responseMap.put("error", "CIF length is invalid. Must be either 12 or 16 characters.");
      responseList.add(responseMap);
      return responseList;
    }

    Cimcusm cimcusm = processCifInfo(cif,cardType, responseMap, responseList);
    if(cimcusm != null) {
      cimcusmList.add(cimcusm);
      responseMap.put("data", cimcusmList);
      responseList.add(responseMap);
      return responseList;
    }
    return responseList;
  }

  private Cimcusm processCifInfo(String cif, String cardType, Map<String, Object> responseMap, List<Map<String, Object>> responseList) {
    String actuallyCif = "";
    String actuallyLeftCif = "";

    if (cif.length() == 16) {
      actuallyLeftCif = StringUtils.left(cif, 4);
      actuallyCif = StringUtils.right(cif, 12);
    }

    CardInfo cardInfo = getCustomers(actuallyCif, cardType.toUpperCase());

    Cimcusm cimcusm = new Cimcusm();

    if (cif.length() == 16 && cif.startsWith("0000")) {
      cimcusm.setCif(actuallyLeftCif + actuallyCif);
    } else if (cif.length() != 16 && cif.startsWith("0000")) {
      responseMap.put("error", "CIF length is invalid or does not match the expected format.");
      responseList.add(responseMap);
//      return responseList;
    } else {
      cimcusm.setCif(cardInfo.getCiNo());
    }

    if (!populateCustomerInfo(cimcusm, cardInfo)) {
      responseMap.put("error", "No customer data found for the provided CIF.");
      responseList.add(responseMap);
//      return responseList;
    }

    if (!populateAccountInfo(cimcusm, cardInfo)) {
      responseMap.put("error", "No account data found for the provided CIF.");
      responseList.add(responseMap);
//      return responseList;
    }

    return cimcusm;
  }

  private boolean populateCustomerInfo(Cimcusm cimcusm, CardInfo cardInfo) {
    if (cardInfo.getCustomerEngName().equals("") || cardInfo.getCustomerEngName() == null) {
      log.error("Customer name is empty for CIF: {}", cardInfo.getCiNo());
      return false;
    }
    cimcusm.setCustomerName(cardInfo.getCustomerEngName());

    if (cardInfo.getVillageCode().equals("") || cardInfo.getVillageCode() == null) {
      log.error("Village code is empty for CIF: {}", cardInfo.getCiNo());
      return false;
    } else if (cardInfo.getCityCode().equals("") || cardInfo.getCityCode() == null) {
      log.error("City code is empty for CIF: {}", cardInfo.getCiNo());
      return false;
    } else if (cardInfo.getProvinceCode().equals("") || cardInfo.getProvinceCode() == null) {
      log.error("Province code is empty for CIF: {}", cardInfo.getCiNo());
      return false;
    }

    cimcusm.setAddress1(String.format("%s, %s, %s",
        cardInfo.getVillageName(),
        cardInfo.getCityName(),
        cardInfo.getProvinceName())
    );

    if(cardInfo.getTelNo1().equals("") || cardInfo.getTelNo1() == null) {
      log.error("Telephone number is empty for CIF: {}", cardInfo.getCiNo());
      return false;
    }
    cimcusm.setTellNo(cardInfo.getTelNo1());
    cimcusm.setCountAcct(cardInfo.getAccountList().size());
    cimcusm.setBirthOfDate(cardInfo.getDateBirth());
    cimcusm.setCustomerGender(cardInfo.getCustomerGender());
    cimcusm.setCustomerIDType(cardInfo.getCustomerIdType());
    cimcusm.setBranch(cardInfo.getCiBranch());
    cimcusm.setAddress2(cardInfo.getAddress2());
    cimcusm.setFaxNo(cardInfo.getFaxNo());
    cimcusm.setEmail(cardInfo.getEmail());
    cimcusm.setHandPhone(cardInfo.getTelNo1());
    cimcusm.setNationalityCode(cardInfo.getCustomerNationalityCode());
    cimcusm.setTellNo2(cimcusm.getTellNo2());

    return true;
  }

  private boolean populateAccountInfo(Cimcusm cimcusm, CardInfo cardInfo) {

    StringBuilder branch = new StringBuilder();
    StringBuilder acNo = new StringBuilder();
    StringBuilder acType = new StringBuilder();
    StringBuilder currencyCode = new StringBuilder();
    String acctName = "";
    String status = "";

    for (AccountList account : cardInfo.getAccountList()) {
      branch.append(account.getAcctBranchCode()).append(";");
      acNo.append(account.getAcct()).append(";");
      acType.append(account.getAcctType()).append(";");
      currencyCode.append(account.getCcy()).append(";");

      if (account.getEngName().equals("")) {
        log.error("Account name is empty for account: {}", account.getAcct());
        return false;
      }
      acctName = account.getEngName();
      status = account.getAcStatus();

    }
    return true;
  }


}
