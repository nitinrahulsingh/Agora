package com.intelegain.agora.fragmments;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.intelegain.agora.R;
import com.intelegain.agora.activity.FingerPrintSettingsActivity;
import com.intelegain.agora.activity.LoginActivity;
import com.intelegain.agora.adapter.HomeAdapter;
import com.intelegain.agora.fcm.RegistrationIntentService;
import com.intelegain.agora.utils.MyHandler;
import com.intelegain.agora.utils.NotificationSettings;
import com.intelegain.agora.utils.Sharedprefrences;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class New_Home_activity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnCancelListener {


    /*public Sharedprefrences mSharedPref;*/
    //fingerprint attributes
    public static final String DEFAULT_KEY_NAME = "default_key";
    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String KEY_NAME_NOT_INVALIDATED = "key_not_invalidated";
    Cipher defaultCipher;

    HomeAdapter homeAdapter;
    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private SharedPreferences mSharedPreferences;

    RecyclerView recyclerView;

    public static New_Home_activity mainActivity;
    public static Boolean isVisible = false;
    private static final String TAG = "MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_home);
        Log.w("KKK_NewHome", Sharedprefrences.getInstance(New_Home_activity.this).getString(getString(R.string.key_fcm_token), "NA"));
        findViews();

        mainActivity = this;
        NotificationsManager.handleNotifications(this, NotificationSettings.SenderId, MyHandler.class);
        registerWithNotificationHubs();
    }

    private void findViews() {
        recyclerView = findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        homeAdapter = new HomeAdapter(New_Home_activity.this);
        recyclerView.setAdapter(homeAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFingerPrintDialog() {

        PurchaseButtonClickListener p = new PurchaseButtonClickListener(defaultCipher, DEFAULT_KEY_NAME);
        p.showDialog_finger();

    }


    public void onPurchased(boolean withFingerprint,
                            @Nullable FingerprintManager.CryptoObject cryptoObject) {


        showConfirmation(null);

    }

    private void showConfirmation(byte encrypted[]) {

/*        if (mSharedPref.getBoolean("isChecked", false)) {
            Intent intent = new Intent(New_Home_activity.this, DashBordActivity.class);
            startActivity(intent);
        } else {*/
        Intent intent = new Intent(New_Home_activity.this, LoginActivity.class);
        startActivity(intent);
        /*}*/

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {
        try {
            mKeyStore.load(null);
            KeyGenParameterSpec.Builder builder = null;

            builder = new KeyGenParameterSpec.Builder(keyName,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            mKeyGenerator.init(builder.build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, FingerPrintSettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Dologin() {
        Intent intentLogin = new Intent(New_Home_activity.this, LoginActivity.class);
        startActivity(intentLogin);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //Sharedprefrences mSharedPref = Sharedprefrences.getInstance(getApplicationContext());
//            try {
//                mKeyStore = KeyStore.getInstance("AndroidKeyStore");
//            } catch (KeyStoreException e) {
//                throw new RuntimeException("Failed to get an instance of KeyStore", e);
//            }
//            try {
//                mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
//            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
//                throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
//            }
//
//
//            try {
//                defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
//                        + KeyProperties.BLOCK_MODE_CBC + "/"
//                        + KeyProperties.ENCRYPTION_PADDING_PKCS7);
//
//
//            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
//                throw new RuntimeException("Failed to get an instance of Cipher", e);
//            }
//            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(New_Home_activity.this);
//
//
//            KeyguardManager keyguardManager = null;
//            keyguardManager = getSystemService(KeyguardManager.class);
//
//            FingerprintManager fingerprintManager = null;
//            fingerprintManager = getSystemService(FingerprintManager.class);
//
//            if (!keyguardManager.isKeyguardSecure()) {
//
//                Intent intent = new Intent(New_Home_activity.this, LoginActivity.class);
//                startActivity(intent);
//                return;
//            }
//
//            if (!fingerprintManager.isHardwareDetected()) {
//                Intent intent = new Intent(New_Home_activity.this, LoginActivity.class);
//                startActivity(intent);
//                return;
//            }
//            // noinspection ResourceType
//            if (!fingerprintManager.hasEnrolledFingerprints()) {
//
//                Intent intent = new Intent(New_Home_activity.this, LoginActivity.class);
//                startActivity(intent);
//                return;
//            } else {
//                /*if (mSharedPref.getBoolean("isChecked", false)) {*/
//                createKey(DEFAULT_KEY_NAME, true);
//                createKey(KEY_NAME_NOT_INVALIDATED, false);
//                // Home_linear_leaves.setEnabled(true);
//                showFingerPrintDialog();
//              /*  } else {
//
//                    Intent intent = new Intent(New_Home_activity, LoginActivity.class);
//                    New_Home_activity.startActivity(intent);
//                    New_Home_activity.finish();
//
//                }*/
//            }
//
//        } else {
//            Intent intent = new Intent(New_Home_activity.this, LoginActivity.class);
//            startActivity(intent);
//            // finish();
//        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public class PurchaseButtonClickListener {

        Cipher mCipher;
        String mKeyName;

        PurchaseButtonClickListener(Cipher cipher, String keyName) {
            mCipher = cipher;
            mKeyName = keyName;

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void showDialog_finger() {

            if (initCipher(mCipher, mKeyName)) {

                FingerprintAuthenticationDialogFragment fragment = new FingerprintAuthenticationDialogFragment();

                fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));

                boolean useFingerprintPreference = mSharedPreferences.getBoolean(getString(R.string.use_fingerprint_to_authenticate_key), false);
                if (useFingerprintPreference) {
                    fragment.setStage(FingerprintAuthenticationDialogFragment.Stage.PASSWORD);
                } else {
                    fragment.setStage(FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
                }
                fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);

            }
        }


        private boolean initCipher(Cipher cipher, String keyName) {
            try {
                mKeyStore.load(null);
                SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return true;
            } catch (KeyPermanentlyInvalidatedException e) {
                return false;
            } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                    | NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException("Failed to init Cipher", e);
            }
        }

    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
    }

    public void ToastNotify(final String notificationMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(New_Home_activity.this, notificationMessage, Toast.LENGTH_LONG).show();
                TextView helloText = (TextView) findViewById(R.id.text_hello);
                helloText.setText(notificationMessage);
            }
        });
    }

    public void registerWithNotificationHubs() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with FCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */

    private boolean checkPlayServices()
    {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported by Google Play Services.");
                ToastNotify("This device is not supported by Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }
}

