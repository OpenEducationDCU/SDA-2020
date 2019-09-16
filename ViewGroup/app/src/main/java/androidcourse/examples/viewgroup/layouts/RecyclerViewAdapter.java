package androidcourse.examples.viewgroup.layouts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import java.util.ArrayList;
import androidcourse.example.viewgroup.layouts.R;

/**
 * @author Chris Coughlan 2019
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mNewContext;

    //add array for image names
    private ArrayList<String> mImageText;
    private ArrayList<String> mImages;

    public RecyclerViewAdapter(Context mNewContext, ArrayList<String> imageText, ArrayList<String> images) {
        this.mNewContext = mNewContext;
        this.mImageText = imageText;
        this.mImages = images;
    }

    //declare methods
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: was called");
        
        //use glide here *(rather than a local array of images).
       Glide.with(mNewContext).asBitmap().load(mImages.get(i)).into(viewHolder.imageItem);

       viewHolder.imageText.setText(mImageText.get(i));

     viewHolder.itemParentLayout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v){
             Toast.makeText(mNewContext, mImageText.get(i), Toast.LENGTH_SHORT).show();
         }
     });
       
    }

    @Override
    public int getItemCount() {
        return mImageText.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageItem;
        TextView imageText;
        RelativeLayout itemParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //grab the image, the text and the layout id's
            imageItem = itemView.findViewById(R.id.imageItem);
            imageText = itemView.findViewById(R.id.recyclerText);
            itemParentLayout = itemView.findViewById(R.id.listItemLayout);

        }
    }
}
