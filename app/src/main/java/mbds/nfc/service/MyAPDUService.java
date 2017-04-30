package mbds.nfc.service;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

/**
 * Created by Joe on 09/03/2017.
 */

public class MyAPDUService extends HostApduService {

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        //Process commandApdu
        return commandApdu; //byteResponse;
    }

    @Override
    public void onDeactivated(int reason) {

    }
}
