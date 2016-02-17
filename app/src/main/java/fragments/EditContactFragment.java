package fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chaitanya.akoshabootcampcontacts.Contacts;
import com.example.chaitanya.akoshabootcampcontacts.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import contacts.pojo.ContactsData;
import database.helper.DatabaseHelper;

/**
 * Created by chaitanya on 16/02/16.
 */
public class EditContactFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private static final int REQUEST_CAMERA = 1008;
    private static final int SELECT_FILE = 1;
    private static Uri selectedImageUri = null;
    int id;
    ImageView editImage;
    Button saveChangesButton;
    EditText nameEdit;
    EditText numberEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_contact, container, false);
        id = getArguments().getInt("ID");
        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        getViews(view);
        setData();
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldsEmpty()) {
                    Toast.makeText(getActivity(), "PLEASE FILL THE FIELDS APPROPRIATELY", Toast.LENGTH_LONG).show();
                } else {
                    if (selectedImageUri == null)
                        databaseHelper.editContact(nameEdit.getText().toString(), numberEdit.getText().toString(), id);
                    else
                        databaseHelper.editContact(nameEdit.getText().toString(), numberEdit.getText().toString(), selectedImageUri.toString(), id);

                    ((Contacts) getActivity()).execAsync();
                }
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        return view;

    }

    private void setData() {

        ContactsData contactsData = getContactObject();
        if (contactsData == null)
            return;
        if (contactsData.getPhoto() != null)
            editImage.setImageURI(Uri.parse(contactsData.getPhoto()));
        else {
            int dummyImage = getActivity().getResources().getIdentifier("ic_launcher", "mipmap", getActivity().getPackageName());
            editImage.setImageResource(dummyImage);
        }
        nameEdit.setText(contactsData.getName());
        numberEdit.setText(contactsData.getPhone());
    }

    private ContactsData getContactObject() {
        for (ContactsData contactsData : DatabaseHelper.DATA_LIST) {
            if (contactsData.getId() == id)
                return contactsData;
        }
        return null;
    }

    private boolean fieldsEmpty() {
        if (nameEdit.getText().toString().matches("") && numberEdit.getText().toString().matches(""))
            return true;
        return false;
    }

    private void getViews(View view) {
        editImage = (ImageView) view.findViewById(R.id.edit_image);
        saveChangesButton = (Button) view.findViewById(R.id.save_changes);
        nameEdit = (EditText) view.findViewById(R.id.name_to_edit);
        numberEdit = (EditText) view.findViewById(R.id.number_to_edit);
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    selectedImageUri = Uri.fromFile(destination);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(getActivity(), selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                selectedImageUri = Uri.fromFile(new File(selectedImagePath));
            }
        }
        editImage.setImageURI(selectedImageUri);
    }
}
