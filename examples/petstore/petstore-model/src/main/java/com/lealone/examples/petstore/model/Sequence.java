package com.lealone.examples.petstore.model;

import com.lealone.orm.Model;
import com.lealone.orm.ModelProperty;
import com.lealone.orm.ModelTable;
import com.lealone.orm.format.JsonFormat;
import com.lealone.orm.property.PInteger;
import com.lealone.orm.property.PString;

/**
 * Model for table 'SEQUENCE'.
 *
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
public class Sequence extends Model<Sequence> {

    public static final Sequence dao = new Sequence(null, ROOT_DAO);

    public final PString<Sequence> name;
    public final PInteger<Sequence> nextid;

    public Sequence() {
        this(null, REGULAR_MODEL);
    }

    private Sequence(ModelTable t, short modelType) {
        super(t == null ? new ModelTable("PETSTORE", "PUBLIC", "SEQUENCE") : t, modelType);
        name = new PString<>("NAME", this);
        nextid = new PInteger<>("NEXTID", this);
        super.setModelProperties(new ModelProperty[] { name, nextid });
    }

    @Override
    protected Sequence newInstance(ModelTable t, short modelType) {
        return new Sequence(t, modelType);
    }

    public static Sequence decode(String str) {
        return decode(str, null);
    }

    public static Sequence decode(String str, JsonFormat format) {
        return new Sequence().decode0(str, format);
    }
}
