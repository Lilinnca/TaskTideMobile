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
        // Não é necessário implementar nada aqui no momento
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(current)) {
            String clean = s.toString().replaceAll("[^\\d]", ""); // Remove tudo que não for dígito
            String cleanCurrent = current.replaceAll("[^\\d]", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }

            // Garantir que no máximo 8 dígitos sejam inseridos
            if (clean.length() < 8) {
                clean += ddmmyyyy.substring(clean.length());
            } else {
                int day = Integer.parseInt(clean.substring(0, 2));
                int month = Integer.parseInt(clean.substring(2, 4));
                int year = Integer.parseInt(clean.substring(4, 8));

                // Ajuste do mês para estar no intervalo [1, 12]
                month = Math.max(1, Math.min(month, 12));
                cal.set(Calendar.MONTH, month - 1);

                // Ajuste do ano para estar no intervalo [1900, 2100]
                year = Math.max(1900, Math.min(year, 2100));
                cal.set(Calendar.YEAR, year);

                // Ajuste do dia para ser válido no mês e ano selecionados
                day = Math.min(day, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

                // Formatação da data no padrão DDMMYYYY
                clean = String.format("%02d%02d%04d", day, month, year);
            }

            // Inserção das barras para formar o formato DD/MM/YYYY
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
        // Não é necessário implementar nada aqui no momento
    }
}
