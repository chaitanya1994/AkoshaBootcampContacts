package com.example.chaitanya.akoshabootcampcontacts;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import contacts.reader.ContactsReader;
import fragments.AddContactFragment;
import fragments.ContactDetailsFragment;
import fragments.ContactsFragment;
import fragments.EditContactFragment;

public class Contacts extends FragmentActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        if (findViewById(R.id.fragmentFrame) != null) {
            if (savedInstanceState != null) {
                return;
            }
            execAsync();
        }

    }

    public void execAsync() {
        new GetFromDb().execute();
    }

    public void callDetailsFragment(int id) {
        ContactDetailsFragment contactDetails = new ContactDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        contactDetails.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame, contactDetails);
        fragmentTransaction.commit();
    }


    private void callContactListFragment() {
        ContactsFragment contactsList = new ContactsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame, contactsList);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void callAddContact() {
        AddContactFragment addContactFragment = new AddContactFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame, addContactFragment);
        fragmentTransaction.commit();
    }

    public void callEditFragment(int id) {

        EditContactFragment editContactFragment = new EditContactFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        editContactFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentFrame, editContactFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    class GetFromDb extends AsyncTask<Void, Void, Integer> {


        @Override
        protected Integer doInBackground(Void... params) {
            new ContactsReader().addContactstoDb(context);

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            callContactListFragment();
            Toast.makeText(context, "REFRESHED", Toast.LENGTH_SHORT).show();
            super.onPostExecute(integer);
        }
    }

    @Override
    public void onBackPressed() {
        callContactListFragment();
    }
}
