package com.lealone.examples.petstore.model;

import com.lealone.orm.Model;
import com.lealone.orm.ModelProperty;
import com.lealone.orm.ModelTable;
import com.lealone.orm.format.JsonFormat;
import com.lealone.orm.property.PDate;
import com.lealone.orm.property.PString;

/**
 * Model for table 'ACCOUNT'.
 *
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
public class Account extends Model<Account> {

    public static final Account dao = new Account(null, ROOT_DAO);

    public final PString<Account> userId;
    public final PString<Account> email;
    public final PString<Account> firstName;
    public final PString<Account> lastName;
    public final PString<Account> status;
    public final PString<Account> address1;
    public final PString<Account> address2;
    public final PString<Account> city;
    public final PString<Account> state;
    public final PString<Account> zip;
    public final PString<Account> country;
    public final PString<Account> phone;
    public final PString<Account> creditCardNumber;
    public final PString<Account> creditCardType;
    public final PDate<Account> creditCardExpiry;

    public Account() {
        this(null, REGULAR_MODEL);
    }

    private Account(ModelTable t, short modelType) {
        super(t == null ? new ModelTable("PETSTORE", "PUBLIC", "ACCOUNT") : t, modelType);
        userId = new PString<>("USER_ID", this);
        email = new PString<>("EMAIL", this);
        firstName = new PString<>("FIRST_NAME", this);
        lastName = new PString<>("LAST_NAME", this);
        status = new PString<>("STATUS", this);
        address1 = new PString<>("ADDRESS_1", this);
        address2 = new PString<>("ADDRESS_2", this);
        city = new PString<>("CITY", this);
        state = new PString<>("STATE", this);
        zip = new PString<>("ZIP", this);
        country = new PString<>("COUNTRY", this);
        phone = new PString<>("PHONE", this);
        creditCardNumber = new PString<>("CREDIT_CARD_NUMBER", this);
        creditCardType = new PString<>("CREDIT_CARD_TYPE", this);
        creditCardExpiry = new PDate<>("CREDIT_CARD_EXPIRY", this);
        super.setModelProperties(new ModelProperty[] { userId, email, firstName, lastName, status, address1, address2, city, state, zip, country, phone, creditCardNumber, creditCardType, creditCardExpiry });
    }

    @Override
    protected Account newInstance(ModelTable t, short modelType) {
        return new Account(t, modelType);
    }

    public static Account decode(String str) {
        return decode(str, null);
    }

    public static Account decode(String str, JsonFormat format) {
        return new Account().decode0(str, format);
    }
}
