package fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaitanya.akoshabootcampcontacts.Contacts;
import com.example.chaitanya.akoshabootcampcontacts.R;

import contacts.pojo.ContactsData;
import database.helper.DatabaseHelper;

/**
 * Created by chaitanya on 16/02/16.
 */
public class ContactDetailsFragment extends Fragment {
    ImageView contactImage;
    TextView contactName;
    TextView contactNumber;
    Button deleteButton, editButton, deleteConfirm, cancelDelete;
    int id;
    boolean detailExists = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        id = getArguments().getInt("ID");
        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        View view = inflater.inflate(R.layout.details_fragment, container, false);
        getViews(view);
        fillViews(contactImage, contactName, contactNumber);
        if (!detailExists)
            Toast.makeText(getActivity(), "NO DETAILS FOUND", Toast.LENGTH_LONG).show();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirm.setVisibility(View.VISIBLE);
                cancelDelete.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Contacts)getActivity()).callEditFragment(id);
            }
        });

        deleteConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteContact(id);
                ((Contacts) getActivity()).execAsync();
            }
        });

        cancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirm.setVisibility(View.GONE);
                cancelDelete.setVisibility(View.GONE);
                deleteButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void getViews(View view) {
        contactImage = (ImageView) view.findViewById(R.id.details_image);
        contactName = (TextView) view.findViewById(R.id.details_name);
        contactNumber = (TextView) view.findViewById(R.id.details_number);
        deleteButton = (Button) view.findViewById(R.id.delete_contact);
        editButton = (Button) view.findViewById(R.id.edit_contact);
        deleteConfirm = (Button) view.findViewById(R.id.delete_confirm);
        cancelDelete = (Button) view.findViewById(R.id.cancel_delete);
    }

    void fillViews(ImageView contactImage, TextView contactName, TextView contactNumber) {

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        for (ContactsData contactsData : databaseHelper.DATA_LIST) {
            if (contactsData.getId() == id) {
                if (contactsData.getPhoto() != null)
                    contactImage.setImageURI(Uri.parse(contactsData.getPhoto()));
                else {
                    int dummyImage = getActivity().getResources().getIdentifier("ic_launcher", "mipmap", getActivity().getPackageName());
                    contactImage.setImageResource(dummyImage);
                }
                contactName.setText(contactsData.getName());
                contactNumber.setText(contactsData.getPhone());
                detailExists = true;
            }
        }
    }
}
