package com.cbs.center_service.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TellerService {

  @Autowired
  private DataSourceManager dataSourceManager;

  public List<Map<String,Object>> getTellers(){
    List<Map<String ,Object>> lists = new ArrayList<>();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      conn = dataSourceManager.getCoreConnection();
      stmt = conn.prepareStatement("WITH ROLE_DETAIL AS (SELECT\n" +
          "                       ASS_ID,\n" +
          "                       ATH_TYP,\n" +
          "                       ROLE_CNT,\n" +
          "                       TRIM(TRAILING ',' FROM\n" +
          "                            REPLACE(\n" +
          "                                REGEXP_REPLACE(CAST(ROLE_TXT AS CHAR), '[^A-Z0]', ''),\n" +
          "                                'Y0',\n" +
          "                                ','\n" +
          "                            )\n" +
          "                       ) AS ROLE_CODES\n" +
          "                     FROM core001.bptgrpl),\n" +
          "     ROLE_PIVOT AS (SELECT\n" +
          "                      ASS_ID,\n" +
          "                      MAX(CASE WHEN ATH_TYP = '0' THEN ROLE_CNT END)   AS Operation_CNT,\n" +
          "                      MAX(CASE WHEN ATH_TYP = '0' THEN ROLE_CODES END) AS Operation_Roles,\n" +
          "                      MAX(CASE WHEN ATH_TYP = '1' THEN ROLE_CNT END)   AS Authorization_CNT,\n" +
          "                      MAX(CASE WHEN ATH_TYP = '1' THEN ROLE_CODES END) AS Authorization_Roles\n" +
          "                    FROM ROLE_DETAIL\n" +
          "                    GROUP BY ASS_ID),\n" +
          "     TELLER_INFO AS (SELECT\n" +
          "                       tlr                       AS UserID,\n" +
          "                       tlr_br                    AS Branch,\n" +
          "                       tlr_cn_nm                 AS UserName,\n" +
          "                       tlr_lvl                   AS UserLevel,\n" +
          "                       TX_LVL                    AS TransactionLevel,\n" +
          "                       ATH_LVL                   AS AuthorizationLevel,\n" +
          "                       TRM_TYP                   AS TerminalType,\n" +
          "                       tlr_typ                   AS UserType,\n" +
          "                       dpt_no                    AS Department,\n" +
          "                       CASE ATH_RGN\n" +
          "                         WHEN 0 THEN 'Current Acct.Centre'\n" +
          "                         WHEN 1 THEN 'The same and next lower level'\n" +
          "                         WHEN 2 THEN 'The same and all lower level'\n" +
          "                         END                     AS AuthorizationRegion,\n" +
          "                       AA_CODE                   AS AmountAuthorizationCode,\n" +
          "                       TELE                      AS Telephone,\n" +
          "                       PST_ADDRESS               AS Email,\n" +
          "                       TRIM(TRAILING ',' FROM CONCAT(\n" +
          "                           IF(SUBSTRING(TLR_STSW, 1, 1) = '1', 'Voucher Box,', ''),\n" +
          "                           IF(SUBSTRING(TLR_STSW, 2, 1) = '1', 'Voucher Vault,', ''),\n" +
          "                           IF(SUBSTRING(TLR_STSW, 3, 1) = '1', 'Cash Box,', ''),\n" +
          "                           IF(SUBSTRING(TLR_STSW, 4, 1) = '1', 'Cash Vault,', ''),\n" +
          "                           IF(SUBSTRING(TLR_STSW, 5, 1) = '1', 'Authority User,', ''),\n" +
          "                           IF(SUBSTRING(TLR_STSW, 6, 1) = '1', 'Account User,', ''),\n" +
          "                           IF(SUBSTRING(TLR_STSW, 7, 1) = '1', 'Manager User,', ''),\n" +
          "                           IF(SUBSTRING(TLR_STSW, 8, 1) = '1', 'ATM User,', ''),\n" +
          "                           IF(SUBSTRING(TLR_STSW, 9, 1) = '1', 'Electronic User,', ''),\n" +
          "                           IF(SUBSTRING(TLR_STSW, 10, 1) = '1', 'Bulk User,', '')\n" +
          "                                              )) AS ActiveStatus\n" +
          "                     FROM bpttlt\n" +
          "                     where TLR_TYP != 'S')\n" +
          "SELECT\n" +
          "  b.UserID,\n" +
          "  b.Branch,\n" +
          "  b.UserName,\n" +
          "  b.UserLevel,\n" +
          "  b.TransactionLevel,\n" +
          "  b.AuthorizationLevel,\n" +
          "  b.TerminalType,\n" +
          "  b.UserType,\n" +
          "  b.Department,\n" +
          "  b.AuthorizationRegion,\n" +
          "  b.AmountAuthorizationCode,\n" +
          "  b.Telephone,\n" +
          "  b.Email,\n" +
          "  b.ActiveStatus,\n" +
          "  a.Operation_CNT,\n" +
          "  a.Operation_Roles,\n" +
          "  a.Authorization_CNT,\n" +
          "  a.Authorization_Roles\n" +
          "FROM ROLE_PIVOT a\n" +
          "       RIGHT JOIN TELLER_INFO b ON a.ASS_ID = b.UserID");
      rs = stmt.executeQuery();
      while (rs.next()){
        Map<String ,Object> row = new HashMap<>();
        row.put("UserID",rs.getString("UserID"));
        row.put("Branch",rs.getString("Branch"));
        row.put("UserName",rs.getString("UserName"));
        row.put("UserLevel",rs.getString("UserLevel"));
        row.put("TransactionLevel",rs.getString("TransactionLevel"));
        row.put("AuthorizationLevel",rs.getString("AuthorizationLevel"));
        row.put("TerminalType",rs.getString("TerminalType"));
        row.put("UserType",rs.getString("UserType"));
        row.put("Department",rs.getString("Department"));
        row.put("AuthorizationRegion",rs.getString("AuthorizationRegion"));
        row.put("AmountAuthorizationCode",rs.getString("AmountAuthorizationCode"));
        row.put("Telephone",rs.getString("Telephone"));
        row.put("Email",rs.getString("Email"));
        row.put("ActiveStatus",rs.getString("ActiveStatus"));
        row.put("Operation_CNT",rs.getString("Operation_CNT"));
        row.put("Operation_Roles",rs.getString("Operation_Roles"));
        row.put("Authorization_CNT",rs.getString("Authorization_CNT"));
        row.put("Authorization_Roles",rs.getString("Authorization_Roles"));
        lists.add(row);
      }
      return lists;
    }catch (SQLException ex){
    log.error("Error getting tellers: {}", ex.getMessage());
    }finally {
      dataSourceManager.closeResource(conn,stmt,null,rs);
    }
    return lists;
  }
}
