package com.merlin.cortina;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
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


    private ArrayList<Curtain> arrListNewCurtains;
    private ArrayList<List<String>> arrListTitles;
    private List<String> listTitles;
    private ListViewCurtainAdapter arrAdaperCurtain;
    private ListViewTitleAdapter arrAdapterTitle;
    private ListView curtainList;
    private ListView titleList;
    private TextView textViewName;
    private Invoice editedInvoice;
    private Snackbar errorMessage;
    private String requestCode;
    private boolean isEditing;
    private CustomExceptionHandler exceptionHandler;

    public void addCurtain(Curtain curtain) {
        arrAdaperCurtain.productList.add(curtain);
    }

    public void replaceCurtain(Curtain toRemove, Curtain toAdd) {
        int index = arrAdaperCurtain.productList.indexOf(toRemove);
        arrAdaperCurtain.productList.remove(toRemove);
        arrAdaperCurtain.productList.add(index, toAdd);
        arrAdaperCurtain.notifyDataSetChanged();
    }

    private void clearEditedInvoice() {
        editedInvoice = null;
        isEditing = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newinvoice);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();
        exceptionHandler = new CustomExceptionHandler(oldHandler, NewInvoiceActivity.this);
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
        }
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnReturn = (Button) findViewById(R.id.btnReturnCurtain);
        Button btnNewCurtain = (Button) findViewById(R.id.btnNewCurtain);

        instantiateObjects();
        textViewName.requestFocus();
        Bundle b = getIntent().getExtras();


        if (b != null) {
            requestCode = (String) b.get(getResources().getString(R.string.requestCode));
            if (getResources().getString(R.string.edit).equalsIgnoreCase(requestCode)) {
                isEditing = true;
                editedInvoice = getIntent().getExtras().getParcelable(getResources().getString(R.string.idInvoice));
                editedInvoice.setContext(this);
                populateFieldsFromEdited();
            }
        }

        listTitles = Arrays.asList(getResources().getStringArray(R.array.listTitles));
        arrListTitles = new ArrayList<>();
        arrListTitles.add(listTitles);
        arrAdapterTitle = new ListViewTitleAdapter(this, arrListTitles);
        titleList.setAdapter(arrAdapterTitle);
        arrAdapterTitle.notifyDataSetChanged();
        arrAdaperCurtain = new ListViewCurtainAdapter(this, arrListNewCurtains);
        curtainList.setAdapter(arrAdaperCurtain);
        arrAdaperCurtain.notifyDataSetChanged();

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
                Intent newIntent = new Intent();
                Invoice selected;
                String name = textViewName.getText().toString();
                if (StringUtils.isEmpty(name)) {
                    errorMessage.show();
                } else {
                    Calendar today = Calendar.getInstance();
                    if (isEditing) {
                        editedInvoice.setClient(name);
                        editedInvoice.setCurtains(arrAdaperCurtain.productList);

                        selected = editedInvoice;
                    } else {
                        selected = new Invoice(name, today, arrListNewCurtains, NewInvoiceActivity.this);
                    }
                    arrListNewCurtains = new ArrayList<>();
                    GeneralUtils.copyInvoiceToClippboard(selected, NewInvoiceActivity.this);
                    newIntent.putExtra(getResources().getString(R.string.idInvoice), (Parcelable) selected);
                    setResult(getResources().getInteger(R.integer.result_OK), newIntent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == getResources().getInteger(R.integer.result_OK)) {
                if (requestCode == getResources().getInteger(R.integer.request_NORMAL)) {
                    ArrayList<Curtain> arrListTemp = data.getParcelableArrayListExtra(getResources().getString(R.string.idCurtain));
                    for (Curtain newTempCurtain : arrListTemp) {
                        newTempCurtain.setContext(this);
                        addCurtain(newTempCurtain);
                    }

                } else if (requestCode == getResources().getInteger(R.integer.request_EDIT)) {
                    Curtain newCurtain = data.getExtras().getParcelable(getResources().getString(R.string.idCurtain));
                    Curtain oldCurtain = data.getExtras().getParcelable(getResources().getString(R.string.idOldCurtain));
                    newCurtain.setContext(this);
                    replaceCurtain(oldCurtain, newCurtain);
                }
                arrAdaperCurtain.notifyDataSetChanged();
            }
        } catch (Throwable e) {
            StackTraceElement[] arrSt = e.getStackTrace();
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement st : arrSt) {
                sb.append("\n" + st.toString());
            }
            GeneralUtils.copyTextToClippboard(e.toString() + sb.toString(), this);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listCourtain) {
            List<String> menuOptions = Arrays.asList(getResources().getStringArray(R.array.menu_curtain));
            for (int i = 0; i < menuOptions.size(); i++) {
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
                intent = new Intent(NewInvoiceActivity.this, NewCurtainActivity.class);
                intent.putExtra(getResources().getString(R.string.idCurtain), (Parcelable) selected);
                intent.putExtra(getResources().getString(R.string.requestCode), menuItemName);
                startActivityForResult(intent, getResources().getInteger(R.integer.request_EDIT));
                return true;

            case "Duplicar":
                addCurtain(selected.clone());
                arrAdaperCurtain.notifyDataSetChanged();
                return true;

            case "Borrar":
                arrAdaperCurtain.remove(selected);
                arrAdaperCurtain.notifyDataSetChanged();
                return true;

            case "Opcional":
                if (selected.isOptional()) {
                    selected.setOptional(false);
                } else {
                    selected.setOptional(true);
                }
                arrAdaperCurtain.notifyDataSetChanged();
                return true;

            default:
                return false;

        }
    }

    private void populateFieldsFromEdited() {
        arrListNewCurtains = editedInvoice.getCurtains();
        textViewName.setText(editedInvoice.getClient());
    }

    private void instantiateObjects() {
        textViewName = (TextView) findViewById(R.id.editTextName);
        arrListNewCurtains = new ArrayList<>();
        titleList = (ListView) findViewById(R.id.listTitles);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_Invoice);
        errorMessage = Snackbar.make(coordinatorLayout, getResources().getString(R.string.error_empty_fields), Snackbar.LENGTH_LONG);
        View view = errorMessage.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);

        curtainList = (ListView) findViewById(R.id.listCourtain);
    }
}
