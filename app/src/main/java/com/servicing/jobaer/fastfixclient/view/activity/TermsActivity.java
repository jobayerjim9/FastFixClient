package com.servicing.jobaer.fastfixclient.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.servicing.jobaer.fastfixclient.R;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        CheckBox checkBox = findViewById(R.id.termsBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent i = new Intent(Intent.ACTION_DIAL);
                    String p = "tel:" + "00966540805117";
                    i.setData(Uri.parse(p));
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}
