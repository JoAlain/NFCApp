package mbds.nfc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mbds.nfc.R;
import mbds.nfc.modele.Activite;
import mbds.nfc.modele.Commande;

/**
 * Created by Alain on 29/04/2017.
 */

public class CommandeAdapter extends ArrayAdapter<Commande> {

    private Commande activity;
    private List<Commande> commandeList = new ArrayList<Commande>();
    private static LayoutInflater inflater=null;

    public List<Commande> getCommandeList() {
        return commandeList;
    }

    public void setCommandeList(List<Commande> commandeList) {
        this.commandeList = commandeList;
    }

    TextView id;
    TextView refActivite;
    TextView user;
    TextView activite;
    TextView prix;
    TextView remarque;
    TextView valide;

    public CommandeAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    @Override
    public void add(Commande object) {
        commandeList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.commandeList.size();
    }

    @Override
    public Commande getItem(int index) {
        return this.commandeList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.commande_row, parent, false);

            activite = (TextView)row.findViewById(R.id.activite); // title
            prix = (TextView)row.findViewById(R.id.duration); // price
            id = (TextView)row.findViewById(R.id.numero); // price
            valide = (TextView)row.findViewById(R.id.valide);
            refActivite = (TextView)row.findViewById(R.id.refActivite);
        }

        final Commande commande = getItem(position);
        activite.setText(commande.getActivite());
        valide.setText(commande.getValide().toUpperCase());
        prix.setText(commande.getPrix()+"");
        refActivite.setText(commande.getId());
        return row;
    }
}
