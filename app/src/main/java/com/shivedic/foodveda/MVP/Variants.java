package com.shivedic.foodveda.MVP;

/**
 * Created by AbhiAndroid
 */

public class Variants {
    public String getVariantname() {
        return variantname;
    }

    public void setVariantname(String variantname) {
        this.variantname = variantname;
    }

    public String getVarprice() {
        return varprice;
    }

    public void setVarprice(String varprice) {
        this.varprice = varprice;
    }

    public String getVarientid() {
        return varientid;
    }

    public void setVarientid(String varientid) {
        this.varientid = varientid;
    }

    private String varientid;
    private String variantname;
    private String varprice;

    public Variants(String variantid, String variantname, String varprice){
        this.varientid = variantid;
        this.variantname = variantname;
        this.varprice = varprice;
    }

    public Variants(String variantid, String variantname, String varprice, String varquantity){
        this.varientid = variantid;
        this.variantname = variantname;
        this.varprice = varprice;
        this.varquantity = varquantity;
    }

    public String getVarquantity() {
        return varquantity;
    }

    public void setVarquantity(String varquantity) {
        this.varquantity = varquantity;
    }

    private String varquantity;
}