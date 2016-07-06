package com.ravijain.sankalp.data;

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
    private String _description;
    private Date _creationDate;
    private int _exceptionFrequencyId;
    private int _exceptionFrequencyCount;
    private int _isLifetime = SpDataConstants.SANKALP_IS_LIFTIME_FALSE;

    public SpSankalp(int categoryId, int itemId)
    {
        _categoryID = categoryId;
        _itemId = itemId;
    }

    public SpSankalp()
    {

    }

    public int getExceptionFrequencyCount() {
        return _exceptionFrequencyCount;
    }

    public void setExceptionFrequencyCount(int exceptionFrequencyCount) {
        this._exceptionFrequencyCount = exceptionFrequencyCount;
    }

    public int getExceptionFrequencyId() {
        return _exceptionFrequencyId;
    }

    public void setExceptionFrequencyId(int exceptionFrequencyId) {
        this._exceptionFrequencyId = exceptionFrequencyId;
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


}
