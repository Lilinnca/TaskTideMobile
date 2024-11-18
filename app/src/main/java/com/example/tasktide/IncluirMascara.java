package com.example.tasktide;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Calendar;

public class IncluirMascara implements TextWatcher {

    private final EditText editText;
    private String current = "";
    private final String ddmmyyyy = "DDMMYYYY";
    private final Calendar cal = Calendar.getInstance();

    public IncluirMascara(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(current)) {
            String clean = s.toString().replaceAll("[^\\d]", "");
            String cleanCurrent = current.replaceAll("[^\\d]", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }

            if (clean.length() < 8) {
                clean += ddmmyyyy.substring(clean.length());
            } else {
                int day = Integer.parseInt(clean.substring(0, 2));
                int month = Integer.parseInt(clean.substring(2, 4));
                int year = Integer.parseInt(clean.substring(4, 8));

                month = Math.max(1, Math.min(month, 12));
                cal.set(Calendar.MONTH, month - 1);

                year = Math.max(1900, Math.min(year, 2100));
                cal.set(Calendar.YEAR, year);

                day = Math.min(day, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

                clean = String.format("%02d%02d%04d", day, month, year);
            }

            clean = String.format("%s/%s/%s",
                    clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = Math.min(sel, clean.length());
            current = clean;
            editText.setText(current);
            editText.setSelection(sel);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
