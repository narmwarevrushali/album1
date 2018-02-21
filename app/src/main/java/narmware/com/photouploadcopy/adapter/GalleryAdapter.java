package narmware.com.photouploadcopy.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import narmware.com.photouploadcopy.R;
import narmware.com.photouploadcopy.models.Image;

/**
 * Created by comp16 on 12/18/2017.
 */

public class GalleryAdapter extends BaseAdapter {

    ArrayList<Image> images;

    Context context;
    LayoutInflater layoutInflater;
    int position;

    public GalleryAdapter(ArrayList<Image> images, Context context) {
        this.images = images;
        this.context = context;
        layoutInflater=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return getItem(i);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view = layoutInflater.inflate(R.layout.gallery_item, null);
            final Image image=images.get(i);
            File imgFile = new  File(images.get(i).path);
            if(imgFile.exists()){

                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    options.inSampleSize=8;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inDither = true;

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
                    ImageView mImg = view.findViewById(R.id.img);
                    mImg.setImageBitmap(myBitmap);

                }catch (Exception e)
                {

                }
        }

        ImageView mImgClose=view.findViewById(R.id.img_btn_close);
       /* mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,image.album,Toast.LENGTH_LONG).show();

                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                databaseAccess.open();
                databaseAccess.deleteSingle(images.get(i).id,images.get(i).album);
                SelectImagesActivity.countSelected--;

                if(SelectImagesActivity.countSelected==0)
                {
                    //finish();
                }
                try{
                   // Log.e("Remove items", this.getItemIdAtPosition(i) + "    " + images.get(i).id);
                    images.remove(images.get(i));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                SelectImagesActivity.mAdapter.notifyDataSetChanged();
            }
        });*/

        return view;
    }
}
