package com.codemaicon.myimc;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;


import static android.app.AlarmManager.INTERVAL_DAY;



//projeto valido este só falta programar a notificacao
public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private static final int REQUEST_PERMISSAO = 10;
    SeekBar prg;
    String arquivo;
    Uri outputFileUri;
    Intent notificationIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //substitui o progressBar pelo SeekBar
        prg = (SeekBar) findViewById(R.id.prgBar);

        //calculo imc
        final float[] imc = new float[1];
        Button btnCalcular = (Button) findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final TextView edtPeso = (TextView) findViewById(R.id.edtPeso);
                final TextView edtAltura = (TextView) findViewById(R.id.edtAltura);
                final TextView txtResultado = (TextView) findViewById(R.id.txtResultado);
                final TextView txtDescricao = (TextView) findViewById(R.id.txtDescricao);


                final float peso = Integer.parseInt(edtPeso.getText().toString());
                float altura = Float.parseFloat(edtAltura.getText().toString());
                imc[0] = peso / (altura * altura);
                //exibe progresso do seekBar de acordo como solicitado
                int progresso = Math.round((((imc[0] / 15) - 1) * 100));
                // pega valor maximo do seek
                int maximo = prg.getMax();

                if (imc[0] < 18.5) {
                    txtResultado.setText(imc[0] + "");
                    txtDescricao.setText("baixo peso");
                    prg.setProgress(0);
                } else {
                    if (imc[0] < 25) {
                        txtResultado.setText(imc[0] + "");
                        txtDescricao.setText("peso adequado");
                        prg.setProgress(progresso);

                    } else {
                        if (imc[0] <= 30) {
                            txtResultado.setText(imc[0] + "");
                            txtDescricao.setText("sobrepeso");
                            prg.setProgress(progresso);

                        } else {
                            txtResultado.setText(imc[0] + "");
                            txtDescricao.setText("obeso - Procure um médico");
                            prg.setProgress(maximo);

                        }


                    }


                }
                //SharedPreferences
                Button gravar = (Button) findViewById(R.id.btnGravar);
                gravar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences prefs = getSharedPreferences("arquivoPreferencia",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("peso", edtPeso.getText().toString());
                        editor.putString("resultado", txtResultado.getText().toString());
                        editor.putString("descricao", txtDescricao.getText().toString());
                        editor.putString("altura", edtAltura.getText().toString());
                        editor.apply();
                        Toast.makeText(getBaseContext(), "Gravado com Sucesso", Toast.LENGTH_SHORT).show();
                    }
                });
                Button limpar = (Button) findViewById(R.id.btnLimpar);
                limpar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtPeso.setText("");
                        edtAltura.setText("");
                        txtResultado.setText("");
                        txtDescricao.setText("");
                    }
                });
                Button recuperar = (Button) findViewById(R.id.btnRecuperar);
                recuperar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences prefs = getSharedPreferences("arquivoPreferencia",
                                Context.MODE_PRIVATE);
                        edtPeso.setText(prefs.getString("peso", "peso não informado"));
                        edtAltura.setText(prefs.getString("altura", "altura não informado"));
                        txtResultado.setText(prefs.getString("resultado", "ainda não processado"));
                        txtDescricao.setText(prefs.getString("descricao", "ainda não processado"));

                    }
                });

            }
        });

    }
    public void chamarNotificacao(View v){
        Button botao = (Button) v;
        notificationIntent = new Intent(this, NotificationPublisher.class);
        scheduleNotification(getNotification());

    }


    private void scheduleNotification(Notification notification) {

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, NotificationPublisher.NOTIFICATION_ID_VALUE);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        notificationIntent.putExtra("action", Constantes.BRODCAST_NOTIFICAR);

        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, NotificationPublisher.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, Constantes.BRODCAST_NOTIFICAR, notificationIntent ,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 00);
        long inicio = calendar.getTimeInMillis();
        long intervalo = INTERVAL_DAY;

        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), INTERVAL_DAY, alarmIntent);

    }

    String[] frases = {
            "Quando vires um homem bom, tenta imitá-lo; quando vires um homem mau, examina-te a ti mesmo.\n" +
                    "Confúcio ",
            "Tente mover o mundo - o primeiro passo será mover a si mesmo.\n" +
                    "Platão ",
            "A religião do futuro será cósmica e transcenderá um Deus pessoal, evitando os dogmas e a teologia.\n" +
                    "Albert Einstein ",
            "Não ser descoberto numa mentira é o mesmo que dizer a verdade.\n" +
                    "Aristóteles Onassis ",
            "Fiquei magoado, não por me teres mentido, mas por não poder voltar a acreditar-te.\n" +
                    "Friedrich Nietzsche ",

    };
    int index = new Random(5).nextInt(frases.length);
    String randon = (frases[index]);
    @TargetApi(Build.VERSION_CODES.N)

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Bom dia");
        builder.setContentText(randon);
        builder.setSmallIcon(R.drawable.logo);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);



        notificationIntent.putExtra("action", Constantes.BRODCAST_INICIAR_APP);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, Constantes.BRODCAST_INICIAR_APP, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationIntent.putExtra("action", Constantes.BRODCAST_EXECUTAR_ACAO);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(this, Constantes.BRODCAST_EXECUTAR_ACAO, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addAction(R.drawable.ic_notificacao, "Abrir", pendingIntent2);
        //      builder.addAction(android.R.drawable.ic_menu_add, "", pendingIntent3);
        builder.setAutoCancel(true);

        return  builder.build();
    }

    //Chamado quando acionado pelo usuário
    public void abrirCamera(View v){
        //Cria uma intenção para abrir a camera fotográfica
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Informa que a camera a ser aberta é a frontal
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);

        //Montagem do caminho onde o arquivo será salvo
        arquivo = Environment.getExternalStorageDirectory() + "/Pictures/fotoMyIMC.jpg";

        //Abre o caminho onde a foto será salva
        File file = new File(arquivo);
        outputFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        //Abre a camera
        startActivityForResult(intent, RESULT_FIRST_USER);
    }


    public void verImagem( View v ){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        arquivo = Environment.getExternalStorageDirectory() + "/Pictures/fotoMyIMC.jpg";
        intent.setDataAndType(Uri.parse("file://" + arquivo), "image/*");
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        //TODO Auto-generated method stub
        super.onResume();
        carregaImagem();
    }

    public void carregaImagem(){
        ImageView imageView = (ImageView) findViewById(R.id.foto);
        arquivo = Environment.getExternalStorageDirectory() + "/Pictures/fotoMyIMC.jpg";
        imageView.setImageURI( Uri.parse(arquivo) );
    }
}

