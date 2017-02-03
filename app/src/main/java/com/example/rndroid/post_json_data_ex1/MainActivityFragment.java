package com.example.rndroid.post_json_data_ex1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    TextView textView;
    Button buttonPost, buttonPostDelay;
    EditText editTextName, editTextCountry, editTextTwitter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("show_response");
        MyReciever myReciever = new MyReciever();
        getActivity().registerReceiver(myReciever, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        editTextCountry = (EditText) v.findViewById(R.id.edittext_country);
        editTextName = (EditText) v.findViewById(R.id.edittext_name);
        editTextTwitter = (EditText) v.findViewById(R.id.edittext_twitter);
        textView = (TextView) v.findViewById(R.id.textview);
        buttonPost = (Button) v.findViewById(R.id.button_post);
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyService.class);
                intent.putExtra("name", editTextName.getText().toString());
                intent.putExtra("country", editTextCountry.getText().toString());
                intent.putExtra("twitter", editTextTwitter.getText().toString());
                getActivity().startService(intent);
            }
        });

        // posting data after 1 min - using Alarm
        buttonPostDelay = (Button) v.findViewById(R.id.button_post_repeat);
        buttonPostDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);// create alarm manager
                Intent intent = new Intent(getActivity(), MyService.class);
                intent.putExtra("name", editTextName.getText().toString());
                intent.putExtra("country", editTextCountry.getText().toString());
                intent.putExtra("twitter", editTextTwitter.getText().toString());
                PendingIntent pendingIntent = PendingIntent.getService(getActivity(),0,intent,0);// prepare pending intent
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+6000, 6000, pendingIntent);//set time
                alarmManager.set(AlarmManager.RTC_WAKEUP, 6000, pendingIntent);//set time
            }
        });
        return v;
    }
// reciever for geting  response from service
    public class MyReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String response = bundle.getString("response");
            textView.setText(response);
        }
    }
}
