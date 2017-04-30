package mbds.nfc.service;

import android.content.Intent;
import android.nfc.cardemulation.OffHostApduService;
import android.os.IBinder;

/**
 * Created by Joe on 09/03/2017.
 */

public class MyOffHostAPDUService extends OffHostApduService {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
