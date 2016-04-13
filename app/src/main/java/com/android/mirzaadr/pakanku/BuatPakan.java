package com.android.mirzaadr.pakanku;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.StackView;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import android.app.ActionBar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class BuatPakan extends AppCompatActivity {

    /*private static StackView stackView;
    private static ArrayList<Stack_Items> list;

    //Integer array for images
    private static final Integer[] icons = { R.drawable.logo, R.drawable.logo,
            R.drawable.logo, R.drawable.logo2 };/*/

    int imageSwitcherImages[] = {R.drawable.sapi, R.drawable.ayam, R.drawable.kambing, R.drawable.domba};
    private String[] namaTernak = {"Sapi", "Ayam", "Kambing", "Domba"};
    public ImageView indicatorImages[];

    ImageSwitcher myImageSwitcher;
    int switcherImage = imageSwitcherImages.length;
    int prev;
    int counter = 0;
    Animation animationOut;
    Animation animationIn;
    Animation animationprevOut;
    Animation animationprevIn;

    CheckBox check_potong;
    CheckBox check_perah;
    CheckBox check_petelur;
    CheckBox check_hobi;
    CheckBox check_kerja;

    TextView buttonPlus1;
    TextView buttonPlus2;
    TextView buttonPlus3;
    TextView buttonMin1;
    TextView buttonMin2;
    TextView buttonMin3;

    EditText textEdit;
    EditText editBobot;
    EditText editJumlah;
    EditText editHari;

    LinearLayout layoutPotong;
    LinearLayout layoutPetelur;
    LinearLayout layoutPerah;
    LinearLayout layoutKerja;
    LinearLayout layoutHobi;

    String hewan;
    String tujuan;
    String nama;
    double berat1;
    int jumlah;
    int lama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_pakan);

        indicatorImages = new ImageView[switcherImage];
        indicatorImages[0] = (ImageView) findViewById(R.id.btn1);
        indicatorImages[1] = (ImageView) findViewById(R.id.btn2);
        indicatorImages[2] = (ImageView) findViewById(R.id.btn3);
        indicatorImages[3] = (ImageView) findViewById(R.id.btn4);

        check_potong = (CheckBox) findViewById(R.id.checkPotong);
        check_perah = (CheckBox) findViewById(R.id.checkPerah);
        check_petelur = (CheckBox) findViewById(R.id.checkPetelur);
        check_hobi = (CheckBox) findViewById(R.id.checkHobi);
        check_kerja = (CheckBox) findViewById(R.id.checkKerja);

        layoutPotong = (LinearLayout) findViewById(R.id.layoutPotongBuat);
        layoutKerja = (LinearLayout) findViewById(R.id.layoutKerjaBuat);
        layoutPerah = (LinearLayout) findViewById(R.id.layoutPerahBuat);
        layoutPetelur = (LinearLayout) findViewById(R.id.layoutPetelurBuat);
        layoutHobi = (LinearLayout) findViewById(R.id.layoutHobiBuat);

        buttonPlus1 = (TextView) findViewById(R.id.buttonplus1);
        buttonPlus2 = (TextView) findViewById(R.id.buttonplus2);
        buttonPlus3 = (TextView) findViewById(R.id.buttonplus3);
        buttonMin1 = (TextView) findViewById(R.id.buttonmin1);
        buttonMin2 = (TextView) findViewById(R.id.buttonmin2);
        buttonMin3 = (TextView) findViewById(R.id.buttonmin3);

        editBobot = (EditText) findViewById(R.id.editbobot);
        editJumlah = (EditText) findViewById(R.id.editjumlah);
        editHari = (EditText) findViewById(R.id.editHari);

        editHari.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editBobot.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editJumlah.setImeOptions(EditorInfo.IME_ACTION_DONE);

        hewan = "Sapi";

        if(hewan.equals("Sapi")) {

            check_hobi.setClickable(false);
            check_petelur.setClickable(false);
            layoutPetelur.setVisibility(View.INVISIBLE);
            layoutHobi.setVisibility(View.INVISIBLE);

        }
        else if(hewan.equals("Kambing")) {

            check_kerja.setClickable(false);
            check_hobi.setClickable(false);
            check_petelur.setClickable(false);
            layoutPetelur.setVisibility(View.INVISIBLE);
            layoutHobi.setVisibility(View.INVISIBLE);
            layoutKerja.setVisibility(View.INVISIBLE);

        }
        else if(hewan.equals("Domba")) {

            check_kerja.setClickable(false);
            check_hobi.setClickable(false);
            check_petelur.setClickable(false);
            layoutPetelur.setVisibility(View.INVISIBLE);
            layoutHobi.setVisibility(View.INVISIBLE);
            layoutKerja.setVisibility(View.INVISIBLE);


        }
        else if(hewan.equals("Ayam")) {

            check_kerja.setClickable(false);
            check_perah.setClickable(false);
            layoutKerja.setVisibility(View.INVISIBLE);
            layoutPerah.setVisibility(View.INVISIBLE);

        }

        buttonPlus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editBobot.getText().toString().trim().length() > 0) {

                    editBobot.setText(String.valueOf(Integer.parseInt(editBobot.getText().toString()) + 1));

                }
                else {
                    editBobot.setText(String.valueOf(Integer.parseInt("0")));
                }
            }
        });

        buttonMin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editBobot.getText().toString().trim().length() > 0) {

                    if (Integer.parseInt(editBobot.getText().toString()) <= 0) {

                        editBobot.setText(String.valueOf(0));

                    }
                    else {

                        editBobot.setText(String.valueOf(Integer.parseInt(editBobot.getText().toString()) - 1));

                    }

                }
                else {

                    editBobot.setText(String.valueOf(0));

                }



            }
        });

        buttonPlus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editJumlah.getText().toString().trim().length() > 0) {

                    editJumlah.setText(String.valueOf(Integer.parseInt(editJumlah.getText().toString()) + 1));

                }
                else {

                    editJumlah.setText(String.valueOf(Integer.parseInt("0")));

                }

            }
        });

        buttonMin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editJumlah.getText().toString().trim().length() > 0) {

                    if (Integer.parseInt(editJumlah.getText().toString()) <= 0) {

                        editJumlah.setText(String.valueOf(0));

                    }
                    else {

                        editJumlah.setText(String.valueOf(Integer.parseInt(editJumlah.getText().toString()) - 1));

                    }



                }
                else {

                    editJumlah.setText(String.valueOf(0));

                }

            }
        });

        buttonPlus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editHari.getText().toString().trim().length() > 0) {

                    editHari.setText(String.valueOf(Integer.parseInt(editHari.getText().toString()) + 1));

                }
                else {

                    editHari.setText(String.valueOf(Integer.parseInt("0")));

                }

            }
        });

        buttonMin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editJumlah.getText().toString().trim().length() > 0) {

                    if (Integer.parseInt(editJumlah.getText().toString()) <= 0) {

                        editHari.setText(String.valueOf(0));

                    }
                    else {

                        editHari.setText(String.valueOf(Integer.parseInt(editHari.getText().toString()) - 1));

                    }
                    
                }
                else {

                    editHari.setText(String.valueOf(0));

                }

            }
        });

        check_potong.setChecked(true);

        check_potong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_potong.setChecked(true);
                check_perah.setChecked(false);
                check_petelur.setChecked(false);
                check_hobi.setChecked(false);
                check_kerja.setChecked(false);
            }
        });

        check_perah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_potong.setChecked(false);
                check_perah.setChecked(true);
                check_petelur.setChecked(false);
                check_hobi.setChecked(false);
                check_kerja.setChecked(false);
            }
        });

        check_petelur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_potong.setChecked(false);
                check_perah.setChecked(false);
                check_petelur.setChecked(true);
                check_hobi.setChecked(false);
                check_kerja.setChecked(false);
            }
        });

        check_hobi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_potong.setChecked(false);
                check_perah.setChecked(false);
                check_petelur.setChecked(false);
                check_hobi.setChecked(true);
                check_kerja.setChecked(false);
            }
        });

        check_kerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_potong.setChecked(false);
                check_perah.setChecked(false);
                check_petelur.setChecked(false);
                check_hobi.setChecked(false);
                check_kerja.setChecked(true);
            }
        });

        textEdit = (EditText) findViewById(R.id.JenisTernak);
        //textEdit.setFocusableInTouchMode(false);
        myImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);

        myImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView switcherImageView = new ImageView(getApplicationContext());
                switcherImageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT
                ));
                switcherImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                switcherImageView.setImageResource(R.drawable.sapi);
                textEdit.setText(namaTernak[0]);
                indicatorImages[0].setImageResource(R.drawable.fill_circle);
                //switcherImageView.setMaxHeight(100);
                return switcherImageView;
            }
        });

        animationOut = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        animationIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

        animationprevOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        animationprevIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

    }

    public void nextImageButton(View view) {
        prev = counter;
        counter ++;
        myImageSwitcher.setOutAnimation(animationOut);
        myImageSwitcher.setInAnimation(animationIn);

        if (counter == switcherImage)
            counter = 0;
        myImageSwitcher.setImageResource(imageSwitcherImages[counter]);
        textEdit.setText(namaTernak[counter]);
        hewan = namaTernak[counter];

        check_kerja.setClickable(true);
        check_hobi.setClickable(true);
        check_petelur.setClickable(true);
        check_potong.setClickable(true);
        check_perah.setClickable(true);

        layoutPetelur.setVisibility(View.VISIBLE);
        layoutHobi.setVisibility(View.VISIBLE);
        layoutKerja.setVisibility(View.VISIBLE);
        layoutPotong.setVisibility(View.VISIBLE);
        layoutPerah.setVisibility(View.VISIBLE);

        if(hewan.equals("Sapi")) {

            check_hobi.setClickable(false);
            check_petelur.setClickable(false);
            layoutPetelur.setVisibility(View.INVISIBLE);
            layoutHobi.setVisibility(View.INVISIBLE);

        }
        else if(hewan.equals("Kambing")) {

            check_kerja.setClickable(false);
            check_hobi.setClickable(false);
            check_petelur.setClickable(false);
            layoutPetelur.setVisibility(View.INVISIBLE);
            layoutHobi.setVisibility(View.INVISIBLE);
            layoutKerja.setVisibility(View.INVISIBLE);

        }
        else if(hewan.equals("Domba")) {

            check_kerja.setClickable(false);
            check_hobi.setClickable(false);
            check_petelur.setClickable(false);
            layoutPetelur.setVisibility(View.INVISIBLE);
            layoutHobi.setVisibility(View.INVISIBLE);
            layoutKerja.setVisibility(View.INVISIBLE);


        }
        else if(hewan.equals("Ayam")) {

            check_kerja.setClickable(false);
            check_perah.setClickable(false);
            layoutKerja.setVisibility(View.INVISIBLE);
            layoutPerah.setVisibility(View.INVISIBLE);

        }
        indicatorImages[prev].setImageResource(R.drawable.holo_circle);
        indicatorImages[counter].setImageResource(R.drawable.fill_circle);
    }

    public void prevImageButton(View view) {
        prev = counter;
        counter--;
        myImageSwitcher.setOutAnimation(animationprevOut);
        myImageSwitcher.setInAnimation(animationprevIn);

        int max = switcherImage - 1;
        if (counter == -1)
            counter = max;
        myImageSwitcher.setImageResource(imageSwitcherImages[counter]);
        textEdit.setText(namaTernak[counter]);
        hewan = namaTernak[counter];

        check_kerja.setClickable(true);
        check_hobi.setClickable(true);
        check_petelur.setClickable(true);
        check_potong.setClickable(true);
        check_perah.setClickable(true);

        layoutPetelur.setVisibility(View.VISIBLE);
        layoutHobi.setVisibility(View.VISIBLE);
        layoutKerja.setVisibility(View.VISIBLE);
        layoutPotong.setVisibility(View.VISIBLE);
        layoutPerah.setVisibility(View.VISIBLE);


        if(hewan.equals("Sapi")) {

            check_hobi.setClickable(false);
            check_petelur.setClickable(false);
            layoutPetelur.setVisibility(View.INVISIBLE);
            layoutHobi.setVisibility(View.INVISIBLE);

        }
        else if(hewan.equals("Kambing")) {

            check_kerja.setClickable(false);
            check_hobi.setClickable(false);
            check_petelur.setClickable(false);
            layoutPetelur.setVisibility(View.INVISIBLE);
            layoutHobi.setVisibility(View.INVISIBLE);
            layoutKerja.setVisibility(View.INVISIBLE);

        }
        else if(hewan.equals("Domba")) {

            check_kerja.setClickable(false);
            check_hobi.setClickable(false);
            check_petelur.setClickable(false);
            layoutPetelur.setVisibility(View.INVISIBLE);
            layoutHobi.setVisibility(View.INVISIBLE);
            layoutKerja.setVisibility(View.INVISIBLE);


        }
        else if(hewan.equals("Ayam")) {

            check_kerja.setClickable(false);
            check_perah.setClickable(false);
            layoutKerja.setVisibility(View.INVISIBLE);
            layoutPerah.setVisibility(View.INVISIBLE);

        }

        indicatorImages[prev].setImageResource(R.drawable.holo_circle);
        indicatorImages[counter].setImageResource(R.drawable.fill_circle);
    }

    public void editAction (View view){
        textEdit.setFocusableInTouchMode(true);
        textEdit.requestFocus();
    }

    public void editBobot (View view){
        editBobot.setFocusableInTouchMode(true);
        editBobot.requestFocus();
    }

    public void editJumlah (View view){
        editJumlah.setFocusableInTouchMode(true);
        editJumlah.requestFocus();
    }

    public void editHari (View view){
        editHari.setFocusableInTouchMode(true);
        editHari.requestFocus();
    }

    public void nextClick(View v) {

        if(hewan.equals("Ayam") || check_hobi.isChecked() || check_kerja.isChecked() || check_petelur.isChecked()) {

            Toast.makeText(getBaseContext(), "Fitur belum tersedia, aplikasi masih dalam versi beta", Toast.LENGTH_SHORT).show();

        }
        else {
            final Dialog dialog = new Dialog(BuatPakan.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_paket);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            final CheckBox check_hemat = (CheckBox) dialog.findViewById(R.id.cekPaket);
            final CheckBox check_hebat = (CheckBox) dialog.findViewById(R.id.cekPaket2);
            final CheckBox check_buat = (CheckBox) dialog.findViewById(R.id.cekBuat);
            final Button buat_ransum = (Button) dialog.findViewById(R.id.buttonk);

            check_buat.setChecked(true);

            check_hemat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    check_hemat.setChecked(true);
                    check_hebat.setChecked(false);
                    check_buat.setChecked(false);

                }
            });

            check_hebat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    check_hemat.setChecked(false);
                    check_hebat.setChecked(true);
                    check_buat.setChecked(false);

                }
            });

            check_buat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    check_hemat.setChecked(false);
                    check_hebat.setChecked(false);
                    check_buat.setChecked(true);

                }
            });

            buat_ransum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (check_buat.isChecked()) {

                        Intent intent = new Intent(BuatPakan.this, ternak2.class);
                        Bundle var_resep = new Bundle();

                        var_resep.putString("nama", nama);
                        var_resep.putString("hewan", hewan);
                        var_resep.putString("tujuan", tujuan);
                        var_resep.putDouble("berat1", berat1);
                        var_resep.putInt("jumlah", jumlah);
                        var_resep.putInt("lama", lama);
                        intent.putExtras(var_resep);

                        startActivity(intent);

                        dialog.dismiss();

                    }
                    else {

                        Toast.makeText(getBaseContext(), "Fitur belum tersedia, aplikasi masih dalam versi beta", Toast.LENGTH_SHORT).show();

                    }


                }
            });

            dialog.show();

            if (check_potong.isChecked()) {

                tujuan = "Potong";

            } else if (check_perah.isChecked()) {

                tujuan = "Perah";

            } else if (check_petelur.isChecked()) {

                tujuan = "Petelur";

            } else if (check_hobi.isChecked()) {

                tujuan = "Hobi";

            } else if (check_kerja.isChecked()) {

                tujuan = "Kerja";

            } else {

                Toast.makeText(getBaseContext(), "No checkbox selected", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

            if (editBobot.getText().toString().trim().length() > 0) {

                if (editBobot.getText().toString().equals("0")) {

                    Toast.makeText(getBaseContext(), "Bobot tidak boleh nol", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } else {

                    berat1 = Double.parseDouble(editBobot.getText().toString());

                }


            } else {

                Toast.makeText(getBaseContext(), "Bobot tidak boleh kosong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

            if (editJumlah.getText().toString().trim().length() > 0) {

                if (editJumlah.getText().toString().equals("0")) {

                    Toast.makeText(getBaseContext(), "Jumlah tidak boleh nol", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } else {

                    jumlah = Integer.parseInt(editJumlah.getText().toString());

                }

            } else {

                Toast.makeText(getBaseContext(), "Banyak ternak tidak boleh kosong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }


            if (editHari.getText().toString().trim().length() > 0) {

                if (editHari.getText().toString().equals("0")) {

                    Toast.makeText(getBaseContext(), "Lama pemeliharaan tidak boleh nol", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } else {

                    lama = Integer.parseInt(editHari.getText().toString());

                }

            } else {

                Toast.makeText(getBaseContext(), "Lama pemeliharaan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

            if (textEdit.getText().toString().trim().length() > 0) {

                nama = textEdit.getText().toString();

            } else {

                Toast.makeText(getBaseContext(), "Nama ternak tidak boleh kosong", Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        }

    }

}
