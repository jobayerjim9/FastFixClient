package com.servicing.jobaer.fastfixclient.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.servicing.jobaer.fastfixclient.R;
import com.servicing.jobaer.fastfixclient.view.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {
    EditText name,subject,message;
    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_contact, container, false);

        Button send=v.findViewById(R.id.sendButton);
        name=v.findViewById(R.id.name);
        subject=v.findViewById(R.id.subject);
        message=v.findViewById(R.id.message);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        return v;
    }

    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"bilaljmal@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, message.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Finished", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(),
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
