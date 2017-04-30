package mbds.nfc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mbds.nfc.R;
import mbds.nfc.modele.Activite;
import mbds.nfc.utils.ImageLoader;

/**
 * Created by Alain on 25/04/2017.
 */

public class ActivityAdapter extends ArrayAdapter<Activite> {

    private Activity activity;
    private List<Activite> activiteList = new ArrayList<Activite>();
    private static LayoutInflater inflater=null;

    public void setActiviteList(List<Activite> activiteList) {
        this.activiteList = activiteList;
    }

    TextView title;
    TextView prix;
    TextView description;

    //public ImageLoader imageLoader;

    public ActivityAdapter(Context context,  int textViewResourceId) {
        super(context, textViewResourceId);
        //imageLoader = new ImageLoader();
    }

    @Override
    public void add(Activite object) {
        activiteList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.activiteList.size();
    }

    @Override
    public Activite getItem(int index) {
        return this.activiteList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_row, parent, false);

            title = (TextView)row.findViewById(R.id.title); // title
            prix = (TextView)row.findViewById(R.id.duration); // price
            description = (TextView)row.findViewById(R.id.artist); // price


        }

        final Activite activite = getItem(position);
        title.setText(activite.getLibelle());
        description.setText(activite.getDescription());
        prix.setText(activite.getPrix()+"");

        return row;
    }
}
