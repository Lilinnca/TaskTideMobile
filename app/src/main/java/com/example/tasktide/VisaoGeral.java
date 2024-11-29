package com.example.tasktide;

import static com.example.tasktide.DAO.DAO.TABELA_EVENTO;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

import com.bumptech.glide.Glide;
import com.example.tasktide.DAO.DAO;
import com.example.tasktide.Objetos.Atividade;
import com.example.tasktide.Objetos.Evento;
import com.example.tasktide.Objetos.Informacoes;
import com.example.tasktide.Objetos.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VisaoGeral extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PICK_IMAGE_REQUEST_CODE = 2;
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
    private Button btnInscrever, btnComprarIngresso;
    private DAO dao;
    private long idEvento;
    private long usuarioId;
    private boolean isEditingNome = false;
    private boolean isEditingLocal = false;
    private boolean isEditingHorarioInicio = false;
    private boolean isEditingHorarioTermino = false;
    private boolean isEditingPrazoInscricao = false;
    private boolean isEditingValorEvento = false;
    private boolean isEditingDescricao = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visao_geral);

        imgBanner = findViewById(R.id.imgBanner);
        ImageView btnMudarBanner = findViewById(R.id.imgbtnMudarBanner);
        ImageButton imgbtnCriarCronograma = findViewById(R.id.imgbtnCriarCronograma);

        ImageButton imgbtnMudarBanner = findViewById(R.id.imgbtnMudarBanner);

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
        btnInscrever = findViewById(R.id.btnInscrever);
        btnComprarIngresso = findViewById(R.id.btnComprarIngresso);



        dao = new DAO(this);


        Intent intent = getIntent();
        long eventoId = intent.getLongExtra("evento_id", -1);


        usuarioId = getUsuarioId();

        Evento evento = dao.buscarEventoPorId(eventoId);
        if (evento != null) {
            configurarCamposDeHora(eventoId);
            configurarDescricaoDoEvento(eventoId);

            txtMostraNomeDoEvento.setText(evento.getNomeEvento());
            edtxtMostraDescricao.setText(evento.getDescricao());
            txtMostraTipoDoEvento.setText(evento.getTipoEvento());
            txtMostraHorasComplementaresEvento.setText(evento.getHorasComplementares());

            String[] informacoes = dao.buscarInformacoesPorEvento(eventoId);

            txtMostraDataDoEvento.setText("De " + formatarData(informacoes[0]) + " até " + formatarData(informacoes[1]));
            txtMostraLocalDoEvento.setText(!informacoes[2].isEmpty() ? informacoes[2] : "Local não definido");
            edtxtMostraPrazoInscricao.setText(formatarData(informacoes[3]));

            try {
                double valorEvento = Double.parseDouble(informacoes[4].isEmpty() ? "0.00" : informacoes[4]);
                edtxtMostrarValorDoEvento.setText(String.format("%.2f", valorEvento));
                configurarCamposValor(valorEvento);
            } catch (NumberFormatException e) {
                edtxtMostrarValorDoEvento.setText("0.00");
            }

            txtMostraHoraDeInicioEvento.setText(!informacoes[5].isEmpty() ? informacoes[5] : "Não definido");
            if (informacoes[6] != null && !informacoes[6].isEmpty()) {
                txtHoraDeTerminoEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeTerminoEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeTerminoEvento.setText(informacoes[6]);
            } else {
                txtHoraDeTerminoEvento.setVisibility(View.GONE);
                txtMostraHoraDeTerminoEvento.setVisibility(View.GONE);
            }

            carregarBanner(eventoId);
        } else {
            Toast.makeText(this, "Evento não encontrado", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

        boolean vemDaTelaInicial = intent.getBooleanExtra("VEM_DA_TELA_INICIAL", false);

        if (vemDaTelaInicial) {
            imgbtnCriarCronograma.setVisibility(View.GONE);
            imgbtnEditarDescricao.setVisibility(View.GONE);
            imgbtnEditarTipoEvento.setVisibility(View.GONE);
            imgbtnEditarValorEvento.setVisibility(View.GONE);
            imgButtonEditarPrazoIncricao.setVisibility(View.GONE);
            imgbtnAlterarData.setVisibility(View.GONE);
            imgbtnAlterarLocal.setVisibility(View.GONE);
            imgbtnAlterarNome.setVisibility(View.GONE);
            imgbtnAlterarHorarioInicio.setVisibility(View.GONE);
            imgbtnEditarHoraDeTermino.setVisibility(View.GONE);
            imgbtnMudarBanner.setVisibility(View.GONE);
            configurarVisibilidadeBotoes(idEvento);
        }

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
                        if (itemId == R.id.voltar) {
                            onBackPressed();
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
                                String data = horario;

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

                        // Adicionando o botão de cancelar ao layout do diálogo
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
                if (!isEditingNome) {
                    txtMostraNomeDoEvento.setEnabled(true);
                    imgbtnAlterarNome.setImageResource(R.drawable.botao_salvar);
                } else {
                    String nomeAlterado = txtMostraNomeDoEvento.getText().toString();
                    dao.atualizarNomeEvento(nomeAlterado, eventoId);
                    txtMostraNomeDoEvento.setText(nomeAlterado);

                    txtMostraNomeDoEvento.setEnabled(false);
                    imgbtnAlterarNome.setImageResource(R.drawable.editarinformacoes);
                    Toast.makeText(getApplicationContext(), "Nome do evento atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                }
                isEditingNome = !isEditingNome;
            }
        });


        imgbtnAlterarLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditingLocal) {
                    txtMostraLocalDoEvento.setEnabled(true);
                    imgbtnAlterarLocal.setImageResource(R.drawable.botao_salvar);
                } else {
                    String localAlterado = txtMostraLocalDoEvento.getText().toString();
                    dao.atualizarLocalEvento(localAlterado, eventoId);

                    txtMostraLocalDoEvento.setText(localAlterado);
                    txtMostraLocalDoEvento.setEnabled(false);
                    imgbtnAlterarLocal.setImageResource(R.drawable.editarinformacoes);

                    Toast.makeText(getApplicationContext(), "Local do evento atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                }
                isEditingLocal = !isEditingLocal;
            }
        });


        imgbtnAlterarHorarioInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditingHorarioInicio) {
                    txtHoraDeInicioEvento.setEnabled(true);
                    imgbtnAlterarHorarioInicio.setImageResource(R.drawable.botao_salvar);
                } else {
                    String horarioInicioAlterado = txtHoraDeInicioEvento.getText().toString();
                    dao.atualizarHorarioInicioEvento(horarioInicioAlterado, eventoId);

                    txtHoraDeInicioEvento.setText(horarioInicioAlterado);
                    txtHoraDeInicioEvento.setEnabled(false);
                    imgbtnAlterarHorarioInicio.setImageResource(R.drawable.editarinformacoes);

                    Toast.makeText(getApplicationContext(), "Horário de início do evento atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                }
                isEditingHorarioInicio = !isEditingHorarioInicio;
            }
        });


        imgbtnEditarHoraDeTermino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditingHorarioTermino) {
                    txtHoraDeTerminoEvento.setEnabled(true);
                    imgbtnEditarHoraDeTermino.setImageResource(R.drawable.botao_salvar);
                } else {
                    String horarioTerminoAlterado = txtHoraDeTerminoEvento.getText().toString();
                    dao.atualizarHorarioTerminoEvento(horarioTerminoAlterado, eventoId);

                    txtHoraDeTerminoEvento.setText(horarioTerminoAlterado);
                    txtHoraDeTerminoEvento.setEnabled(false);
                    imgbtnEditarHoraDeTermino.setImageResource(R.drawable.editarinformacoes);

                    Toast.makeText(getApplicationContext(), "Hora de término do evento atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                }
                isEditingHorarioTermino = !isEditingHorarioTermino;
            }
        });


        imgButtonEditarPrazoIncricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditingPrazoInscricao) {
                    edtxtMostraPrazoInscricao.setEnabled(true);
                    imgButtonEditarPrazoIncricao.setImageResource(R.drawable.botao_salvar);
                } else {
                    String prazoInscricaoAlterado = edtxtMostraPrazoInscricao.getText().toString();
                    dao.atualizarPrazoInscricaoEvento(prazoInscricaoAlterado, eventoId);

                    edtxtMostraPrazoInscricao.setText(prazoInscricaoAlterado);
                    edtxtMostraPrazoInscricao.setEnabled(false);
                    imgButtonEditarPrazoIncricao.setImageResource(R.drawable.editarinformacoes);

                    Toast.makeText(getApplicationContext(), "Prazo de inscrição do evento atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                }
                isEditingPrazoInscricao = !isEditingPrazoInscricao;
            }
        });


        imgbtnEditarValorEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditingValorEvento) {
                    txtValorDoEvento.setEnabled(true);
                    imgbtnEditarValorEvento.setImageResource(R.drawable.botao_salvar); // Troca o ícone para salvar
                } else {
                    String valorEventoAlterado = txtValorDoEvento.getText().toString();
                    dao.atualizarValorEvento(Double.parseDouble(valorEventoAlterado), eventoId); // Atualiza no banco de dados

                    txtValorDoEvento.setText(valorEventoAlterado);
                    txtValorDoEvento.setEnabled(false);
                    imgbtnEditarValorEvento.setImageResource(R.drawable.editarinformacoes); // Troca o ícone para editar

                    Toast.makeText(getApplicationContext(), "Valor do evento atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                }
                isEditingValorEvento = !isEditingValorEvento;
            }
        });

        imgbtnEditarDescricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditingDescricao) {
                    edtxtMostraDescricao.setEnabled(true);
                    imgbtnEditarDescricao.setImageResource(R.drawable.botao_salvar);
                } else {
                    String descricaoEventoAlterada = edtxtMostraDescricao.getText().toString();
                    dao.atualizarDescricaoEvento(descricaoEventoAlterada, eventoId);

                    edtxtMostraDescricao.setText(descricaoEventoAlterada);
                    edtxtMostraDescricao.setEnabled(false);
                    imgbtnEditarDescricao.setImageResource(R.drawable.editarinformacoes);

                    Toast.makeText(getApplicationContext(), "Descrição do evento atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                }
                isEditingDescricao = !isEditingDescricao;
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

                        dao.atualizarTipoEhorasNoBanco(novoTipoEvento, novasHorasComplementares, eventoId);

                        Toast.makeText(VisaoGeral.this, "Tipo do evento e horas complementares atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });

        btnInscrever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnInscrever.setEnabled(false);

                Intent intent = getIntent();
                long eventoId = intent.getLongExtra("evento_id", -1);

                if (eventoId != -1) {
                    if (dao.verificarInscricao(usuarioId, eventoId)) {
                        Toast.makeText(VisaoGeral.this, "Você já está inscrito neste evento!", Toast.LENGTH_SHORT).show();
                    } else {
                        dao.inscreverNoEvento(usuarioId, eventoId);
                        Toast.makeText(VisaoGeral.this, "Inscrição realizada com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VisaoGeral.this, "Evento não encontrado!", Toast.LENGTH_SHORT).show();
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnInscrever.setEnabled(true);
                    }
                }, 1000);
            }
        });
    }

    private String formatarData(String data) {
        if (data == null || data.isEmpty()) {
            return "Data inválida";
        }
        try {
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdfEntrada.parse(data);


            SimpleDateFormat sdfSaida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdfSaida.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Data inválida";
        }
    }

    private long getUsuarioId() {
        String usuarioEmail = getEmailUsuario();


        if (usuarioEmail == null) {
            return -1;
        }


        Usuario usuario = dao.buscarUsuarioPorEmail(usuarioEmail);


        if (usuario != null) {
            return usuario.getId();
        } else {
            return -1;
        }
    }


    private String getEmailUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("email", null);
    }

    private void configurarVisibilidadeBotoes(long idEvento) {
        boolean eventoPago = dao.EventoPago(idEvento);

        Button btnComprarIngresso = findViewById(R.id.btnComprarIngresso);
        Button btnInscrever = findViewById(R.id.btnInscrever);

        if (eventoPago) {
            btnComprarIngresso.setVisibility(View.VISIBLE);
            btnInscrever.setVisibility(View.GONE);
        } else {
            btnComprarIngresso.setVisibility(View.GONE);
            btnInscrever.setVisibility(View.VISIBLE);
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(VisaoGeral.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int horaEscolhida, int minutoEscolhido) {
                        String dataHorarioEscolhido = String.format("%02d/%02d/%d - %02d:%02d", diaEscolhido, mesEscolhido + 1, anoEscolhido, horaEscolhida, minutoEscolhido);
                        btnEscolherDataHorario.setText(dataHorarioEscolhido);
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

        List<Atividade> atividades = dao.buscarAtividadesPorEvento(idEvento);

        for (Atividade atividade : atividades) {
            TableRow row = new TableRow(VisaoGeral.this);
            row.setPadding(8, 8, 8, 8);

            TextView txtTimestamp = new TextView(VisaoGeral.this);
            String timestamp = atividade.getData() + " - " + atividade.getHorario();
            txtTimestamp.setText(timestamp);
            txtTimestamp.setPadding(12, 12, 12, 12);
            txtTimestamp.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            row.addView(txtTimestamp);

            TextView txtNomeAtividade = new TextView(VisaoGeral.this);
            txtNomeAtividade.setText(atividade.getNomeAtividade());
            txtNomeAtividade.setPadding(12, 12, 12, 12);
            txtNomeAtividade.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            row.addView(txtNomeAtividade);

            TextView txtPalestrante = new TextView(VisaoGeral.this);
            txtPalestrante.setText(atividade.getPalestrante());
            txtPalestrante.setPadding(12, 12, 12, 12);
            txtPalestrante.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            row.addView(txtPalestrante);

            TextView txtLocal = new TextView(VisaoGeral.this);
            txtLocal.setText(atividade.getLocalAtividade());
            txtLocal.setPadding(12, 12, 12, 12);
            txtLocal.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1)); // Define peso para responsividade
            row.addView(txtLocal);

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

    private void configurarCamposDeHora(long idEvento) {
        Informacoes informacoes = dao.getInformacoesById(idEvento);

        if (informacoes != null) {
            String horaInicio = informacoes.getHorarioInicio();
            String horarioTermino = informacoes.getHorarioTermino();

            // Define os textos dos campos de hora
            if (horaInicio != null && !horaInicio.isEmpty()) {
                txtHoraDeInicioEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeInicioEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeInicioEvento.setText(horaInicio);
                // Tornar visível o botão de editar horário de início
                ImageButton imgbtnAlterarHorarioInicio = findViewById(R.id.imgbtnAlterarHorarioInicio);
                imgbtnAlterarHorarioInicio.setVisibility(View.VISIBLE);
            } else {
                txtHoraDeInicioEvento.setVisibility(View.GONE);
                txtMostraHoraDeInicioEvento.setVisibility(View.GONE);
            }

            if (horarioTermino != null && !horarioTermino.isEmpty()) {
                txtHoraDeTerminoEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeTerminoEvento.setVisibility(View.VISIBLE);
                txtMostraHoraDeTerminoEvento.setText(horarioTermino);
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


    // Métodos de Imagem
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgBanner.setImageBitmap(bitmap);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] imageBytes = outputStream.toByteArray();

                long eventoId = getIntent().getLongExtra("evento_id", -1);
                if (eventoId != -1) {
                    dao.atualizarBannerEvento(eventoId, imageBytes);
                    Toast.makeText(this, "Banner atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                    editor.putString("bannerImagem", imageUri.toString());
                    editor.apply();

                    Toast.makeText(this, "Banner salvo com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "ID do evento inválido!", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao processar a imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void carregarBanner(long eventoId) {
        // Verifica se o eventoId é válido
        if (eventoId != -1) {
            byte[] bannerBytes = dao.obterBannerEvento(eventoId);

            if (bannerBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bannerBytes, 0, bannerBytes.length);
                imgBanner.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "Banner não encontrado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Métodos de Navegação
    public void IrTelaPagar(View view) {
        Intent in = new Intent(VisaoGeral.this, PagamentoEvento.class);
        startActivity(in);
    }

    public void inicialVG(View view){
        Intent in = new Intent(this, TelaInicial.class);
        startActivity(in);
    }

    public void localizacaoVG(View view){
        Intent in = new Intent(this, TelaInicial.class);
        startActivity(in);
    }

    public void criarVG(View view){
        Intent in = new Intent(this, TelaInicial.class);
        startActivity(in);
    }

    public void meuseventosVG(View view){
        Intent in = new Intent(this, TelaInicial.class);
        startActivity(in);
    }

    public void perfilVG(View view){
        Intent in = new Intent(this, TelaInicial.class);
        startActivity(in);
    }
}