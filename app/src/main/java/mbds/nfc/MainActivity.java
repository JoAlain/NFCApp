package mbds.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Arrays;

import mbds.nfc.activity.CommandeActivity;
import mbds.nfc.activity.FragmentDrawer;
import mbds.nfc.activity.LoginActivity;
import mbds.nfc.activity.RegisterAcivity;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    // Instanciation de l’intent qui représentera le tag NFC
    NfcAdapter nfcAdapter = null;
    PendingIntent pendingIntent = null;

    private Button btnConnect;
    private Button btnCommande;
    private Toolbar toolbar;
    FrameLayout statusBar;

    private String IDUSER = "";
    private HomeFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        Bundle extras = getIntent().getExtras();
        IDUSER = extras.getString("IDUSER");

        btnConnect = (Button) findViewById(R.id.buttonConnect);
        btnCommande = (Button) findViewById(R.id.buttonCommande);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (HomeFragment)getSupportFragmentManager().findFragmentById(R.id.frame_container);
        //drawerFragment.set (R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), toolbar);
        //drawerFragment.setDrawerListener(this);


        btnConnect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Scan.class);
                i.putExtra("IDUSER",IDUSER);
                startActivity(i);
                finish();
            }
        });

        btnCommande.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CommandeActivity.class);
                i.putExtra("IDUSER",IDUSER);
                startActivity(i);
                finish();
            }
        });
        toolbarStatusBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Activation de la découverte de tag NFC
    public void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null,  null); //intentFiltersArray, techListsArray
    }

    // Désactivation de la découverte des tags NFC
    public void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }



    public void toolbarStatusBar() {

        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Accueil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {

    }
}
