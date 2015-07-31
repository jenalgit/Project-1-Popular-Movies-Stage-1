package com.gyasistory.project1moviedatabase;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by gyasistory on 7/30/15.
 */
public class OrganizationPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.organization_preference);
    }
}
