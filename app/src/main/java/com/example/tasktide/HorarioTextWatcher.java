package com.example.tasktide;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


public class HorarioTextWatcher implements TextWatcher {


    private EditText editText;


    public HorarioTextWatcher(EditText editText) {
        this.editText = editText;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0 && s.length() == 2 && !s.toString().contains(":")) {
            String newText = s.toString() + ":";
            editText.removeTextChangedListener(this);
            editText.setText(newText);
            editText.setSelection(newText.length());
            editText.addTextChangedListener(this);
        } else if (s.length() > 5) {
            String newText = s.toString().substring(0, 5);
            editText.removeTextChangedListener(this);
            editText.setText(newText);
            editText.setSelection(newText.length());
            editText.addTextChangedListener(this);
        }
    }


    @Override
    public void afterTextChanged(Editable s) {}
}

