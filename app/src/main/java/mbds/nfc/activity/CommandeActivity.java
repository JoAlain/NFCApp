package mbds.nfc.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mbds.nfc.R;
import mbds.nfc.Scan;
import mbds.nfc.adapter.ActivityAdapter;
import mbds.nfc.adapter.CommandeAdapter;
import mbds.nfc.modele.Activite;
import mbds.nfc.modele.Commande;
import mbds.nfc.utils.PnToast;
import mbds.nfc.utils.SessionManager;
import mbds.nfc.utils.Utilitaire;

public class CommandeActivity extends AppCompatActivity {

    SessionManager session;

    private Toolbar toolbar;
    FrameLayout statusBar;
    CommandeAdapter adapter;
    private ListView listView;
    private String IDUSER = "";

    private CommandeActivity.ListeCommandeActivityTask scanActivityTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        session=new SessionManager(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        IDUSER = extras.getString("IDUSER");

        String url = Utilitaire.getListeCommande()+"user="+IDUSER;
        Log.e("liste commande", url);
        scanActivityTask = new CommandeActivity.ListeCommandeActivityTask (url, this);
        scanActivityTask.execute((Void) null);
    }

    public class ListeCommandeActivityTask extends AsyncTask<Void, Void, Boolean> {

        private final String url;
        private CommandeActivity context;
        ListeCommandeActivityTask(String uri, CommandeActivity scan) {
            url = uri;
            context = scan;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            InputStream is = null;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            String result = null;

            try {
                result = Utilitaire.getRequest(url);

                Log.e("resultat scan: ", result);

                JSONArray arrayListeActivite = new JSONArray(result);
                ArrayList<String> retour = new ArrayList<String>();
                for (int indice = 0; indice < arrayListeActivite.length(); indice++) {
                    JSONObject tempjson = arrayListeActivite.getJSONObject(indice);
                    String ret = Utilitaire.parseCommandeJSON(tempjson);
                    retour.add(ret);
                }

                List<Commande> liste = Utilitaire.decoderStringCommande(retour);

                adapter = new CommandeAdapter(getApplicationContext(), R.layout.activity_commande);
                adapter.setCommandeList(liste);


            }catch (Exception exc){
                exc.printStackTrace();
            }
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                context.setContentView(R.layout.activity_commande);
                listView = (ListView) findViewById(R.id.listCommande);
                listView.setAdapter(adapter);
                PnToast.show(context, "Nombre resultat"+adapter.getCommandeList().size(), true);
                toolbarStatusBar();
            } else {
                PnToast.show(context, "Erreur de connexion", true);
            }
        }
    }
    public void toolbarStatusBar() {

        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Liste commande");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
