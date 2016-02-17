package view.helper;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chaitanya.akoshabootcampcontacts.R;

import java.util.List;

import contacts.pojo.ContactsData;


/**
 * Created by chaitanya on 15/02/16.
 */
public class ListAdaptor extends RecyclerView.Adapter<ListAdaptor.ViewHolder> {


    private List<ContactsData> contactsPOJOs;

    public ListAdaptor(List<ContactsData> allContacts) {
        contactsPOJOs = allContacts;
    }

    @Override
    public ListAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_layout, null);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }
    Context context;
    @Override
    public void onBindViewHolder(ListAdaptor.ViewHolder holder, int position) {
        holder.nameView.setText(contactsPOJOs.get(position).getName());
        holder.numberView.setText(contactsPOJOs.get(position).getPhone());
        if (contactsPOJOs.get(position).getPhoto() != null) {
            holder.imageView.setImageURI(Uri.parse(contactsPOJOs.get(position).getPhoto()));
        } else {
            int dummyImage = context.getResources().getIdentifier("ic_launcher", "mipmap", context.getPackageName());
            holder.imageView.setImageResource(dummyImage);
        }
    }

    @Override
    public int getItemCount() {
        return contactsPOJOs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameView;
        public TextView numberView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameView = (TextView) itemView.findViewById(R.id.contact_name);
            numberView = (TextView) itemView.findViewById(R.id.contact_number);
            imageView = (ImageView) itemView.findViewById(R.id.contact_image);

        }
    }
}
