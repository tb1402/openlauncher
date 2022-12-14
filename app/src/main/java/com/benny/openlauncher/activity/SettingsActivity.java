package com.benny.openlauncher.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.appcompat.widget.Toolbar;

import com.benny.openlauncher.R;
import com.benny.openlauncher.fragment.SettingsBaseFragment;
import com.benny.openlauncher.fragment.SettingsMasterFragment;
import com.benny.openlauncher.manager.Setup;
import com.benny.openlauncher.util.AppSettings;
import com.benny.openlauncher.util.BackupHelper;
import com.benny.openlauncher.util.Definitions;

import net.gsantner.opoc.util.ContextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SettingsActivity extends ColorActivity implements SettingsBaseFragment.OnPreferenceStartFragmentCallback {
    protected Toolbar toolbar;

    public void onCreate(Bundle b) {
        // must be applied before setContentView
        super.onCreate(b);

        //check for lock
        Intent i = getIntent();
        if (AppSettings.get().isSettingLockActive() && i != null && !i.getBooleanExtra("pw", false)) {
            finish();
            return;
        }

        ContextUtils contextUtils = new ContextUtils(this);
        contextUtils.setAppLanguage(_appSettings.getLanguage());

        setContentView(R.layout.activity_settings);
        toolbar=findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.pref_title__settings);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setBackgroundColor(_appSettings.getPrimaryColor());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, new SettingsMasterFragment()).commit();

        // if system exit is called the app will open settings activity again
        // this pushes the user back out to the home activity
        if (_appSettings.getAppRestartRequired()) {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference preference) {
        Fragment fragment = Fragment.instantiate(this, preference.getFragment(), preference.getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder, fragment).addToBackStack(fragment.getTag()).commit();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Setup.dataManager().close();
            //List<Uri> files = Utils.getSelectedFilesFromResult(data);
            switch (requestCode) {
                case Definitions.INTENT_BACKUP:
                    BackupHelper.backupConfig(this, data.getData());
                    Setup.dataManager().open();
                    break;
                case Definitions.INTENT_RESTORE:
                    BackupHelper.restoreConfig(this, data.getData());
                    System.exit(0);
                    break;
            }
        }
    }
}
