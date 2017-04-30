package mbds.nfc.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mbds.nfc.MainActivity;
import mbds.nfc.R;
import mbds.nfc.Scan;
import mbds.nfc.utils.PnToast;
import mbds.nfc.utils.SessionManager;
import mbds.nfc.utils.Utilitaire;

import static mbds.nfc.utils.Utilitaire.getRequest;

public class LoginActivity extends AppCompatActivity {

    private static final String DUMMY_CREDENTIALS = "test@yopmail.com:test123";
    private Context ctx;
    private UserLoginTask userLoginTask = null;
    private EditText passwordTextView;
    private EditText login;
    private TextView signUpTextView;
    private boolean error;
    SessionManager session;
    private Toolbar mToolbar;
    private String IDUSER;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        error = false;

        ctx = this;


        login = (EditText) findViewById(R.id.login);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //drawerFragment = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        //drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), mToolbar);
        //drawerFragment.setDrawerListener(this);

        passwordTextView = (EditText) findViewById(R.id.password);
        passwordTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    initLogin();
                    return true;
                }
                return false;
            }
        });

        Button loginButton = (Button) findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                            initLogin();

            }
        });

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        signUpTextView.setPaintFlags(signUpTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(signUpTextView, Linkify.ALL);

        signUpTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterAcivity.class);
                startActivity(i);
            }
        });
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void initLogin() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    //boolean b = isOnline();
                    //if(b){
                    if (userLoginTask != null) {
                        return;
                    }

                    // login.setError(null);
                    // passwordTextView.setError(null);

                    String identifiant = login.getText().toString();
                    String password = passwordTextView.getText().toString();

                    //Log.e("passage log","identifian="+identifiant+" mot de passe ="+password);
                    boolean cancelLogin = false;
                    View focusView = null;

                    if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                        passwordTextView.setError(getString(R.string.invalid_password));
                        focusView = passwordTextView;
                        cancelLogin = true;
                    }

                    if (TextUtils.isEmpty(identifiant)) {
                        login.setError(getString(R.string.field_required));
                        focusView = login;
                        cancelLogin = true;
                    }

                    if (cancelLogin) {
                        focusView.requestFocus();
                    } else {

                        userLoginTask = new UserLoginTask(identifiant, password);
                        userLoginTask.execute((Void) null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }




    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String matrStr;
        private final String passwordStr;

        UserLoginTask(String matricule, String password) {
            matrStr = matricule;
            passwordStr = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Log.e("errror 12", "test 1");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

                return false;
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            Log.e("errror 12", "test 2");


            InputStream is = null;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("login", matrStr));
            nameValuePairs.add(new BasicNameValuePair("password", passwordStr));
            String result = null;
            try{
                Log.e("errror 12", "test 3");
                HttpClient httpClient = new DefaultHttpClient();

                String url = Utilitaire.getAdresseAuthentification()+"login="+matrStr+"&password="+passwordStr;
                result = getRequest(url);

            } catch (ClientProtocolException e) {
                Log.e("erreur ClientProtocol ", e.getMessage());
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                Log.e("erreur UnsupportedEn", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("erreur IOException", e.getMessage());
                e.printStackTrace();
                error = true;
            } catch (Exception e){
                Log.e("erreur", e.getMessage());
                e.printStackTrace();
            }

            // String s = result.trim();
            if(result == null || result.isEmpty() || result.equals("[]") || result.equals("[]\n")){
                return false;
            }

            //Log.e("resultat ============ ", result);

            try {
                JSONArray jsonArray = new JSONArray(result);

                //Log.e("resultat ======== ", result);
                JSONObject pers = jsonArray.getJSONObject(0);
                //String value = Utilitaire.parseUserJson(result);
                session = new SessionManager(getApplicationContext());
                //String[] split = value.split(Utilitaire.getSeparator());
                session.createLoginSession(pers.getString("id"), pers.getString("nom")+" "+pers.getString("prenom"), pers.getString("login"), pers.getString("type"), pers.getString("id"));
                IDUSER = pers.getString("id");
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return true;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            userLoginTask = null;

            if (success) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("IDUSER",IDUSER);
                startActivity(i);
            } else {
                if(error){
                    PnToast.show(ctx, "Erreur de connexion", true);
                }
                else {
                    passwordTextView.setError(getString(R.string.incorrect_password));
                    passwordTextView.requestFocus();
                }
            }
        }

        @Override
        protected void onCancelled() {
            userLoginTask = null;
        }
    }
}