package com.intelegain.agora.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.intelegain.agora.R;
import com.intelegain.agora.fragmments.CareersFragment;

public class NewCareerActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    private Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_career);
        InitializeWidget();
        setToolbar();
        setEventClickListener();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView,new CareersFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void InitializeWidget() {
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
    }

    /**
     * Set the event click listener for views
     */
    private void setEventClickListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Set toolbar properties
     */
    private void setToolbar() {
        setSupportActionBar(toolbar);
        String strTitleArray[] = getResources().getStringArray(R.array.menu_drawer);
        int iIndexForContactUs = 10; // Index no of contact us in string.xml file
        String strScreenTitle = strTitleArray[iIndexForContactUs];
        getSupportActionBar().setTitle(strScreenTitle);
        toolbar_title.setText(strScreenTitle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
