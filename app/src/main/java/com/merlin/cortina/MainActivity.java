package com.merlin.cortina;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.merlin.entities.Invoice;
import com.merlin.utils.CustomExceptionHandler;
import com.merlin.utils.GeneralUtils;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<Invoice> arrListNewInvoices = new ArrayList<>();
    private ArrayAdapter<Invoice> arrAdptInvoice;
    private ListView invoicesListView;
    private static Context context;
    public static void addInvoice(Invoice newInvoice){
        arrListNewInvoices.add(newInvoice);
    }
    public static Context getContext(){
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment mainFragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);
        mainFragment.setRetainInstance(true);
        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(CustomExceptionHandler.getHandler(oldHandler));
        }
        context = this;
        readData();
        invoicesListView = (ListView) findViewById(R.id.invoicesList);
        arrAdptInvoice = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrListNewInvoices);
        invoicesListView.setAdapter(arrAdptInvoice);

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
    protected void onDestroy(){
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
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        arrAdptInvoice.notifyDataSetChanged();
        saveData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.invoicesList){
            List<String> menuOptions = Arrays.asList(getResources().getStringArray(R.array.menu_invoice));
            for (int i = 0 ; i < menuOptions.size(); i++) {
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
        if(menuItemName.equalsIgnoreCase(getResources().getString(R.string.delete))){
            arrAdptInvoice.remove(selected);
            arrAdptInvoice.notifyDataSetChanged();
        }else if(menuItemName.equalsIgnoreCase(getResources().getString(R.string.edit))){
            NewInvoiceActivity.setEditedInvoice(selected);
            intent = new Intent(MainActivity.this, NewInvoiceActivity.class);
            intent.putExtra(getResources().getString(R.string.requestCode), menuItemName);
            startActivityForResult(intent, getResources().getInteger(R.integer.request_EDIT));
        }else if(menuItemName.equalsIgnoreCase(getResources().getString(R.string.copyToClippboard))){
            GeneralUtils.copyInvoiceToClippboard(selected);
        }
        return true;
    }

    private void readData(){
        FileInputStream fis = null;
        ObjectInputStream is = null;
        try{
            fis = this.openFileInput(getResources().getString(R.string.saveFile));
            is = new ObjectInputStream(fis);
            arrListNewInvoices = (ArrayList<Invoice>) is.readObject();

        }catch(Exception e){

        }finally{
            try{
                is.close();
                fis.close();
            }catch(Exception f){

            }
        }
    }

    private void saveData(){
        FileOutputStream fos = null;
        ObjectOutputStream os = null;
        try {
            fos = this.openFileOutput(getResources().getString(R.string.saveFile), Context.MODE_PRIVATE);
            os = new ObjectOutputStream(fos);
            os.writeObject(arrListNewInvoices);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(os != null || fos != null) {
                    os.close();
                    fos.close();
                }
            }
            catch(IOException f){

            }}
    }

}
