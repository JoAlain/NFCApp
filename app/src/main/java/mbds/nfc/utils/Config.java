package mbds.nfc.utils;

/**
 * Created by Joe on 26/03/2017.
 */

public class Config {

    //public static final String SERVEUR = "http://192.168.2.53/";
    public static final String SERVEUR = "http://g-park-mbds.esy.es/nfcController";
    public static final String AUTHENTIFICATION = SERVEUR+"/connection.php?";
    public static final String LISTE_COMMANDE = SERVEUR+"/listerCommande.php?";
    public static final String LISTE_ACTIVITE = SERVEUR+"/listerActivite.php";
    public static final String DETAILS_ACTIVITE = SERVEUR+"/detailsActivite.php?";
    public static final String COMMANDER_ACTIVITE = SERVEUR+"/commander.php?";


}
