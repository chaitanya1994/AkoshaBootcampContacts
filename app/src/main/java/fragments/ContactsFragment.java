package fragments;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.chaitanya.akoshabootcampcontacts.Contacts;
import com.example.chaitanya.akoshabootcampcontacts.R;

import database.helper.DatabaseHelper;
import view.helper.ListAdaptor;
import view.helper.RecyclerItemClickListener;

/**
 * Created by chaitanya on 15/02/16.
 */
public class ContactsFragment extends Fragment {

    RecyclerView recyclerView;
    ListAdaptor listAdaptor;
    ImageButton addButton;
    final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
//    GetFromDb getFromDb = new GetFromDb();

    public ContactsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_fragment_layout, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        addButton = (ImageButton) rootView.findViewById(R.id.add_button);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        int itemId = databaseHelper.DATA_LIST.get(position).getId();
                        ((Contacts) getActivity()).callDetailsFragment(itemId);
                    }
                })
        );
        listAdaptor = new ListAdaptor(databaseHelper.DATA_LIST);
        recyclerView.setAdapter(listAdaptor);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Contacts)getActivity()).callAddContact();
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

}
