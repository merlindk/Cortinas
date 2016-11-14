package com.merlin.cortina;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.merlin.customs.ListViewCurtainAdapter;
import com.merlin.customs.ListViewTitleAdapter;
import com.merlin.entities.Curtain;
import com.merlin.entities.Invoice;
import com.merlin.utils.CustomExceptionHandler;
import com.merlin.utils.GeneralUtils;
import com.merlin.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Merlin on 10/11/2016.
 */

public class NewInvoiceActivity extends AppCompatActivity {


    private static ArrayList<Curtain> arrListNewCurtains;
    private static ArrayList<List<String>> arrListTitles;
    private static List<String> listTitles;
    private static ListViewCurtainAdapter arrAdpCurtain;
    private static ListViewTitleAdapter arrAdpTitle;
    private ListView curtainList;
    private ListView titleList;
    private TextView textViewName;
    private static Invoice editedInvoice;
    private Snackbar errorMessage;
    private String requestCode;
    private boolean isEditing;

    public static void addCurtain(Curtain curtain){
        arrListNewCurtains.add(curtain);
    }
    public static void setEditedInvoice(Invoice invoice){
        editedInvoice = invoice;}
    public static void removeCurtain(Curtain toRemove){
        arrAdpCurtain.remove(toRemove);
    }
    private void clearEditedInvoice(){
        editedInvoice = null;
        isEditing = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newinvoice);
        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(CustomExceptionHandler.getHandler(oldHandler));
        }
        instantiateObjects();

        Bundle b = getIntent().getExtras();
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnReturn = (Button) findViewById(R.id.btnReturnCurtain);
        Button btnNewCurtain = (Button) findViewById(R.id.btnNewCurtain);
        textViewName.requestFocus();

        if(b != null){
            requestCode = (String) b.get(getResources().getString(R.string.requestCode));
            if(getResources().getString(R.string.edit).equalsIgnoreCase(requestCode)){
                populateFieldsFromEdited();
                isEditing = true;
                }}

        listTitles = Arrays.asList(getResources().getStringArray(R.array.listTitles));
        arrListTitles = new ArrayList<>();
        arrListTitles.add(listTitles);
        arrAdpTitle = new ListViewTitleAdapter(this, arrListTitles);
        titleList.setAdapter(arrAdpTitle);
        arrAdpTitle.notifyDataSetChanged();
        arrAdpCurtain = new ListViewCurtainAdapter(this, arrListNewCurtains);
        curtainList.setAdapter(arrAdpCurtain);
        arrAdpCurtain.notifyDataSetChanged();

        curtainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                registerForContextMenu(curtainList);
                curtainList.showContextMenuForChild(view);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Invoice newInvoice;
                Invoice selected;
                String name = textViewName.getText().toString();
                if(StringUtils.isEmpty(name)){
                    errorMessage.show();
                } else {
                    Calendar today = Calendar.getInstance();
                    if(isEditing){
                        editedInvoice.setClient(name);
                        editedInvoice.setCurtains(arrListNewCurtains);
                        selected = editedInvoice;
                    }
                    else {
                        newInvoice = new Invoice(name, today, arrListNewCurtains);
                        selected = newInvoice;
                        MainActivity.addInvoice(newInvoice);
                    }
                    arrListNewCurtains = new ArrayList<>();
                    GeneralUtils.copyInvoiceToClippboard(selected);

                    setResult(getResources().getInteger(R.integer.result_OK));
                    clearEditedInvoice();
                    finish();
                }
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(getResources().getInteger(R.integer.result_CANCEL));
                clearEditedInvoice();
                finish();
            }
        });

        btnNewCurtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(NewInvoiceActivity.this, NewCurtainActivity.class), getResources().getInteger(R.integer.request_NORMAL));

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == getResources().getInteger(R.integer.result_OK)){
            arrAdpCurtain.notifyDataSetChanged();
        }else{

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.listCourtain){
            List<String> menuOptions = Arrays.asList(getResources().getStringArray(R.array.menu_curtain));
            for (int i = 0 ; i < menuOptions.size(); i++) {
                menu.add(Menu.NONE, i, i, menuOptions.get(i));
             }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Curtain selected = arrListNewCurtains.get(info.position);
        String[] menuItems = getResources().getStringArray(R.array.menu_curtain);
        String menuItemName = menuItems[item.getItemId()];
        Intent intent;

        switch (menuItemName) {
            case "Editar":
                NewCurtainActivity.setEditedCurtain(selected);
                intent = new Intent(NewInvoiceActivity.this, NewCurtainActivity.class);
                intent.putExtra(getResources().getString(R.string.requestCode), menuItemName);
                startActivityForResult(intent, getResources().getInteger(R.integer.request_EDIT));
                return true;

            case "Duplicar":
                addCurtain(selected.clone());
                arrAdpCurtain.notifyDataSetChanged();
                return true;

            case "Borrar":
                arrAdpCurtain.remove(selected);
                arrAdpCurtain.notifyDataSetChanged();
                return true;

            case "Opcional":
                if (selected.isOptional()) {
                    selected.setOptional(false);
                } else {
                    selected.setOptional(true);
                }
                arrAdpCurtain.notifyDataSetChanged();
                return true;

            default: return false;

        }
    }

    private void populateFieldsFromEdited(){
        arrListNewCurtains = editedInvoice.getCurtains();
        textViewName.setText(editedInvoice.getClient());
    }

    private void instantiateObjects(){
        textViewName = (TextView) findViewById(R.id.editTextName);
        arrListNewCurtains = new ArrayList<>();
        titleList = (ListView) findViewById(R.id.listTitles);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_Invoice);
        errorMessage = Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_fields), Snackbar.LENGTH_LONG);
        View view = errorMessage.getView();
        CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);

        curtainList= (ListView) findViewById(R.id.listCourtain);
    }
}
