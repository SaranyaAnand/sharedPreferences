package com.example.appy_saranya.myofflineproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView imageView;
    private static final int CAMERA_REQUEST = 1888;
    private Context context;
    private Bitmap image;
    EditText userName;
    EditText password;
    TextView dataView;
    int counter = 0;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    private RecyclerView recycler_view;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    RecyclerViewAdapter adapter;
    ArrayList<String> list;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(recyclerViewlayoutManager);
        preferences=getSharedPreferences("PREFSNAME",Context.MODE_PRIVATE);

        List<String> input = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            input.add("Test" + i);
        }
        list = new ArrayList<>();
        int totalImages=preferences.getInt("counter",0);
        for(int i=1;i<=totalImages;i++)
            list.add("pic"+i+".png");
        adapter = new RecyclerViewAdapter(list);
        recycler_view.setAdapter(adapter);





        imageView = (ImageView) findViewById(R.id.phone_icon);
        editor = preferences.edit();
        imageView.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });



     /*   String path = Environment.getExternalStorageDirectory() + "/CameraImages/example.jpg";
        File file = new File(path);
        Uri outputFileUri = Uri.fromFile( file );
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );

        startActivityForResult( intent, CAPTURE_IMAGE );*/



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        String prevImage = preferences.getString("myimage","");

        for(int i = 0; i < counter ; i++ ){

            File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myImages/pic"+i+".png");
        }

        if(prevImage.length()!=0)
        {
            Bitmap prevImageBitmap = StringToBitMap(prevImage);

            try {
                function(prevImageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        counter=preferences.getInt("counter",0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("coming===","in onActivity result========");
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                counter++;
                File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myImages/pic"+counter+".png");
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
                editor.putInt("counter",counter);
                editor.apply();

                list.add("pic"+counter+".png");
                adapter.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                Log.d("error==", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("error==", "Error accessing file: " + e.getMessage());
            }
        }
    }

    private void function(Bitmap photo) throws IOException {

        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        Integer counter = 0;
        File file = new File(path, "FitnessGirl" + counter + ".jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
        fOut = new FileOutputStream(file);

        Bitmap pictureBitmap =photo;

        pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
        fOut.flush(); // Not really required
        fOut.close(); // do not forget to close the stream

      MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
