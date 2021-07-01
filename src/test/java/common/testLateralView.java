package common;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.*;
import gudusoft.gsqlparser.nodes.TLateralView;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import junit.framework.TestCase;
import java.util.ArrayList;


public class testLateralView extends TestCase {

   public void testLateralViewClause() {
        TGSqlParser tgSqlParser = new TGSqlParser(EDbVendor.dbvhive);

        tgSqlParser.sqltext = "SELECT pageid, adid FROM pageAds LATERAL VIEW explode(adid_list) adTable AS adid";

        assertTrue(tgSqlParser.parse() == 0);
        TSelectSqlStatement sqlstatements = (TSelectSqlStatement) tgSqlParser.getSqlstatements().get(0);
        TTableList tables = sqlstatements.getTables();
        ArrayList<TLateralView> lateralViewList = tables.getTable(0).getLateralViewList();
        assertTrue(lateralViewList.get(0).toString().equalsIgnoreCase("LATERAL VIEW explode(adid_list) adTable AS adid"));

        TFunctionCall tFunctionCall = lateralViewList.get(0).getUdtf();
        assertTrue(tFunctionCall.toString().equalsIgnoreCase("explode(adid_list)"));

        TAliasClause tableAlias = lateralViewList.get(0).getTableAlias();
        assertTrue(tableAlias.toString().equalsIgnoreCase("adTable"));

        TObjectNameList columnAliasList = lateralViewList.get(0).getColumnAliasList();
        TObjectName element = (TObjectName) columnAliasList.getElement(0);
        assertTrue(element.toString().equalsIgnoreCase("adid"));
    }

}
