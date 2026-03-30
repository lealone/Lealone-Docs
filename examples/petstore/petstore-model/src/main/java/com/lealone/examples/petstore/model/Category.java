package com.lealone.examples.petstore.model;

import com.lealone.orm.Model;
import com.lealone.orm.ModelProperty;
import com.lealone.orm.ModelTable;
import com.lealone.orm.format.JsonFormat;
import com.lealone.orm.property.PString;
import java.util.List;

/**
 * Model for table 'CATEGORY'.
 *
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
public class Category extends Model<Category> {

    public static final Category dao = new Category(null, ROOT_DAO);

    public final PString<Category> catid;
    public final PString<Category> name;
    public final PString<Category> logo;
    public final PString<Category> descn;

    public Category() {
        this(null, REGULAR_MODEL);
    }

    private Category(ModelTable t, short modelType) {
        super(t == null ? new ModelTable("PETSTORE", "PUBLIC", "CATEGORY") : t, modelType);
        catid = new PString<>("CATID", this);
        name = new PString<>("NAME", this);
        logo = new PString<>("LOGO", this);
        descn = new PString<>("DESCN", this);
        super.setModelProperties(new ModelProperty[] { catid, name, logo, descn });
        super.initAdders(new ProductAdder());
    }

    @Override
    protected Category newInstance(ModelTable t, short modelType) {
        return new Category(t, modelType);
    }

    public Category addProduct(Product m) {
        m.setCategory(this);
        super.addModel(m);
        return this;
    }

    public Category addProduct(Product... mArray) {
        for (Product m : mArray)
            addProduct(m);
        return this;
    }

    public List<Product> getProductList() {
        return super.getModelList(Product.class);
    }

    protected class ProductAdder implements AssociateAdder<Product> {
        @Override
        public Product getDao() {
            return Product.dao;
        }

        @Override
        public void add(Product m) {
            if (areEqual(catid, m.categoryid)) {
                addProduct(m);
            }
        }
    }

    public static Category decode(String str) {
        return decode(str, null);
    }

    public static Category decode(String str, JsonFormat format) {
        return new Category().decode0(str, format);
    }
}
