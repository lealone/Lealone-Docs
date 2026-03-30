package com.lealone.examples.petstore.model;

import com.lealone.orm.Model;
import com.lealone.orm.ModelProperty;
import com.lealone.orm.ModelTable;
import com.lealone.orm.format.JsonFormat;
import com.lealone.orm.property.PInteger;
import com.lealone.orm.property.PString;

/**
 * Model for table 'SUPPLIER'.
 *
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
public class Supplier extends Model<Supplier> {

    public static final Supplier dao = new Supplier(null, ROOT_DAO);

    public final PInteger<Supplier> suppid;
    public final PString<Supplier> name;
    public final PString<Supplier> status;
    public final PString<Supplier> addr1;
    public final PString<Supplier> addr2;
    public final PString<Supplier> city;
    public final PString<Supplier> state;
    public final PString<Supplier> zip;
    public final PString<Supplier> phone;

    public Supplier() {
        this(null, REGULAR_MODEL);
    }

    private Supplier(ModelTable t, short modelType) {
        super(t == null ? new ModelTable("PETSTORE", "PUBLIC", "SUPPLIER") : t, modelType);
        suppid = new PInteger<>("SUPPID", this);
        name = new PString<>("NAME", this);
        status = new PString<>("STATUS", this);
        addr1 = new PString<>("ADDR1", this);
        addr2 = new PString<>("ADDR2", this);
        city = new PString<>("CITY", this);
        state = new PString<>("STATE", this);
        zip = new PString<>("ZIP", this);
        phone = new PString<>("PHONE", this);
        super.setModelProperties(new ModelProperty[] { suppid, name, status, addr1, addr2, city, state, zip, phone });
    }

    @Override
    protected Supplier newInstance(ModelTable t, short modelType) {
        return new Supplier(t, modelType);
    }

    public static Supplier decode(String str) {
        return decode(str, null);
    }

    public static Supplier decode(String str, JsonFormat format) {
        return new Supplier().decode0(str, format);
    }
}
