package com.merlin.cortina;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.merlin.entities.Curtain;
import com.merlin.utils.CustomExceptionHandler;
import com.merlin.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Merlin on 9/11/2016.
 */

public class NewCurtainActivity extends AppCompatActivity {

    Button btnReturn;
    Button btnSave;
    TabHost tabHost;
    private TextView hText;
    private TextView wText;
    private TextView altoText;
    private TextView OPSText;
    private TextView roomText;
    private TextView guiaLText;
    private TextView guiaIText;
    private TextView cenefText;
    private Spinner typeFabric;
    private Spinner typeSdo;
    private Spinner typeCdn;
    private Spinner typeMnd;
    private Spinner typeMoto;
    private Spinner typeSupl;
    private Spinner typeSup;
    private Spinner typeProd;
    private CheckBox checkCounter;
    private CheckBox checkInter;
    private CheckBox checkDoble;
    private CheckBox checkGuiasL;
    private CheckBox checkGuiasI;
    private CheckBox checkSupl;
    private CheckBox checkCenef;
    private CheckBox checkSup;
    private CheckBox checkMoto;
    private Snackbar confirmMessage;
    private Snackbar errorMessage;
    private String requestCode;
    private boolean isEditing = false;
    private ArrayList<Curtain> arrCurtain = new ArrayList<>();
    private Curtain editedCurtain;
    private CustomExceptionHandler exceptionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcourtain);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();
        exceptionHandler = new CustomExceptionHandler(oldHandler, NewCurtainActivity.this);
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
        }

        Bundle b = getIntent().getExtras();
        instantiateObjects();
        populateSpinners();
        setTabs();
        if (b != null) {
            requestCode = (String) b.get(getResources().getString(R.string.requestCode));
            if (getResources().getString(R.string.edit).equalsIgnoreCase(requestCode)) {
                isEditing = true;
                editedCurtain = getIntent().getExtras().getParcelable(getResources().getString(R.string.idCurtain));
                populateFieldsFromEdited();
                btnReturn.setVisibility(View.GONE);
                btnSave.setText(getResources().getString(R.string.saveandreturn));
            }
        }





        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                String motoValue = "";
                int glatValue = 0;
                int ginfValue = 0;
                String suplValue = "";
                int cenefValue = 0;
                String supValue = "";
                boolean showError = false;
                boolean areThereExtras = false;
                if (checkGuiasI.isChecked()) {
                    if (StringUtils.isEmpty(guiaIText.getText().toString())) {
                        showError = true;
                    } else {
                        ginfValue = Integer.valueOf(guiaIText.getText().toString());
                        areThereExtras = true;
                    }
                }
                if (checkGuiasL.isChecked()) {
                    if (StringUtils.isEmpty(guiaLText.getText().toString())) {
                        showError = true;
                    } else {
                        glatValue = Integer.valueOf(guiaLText.getText().toString());
                        areThereExtras = true;
                    }
                }
                if (checkCenef.isChecked()) {
                    if (StringUtils.isEmpty(cenefText.getText().toString())) {
                        showError = true;
                    } else {
                        cenefValue = Integer.valueOf(cenefText.getText().toString());
                        areThereExtras = true;
                    }
                }
                String room = roomText.getText().toString();
                if (StringUtils.areEmpty(hText.getText().toString(), wText.getText().toString(), altoText.getText().toString(), room) || showError) {
                    errorMessage.show();

                } else {
                    if (checkMoto.isChecked()) {
                        motoValue = typeMoto.getSelectedItem().toString();
                        areThereExtras = true;
                    }
                    if (checkSupl.isChecked()) {
                        suplValue = typeSupl.getSelectedItem().toString();
                        areThereExtras = true;
                    }
                    if (checkSup.isChecked()) {
                        supValue = typeSup.getSelectedItem().toString();
                        areThereExtras = true;
                    }

                    int altoValue = Integer.valueOf(altoText.getText().toString());
                    int heightValue = Integer.valueOf(hText.getText().toString());
                    int widthValue = Integer.valueOf(wText.getText().toString());
                    boolean counterValue = checkCounter.isChecked();
                    boolean interValue = checkInter.isChecked();
                    boolean dobleValue = checkDoble.isChecked();

                    String prodValue = typeProd.getSelectedItem().toString();
                    String opsValue = OPSText.getText().toString();
                    String fabricValue = typeFabric.getSelectedItem().toString();
                    String sdoValue = typeSdo.getSelectedItem().toString();
                    String cdnValue = typeCdn.getSelectedItem().toString();
                    String mndValue = typeMnd.getSelectedItem().toString();
                    boolean optionalValue;
                    optionalValue = editedCurtain != null && editedCurtain.isOptional();

                    Curtain newCurtain = new Curtain(heightValue, widthValue, fabricValue, room, cdnValue, mndValue, sdoValue, altoValue, opsValue, counterValue, interValue, dobleValue, motoValue, glatValue, ginfValue, suplValue, cenefValue, supValue, prodValue, optionalValue, areThereExtras, NewCurtainActivity.this);
                    if (isEditing) {
                        resultIntent.putExtra(getResources().getString(R.string.idCurtain), newCurtain);
                        resultIntent.putExtra(getResources().getString(R.string.idOldCurtain), editedCurtain);

                        setResult(getResources().getInteger(R.integer.result_OK), resultIntent);
                        cleanNewCurtainFields();
                        clearEditedCurtain();
                        confirmMessage.show();
                        clearEditedCurtain();
                        btnReturn.setVisibility(View.VISIBLE);
                        btnSave.setText(getResources().getString(R.string.textSave));
                        finish();
                    } else {
                        arrCurtain.add(newCurtain);
                        resultIntent.putParcelableArrayListExtra(getResources().getString(R.string.idCurtain), arrCurtain);
                        setResult(getResources().getInteger(R.integer.result_OK), resultIntent);
                        if (areThereExtras) {
                            showConfirmationExtras();
                        }
                        cleanNewCurtainFields();
                        clearEditedCurtain();
                        confirmMessage.show();
                    }


                }

            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrCurtain = new ArrayList<>();
                finish();
            }
        });

    }

    private void populateSpinners() {
        List<String> typeOptions = Arrays.asList(getResources().getStringArray(R.array.Fabric));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item, typeOptions);
        typeFabric.setAdapter(spinnerAdapter);

        typeOptions = Arrays.asList(getResources().getStringArray(R.array.Sdo));

        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item, typeOptions);
        typeSdo.setAdapter(spinnerAdapter);

        typeOptions = Arrays.asList(getResources().getStringArray(R.array.Cdn));

        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item, typeOptions);
        typeCdn.setAdapter(spinnerAdapter);

        typeOptions = Arrays.asList(getResources().getStringArray(R.array.Mnd));

        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item, typeOptions);
        typeMnd.setAdapter(spinnerAdapter);

        typeOptions = Arrays.asList(getResources().getStringArray(R.array.moto));

        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item, typeOptions);
        typeMoto.setAdapter(spinnerAdapter);

        typeOptions = Arrays.asList(getResources().getStringArray(R.array.supl));

        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item, typeOptions);
        typeSupl.setAdapter(spinnerAdapter);

        typeOptions = Arrays.asList(getResources().getStringArray(R.array.sup));

        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item, typeOptions);
        typeSup.setAdapter(spinnerAdapter);

        typeOptions = Arrays.asList(getResources().getStringArray(R.array.Prod));

        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item, typeOptions);
        typeProd.setAdapter(spinnerAdapter);
    }

    private void instantiateObjects() {
        hText = (TextView) findViewById(R.id.editTextHeight);
        wText = (TextView) findViewById(R.id.editTextWidth);
        altoText = (TextView) findViewById(R.id.editTextAlto);
        OPSText = (TextView) findViewById(R.id.editTextOPS);
        roomText = (TextView) findViewById(R.id.editTextRoom);
        guiaLText = (TextView) findViewById(R.id.editTextGLat);
        guiaIText = (TextView) findViewById(R.id.editTextGInf);
        cenefText = (TextView) findViewById(R.id.editTextCenef);

        typeFabric = (Spinner) findViewById(R.id.typeFabric);
        typeSdo = (Spinner) findViewById(R.id.typeSdo);
        typeCdn = (Spinner) findViewById(R.id.typeCdn);
        typeMnd = (Spinner) findViewById(R.id.typeMnd);
        typeMoto = (Spinner) findViewById(R.id.typeMotorized);
        typeSupl = (Spinner) findViewById(R.id.typeSupl);
        typeSup = (Spinner) findViewById(R.id.typeSup);
        typeProd = (Spinner) findViewById(R.id.typeProd);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_Curtain);
        confirmMessage = Snackbar.make(coordinatorLayout, R.string.message_save, Snackbar.LENGTH_SHORT);
        errorMessage = Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_fields), Snackbar.LENGTH_LONG);
        View view = errorMessage.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        view = confirmMessage.getView();
        params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);

        checkCounter = (CheckBox) findViewById(R.id.checkContr);
        checkInter = (CheckBox) findViewById(R.id.checkInter);
        checkDoble = (CheckBox) findViewById(R.id.checkDoble);
        checkCenef = (CheckBox) findViewById(R.id.checkCenef);
        checkGuiasI = (CheckBox) findViewById(R.id.checkGuiasI);
        checkGuiasL = (CheckBox) findViewById(R.id.checkGuiasL);
        checkSupl = (CheckBox) findViewById(R.id.checkSupl);
        checkSup = (CheckBox) findViewById(R.id.checkSup);
        checkMoto = (CheckBox) findViewById(R.id.checkMotorized);

        btnReturn = (Button) findViewById(R.id.btnReturnCurtain);
        btnSave = (Button) findViewById(R.id.btnSave);
    }

    private void cleanNewCurtainFields() {
        hText.setText(R.string.empty_string);
        wText.setText(R.string.empty_string);
        OPSText.setText(R.string.empty_string);
        roomText.setText(R.string.empty_string);

        checkInter.setChecked(false);
        checkDoble.setChecked(false);

        typeSdo.setSelection(1);
        typeCdn.setSelection(1);
        typeMnd.setSelection(1);

    }

    private void cleanNewCurtainFieldsExtras() {
        guiaLText.setText(R.string.empty_string);
        guiaIText.setText(R.string.empty_string);
        cenefText.setText(R.string.empty_string);

        checkCenef.setChecked(false);
        checkGuiasI.setChecked(false);
        checkGuiasL.setChecked(false);
        checkSupl.setChecked(false);
        checkSup.setChecked(false);
        checkMoto.setChecked(false);

        typeMoto.setSelection(1);
        typeSupl.setSelection(1);
        typeSup.setSelection(1);
        typeProd.setSelection(1);

    }

    private void setTabs() {
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("General");
        spec.setContent(R.id.General);
        spec.setIndicator("GENERAL");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Extras");
        spec.setContent(R.id.Extras);
        spec.setIndicator("EXTRAS");
        tabHost.addTab(spec);
    }

    private void populateFieldsFromEdited() {
        List<String> arrFabric = Arrays.asList(getResources().getStringArray(R.array.Fabric));
        List<String> arrMnd = Arrays.asList(getResources().getStringArray(R.array.Mnd));
        List<String> arrSdo = Arrays.asList(getResources().getStringArray(R.array.Sdo));
        List<String> arrCdn = Arrays.asList(getResources().getStringArray(R.array.Cdn));
        List<String> arrMoto = Arrays.asList(getResources().getStringArray(R.array.moto));
        List<String> arrSupl = Arrays.asList(getResources().getStringArray(R.array.supl));
        List<String> arrSup = Arrays.asList(getResources().getStringArray(R.array.sup));
        List<String> arrProd = Arrays.asList(getResources().getStringArray(R.array.Prod));

        roomText.setText(editedCurtain.getRoom());
        hText.setText(String.valueOf(editedCurtain.getHeight()));
        wText.setText(String.valueOf(editedCurtain.getWidth()));
        typeFabric.setSelection(arrFabric.lastIndexOf(editedCurtain.getFabric()));
        typeMnd.setSelection(arrMnd.lastIndexOf(editedCurtain.getMnd()));
        typeSdo.setSelection(arrSdo.lastIndexOf(editedCurtain.getSdo()));
        typeCdn.setSelection(arrCdn.lastIndexOf(editedCurtain.getCdn()));
        typeProd.setSelection(arrProd.lastIndexOf(editedCurtain.getProd()));
        hText.setText(String.valueOf(editedCurtain.getAlto()));
        OPSText.setText(editedCurtain.getOps());
        altoText.setText(String.valueOf(editedCurtain.getAlto()));
        checkCounter.setChecked(editedCurtain.isCounter());
        checkInter.setChecked(editedCurtain.isInter());
        checkDoble.setChecked(editedCurtain.isDoble());

        if (arrMoto.lastIndexOf(editedCurtain.getMoto()) != -1) {
            typeMoto.setSelection(arrMoto.lastIndexOf(editedCurtain.getMoto()));
            checkMoto.setChecked(true);
        }
        if (editedCurtain.getGlat() != 0) {
            checkGuiasL.setChecked(true);
            guiaLText.setText(String.valueOf(editedCurtain.getGlat()));
        }
        if (editedCurtain.getGinf() != 0) {
            checkGuiasI.setChecked(true);
            guiaIText.setText(String.valueOf(editedCurtain.getGinf()));
        }
        if (arrSupl.lastIndexOf(editedCurtain.getSupl()) != -1) {
            typeSupl.setSelection(arrSupl.lastIndexOf(editedCurtain.getSupl()));
            checkSupl.setChecked(true);
        }
        if (editedCurtain.getCenef() != 0) {
            checkCenef.setChecked(true);
            cenefText.setText(String.valueOf(editedCurtain.getCenef()));
        }
        if (arrSup.lastIndexOf(editedCurtain.getSup()) != -1) {
            typeSup.setSelection(arrSup.lastIndexOf(editedCurtain.getSup()));
            checkSup.setChecked(true);
        }


    }

    private void clearEditedCurtain() {
        isEditing = false;
        editedCurtain = null;
    }

    private void showConfirmationExtras() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.extrasdialogtitle))
                .setMessage(
                        getResources().getString(R.string.extrasdialogmessage))
                .setIcon(
                        getResources().getDrawable(
                                android.R.drawable.ic_dialog_alert))
                .setPositiveButton(
                        getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Do Something Here

                            }
                        })
                .setNegativeButton(
                        getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                cleanNewCurtainFieldsExtras();
                            }
                        }).show();
    }

}
