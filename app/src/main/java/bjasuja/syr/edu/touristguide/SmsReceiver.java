package bjasuja.syr.edu.touristguide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    String number;
    String message ;
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage msg;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            msg = msgs[0];
        } else {
            Object pdus[] = (Object[]) intent.getExtras().get("pdus");
            msg = SmsMessage.createFromPdu((byte[]) pdus[0]);
        }

        number = msg.getOriginatingAddress();
        message = msg.getMessageBody();

        if(Signup.number.equals(number))
        {
            if(Signup.message.equals(message))
            {
              Signup.getInstace().setText(message.substring(36));
            }
        }
    }
}
