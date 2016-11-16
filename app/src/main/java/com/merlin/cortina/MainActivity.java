package com.merlin.cortina;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.merlin.entities.Invoice;
import com.merlin.utils.CustomExceptionHandler;
import com.merlin.utils.GeneralUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Invoice> arrListNewInvoices = new ArrayList<>();
    private ArrayAdapter<Invoice> arrAdptInvoice;
    private ListView invoicesListView;
    private CustomExceptionHandler exceptionHandler;

    public void addInvoice(Invoice invoice) {
        arrListNewInvoices.add(invoice);
    }

    public void replaceInvoice(Invoice toRemove, Invoice toAdd) {
        int index = arrListNewInvoices.indexOf(toRemove);
        arrListNewInvoices.remove(toRemove);
        arrListNewInvoices.add(index, toAdd);
        arrAdptInvoice.notifyDataSetChanged();
    }

    private void setArrListNewInvoices(ArrayList<Invoice> arrListNewInvoices) {
        for (Invoice invoice : arrListNewInvoices) {
            invoice.setContext(this);
        }
        this.arrListNewInvoices = arrListNewInvoices;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();
        exceptionHandler = new CustomExceptionHandler(oldHandler, MainActivity.this);
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
        }
        readData();

        invoicesListView = (ListView) findViewById(R.id.invoicesList);
        arrAdptInvoice = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrListNewInvoices);
        invoicesListView.setAdapter(arrAdptInvoice);
        arrAdptInvoice.notifyDataSetChanged();

        Button btnNewRequest = (Button) findViewById(R.id.btnNewRequest);
        invoicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                registerForContextMenu(invoicesListView);
                invoicesListView.showContextMenuForChild(view);
            }
        });
        btnNewRequest.requestFocus();
        btnNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(MainActivity.this, NewInvoiceActivity.class), getResources().getInteger(R.integer.request_NORMAL));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getResources().getInteger(R.integer.result_OK)) {
            Invoice newInvoice = data.getExtras().getParcelable(getResources().getString(R.string.idInvoice));
            newInvoice.setContext(this);
            if (requestCode == getResources().getInteger(R.integer.request_NORMAL)) {

                addInvoice(newInvoice);

            } else if (requestCode == getResources().getInteger(R.integer.request_EDIT)) {
                replaceInvoice(newInvoice, newInvoice);
            }
            arrAdptInvoice.notifyDataSetChanged();
            saveData();
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.invoicesList) {
            List<String> menuOptions = Arrays.asList(getResources().getStringArray(R.array.menu_invoice));
            for (int i = 0; i < menuOptions.size(); i++) {
                menu.add(Menu.NONE, i, i, menuOptions.get(i));
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Invoice selected = arrListNewInvoices.get(info.position);
            Intent intent;
            List<String> menuItems = Arrays.asList(getResources().getStringArray(R.array.menu_invoice));
            int menuIndex = item.getItemId();
            String menuItemName = menuItems.get(menuIndex);
            if (menuItemName.equalsIgnoreCase(getResources().getString(R.string.delete))) {
                arrAdptInvoice.remove(selected);
                arrAdptInvoice.notifyDataSetChanged();
            } else if (menuItemName.equalsIgnoreCase(getResources().getString(R.string.edit))) {
                intent = new Intent(MainActivity.this, NewInvoiceActivity.class);
                intent.putExtra(getResources().getString(R.string.requestCode), menuItemName);
                intent.putExtra(getResources().getString(R.string.idInvoice), (Parcelable) selected);
                startActivityForResult(intent, getResources().getInteger(R.integer.request_EDIT));
            } else if (menuItemName.equalsIgnoreCase(getResources().getString(R.string.copyToClippboard))) {
                GeneralUtils.copyInvoiceToClippboard(selected, this);
            }
        return true;
    }

    private void readData() {
        FileInputStream fis = null;
        ObjectInputStream is = null;
        try {
            fis = this.openFileInput(getResources().getString(R.string.saveFile));
            is = new ObjectInputStream(fis);
            setArrListNewInvoices((ArrayList<Invoice>) is.readObject());

        } catch (Exception e) {
            GeneralUtils.copyTextToClippboard("Error reading data.\n" + e.toString(), this);
            Log.e("MainActivity", "reading data");
        } finally {
            try {
                is.close();
                fis.close();
            } catch (Exception f) {
                GeneralUtils.copyTextToClippboard("Error reading , closing res.\n" + f.toString(), this);
                Log.e("MainActivity", "error closing resources while reading data.\n" + f.toString());
            }
        }
    }

    private void saveData() {
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = this.openFileOutput(getResources().getString(R.string.saveFile), Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(arrListNewInvoices);
        } catch (IOException e) {
            GeneralUtils.copyTextToClippboard("Error saving data.\n" + e.toString(), this);
            Log.e("MainActivity", "Error saving data.\n" + e.toString());
        } finally {
            try {
                if (os != null || fos != null) {
                    os.close();
                    fos.close();
                }
            } catch (Exception f) {
                GeneralUtils.copyTextToClippboard("Error saving data, closing res.\n" + f.toString(), this);
                Log.e("MainActivity", "Error closing resources while saving data.\n" + f.toString());
            }
        }
    }

}
