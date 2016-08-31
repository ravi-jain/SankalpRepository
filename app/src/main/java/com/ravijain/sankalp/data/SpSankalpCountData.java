package com.ravijain.sankalp.data;

/**
 * Created by ravijain on 8/29/2016.
 */
public class SpSankalpCountData {

    private int _currentTyags, _currentNiyams, _lifetimeTyags, _lifetimeNiyams, _upcomingTyags, _upcomingNiyams, _allTyags, _allNiyams;

    public int getCurrentSankalps()
    {
        return _currentTyags + _currentNiyams;
    }

    public int getLifetimeSankalps()
    {
        return _lifetimeTyags + _lifetimeNiyams;
    }

    public int getUpcomingSankalps()
    {
        return _upcomingTyags + _upcomingNiyams;
    }

    public int getAllSankalps()
    {
        return _allTyags + _allNiyams;
    }

    public int getCurrentTyags() {
        return _currentTyags;
    }

    public void setCurrentTyags(int currentTyags) {
        this._currentTyags = currentTyags;
    }

    public int getCurrentNiyams() {
        return _currentNiyams;
    }

    public void setCurrentNiyams(int currentNiyams) {
        this._currentNiyams = currentNiyams;
    }

    public int getLifetimeTyags() {
        return _lifetimeTyags;
    }

    public void setLifetimeTyags(int lifetimeTyags) {
        this._lifetimeTyags = lifetimeTyags;
    }

    public int getLifetimeNiyams() {
        return _lifetimeNiyams;
    }

    public void setLifetimeNiyams(int lifetimeNiyams) {
        this._lifetimeNiyams = lifetimeNiyams;
    }

    public int getUpcomingTyags() {
        return _upcomingTyags;
    }

    public void setUpcomingTyags(int upcomingTyags) {
        this._upcomingTyags = upcomingTyags;
    }

    public int getUpcomingNiyams() {
        return _upcomingNiyams;
    }

    public void setUpcomingNiyams(int upcomingNiyams) {
        this._upcomingNiyams = upcomingNiyams;
    }

    public int getAllTyags() {
        return _allTyags;
    }

    public void setAllTyags(int allTyags) {
        this._allTyags = allTyags;
    }

    public int getAllNiyams() {
        return _allNiyams;
    }

    public void setAllNiyams(int allNiyams) {
        this._allNiyams = allNiyams;
    }

}
