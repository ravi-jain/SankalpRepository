package com.ravijain.sankalp.data;

import android.content.Context;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.support.SpDateUtils;

import java.util.Date;

/**
 * Created by ravijain on 7/4/2016.
 */
public class SpSankalp {

    private int _id;
    private int _categoryID;
    private int _itemId;
    private Date _fromDate;
    private Date _toDate;
    private String _description = "";
    private Date _creationDate;
    private SpExceptionOrTarget _exceptionOrTarget;
    private int _isLifetime = SpConstants.SANKALP_IS_LIFTIME_FALSE;
    private int _isNotificationOn = SpConstants.SANKALP_IS_LIFTIME_FALSE;
    private int _sankalpType;

    private SpCategory _category = null;
    private SpCategoryItem _item = null;

    public SpSankalp(int sankalpType, int categoryId, int itemId) {
        _categoryID = categoryId;
        _itemId = itemId;
        _sankalpType = sankalpType;
    }

    public SpSankalp() {

    }

    public SpSankalp(int sankalpType) {
        _sankalpType = sankalpType;
    }

    public SpSankalp(Context context, int sankalpType) {
    }

    public static SpSankalp getDefaultSankalp(int sankalpType, Context context) {
        return new SpSankalp();
    }

    public boolean isMatch(String query) {
        if (_item != null && _item.getCategoryItemName().toLowerCase().contains(query.toLowerCase())) {
            return true;
        }
        if (_category != null && _category.getCategoryName().toLowerCase().contains(query.toLowerCase())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SpSankalp)) return false;
        if (obj == this) return true;
        SpSankalp s = (SpSankalp) obj;
        if (s.getId() == getId()) return true;
        return false;
    }

    public boolean isSame(Object obj) {
        if (obj == null || !(obj instanceof SpSankalp)) return false;
        SpSankalp s = (SpSankalp) obj;
        if (getSankalpType() != s.getSankalpType()) return false;
        if (getCategoryID() != s.getCategoryID()) return false;
        if (getItemId() != s.getItemId()) return false;
        if (isNotificationOn() != s.isNotificationOn()) return false;
        if (!getFromDate().equals(s.getFromDate())) return false;
        if (getToDate() == null && s.getToDate() != null) return false;
        else if (getToDate() != null && !getToDate().equals(s.getToDate())) return false;
        if (!getDescription().equals(s.getDescription())) return false;
        if (!getExceptionOrTarget().equals(s.getExceptionOrTarget())) return false;
        return true;
    }

    public String getSankalpSummary() {
        StringBuilder s = new StringBuilder();
        String sankalpType = getSankalpType() == SpConstants.SANKALP_TYPE_TYAG ? "Tyag" : "Niyam";
        s.append("I take ").append(sankalpType).append(" of ").append(getItem().getCategoryItemName()).append(" for ");

        if (getFromDate() != null) {
            if (isLifetime() == SpConstants.SANKALP_IS_LIFTIME_TRUE) {
                s.append("lifetime");
            } else {
                s.append(SpDateUtils.getFriendlyPeriodString(getFromDate(), getToDate(), false));
            }
        }


        if (getExceptionOrTarget() != null && getExceptionOrTarget().getExceptionOrTargetCount() > 0) {
            if (getSankalpType() == SpConstants.SANKALP_TYPE_TYAG) {
                s.append(" except ");
            } else if (getSankalpType() == SpConstants.SANKALP_TYPE_NIYAM) {
                s.append(" with a target of ");
            }
            s.append(getExceptionOrTarget().getRepresentationalSummary());
        }

        return s.toString();
    }

    public SpCategoryItem getItem() {
        return _item;
    }

    public void setItem(SpCategoryItem item) {
        this._item = item;
    }

    public SpCategory getCategory() {
        return _category;
    }

    public void setCategory(SpCategory category) {
        this._category = category;
    }

    public int getSankalpType() {
        return _sankalpType;
    }

    public SpExceptionOrTarget getExceptionOrTarget() {
        return _exceptionOrTarget;
    }

    public void setExceptionOrTarget(SpExceptionOrTarget exceptionOrTarget) {
        this._exceptionOrTarget = exceptionOrTarget;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int isLifetime() {
        return _isLifetime;
    }

    public void setLifetime(int lifetime) {
        _isLifetime = lifetime;
    }

    public int isNotificationOn() {
        return _isNotificationOn;
    }

    public void setNotification(int on) {
        _isNotificationOn = on;
    }


    public Date getCreationDate() {
        return _creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this._creationDate = creationDate;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public int getItemId() {
        return _itemId;
    }

    public void setItemId(int itemId) {
        this._itemId = itemId;
    }

    public Date getFromDate() {
        return _fromDate;
    }

    public void setFromDate(Date fromDate) {
        this._fromDate = fromDate;
    }

    public Date getToDate() {
        return _toDate;
    }

    public void setToDate(Date toDate) {
        this._toDate = toDate;
    }

    public int getCategoryID() {
        return _categoryID;
    }

    public void setCategoryID(int categoryID) {
        this._categoryID = categoryID;
    }

    public int compareTo(int compareCriteria, SpSankalp s) {
        switch (compareCriteria) {

            case R.id.rb_startDate:
                return this.getFromDate().compareTo(s.getFromDate());
            case R.id.rb_creationDate:
                return this.getCreationDate().compareTo(s.getCreationDate());
            case R.id.rb_itemName:
                return this.getItem().getCategoryItemName().compareTo(s.getItem().getCategoryItemName());
            case R.id.rb_endDate:
            default:
                if (s.getToDate() == null) return -1;
                if (getToDate() == null) return 1;
                return this.getToDate().compareTo(s.getToDate());
        }
    }
}
