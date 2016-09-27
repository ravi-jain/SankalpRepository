package com.ravijain.sankalp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;

import com.ravijain.sankalp.R;
import com.ravijain.sankalp.support.SpConstants;
import com.ravijain.sankalp.data.SpCategory;
import com.ravijain.sankalp.data.SpContentProvider;

import java.util.ArrayList;

/**
 * Created by ravijain on 9/22/2016.
 */
public class SpItemCreationDialog extends SpSimpleAlertDialog {

    private int _sankalpType;
    private View _view;

    public void configureView(View view) {
        _view = view;
        Bundle args = getArguments();
        if (args != null) {
            _sankalpType = args.getInt(SpConstants.INTENT_KEY_SANKALP_TYPE, SpConstants.SANKALP_TYPE_BOTH);
        }
        CategoriesLoaderTask t = new CategoriesLoaderTask();
        t.execute();
    }

    private void _loadSpinner(ArrayList<SpCategory> categories) {
        AppCompatSpinner spinner = (AppCompatSpinner) _view.findViewById(R.id.categories_spinner);
        ArrayAdapter categoriesAdapter = new ArrayAdapter<SpCategory>(getContext(), R.layout.spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(categoriesAdapter);
    }

    private class CategoriesLoaderTask extends AsyncTask<Void, Void, Boolean> {

        ArrayList<SpCategory> categories = null;

        @Override
        protected Boolean doInBackground(Void... voids) {
            categories = SpContentProvider.getInstance(getContext()).getAllCategoriesBySankalpType(_sankalpType);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                _loadSpinner(categories);
            }
        }
    }


}
