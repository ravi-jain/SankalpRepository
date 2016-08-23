package com.ravijain.sankalp.data;

/**
 * Created by ravijain on 7/1/2016.
 */
public class SpUser {

    private int _id;
    private String _name;
    private String _mobile;
    private String _email;
    private String _city;

    public SpUser(int id)
    {
        _id = id;
    }

    public SpUser(String name, String mobile, String email) {
        _name = name;
        _mobile = mobile;
        _email = email;
    }

    public SpUser(String name, String mobile, String email, String city) {
        _name = name;
        _mobile = mobile;
        _email = email;
        _city = city;
    }

    public void setProperties(String name, String mobile, String email, String city)
    {
        _name = name;
        _mobile = mobile;
        _email = email;
        _city = city;
    }

    public boolean equals(Object u)
    {
        if (u == null) return false;
        if (!(u instanceof SpUser)) return false;
        SpUser user = (SpUser)u;
        if (!areEqual(user.getName(), getName())) return false;
        if (!areEqual(user.getMobile(), getMobile())) return false;
        if (!areEqual(user.getEmail(), getEmail())) return false;
        if (!areEqual(user.getCity(), getCity())) return false;
        return true;
    }

    public boolean areEqual(String s1, String s2)
    {
        if (s1 == null || s2 == null) {
            if (s1 == null && s2 == null) return true;
            return false;
        }
        return s1.equals(s2);
    }

    public String getCity() {
        return _city;
    }

    public void setCity(String city) {
        this._city = city;
    }
    public String getName() {
        return _name;
    }

    public String getMobile() {
        return _mobile;
    }

    public String getEmail() {
        return _email;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setMobile(String mobile) {
        this._mobile = mobile;
    }

    public void setEmail(String email) {
        this._email = email;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
}
