package cn.lili.modules.item.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bcp.ordertableonlyforbean")
public class OrderBean {
    private String orderid;
    private boolean isInDB;

    private String jym, appid, ostate, astate, dsforderid, jgid, ywlxid,
            customid, description, yhid, shid, bstate, nstate, cstate,
            tradingid, jfsj, qssj, istate, iactkey, customname, customtype,
            refundingredid, refundjebyredids, ordertype, shbh, zdbh, pch, pzh,
            ckh, clr, clrq, clzy,yhkh,operid,tstate;
    private String jfje;
    private String orderid_orig, jym_orig, appid_orig, ostate_orig,
            astate_orig, dsforderid_orig, jgid_orig, ywlxid_orig,
            customid_orig, description_orig, yhid_orig, shid_orig, bstate_orig,
            nstate_orig, cstate_orig, tradingid_orig, jfsj_orig, qssj_orig,
            istate_orig, iactkey_orig, customname_orig, customtype_orig,
            refundingredid_orig, refundjebyredids_orig, ordertype_orig,
            shbh_orig, zdbh_orig, pch_orig, pzh_orig, ckh_orig, clr_orig,
            clrq_orig, clzy_orig,yhkh_orig,operid_orig,tstate_orig;
    private String jfje_orig;
    private String extension_orig,
            extensionforbank, extensionforbank_orig;
    private String extension="";
//    public void setExtension(String extension) {
//        if (extension==null){
//            this.extension="";
//        }
//        else {
//            this.extension=extension;
//        }
//    }
//    public String getExtension() {
//        return this.extension;
//    }

//    public String getExtensionforbank() {
//        return extensionforbank;
//    }

//    public void setExtensionforbank(String extensionforbank) {
//        this.extensionforbank = extensionforbank;
//    }

//    public OrderBean(String orderid)  {
//        this.orderid = orderid;
//        this.InitializeOrder();
//    }
//    private void InitializeOrder()  {
//        StringBuffer sqlBF = new StringBuffer();
//        sqlBF.setLength(0);
//        sqlBF
//                .append(" SELECT a.jym, a.appid, a.ostate, a.astate, a.dsforderid,    ");
//        sqlBF
//                .append("        a.jgid, a.ywlxid, a.jfje, a.customid, a.description, ");
//        sqlBF
//                .append("        a.yhid, a.shid, a.bstate, a.nstate, a.cstate,        ");
//        sqlBF
//                .append("        a.tradingid, a.jfsj, a.qssj, a.istate, a.iactkey,    ");
//        sqlBF
//                .append("        a.customname, a.customtype, a.extension,              "); // 增加扩展字段，表结构需要同步升级
//        sqlBF
//                .append(" a.refundingredid, a.refundjebyredids, a.ordertype, a.shbh, a.zdbh,   ");
//
//        sqlBF.append("  a.pch, a.pzh, a.ckh, a.clr, a.clrq, a.clzy ,a.yhkh  ,a.operid, a.extensionforbank ,a.tstate  ");
//
//        sqlBF
//                .append("   FROM bcp.ordertableonlyforbean a                          ");
//        sqlBF
//                .append("  WHERE a.orderid = ?                                        ");
//        sql.setSql(sqlBF.toString());
//        sql.setString(1, this.orderid);
//
//        DataStore dsorder = sql.executeQuery();
//
//        if (dsorder.rowCount() > 0) {
//            this.isInDB = true;
//            this.jym = this.handleString(dsorder.getString(0, "jym"), "");
//            this.appid = this.handleString(dsorder.getString(0, "appid"), "");
//            this.ostate = this.handleString(dsorder.getString(0, "ostate"), "");
//            this.astate = this.handleString(dsorder.getString(0, "astate"), "");
//            this.dsforderid = this.handleString(dsorder.getString(0,
//                    "dsforderid"), "");
//            this.jgid = this.handleString(dsorder.getString(0, "jgid"), "");
//            this.ywlxid = this.handleString(dsorder.getString(0, "ywlxid"), "");
//            this.jfje = this.handleString(String.valueOf(dsorder.getObject(0,
//                    "jfje")), "");
//            this.customid = this.handleString(dsorder.getString(0, "customid"),
//                    "");
//            this.description = this.handleString(dsorder.getString(0,
//                    "description"), "");
//            this.yhid = this.handleString(dsorder.getString(0, "yhid"), "");
//            this.shid = this.handleString(dsorder.getString(0, "shid"), "");
//            this.bstate = this.handleString(dsorder.getString(0, "bstate"), "");
//            this.nstate = this.handleString(dsorder.getString(0, "nstate"), "");
//            this.cstate = this.handleString(dsorder.getString(0, "cstate"), "");
//            this.tradingid = this.handleString(dsorder
//                    .getString(0, "tradingid"), "");
//            this.jfsj = this.handleString(dsorder.getString(0, "jfsj"), "");
//            this.qssj = this.handleString(dsorder.getString(0, "qssj"), "");
//            this.istate = this.handleString(dsorder.getString(0, "istate"), "");
//            this.iactkey = this.handleString(dsorder.getString(0, "iactkey"),
//                    "");
//            this.customname = this.handleString(dsorder.getString(0,
//                    "customname"), "");
//            this.customtype = this.handleString(dsorder.getString(0,
//                    "customtype"), "");
//            // 扩展字段
//            this.extension = this.handleString(dsorder
//                    .getString(0, "extension"), "");
//            this.refundingredid = this.handleString(dsorder.getString(0,
//                    "refundingredid"), "");
//            this.refundjebyredids = this.handleString(String.valueOf(dsorder
//                    .getObject(0, "refundjebyredids")), "");
//            this.ordertype = this.handleString(dsorder
//                    .getString(0, "ordertype"), "");
//            this.shbh = this.handleString(dsorder.getString(0, "shbh"), "");
//            this.zdbh = this.handleString(dsorder.getString(0, "zdbh"), "");
//            this.pch = this.handleString(dsorder.getString(0, "pch"), "");
//            this.pzh = this.handleString(dsorder.getString(0, "pzh"), "");
//            this.ckh = this.handleString(dsorder.getString(0, "ckh"), "");
//            this.clr = this.handleString(dsorder.getString(0, "clr"), "");
//            this.clrq = this.handleString(dsorder.getString(0, "clrq"), "");
//            this.clzy = this.handleString(dsorder.getString(0, "clzy"), "");
//            this.yhkh = this.handleString(dsorder.getString(0, "yhkh"), "");
//            this.operid = this.handleString(dsorder.getString(0, "operid"), "");
//            this.tstate = this.handleString(dsorder.getString(0, "tstate"), "");
//
//            this.extensionforbank = this.handleString(dsorder
//                    .getString(0, "extensionforbank"), "");
//
//        } else {
//            this.isInDB = false;
//            this.jym = "";
//            this.appid = "";
//            this.ostate = "";
//            this.astate = "";
//            this.dsforderid = "";
//            this.jgid = "";
//            this.ywlxid = "";
//            this.customid = "";
//            this.description = "";
//
//            this.yhid = "";
//            this.shid = "";
//            this.bstate = "";
//            this.nstate = "";
//            this.cstate = "";
//            this.tradingid = "";
//            this.jfsj = "";
//            this.qssj = "";
//            this.istate = "";
//            this.iactkey = "";
//
//            this.jfje = "0";
//            this.customname = "";
//            this.customtype = "";
//            // 扩展字段
//            this.extension = "";
//            this.refundingredid = "";
//            this.refundjebyredids = "0";
//            this.ordertype = "";
//            this.shbh = "";
//            this.zdbh = "";
//            this.pch = "";
//            this.pzh = "";
//            this.ckh = "";
//            this.clr = "";
//            this.clrq = "";
//            this.clzy = "";
//            this.yhkh = "";
//            this.operid = "";
//            this.tstate = "";
//            this.extensionforbank = "";
//        }
//        this.copyOrderValueToOrig();
//    }
}
