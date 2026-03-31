package com.lealone.examples.petstore.model;

import com.lealone.orm.Model;
import com.lealone.orm.ModelProperty;
import com.lealone.orm.ModelTable;
import com.lealone.orm.format.JsonFormat;
import com.lealone.orm.property.PString;
import java.util.List;

/**
 * Model for table 'PRODUCT'.
 *
 * THIS IS A GENERATED OBJECT, DO NOT MODIFY THIS CLASS.
 */
public class Product extends Model<Product> {

    public static final Product dao = new Product(null, ROOT_DAO);

    public final PString<Product> productid;
    public final PString<Product> categoryid;
    public final PString<Product> name;
    public final PString<Product> logo;
    public final PString<Product> descn;
    private Category category;

    public Product() {
        this(null, REGULAR_MODEL);
    }

    private Product(ModelTable t, short modelType) {
        super(t == null ? new ModelTable("PETSTORE", "PUBLIC", "PRODUCT") : t, modelType);
        productid = new PString<>("PRODUCTID", this);
        categoryid = new PString<>("CATEGORYID", this);
        name = new PString<>("NAME", this);
        logo = new PString<>("LOGO", this);
        descn = new PString<>("DESCN", this);
        super.setModelProperties(new ModelProperty[] { productid, categoryid, name, logo, descn });
        super.initSetters(new CategorySetter());
        super.initAdders(new ItemAdder());
    }

    @Override
    protected Product newInstance(ModelTable t, short modelType) {
        return new Product(t, modelType);
    }

    public Category getCategory() {
        return category;
    }

    public Product setCategory(Category category) {
        this.category = category;
        this.categoryid.set(category.catid.get());
        return this;
    }

    public Product addItem(Item m) {
        m.setProduct(this);
        super.addModel(m);
        return this;
    }

    public Product addItem(Item... mArray) {
        for (Item m : mArray)
            addItem(m);
        return this;
    }

    public List<Item> getItemList() {
        return super.getModelList(Item.class);
    }

    protected class CategorySetter implements AssociateSetter<Category> {
        @Override
        public Category getDao() {
            return Category.dao;
        }

        @Override
        public boolean set(Category m) {
            if (areEqual(categoryid, m.catid)) {
                setCategory(m);
                return true;
            }
            return false;
        }
    }

    protected class ItemAdder implements AssociateAdder<Item> {
        @Override
        public Item getDao() {
            return Item.dao;
        }

        @Override
        public void add(Item m) {
            if (areEqual(productid, m.productid)) {
                addItem(m);
            }
        }
    }

    public static Product decode(String str) {
        return decode(str, null);
    }

    public static Product decode(String str, JsonFormat format) {
        return new Product().decode0(str, format);
    }
}
