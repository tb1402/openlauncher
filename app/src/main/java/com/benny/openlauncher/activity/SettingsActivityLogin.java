package com.benny.openlauncher.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.benny.openlauncher.R;

import java.util.concurrent.ThreadLocalRandom;

/*import butterknife.BindView;
import butterknife.ButterKnife;*/

import net.gsantner.opoc.util.ContextUtils;

public class SettingsActivityLogin extends ColorActivity {
    protected Toolbar toolbar;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        (new ContextUtils(this)).setAppLanguage(this._appSettings.getLanguage());

        setContentView(R.layout.activity_settings_login);
        toolbar=findViewById(R.id.toolbar);

        getSupportActionBar();
        setSupportActionBar(this.toolbar);
        this.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white));
        this.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        this.toolbar.setBackgroundColor(this._appSettings.getPrimaryColor());

        if (this._appSettings.getAppRestartRequired()) startActivity(new Intent(this, HomeActivity.class));

        //captcha to prevent guessing pw
        int randomA, randomB;
        boolean useCaptcha = true;
        randomA = ThreadLocalRandom.current().nextInt(0, 10);
        randomB = ThreadLocalRandom.current().nextInt(0, 10);

        final int added = randomA + randomB;
        ((TextView) findViewById(R.id.tv_captcha)).setText(String.format("%s+%s", randomA, randomB));

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