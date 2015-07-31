package com.gyasistory.project1moviedatabase;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by gyasistory on 7/30/15.
 */
public class OrganizationPreferenceActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_preference);

        getFragmentManager().beginTransaction().replace(R.id.org_frame,
                new OrganizationPreferenceFragment()).commit();
    }
}
