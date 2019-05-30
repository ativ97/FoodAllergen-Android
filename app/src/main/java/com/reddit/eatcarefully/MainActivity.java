package com.reddit.eatcarefully;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    AllergiesFragment fragmentA = new AllergiesFragment(); CartFragment fragmentB= new CartFragment();ScanFragment fragmentC= new ScanFragment();
    ProfileFragment fragmentD= new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentA= new AllergiesFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragmentA);
        //ft.addToBackStack("optional tag");
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_Allergies) {
            if (fragmentA.isAdded()) { // if the fragment is already in container
                ft.show(fragmentA);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.content_frame, fragmentA);
            }
            if (fragmentB.isAdded()) { ft.hide(fragmentB); }
            if (fragmentC.isAdded()) { ft.hide(fragmentC); }
            if (fragmentD.isAdded()) { ft.hide(fragmentD); }
            setTitle("Allergies");

        } else if (id == R.id.nav_Cart) {
            if (fragmentB.isAdded()) {// if the fragment is already in container
                //fragmentB.ListUpdate();
                ft.show(fragmentB);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.content_frame, fragmentB);
            }
            if (fragmentA.isAdded()) { ft.hide(fragmentA); }
            if (fragmentC.isAdded()) { ft.hide(fragmentC); }
            if (fragmentD.isAdded()) { ft.hide(fragmentD); }
            setTitle("Cart");

        } else if (id == R.id.nav_Scan) {
            if (fragmentC.isAdded()) { // if the fragment is already in container
                ft.show(fragmentC);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.content_frame, fragmentC);
            }
            if (fragmentA.isAdded()) { ft.hide(fragmentA); }
            if (fragmentB.isAdded()) { ft.hide(fragmentB); }
            if (fragmentD.isAdded()) { ft.hide(fragmentD); }
            setTitle("Scan");

        } else if (id == R.id.nav_Profile) {
            if (fragmentD.isAdded()) { // if the fragment is already in container
                ft.show(fragmentD);
            } else { // fragment needs to be added to frame container
                ft.add(R.id.content_frame, fragmentD);
            }
            if (fragmentA.isAdded()) { ft.hide(fragmentA); }
            if (fragmentC.isAdded()) { ft.hide(fragmentC); }
            if (fragmentB.isAdded()) { ft.hide(fragmentB); }
            setTitle("Profile");
        }

        /*if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            ft.hide(f);
        }*/
        //ft.addToBackStack("optional tag");
        //ft.replace(R.id.content_frame, fragment);
        ft.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
