package satella.app.movies4.reminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import satella.app.movies4.BuildConfig;
import satella.app.movies4.R;

public class ReminderSettingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_reminder)
    Toolbar mToolbar;

    @BindView(R.id.switch_daily)
    SwitchCompat mSwitchDaily;

    @BindView(R.id.switch_release)
    SwitchCompat mSwitchRelease;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedEdit;
    private NotificationReceiver mNotificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setting);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.reminder_setting));
        }

        mSharedPreferences = getSharedPreferences(BuildConfig.MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mNotificationReceiver = new NotificationReceiver(this);

        switchApply();
        serPreference();

    }

    private void switchApply() {
        mSwitchDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSharedEdit = mSharedPreferences.edit();
                mSharedEdit.putBoolean("daily_reminder", isChecked);
                mSharedEdit.apply();
                if (isChecked) {
                    mNotificationReceiver.setDailyReminder();
                } else {
                    mNotificationReceiver.cancelDailyRemainder(getApplicationContext());
                }
            }
        });
        mSwitchRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSharedEdit = mSharedPreferences.edit();
                mSharedEdit.putBoolean("release_reminder", isChecked);
                mSharedEdit.apply();
                if (isChecked) {
                    mNotificationReceiver.setReleaseTodayReminder();
                } else {
                    mNotificationReceiver.cancelReleaseToday(getApplicationContext());
                }
            }
        });
    }

    private void serPreference() {
        boolean dailyReminder = mSharedPreferences.getBoolean("daily_reminder",false);
        boolean releaseReminder = mSharedPreferences.getBoolean("release_reminder",false);

        mSwitchDaily.setChecked(dailyReminder);
        mSwitchRelease.setChecked(releaseReminder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
