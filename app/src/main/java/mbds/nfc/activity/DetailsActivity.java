package mbds.nfc.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import mbds.nfc.R;
import mbds.nfc.Scan;
import mbds.nfc.modele.Activite;
import mbds.nfc.utils.PnToast;
import mbds.nfc.utils.SessionManager;
import mbds.nfc.utils.Utilitaire;

public class DetailsActivity extends AppCompatActivity {

    Activite info;
    String idActivite;
    private Toolbar toolbar;
    SessionManager session;
    FrameLayout statusBar;
    private String IDUSER;
    Button commander;
    Button buttonCommandeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();
        session=new SessionManager(getApplicationContext());
        if (extras != null) {
            idActivite = extras.getString("ID");
            IDUSER = extras.getString("IDUSER");
            try {
                info=  new FindActiviteTask().execute(idActivite).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        commander = (Button)findViewById(R.id.commnder);
        buttonCommandeList = (Button)findViewById(R.id.buttonCommandeList);

        buttonCommandeList.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CommandeActivity.class);
                i.putExtra("IDUSER",IDUSER);
                startActivity(i);
                finish();
            }
        });
        commander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    info = new CommanderActiviteTask().execute(idActivite).get();
                    if (info == null){
                        Log.e("erreur commande task","Commande non enregistrée");
                    } else{
                        Log.i("success","Commande enregistrée avec succées");
                    }
                }catch (Exception exc){
                    exc.printStackTrace();
                }
            }
        });


    }

    class CommanderActiviteTask extends AsyncTask<String, Void, Activite>{
        boolean errorconnexion = false;
        private DetailsActivity context;

        @Override
        protected Activite doInBackground(String... params) {
            try{

                Activite infoPersonnep = Utilitaire.enregistrerCommandeClient(idActivite, IDUSER);
                if (infoPersonnep != null){
                    Toast.makeText(getApplicationContext(), "Commande enregistrée avec succès", Toast.LENGTH_SHORT);
                }
                return infoPersonnep;
            }

            catch(IOException e){
                errorconnexion = true;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent i = new Intent(DetailsActivity.this, CommandeActivity.class);
                i.putExtra("ID", idActivite);
                i.putExtra("IDUSER",IDUSER);
                startActivity(i);
            } else {

                PnToast.show(context, "Erreur de connexion", true);
            }
        }
    }

    class FindActiviteTask extends AsyncTask<String, Void, Activite> {
        boolean errorconnexion = false;

        @Override
        protected Activite doInBackground(String... params) {
            try{

                Activite infoPersonnep = Utilitaire.bindInfoActiviteByID(idActivite);
                return infoPersonnep;
            }

            catch(IOException e){
                errorconnexion = true;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Activite result) {
            if(errorconnexion){
                //PnToast.show(thi, "Erreur de connexion", true);
            }
            else if(result != null){

                TextView id = (TextView) findViewById(R.id.refActivite);
                id.setText(result.getId());

                TextView libelle = (TextView) findViewById(R.id.libelle);
                libelle.setText(result.getLibelle()+" Prix:" + result.getPrix()+"");

                TextView description = (TextView) findViewById(R.id.description);
                description.setText(result.getDescription());

            }
            else{
                //PnToast.show(context, "Profil introuvable", true);
            }
            toolbarStatusBar();
        }
    }
    public void toolbarStatusBar() {

        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //HashMap<String, String> user = session.getUserDetails();
        //String privilege = user.get(SessionManager.KEY_PRIVILEGE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
}
