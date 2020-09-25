package ie.mydbs.ca_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.mydbs.ca_app.R;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
public class BookInfoAdapter extends RecyclerView.Adapter<BookInfoAdapter.ViewHolder> {
    //Straight forward adapter class; gets data sets it to views.
    List<HashMap> infoList;
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView infoType, info;
        public View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            infoType = (TextView)view.findViewById(R.id.book_info_info_type);
            info = (TextView)view.findViewById(R.id.book_info_info);
        }
    }
    public BookInfoAdapter(List<HashMap> data){
        infoList = data;
    }
    @NonNull
    @Override
    public BookInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View infoView = inflater.inflate(R.layout.book_info_cardview, parent, false);
        ViewHolder infoViewHolder = new ViewHolder(infoView);
        return infoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final HashMap info = infoList.get(position);
        holder.infoType.setText(info.get("InfoType").toString());
        holder.info.setText(info.get("Info").toString());
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
}
