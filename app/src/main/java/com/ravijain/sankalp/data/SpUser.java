package com.ravijain.sankalp.data;

/**
 * Created by ravijain on 7/1/2016.
 */
public class SpUser {

    private String _name;
    private String _mobile;
    private String _email;

    public SpUser(String name, String mobile, String email) {
        _name = name;
        _mobile = mobile;
        _email = email;
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
}
