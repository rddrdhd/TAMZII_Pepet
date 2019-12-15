package com.example.thegathering.Fourth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.thegathering.R;
import com.example.thegathering.Utils.Score;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FourthActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
   // private CameraBridgeViewBase mOpenCvCameraView;
    private static final String TAG = "OpenCV";
    private CascadeClassifier cascadeClassifier;
    Button camButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    int processFaceID;
    ImageView imageView;
    MatOfRect gRects;
    Mat gMat;
    Rect[] facesArray;
    Mat paintMat;
    Spinner spinner;
    TextView tw;
    Bitmap originalFace;
    Bitmap finalFace;
    Button saveButt;
    String currentPhotoPath;

    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.d(TAG, "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        tw = findViewById(R.id.textViewPhoto);
        imageView = findViewById(R.id.imageView);
        camButton = findViewById(R.id.camButton);
        tw.setText("Take a photo of some face(s). Pepe likes to be with friends, more faces = more socialized Pepe!");

        OpenCVLoader.initDebug();
        initializeOpenCVDependencies();

        spinner = findViewById(R.id.photoSpinner);

        spinner.setVisibility(View.GONE);
        saveButt=findViewById(R.id.button4);
        saveButt.setVisibility(View.GONE);


    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String str = parent.getItemAtPosition(pos).toString();
        if(str.equals("Pepe")){
            processFaceID = R.drawable.pepefacefront;
        } else if(str.equals("Trollface")){
            processFaceID = R.drawable.troll;
        } else if(str.equals("FUUUUUUU")){
            processFaceID = R.drawable.fuuuu;
        } else if(str.equals("Me gusta")){
            processFaceID = R.drawable.megusta;
        } else if(str.equals("Smiley")){
            processFaceID = R.drawable.smile;
        } else if(str.equals("Smiley (transparent)")){
            processFaceID = R.drawable.smiletrans;
        }else if(str.equals("Square")){
            processFaceID = R.drawable.square;
        }
        processImage();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        processFaceID = R.drawable.pepefacefront;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    /* ****************************************************************************************** */

    public void takePhoto(View view) {

        //   https://developer.android.com/training/camera/photobasics

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("takePhoto", ""+ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = getBitmap(currentPhotoPath);
            imageView.setImageBitmap(imageBitmap);
            originalFace = imageBitmap;
            detectFaces();
                // Create dropdown
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.faces_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(this);
                spinner.setVisibility(View.VISIBLE);

                saveButt.setVisibility(View.VISIBLE);

        } else {
            tw.setText("Something went wrong with capturing photo, please try it again");
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Bitmap getBitmap(String path) {
        try {
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            gMat = new Mat(bmp.getWidth(), bmp.getHeight(), CvType.CV_8UC4);
            Utils.bitmapToMat(bmp, gMat);
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void detectFaces() {
        /* Obraz v promenne Mat se pouzije jako vstup pro detektor tvari
           + zobrazit vyslednou detekci */

        gRects = new MatOfRect();
        cascadeClassifier.detectMultiScale(gMat, gRects);
        facesArray = gRects.toArray();
        paintMat = gMat.clone();
        if(facesArray!=null){
            Score.fourthGame += 10*facesArray.length;
            for (Rect rect : facesArray) {
                if(rect.height>200&rect.width>200){

                    Bitmap bm = Bitmap.createBitmap(paintMat.cols(), paintMat.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(paintMat, bm);
                }
            }
        }



    }

    public void processImage() {
        //Nadetekovanou tvar prekreslit

        paintMat = gMat.clone();
        Bitmap face = originalFace;
        for (Rect rect : facesArray) {

            Bitmap smile = BitmapFactory.decodeResource(this.getResources(),
                    processFaceID);

            if(rect.height>200 && rect.width>200){

                smile = Bitmap.createScaledBitmap(smile, rect.width, rect.height, false);

                face = overlay(face, smile, rect);
                imageView.setImageBitmap(face);
            }

        }
        finalFace = face;

        tw.setText("Choose your mask(s) and save pic!");
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2, Rect rect) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, rect.x, rect.y, null);
        return bmOverlay;
    }

    //tmp
    public void save(View view){
        Score.fourthGame += 20;

        String fileName = "PepePic"+new Date().getTime();
        saveImage(getApplicationContext(), finalFace, fileName, "png");
        Log.d("pic", fileName+" saved!");
    }

    public void saveImage(Context context, Bitmap bitmap, String name, String extension){
        name = name + "." + extension;

        try {
            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            File file = new File(path, name); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            fOut = new FileOutputStream(file);

            Bitmap pictureBitmap = finalFace; // obtaining the Bitmap
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeOpenCVDependencies() {
        try{
            // Copy the resource into a temp file so OpenCV can load it
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_APPEND);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            // Load the cascade classifier
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            Log.e("OpenCVActivity", "loading cascade OK");
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }
    }

}
