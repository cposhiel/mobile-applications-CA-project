package ie.mydbs.ca_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ie.mydbs.ca_app.Repository.Database.BooksTableDatabase.Book;
import ie.mydbs.ca_app.R;
import ie.mydbs.ca_app.Utilities.ObjectConverter;
import ie.mydbs.ca_app.Utilities.Utilities;
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    //The Adapters adapt the data from the Runtime Cache for the display by the UI.

    //the onBookListener property takes on the passed through instance of iOnBookListener implemented by the activity
    //this allows that activites method to be enacted when it is passed through to the nested ViewHolder class.
    private iOnBookListener onBookListener;
    private List<Book> books;

    public BooksAdapter(ArrayList<Book> data, iOnBookListener onBookListener){
        books = data;
        this.onBookListener = onBookListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public View view;
        public TextView book_name, author, borrowed_date, return_date;
        public ImageView author_image, lateReturn_image;

        //This on book listener is accessed from the outer class
        iOnBookListener onBookListener;


        public ViewHolder(View itemView, iOnBookListener onBookListener) {
            super(itemView);

                view = itemView;
                book_name = (TextView) view.findViewById(R.id.book_name);
                author = (TextView) view.findViewById(R.id.author);
                borrowed_date = (TextView) view.findViewById(R.id.borrowed_date);
                return_date = (TextView) view.findViewById(R.id.return_date);
                author_image = (ImageView) view.findViewById(R.id.author_image);
                lateReturn_image = (ImageView)view.findViewById(R.id.late_return_image);
                this.onBookListener = onBookListener;
                //This sets the implemented on click listener function to the 'interface' function of the activity passed through in the Book adapters constructor.
                //The onClickMethod below gets the items that was clicked position to pass through to the activities function, where it uses that position to locate
                //the book in the runtime cache and sends through the Book_ID as an extra with the Intent to be used for a get Book_info API call.
                itemView.setOnClickListener(this);
            }

        @Override
        public void onClick(View view) {
                onBookListener.onBookClick(getAdapterPosition());
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View bookView = layoutInflater.inflate(R.layout.book_cardview, parent, false);
            ViewHolder books = new ViewHolder(bookView, onBookListener);
            return books;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

            final Book book = books.get(position);
            holder.book_name.setText(book.BookName);
            holder.author.setText("Author: " + book.Author);
            holder.borrowed_date.setText("Date Borrowed: " + ObjectConverter.formatDate(book.Borrowed_Date, ObjectConverter.FormatType.date));
            holder.return_date.setText("Return Date: " + ObjectConverter.formatDate(book.Due_Date, ObjectConverter.FormatType.date));
            if(checkDueDatePassed(book.Due_Date)){
                holder.return_date.setTextColor(Utilities.appContext.getResources().getColor(R.color.maroon));
                holder.lateReturn_image.setVisibility(View.VISIBLE);
            }
            holder.author_image.setImageBitmap(ObjectConverter.convertByteArrayToBitmap(book.Author_Image));


    }
    //Interface for book click
    public interface  iOnBookListener{
        void onBookClick(int position);
    }
    @Override
    public int getItemCount() {
        return books.size();
    }
    //function to check due date.
    private static Boolean checkDueDatePassed(String date){
       Date dateToCheck = ObjectConverter.convertStringToDate(date);
       Date dateNow = Calendar.getInstance().getTime();
        return dateToCheck.before(dateNow);
    }
//Mobile Apps project for DBS. By Conal O'Shiel (10523829)
}
