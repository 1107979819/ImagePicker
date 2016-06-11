package top.wenyl.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.List;

public class MainActivity extends Activity implements AndroidImagePicker.OnPictureTakeCompleteListener,AndroidImagePicker.OnImageCropCompleteListener,AndroidImagePicker.OnImagePickCompleteListener{
    private Button btn;
    private ImageView iv;
    private final int REQ_IMAGE = 1433;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.button);
        iv = (ImageView)findViewById(R.id.imageView);

        AndroidImagePicker.getInstance().addOnImageCropCompleteListener(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                int requestCode = REQ_IMAGE;
                AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_SINGLE);
                AndroidImagePicker.getInstance().setShouldShowCamera(true);
                intent.setClass( MainActivity.this,ImagesGridActivity.class);
                startActivityForResult(intent, requestCode);
            }
        });


    }

    @Override
    protected void onResume() {
        AndroidImagePicker.getInstance().setOnPictureTakeCompleteListener(this);//watching Picture taking
        AndroidImagePicker.getInstance().setOnImagePickCompleteListener(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        AndroidImagePicker.getInstance().deleteOnImagePickCompleteListener(this);
        AndroidImagePicker.getInstance().removeOnImageCropCompleteListener(this);
        AndroidImagePicker.getInstance().deleteOnPictureTakeCompleteListener(this);

        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            if (requestCode == REQ_IMAGE) {
                Toast.makeText(getApplicationContext(),"onActivityResult:"+AndroidImagePicker.getInstance().getSelectedImages().get(0).path,Toast.LENGTH_SHORT).show();
                iv.setImageBitmap(BitmapFactory.decodeFile(AndroidImagePicker.getInstance().getSelectedImages().get(0).path));
            }
        }

    }

    @Override
    public void onPictureTakeComplete(String picturePath) {
        Toast.makeText(getApplicationContext()," onPictureTakeComplete:"+picturePath,Toast.LENGTH_SHORT).show();
        iv.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

    @Override
    public void onImageCropComplete(Bitmap bmp, float ratio) {
        Toast.makeText(getApplicationContext(),"onImageCropComplet",Toast.LENGTH_SHORT).show();
        iv.setImageBitmap(bmp);
    }

    @Override
    public void onImagePickComplete(List<ImageItem> items) {
        Toast.makeText(getApplicationContext()," onImagePickComplete:"+items.get(0).path,Toast.LENGTH_SHORT).show();
        iv.setImageBitmap(BitmapFactory.decodeFile(items.get(0).path));
    }
}
