package br.edu.ifpe.tads.pdm.android_armazenamento;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonSave = (Button) findViewById(R.id.button_save);
        Button buttonRead = (Button) findViewById(R.id.button_read);
        CheckBox checkBoxSaveToFile = (CheckBox) findViewById(R.id.checkbox_savetofile);
        buttonSave.setOnClickListener(buttonSaveClick());
        buttonRead.setOnClickListener(buttonReadClick());
        checkBoxSaveToFile.setOnCheckedChangeListener(checkBoxChange());
    }

    public Context getContext() {
        return this;
    }

    public View.OnClickListener buttonSaveClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox checkBoxSaveToFile = (CheckBox) findViewById(R.id.checkbox_savetofile);
                EditText editTextPrefName = (EditText) findViewById(R.id.edit_pref_name);
                EditText editTextPrefValue = (EditText) findViewById(R.id.edit_pref_value);

                String prefName = editTextPrefName.getText().toString();
                String prefValue = editTextPrefValue.getText().toString();

                Boolean saveToFile = checkBoxSaveToFile.isChecked();
                if (!saveToFile) {
                    SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(prefName, prefValue);
                    editor.apply();
                } else {
                    DataOutputStream outputStream;
                    try {
                        FileOutputStream fileOutputStream = openFileOutput(prefName, Context.MODE_PRIVATE);
                        outputStream = new DataOutputStream(fileOutputStream);
                        outputStream.writeUTF(prefValue);
                        outputStream.close();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage(), e);
                    }
                }
                Toast.makeText(getContext(), "Saved: " + prefName + " = " + prefValue +
                        (saveToFile ? " to file" : ""), Toast.LENGTH_SHORT).show();
            }
        };
    }

    public View.OnClickListener buttonReadClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox checkBoxSaveToFile = (CheckBox) findViewById(R.id.checkbox_savetofile);
                EditText editTextPrefName = (EditText) findViewById(R.id.edit_pref_name);
                EditText editTextPrefValue = (EditText) findViewById(R.id.edit_pref_value);
                String prefName = editTextPrefName.getText().toString();
                String prefValue = "ERROR";

                if ( !checkBoxSaveToFile.isChecked() ) {
                    SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                    prefValue = prefs.getString(prefName, "NOT_FOUND");
                } else {
                    DataInputStream dataInputStream;
                    try {
                        InputStream inputStream = openFileInput(prefName);
                        dataInputStream = new DataInputStream(inputStream);
                        prefValue = dataInputStream.readUTF();
                        inputStream.close();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage(), e);
                    }
                }

                editTextPrefValue.setText(prefValue);
            }
        };
    }

    public CheckBox.OnCheckedChangeListener checkBoxChange() {
        return new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText editTextPrefName = (EditText) findViewById(R.id.edit_pref_name);
                EditText editTextPrefValue = (EditText) findViewById(R.id.edit_pref_value);
                CheckBox checkBoxSaveToFile = (CheckBox) findViewById(R.id.checkbox_savetofile);
                if ( checkBoxSaveToFile.isChecked() ) {
                    editTextPrefName.setHint(R.string.file_name);
                    editTextPrefValue.setHint(R.string.file_value);
                } else {
                    editTextPrefName.setHint(R.string.pref_name);
                    editTextPrefValue.setHint(R.string.pref_value);
                }
            }
        };

    }

 }
