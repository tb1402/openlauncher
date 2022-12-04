package com.benny.openlauncher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.benny.openlauncher.R;

import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;

import net.gsantner.opoc.util.ContextUtils;

public class SettingsActivityLogin extends ColorActivity {
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        (new ContextUtils(this)).setAppLanguage(this._appSettings.getLanguage());

        setContentView(R.layout.activity_settings_login);
        ButterKnife.bind(this);

        //this.toolbar.setTitle(2131755388);
        getSupportActionBar();
        setSupportActionBar(this.toolbar);
        this.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white));
        this.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        this.toolbar.setBackgroundColor(this._appSettings.getPrimaryColor());

        if (this._appSettings.getAppRestartRequired()) startActivity(new Intent(this, HomeActivity.class));

        //captcha to prevent guessing pw
        int randomA = -1, randomB = -1;
        boolean useCaptcha = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            randomA = ThreadLocalRandom.current().nextInt(0, 10);
            randomB = ThreadLocalRandom.current().nextInt(0, 10);
            useCaptcha = true;
        }

        final int added = useCaptcha ? randomA + randomB : -1;
        if (useCaptcha) ((TextView) findViewById(R.id.tv_captcha)).setText(String.format("%s+%s", randomA, randomB));

        final String password = _appSettings.getPassword();
        final EditText etPassword = findViewById(R.id.et_password);

        boolean finalUseCaptcha = useCaptcha;
        findViewById(R.id.btn_login).setOnClickListener(param1View -> {
            String userPw = etPassword.getText().toString();
            if (userPw.equalsIgnoreCase(password + (finalUseCaptcha ? String.valueOf(added) : ""))) {
                SettingsActivityLogin.this.startActivity(
                        new Intent(SettingsActivityLogin.this, SettingsActivity.class)
                                .putExtra("pw", true));
                SettingsActivityLogin.this.finish();
                return;
            }

            Toast.makeText(SettingsActivityLogin.this, getString(R.string.activity_login_password_wrong), Toast.LENGTH_SHORT).show();
        });
    }
}