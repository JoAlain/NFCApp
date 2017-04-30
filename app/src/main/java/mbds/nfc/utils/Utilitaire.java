package mbds.nfc.utils;



import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mbds.nfc.modele.Activite;
import mbds.nfc.modele.Commande;

public class Utilitaire {
    private static final String separator = "-";


    //private static final String SERVEUR = "http://192.168.1.7:8888/WebServiceNFC/index.php/";
    public static String getRequest(String uri) throws Exception{
        InputStream is = null;
        String result = null;
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(uri);

        HttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        is = entity.getContent();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        result = sb.toString();
        return result;

    }

    public static List<Commande> decoderStringCommande(ArrayList<String> p){
        List<Commande> retour = new ArrayList<Commande>();
        for(int i =0; i < p.size(); i++){
            String[] persString = p.get(i).split(getSeparator());
            Commande pers = new Commande();
            pers.setId(persString[0]);
            pers.setUser(persString[1]);
            pers.setActivite(persString[2]);
            pers.setPrix(persString[3]);
            pers.setRemarque(persString[4]);
            pers.setValide(persString[5]);
            retour.add(pers);

        }
        return retour;
    }

    public static List<Activite> decoderStringActivite(ArrayList<String> p){
        List<Activite> retour = new ArrayList<Activite>();
        for(int i =0; i < p.size(); i++){
            String[] persString = p.get(i).split(getSeparator());
            Activite pers = new Activite();
            pers.setId(persString[0]);
            pers.setLibelle(persString[1]);
            pers.setDescription(persString[2]);
            pers.setPrix(Double.valueOf(persString[3]));
            retour.add(pers);

        }
        return retour;
    }

    public static Activite enregistrerCommandeClient(String idactivite, String idUser)throws Exception{

        String urlCommande = Utilitaire.getLienCommande()+"id="+idactivite+"&user="+idUser;
        String result = getRequest(urlCommande);
        if(result==null || result.equals("echec") || result.isEmpty()) {
            return null;
        }
        Log.e("urlCommande ======== ", urlCommande);
        Log.e("result ======== ", result);
        JSONArray jsonArray = new JSONArray(result);

        JSONObject pers = jsonArray.getJSONObject(0);

        Activite activite = new Activite();
        activite.setId(pers.getString("ID"));
        activite.setPrix(Double.valueOf(pers.getString("tarif")));
        activite.setLibelle(pers.getString("libelle"));
        activite.setDescription(pers.getString("description"));

        return activite;
    }
    public static Activite bindInfoActiviteByID(String idactivite) throws Exception{

        String url = getDetailsActivites()+"id="+idactivite;
        Log.e("resultat ======== ", url);
        String result = getRequest(url);
        if(result==null || result.equals("echec") || result.isEmpty())
            throw new Exception(" Activite non trouvée");
        JSONArray jsonArray = new JSONArray(result);

        Log.e("resultat ======== ", result);
        JSONObject pers = jsonArray.getJSONObject(0);

        Activite activite = new Activite();
        activite.setId(pers.getString("ID"));
        activite.setPrix(Double.valueOf(pers.getString("tarif")));
        activite.setLibelle(pers.getString("libelle"));
        activite.setDescription(pers.getString("description"));

        return activite;
    }
    public static String parseCommandeJSON(JSONObject jsonObject){
        try {

            String ID =  jsonObject.getString("ID");
            String IDUSER = jsonObject.getString("IDUSER");
            String IDACTIVITE = jsonObject.getString("IDACTIVITE");
            String PRIX = jsonObject.getString("PRIX");
            String REMARQUE = jsonObject.getString("REMARQUE");

            String VALIDE = jsonObject.getString("VALIDE");

            String temp = ID + getSeparator() + IDUSER + getSeparator() + IDACTIVITE + getSeparator() + PRIX+ getSeparator() + REMARQUE+ getSeparator() + VALIDE;
            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String parseActiviteJson(JSONObject jsonObject){
        List<Activite> list = new ArrayList<Activite>();
        try {

            String id =  jsonObject.getString("ID");
            String libelle = jsonObject.getString("libelle");
            String description = jsonObject.getString("description");
            String prix = jsonObject.getString("tarif");

            String temp = id + getSeparator() + libelle + getSeparator() + description + getSeparator() + prix;
            return temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static String parseUserJson(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);

            int login =  jsonObject.getInt("ID");
            String typeutilisateur = jsonObject.getString("TYPE");
            int idutilitsateur = jsonObject.getInt("ID");
            String nomPrenom = jsonObject.getString("NOM") + " " + jsonObject.getString("PRENOM");
            String token = jsonObject.getString("TOKEN");

            return  token + getSeparator() + nomPrenom + getSeparator() + login + getSeparator() + typeutilisateur + getSeparator() + idutilitsateur ;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean checkValidPerson(String idtag) throws Exception {
        String result = getRequest(Config.SERVEUR+"nfcControler/getAllInfoPersonneByTag/" + idtag);
        if (result == null || result.equals("echec") || result.isEmpty())
            return false;
        return true;

    }




    public static String getSeparator(){
        return separator;
    }

    public static String formatterDateString(String aformater){
        String year = aformater.substring(0,4);
        String month = aformater.substring(5,7);
        String day = aformater.substring(8);
        return day + "/"+month+"/"+year;
    }
    public static String getListeCommande() {return Config.LISTE_COMMANDE;}
    public static String getAdresseAuthentification(){return Config.AUTHENTIFICATION;}
    public static String getListaActivites(){return Config.LISTE_ACTIVITE;}
    public static String getDetailsActivites(){return Config.DETAILS_ACTIVITE;}
    public static String getLienCommande(){return Config.COMMANDER_ACTIVITE;}

}
