package mbds.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mbds.nfc.activity.DetailsActivity;
import mbds.nfc.activity.FragmentDrawer;
import mbds.nfc.adapter.ActivityAdapter;
import mbds.nfc.modele.Activite;
import mbds.nfc.utils.PnToast;
import mbds.nfc.utils.SessionManager;
import mbds.nfc.utils.Utilitaire;

public class Scan extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    private Toolbar mToolbar;
    private ScanActivityTask scanActivityTask = null;
    private FragmentDrawer drawerFragment;

    FrameLayout statusBar;
    Toolbar toolbar;

    SessionManager session;
    ActivityAdapter adapter;
    private ListView listView;

    private String IDUSER = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Bundle extras = getIntent().getExtras();
        IDUSER = extras.getString("IDUSER");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        session=new SessionManager(getApplicationContext());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //drawerFragment = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        //drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), mToolbar);
        //drawerFragment.setDrawerListener(this);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!nfcAdapter.isEnabled())
        {
            //Demander à l’utilisateur d’activer l’option NFC
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    public void processNfcIntent (Intent intent)
    {
        //Infos sur le tag
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] id =tag.getId();
        String[] technologies = tag.getTechList();
        int content = tag.describeContents();
        Ndef ndef = Ndef.get(tag);
        boolean isWritable = ndef.isWritable();
        boolean canMakeReadOnly = ndef.canMakeReadOnly();
        //Récupération des messages
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage[] msgs;
        //Boucle sur les enregistrements
        if (rawMsgs != null)
        {
            msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++)
            {
                msgs[i] = (NdefMessage) rawMsgs[i];
                NdefRecord record = msgs[i].getRecords()[i];
                byte[] idRec = record.getId();
                short tnf = record.getTnf();
                byte[] type = record.getType();
                String message = new String(record.getPayload()).trim();

                System.out.println("message ================== " + message.trim());

                ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
                Glide.with(this).load(R.drawable.wait).into(imageViewTarget);
                //http://g-park-mbds.esy.es/nfcController/listerActivite.php

                // get All activities


                scanActivityTask = new ScanActivityTask("http://"+message, this);
                scanActivityTask.execute((Void) null);


                //listView.addHeaderView(new View(this));
                //listView.addFooterView(new View(this));


            }
        }

        //Traiter les informations...
    }
    public void toolbarStatusBar() {

        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Scan TAG");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void onNewIntent(Intent intent)
    {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
        {
            //Méthode qui va traiter le tag NFC
            processNfcIntent(intent) ;
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    public class ScanActivityTask extends AsyncTask<Void, Void, Boolean> {

        private final String url;
        private Scan context;

        ScanActivityTask(String uri, Scan scan) {
            url = uri;
            context = scan;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

                return false;
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            InputStream is = null;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            String result = null;

            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //Log.e(" url connection ", httpPost.getURI().getPath());
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();

                is = entity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    Log.e("errror 12", "test 4");
                    sb.append(line + "\n");
                }
                result = sb.toString();

                Log.e("resultat scan: ", result);

                JSONArray arrayListeActivite = new JSONArray(result);
                ArrayList<String> retour = new ArrayList<String>();
                for(int indice = 0; indice < arrayListeActivite.length(); indice++){
                    JSONObject tempjson = arrayListeActivite.getJSONObject(indice);
                    String ret = Utilitaire.parseActiviteJson(tempjson);
                    retour.add(ret);
                }

                List<Activite> liste = Utilitaire.decoderStringActivite(retour);

                adapter = new ActivityAdapter(getApplicationContext(), R.layout.activity_liste);
                adapter.setActiviteList(liste);
                /*for(int i = 0; i< liste.size(); i++){
                    adapter.add(liste.get(i));
                }*/


            }catch (IOException e) {
                Log.e("erreur IOException", e.getMessage());
                e.printStackTrace();
            }catch (Exception exc){
                exc.printStackTrace();
            }
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                context.setContentView(R.layout.activity_liste);
                listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(adapter);

                statusBar = (FrameLayout) findViewById(R.id.statusBar);
                toolbar = (Toolbar) findViewById(R.id.toolbar);

                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle("Liste activités");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Activite activite = (Activite) parent.getItemAtPosition(position);
                        Intent i = new Intent(Scan.this, DetailsActivity.class);
                        i.putExtra("ID", activite.getId());
                        i.putExtra("IDUSER",IDUSER);
                        startActivity(i);
                    }
                });
                toolbarStatusBar();
            } else {

                    PnToast.show(context, "Erreur de connexion", true);
            }
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

}
