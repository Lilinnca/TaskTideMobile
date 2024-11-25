package com.example.tasktide;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Atividade;
import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Informacoes;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class VisaoGeral extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private ImageView imgBanner;
    private EditText txtMostraNomeDoEvento, edtxtMostraDescricao;
    private EditText txtMostraLocalDoEvento;
    private EditText txtMostraDataDoEvento;
    private EditText txtMostraHoraDeInicioEvento;
    private EditText txtMostraHoraDeTerminoEvento;
    private EditText txtMostraTipoDoEvento, edtxtMostraPrazoInscricao, txtMostraHorasComplementaresEvento, edtxtMostrarValorDoEvento;
    private ImageButton imgbtnEditarValorEvento, imgbtnEditarDescricao;
    private TextView  txtHoraDeInicioEvento, txtHoraDeTerminoEvento, txtValorDoEvento,txtDescricaoVG;
    private ImageButton imgbtnAlterarNome, imgbtnAlterarLocal, imgbtnAlterarData, imgbtnAlterarHorarioInicio, imgbtnEditarHoraDeTermino, imgbtnEditarTipoEvento, imgButtonEditarPrazoIncricao;
    private DAO dao;
    private long idEvento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visao_geral);


        imgBanner = findViewById(R.id.imgBanner);
        ImageView btnMudarBanner = findViewById(R.id.imgbtnMudarBanner);
        ImageButton imgbtnCriarCronograma = findViewById(R.id.imgbtnCriarCronograma);


        txtDescricaoVG = findViewById(R.id.txtDescricaoVG);
        imgbtnEditarDescricao = findViewById(R.id.imgbtnEditarDescricao);
        edtxtMostraDescricao = findViewById(R.id.edtxtMostraDescricao);
        edtxtMostraPrazoInscricao = findViewById(R.id.edtxtMostraPrazoInscricao);
        txtHoraDeInicioEvento = findViewById(R.id.txtHoraDeInicioEvento);
        txtHoraDeTerminoEvento = findViewById(R.id.txtHoraDeTerminoEvento);
        txtMostraLocalDoEvento = findViewById(R.id.txtMostraLocalDoEvento);
        txtMostraNomeDoEvento = findViewById(R.id.txtMostraNomeDoEvento);
        txtMostraDataDoEvento = findViewById(R.id.txtMostraDataDoEvento);
        txtMostraHoraDeInicioEvento = findViewById(R.id.txtMostraHoraDeInicioEvento);
        txtMostraHoraDeTerminoEvento = findViewById(R.id.txtMostraHoraDeTerminoEvento);
        txtMostraTipoDoEvento = findViewById(R.id.txtMostraTipoDoEvento);
        txtMostraHorasComplementaresEvento = findViewById(R.id.txtMostraHorasComplementaresEvento);


        imgbtnAlterarNome = findViewById(R.id.imgbtnAlterarNome);
        imgbtnAlterarLocal = findViewById(R.id.imgbtnAlterarLocal);
        imgbtnAlterarData = findViewById(R.id.imgbtnAlterarData);
        imgbtnAlterarHorarioInicio = findViewById(R.id.imgbtnAlterarHorarioInicio);
        imgbtnEditarHoraDeTermino = findViewById(R.id.imgbtnEditarHoraDeTermino);
        imgbtnEditarTipoEvento = findViewById(R.id.imgbtnEditarTipoEvento);
        imgButtonEditarPrazoIncricao = findViewById(R.id.imgButtonEditarPrazoIncricao);


        txtValorDoEvento = findViewById(R.id.txtValorDoEvento);
        edtxtMostrarValorDoEvento = findViewById(R.id.edtxtMostrarValorDoEvento);
        imgbtnEditarValorEvento = findViewById(R.id.imgbtnEditarValorEvento);

        dao = new DAO(this);

        Intent intent = getIntent();
        long eventoId = intent.getLongExtra("evento_id", -1);

        Evento evento = dao.buscarEventoPorId(eventoId);
        if (evento != null) {
            txtMostraNomeDoEvento.setText(evento.getNomeEvento());
            edtxtMostraDescricao.setText(evento.getDescricao());
            txtMostraTipoDoEvento.setText(evento.getTipoEvento());
            edtxtMostraPrazoInscricao.setText(evento.getTipoEvento());
            txtMostraHorasComplementaresEvento.setText(evento.getHorasComplementares());

            String[] informacoes = dao.buscarInformacoesPorEvento(eventoId);
            txtMostraDataDoEvento.setText("De " + formatarData(informacoes[0]) + " até " + formatarData(informacoes[1]));
            txtMostraLocalDoEvento.setText(!informacoes[2].isEmpty() ? informacoes[2] : "Local não definido");
        } else {
            Toast.makeText(this, "Evento não encontrado", Toast.LENGTH_SHORT).show();
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

        //btnMudarBanner.setOnClickListener(v -> openImageChooser());
        btnMudarBanner.setOnClickListener(v -> showImageSizeWarningDialog());


        ImageButton btnPopupMenu = findViewById(R.id.btnPopupMenu);
        btnPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(VisaoGeral.this, v);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.SeInscrever) {
                            seInscrever();
                            return true;
                        } else if (itemId == R.id.voltar) {
                            Intent intent = new Intent(VisaoGeral.this, MeusEventosCriador.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.visualizarCronograma) {
                            visualizarCronograma();
                            return true;
                        }
                        return false;
                    }
                });


                popup.show();
            }
        });

        imgbtnCriarCronograma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VisaoGeral.this);
                builder.setTitle("Criar Cronograma");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.adicionar_atividades, null);
                        AlertDialog dialogAtividades = new AlertDialog.Builder(VisaoGeral.this)
                                .setView(dialogView)
                                .setCancelable(true)
                                .create();

                        Button btnEscolherDataHorario = dialogView.findViewById(R.id.btnEscolherDataHorario);
                        btnEscolherDataHorario.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                escolherDataEHoras(btnEscolherDataHorario);
                            }
                        });

                        EditText edtNomeAtividade = dialogView.findViewById(R.id.edtNomeAtividade);
                        EditText edtLocalAtividade = dialogView.findViewById(R.id.edtLocalAtividade);
                        EditText edtResponsavelAtividade = dialogView.findViewById(R.id.edtResponsavelAtividade);

                        dialogView.findViewById(R.id.btnAdicionarAtividade).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String horario = btnEscolherDataHorario.getText().toString();

                                String nomeAtividade = edtNomeAtividade.getText().toString();
                                String localAtividade = edtLocalAtividade.getText().toString();
                                String responsavel = edtResponsavelAtividade.getText().toString();
                                String data = "";

                                if (nomeAtividade.isEmpty() || localAtividade.isEmpty() || responsavel.isEmpty() || horario.isEmpty()) {
                                    Toast.makeText(VisaoGeral.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                                } else {
                                    dao.adicionarAtividade(idEvento, nomeAtividade, horario, responsavel, localAtividade, data);

                                    edtNomeAtividade.setText("");
                                    edtLocalAtividade.setText("");
                                    edtResponsavelAtividade.setText("");
                                    btnEscolherDataHorario.setText("Escolher Data e Horário");

                                    Toast.makeText(VisaoGeral.this, "Atividade adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        Button btnCancelar = new Button(VisaoGeral.this);
                        btnCancelar.setText("Cancelar");
                        btnCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogAtividades.dismiss();
                            }
                        });

                        ((LinearLayout) dialogView).addView(btnCancelar);
                        dialogAtividades.show();
                    }
                });


                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        imgbtnAlterarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarEdicao(txtMostraNomeDoEvento);
            }
        });

        imgbtnAlterarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarEdicao(txtMostraLocalDoEvento);
            }
        });

        imgbtnAlterarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarEdicao(txtMostraDataDoEvento);
            }
        });

        imgbtnAlterarHorarioInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarEdicao(txtMostraHoraDeInicioEvento);
            }
        });

        imgbtnEditarHoraDeTermino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarEdicao(txtMostraHoraDeTerminoEvento);
            }
        });

        imgButtonEditarPrazoIncricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarEdicao(edtxtMostraPrazoInscricao);
            }
        });

        imgbtnEditarValorEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarEdicao(edtxtMostrarValorDoEvento);
            }
        });

        imgbtnEditarDescricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarEdicao(edtxtMostraDescricao);
            }
        });

        imgbtnEditarTipoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VisaoGeral.this);
                builder.setTitle("Escolha o Tipo de Evento");

                String[] tiposDeEvento = {"Atividade cultural", "Atividade esportiva", "Colóquio",
                        "Conferência", "Congresso", "Encontro", "Fórum",
                        "Mesa-redonda", "Palestra", "Seminário", "Visita técnica", "Workshop"};

                builder.setItems(tiposDeEvento, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String novoTipoEvento = tiposDeEvento[which];
                        txtMostraTipoDoEvento.setText(novoTipoEvento);

                        String novasHorasComplementares = calcularHorasComplementares(novoTipoEvento);
                        txtMostraHorasComplementaresEvento.setText(novasHorasComplementares);

                        atualizarTipoEhorasNoBanco(novoTipoEvento, novasHorasComplementares);

                        Toast.makeText(VisaoGeral.this, "Tipo do evento atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    private void atualizarTipoEhorasNoBanco(String tipoEvento, String horasComplementares) {
        if (idEvento != -1) {
            if (dao == null) {
                dao = new DAO(this);  // Verifique se a instância de DAO está correta
            }
            dao.atualizarEventoTipoEhoras(idEvento, tipoEvento, horasComplementares);
        }
    }



    private String formatarData(String data) {
        if (data == null || data.isEmpty()) {
            return "Data inválida";
        }
        try {
            // Verifique se o formato da data é "dd/MM/yyyy"
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdfEntrada.parse(data);  // Parse a data no formato esperado

            SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfSaida.format(date);  // Retorne a data formatada
        } catch (Exception e) {
            e.printStackTrace();
            return "Data inválida";
        }
    }

    private String calcularHorasComplementares(String selectedItem) {
        switch (selectedItem) {
            case "Atividade cultural":
                return "3 horas";
            case "Atividade esportiva":
                return "4 horas";
            case "Colóquio":
            case "Conferência":
            case "Congresso":
            case "Encontro":
            case "Fórum":
                return "5 horas";
            case "Mesa-redonda":
            case "Palestra":
                return "2 horas";
            case "Seminário":
            case "Visita técnica":
            case "Workshop":
                return "5 horas";
            default:
                return "";
        }
    }

    private void escolherDataEHoras(Button btnEscolherDataHorario) {
        Locale locale = new Locale("pt", "BR");
        Locale.setDefault(locale);

        Calendar calendario = Calendar.getInstance();
        int ano = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(VisaoGeral.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int anoEscolhido, int mesEscolhido, int diaEscolhido) {
                //agora mostra o TimePickerDialog para escolher o horário
                TimePickerDialog timePickerDialog = new TimePickerDialog(VisaoGeral.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int horaEscolhida, int minutoEscolhido) {
                        // Data e horário foram escolhidos
                        String dataHorarioEscolhido = String.format("%02d/%02d/%d - %02d:%02d", diaEscolhido, mesEscolhido + 1, anoEscolhido, horaEscolhida, minutoEscolhido);
                        btnEscolherDataHorario.setText(dataHorarioEscolhido); // Exibe a data e o horário no botão
                    }
                }, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        }, ano, mes, dia);
        datePickerDialog.show();
    }

    private void visualizarCronograma() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VisaoGeral.this);
        builder.setTitle("Cronograma");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.tabela, null);
        TableLayout tableLayout = dialogView.findViewById(R.id.tableLayoutCronograma);

        // Obter atividades
        List<Atividade> atividades = dao.buscarAtividadesPorEvento(idEvento);

        // Adicionar cabeçalhos, se necessário
        TableRow headerRow = new TableRow(VisaoGeral.this);
        headerRow.setBackgroundColor(Color.LTGRAY);  // Adiciona uma cor de fundo para a linha do cabeçalho
        addHeaderCell(headerRow, "Data e Horário");
        addHeaderCell(headerRow, "Nome da Atividade");
        addHeaderCell(headerRow, "Palestrante");
        addHeaderCell(headerRow, "Local");

        tableLayout.addView(headerRow);

        // Adicionar as atividades na tabela
        for (Atividade atividade : atividades) {
            TableRow row = new TableRow(VisaoGeral.this);
            row.setPadding(8, 8, 8, 8);

            // Coluna de data e horário
            addCellToRow(row, atividade.getData() + " - " + atividade.getHorario());

            // Coluna de nome da atividade
            addCellToRow(row, atividade.getNomeAtividade());

            // Coluna de palestrante
            addCellToRow(row, atividade.getPalestrante());

            // Coluna de local
            addCellToRow(row, atividade.getLocalAtividade());

            // Adiciona a linha à tabela
            tableLayout.addView(row);
        }

        builder.setView(dialogView);
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addHeaderCell(TableRow row, String text) {
        TextView cell = new TextView(VisaoGeral.this);
        cell.setText(text);
        cell.setPadding(12, 12, 12, 12);
        cell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        row.addView(cell);
    }

    private void addCellToRow(TableRow row, String text) {
        TextView cell = new TextView(VisaoGeral.this);
        cell.setText(text);
        cell.setPadding(12, 12, 12, 12);
        cell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
        row.addView(cell);
    }

    private void habilitarEdicao(final EditText editText) {
        editText.setEnabled(true);
        editText.requestFocus();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    salvarDados(editText);
                    editText.setEnabled(false);
                    // Esconde o teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    salvarDados(editText);
                    editText.setEnabled(false);
                }
            }
        });
    }

    private void salvarDados(EditText editText) {
        String novoTexto = editText.getText().toString().trim();

        if (editText.getId() == R.id.txtMostraNomeDoEvento) {
            dao.atualizarNomeEvento(idEvento, novoTexto);
        } else if (editText.getId() == R.id.txtMostraLocalDoEvento) {
            dao.atualizarLocalEvento(idEvento, novoTexto);
        } else if (editText.getId() == R.id.txtMostraDataDoEvento) {
            dao.atualizarDataEvento(idEvento, novoTexto);
        } else if (editText.getId() == R.id.txtMostraHoraDeInicioEvento) {
            dao.atualizarHorarioInicio(idEvento, novoTexto);
        } else if (editText.getId() == R.id.txtMostraHoraDeTerminoEvento) {
            dao.atualizarHorarioTermino(idEvento, novoTexto);
        } else if (editText.getId() == R.id.txtMostraTipoDoEvento) {
            dao.atualizarTipoEvento(idEvento, novoTexto);
        } else if (editText.getId() == R.id.edtxtMostraPrazoInscricao) {
            dao.atualizarPrazoEvento(idEvento, novoTexto);
        } else if (editText.getId() == R.id.edtxtMostrarValorDoEvento) {
            dao.atualizarValorEvento(idEvento, novoTexto);
        }else if (editText.getId() == R.id.edtxtMostraDescricao) {
            dao.atualizarDescricaoEvento(idEvento, novoTexto);
        }

        Log.d("VisaoGeral", "Dados salvos: " + novoTexto);
    }
    private void configurarCamposDeHora(long idEvento) {
        Informacoes informacoes = dao.getInformacoesById(idEvento);

        if (informacoes != null) {
            String horaInicio = informacoes.getHorarioInicio();
            String horarioFim = informacoes.getHorarioFim();

            if (horaInicio != null && !horaInicio.isEmpty()) {
                txtHoraDeInicioEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeInicioEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeInicioEvento.setText(horaInicio);
                ImageButton imgbtnAlterarHorarioInicio = findViewById(R.id.imgbtnAlterarHorarioInicio);
                imgbtnAlterarHorarioInicio.setVisibility(View.VISIBLE);
            } else {
                txtHoraDeInicioEvento.setVisibility(View.GONE);
                txtMostraHoraDeInicioEvento.setVisibility(View.GONE);
            }

            if (horarioFim != null && !horarioFim.isEmpty()) {
                txtHoraDeTerminoEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeTerminoEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeTerminoEvento.setText(horarioFim);
                // Tornar visível o botão de editar horário de término
                ImageButton imgbtnEditarHoraDeTermino = findViewById(R.id.imgbtnEditarHoraDeTermino);
                imgbtnEditarHoraDeTermino.setVisibility(View.VISIBLE);
            } else {
                txtHoraDeTerminoEvento.setVisibility(View.GONE);
                txtMostraHoraDeTerminoEvento.setVisibility(View.GONE);
            }
        }
    }


    private void configurarDescricaoDoEvento(long idEvento) {
        String descricao = dao.obterDescricaoEvento(idEvento);

        EditText edtxtMostraDescricao = findViewById(R.id.edtxtMostraDescricao);
        ImageButton imgbtnEditarDescricao = findViewById(R.id.imgbtnEditarDescricao);
        TextView txtDescricaoVG = findViewById(R.id.txtDescricaoVG);

        if (descricao != null && !descricao.isEmpty()) {
            edtxtMostraDescricao.setText(descricao);
            edtxtMostraDescricao.setVisibility(View.VISIBLE);
            imgbtnEditarDescricao.setVisibility(View.VISIBLE);
            txtDescricaoVG.setVisibility(View.VISIBLE);

            edtxtMostraDescricao.setMinLines(1);
            edtxtMostraDescricao.setMaxLines(10);
        } else {
            edtxtMostraDescricao.setVisibility(View.GONE);
            imgbtnEditarDescricao.setVisibility(View.GONE);
            txtDescricaoVG.setVisibility(View.GONE);
        }
    }

    private void configurarCamposValor(Double valorEvento) {
        if (valorEvento != null && valorEvento > 0) {
            txtValorDoEvento.setVisibility(View.VISIBLE);
            edtxtMostrarValorDoEvento.setVisibility(View.VISIBLE);
            imgbtnEditarValorEvento.setVisibility(View.VISIBLE);
            edtxtMostrarValorDoEvento.setText(String.format("%.2f", valorEvento));
        } else {
            txtValorDoEvento.setVisibility(View.GONE);
            edtxtMostrarValorDoEvento.setVisibility(View.GONE);
            imgbtnEditarValorEvento.setVisibility(View.GONE);
        }
    }

    private void limparIdEvento() {
        SharedPreferences prefs = getSharedPreferences("EventPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("EVENTO_ID");
        editor.apply();
    }


    private void seInscrever() {
    }


    private void preencherCamposComDadosDoEvento(long idEvento) {
        Evento evento = dao.getEventoById((int)idEvento);
        Informacoes informacoes = dao.getInformacoesById(idEvento);


        if (evento != null) {
            txtMostraNomeDoEvento.setText(evento.getNomeEvento());
            txtMostraTipoDoEvento.setText(evento.getTipoEvento());
            txtMostraHorasComplementaresEvento.setText(evento.getHorasComplementares());

            edtxtMostraDescricao.setText(evento.getDescricao());
            edtxtMostraDescricao.setEnabled(true);
            edtxtMostraDescricao.setFocusable(true);
        }


        byte[] imagemBytes = dao.obterBannerEvento(idEvento);
        if (imagemBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagemBytes, 0, imagemBytes.length);
            imgBanner.setImageBitmap(bitmap);
        }


        if (informacoes != null) {
            txtMostraLocalDoEvento.setText(informacoes.getLocal());
            txtMostraDataDoEvento.setText(informacoes.getDataPrevista());

            if (informacoes.getHorarioInicio() != null && !informacoes.getHorarioInicio().isEmpty()) {
                txtHoraDeInicioEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeInicioEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeInicioEvento.setText(informacoes.getHorarioInicio());
            } else {
                txtHoraDeInicioEvento.setVisibility(View.GONE);
                txtMostraHoraDeInicioEvento.setVisibility(View.GONE);
            }

            if (informacoes.getHorarioFim() != null && !informacoes.getHorarioFim().isEmpty()) {
                txtHoraDeTerminoEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeTerminoEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeTerminoEvento.setText(informacoes.getHorarioFim());
            } else {
                txtHoraDeTerminoEvento.setVisibility(View.GONE);
                txtMostraHoraDeTerminoEvento.setVisibility(View.GONE);
            }

            double valorEvento = informacoes.getValorEvento();
            edtxtMostrarValorDoEvento.setText(String.format("%.2f", valorEvento));
            edtxtMostraPrazoInscricao.setText(informacoes.getPrazo());

            configurarCamposValor(valorEvento);
        }
    }

    private void showImageSizeWarningDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Aviso de Imagem")
                .setMessage("A imagem deve ter preferencialmente 310 x 160 pixels. Deseja continuar?")
                .setPositiveButton("Continuar", (dialog, which) -> openImageChooser())
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                // Converte a imagem para um Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgBanner.setImageBitmap(bitmap);

                // Converte o Bitmap para byte[]
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imageBytes = outputStream.toByteArray();

                // Atualiza o banner no banco de dados
                dao.atualizarBannerEvento(idEvento, imageBytes);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao processar a imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }
}