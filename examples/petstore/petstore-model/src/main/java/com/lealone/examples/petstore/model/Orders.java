package com.lealone.examples.petstore.model;

import com.lealone.orm.Model;
import com.lealone.orm.ModelProperty;
import com.lealone.orm.ModelTable;
import com.lealone.orm.format.JsonFormat;
import com.lealone.orm.property.PBigDecimal;
import com.lealone.orm.property.PDate;
import com.lealone.orm.property.PInteger;
import com.lealone.orm.property.PString;

/**
 * Model for table 'ORDERS'.
 *
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
public class Orders extends Model<Orders> {

    public static final Orders dao = new Orders(null, ROOT_DAO);

    public final PInteger<Orders> orderid;
    public final PString<Orders> userid;
    public final PDate<Orders> orderdate;
    public final PString<Orders> shipaddr1;
    public final PString<Orders> shipaddr2;
    public final PString<Orders> shipcity;
    public final PString<Orders> shipstate;
    public final PString<Orders> shipzip;
    public final PString<Orders> shipcountry;
    public final PString<Orders> billaddr1;
    public final PString<Orders> billaddr2;
    public final PString<Orders> billcity;
    public final PString<Orders> billstate;
    public final PString<Orders> billzip;
    public final PString<Orders> billcountry;
    public final PString<Orders> courier;
    public final PBigDecimal<Orders> totalprice;
    public final PString<Orders> billtofirstname;
    public final PString<Orders> billtolastname;
    public final PString<Orders> shiptofirstname;
    public final PString<Orders> shiptolastname;
    public final PString<Orders> creditcard;
    public final PString<Orders> exprdate;
    public final PString<Orders> cardtype;
    public final PString<Orders> locale;

    public Orders() {
        this(null, REGULAR_MODEL);
    }

    private Orders(ModelTable t, short modelType) {
        super(t == null ? new ModelTable("PETSTORE", "PUBLIC", "ORDERS") : t, modelType);
        orderid = new PInteger<>("ORDERID", this);
        userid = new PString<>("USERID", this);
        orderdate = new PDate<>("ORDERDATE", this);
        shipaddr1 = new PString<>("SHIPADDR1", this);
        shipaddr2 = new PString<>("SHIPADDR2", this);
        shipcity = new PString<>("SHIPCITY", this);
        shipstate = new PString<>("SHIPSTATE", this);
        shipzip = new PString<>("SHIPZIP", this);
        shipcountry = new PString<>("SHIPCOUNTRY", this);
        billaddr1 = new PString<>("BILLADDR1", this);
        billaddr2 = new PString<>("BILLADDR2", this);
        billcity = new PString<>("BILLCITY", this);
        billstate = new PString<>("BILLSTATE", this);
        billzip = new PString<>("BILLZIP", this);
        billcountry = new PString<>("BILLCOUNTRY", this);
        courier = new PString<>("COURIER", this);
        totalprice = new PBigDecimal<>("TOTALPRICE", this);
        billtofirstname = new PString<>("BILLTOFIRSTNAME", this);
        billtolastname = new PString<>("BILLTOLASTNAME", this);
        shiptofirstname = new PString<>("SHIPTOFIRSTNAME", this);
        shiptolastname = new PString<>("SHIPTOLASTNAME", this);
        creditcard = new PString<>("CREDITCARD", this);
        exprdate = new PString<>("EXPRDATE", this);
        cardtype = new PString<>("CARDTYPE", this);
        locale = new PString<>("LOCALE", this);
        super.setModelProperties(new ModelProperty[] { orderid, userid, orderdate, shipaddr1, shipaddr2, shipcity, shipstate, shipzip, shipcountry, billaddr1, billaddr2, billcity, billstate, billzip, billcountry, courier, totalprice, billtofirstname, billtolastname, shiptofirstname, shiptolastname, creditcard, exprdate, cardtype, locale });
    }

    @Override
    protected Orders newInstance(ModelTable t, short modelType) {
        return new Orders(t, modelType);
    }

    public static Orders decode(String str) {
        return decode(str, null);
    }

    public static Orders decode(String str, JsonFormat format) {
        return new Orders().decode0(str, format);
    }
}
