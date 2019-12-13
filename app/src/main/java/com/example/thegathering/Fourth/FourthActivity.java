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
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.thegathering.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FourthActivity extends AppCompatActivity {
    private CameraBridgeViewBase mOpenCvCameraView;
    private static final String TAG = "OpenCV";
    private CascadeClassifier cascadeClassifier;
    Button camButton, detectButton, processButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView imageView;
    MatOfRect gRects;
    Mat gMat;
    Rect[] facesArray;
    Mat paintMat;

    Bitmap originalFace;
    Bitmap finalFace;

    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.d(TAG, "OpenCV loaded successfully");
                    //System.loadLibrary("detection_based_tracker");
                    //mOpenCvCameraView.enableView();

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

        imageView = findViewById(R.id.imageView);
        camButton = findViewById(R.id.camButton);
        detectButton = findViewById(R.id.detectButton);
        processButton = findViewById(R.id.processButton);


        detectButton.setEnabled(false);
        processButton.setEnabled(false);


        OpenCVLoader.initDebug();
        initializeOpenCVDependencies();
    }

    private void showImg(Mat img) {
        Bitmap bm = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img, bm);
        imageView.setImageBitmap(bm);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
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
                // Error occurred while creating the File
                Log.d("takePhoto", ""+ex);
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

            detectButton.setEnabled(true);
        }
    }

    String currentPhotoPath;

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

        // Save a file: path for use with ACTION_VIEW intents
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

    public void detectFace(View view) {
        /* Obraz v promenne Mat se pouzije jako vstup pro detektor tvari
           + zobrazit vyslednou detekci */

        gRects = new MatOfRect();
        cascadeClassifier.detectMultiScale(gMat, gRects);
        facesArray = gRects.toArray();
        paintMat = gMat.clone();

        for (Rect rect : facesArray) {
            Imgproc.rectangle(paintMat, rect.tl(), rect.br(), new Scalar(0, 255, 0, 255), 6);

            Bitmap bm = Bitmap.createBitmap(paintMat.cols(), paintMat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(paintMat, bm);
            imageView.setImageBitmap(bm);
        }
        processButton.setEnabled(true);
    }



    public void processImage(View view) {
        //Nadetekovanou tvar prekreslit

        paintMat = gMat.clone();
        Bitmap face = originalFace;
        for (Rect rect : facesArray) {

            Bitmap smile = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.happypepe);
            smile = Bitmap.createScaledBitmap(smile, rect.width, rect.height, false);

            face = overlay(face, smile, rect);
            imageView.setImageBitmap(face);
        }
        finalFace = face;
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
       /* String result="from second";
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Second",result);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();*/
       String fileName = "PepePic"+new Date().getTime();
        /*new ImageSaver(getApplicationContext()).
                setExternal(true).
                setFileName(fileName).
                setDirectoryName("images").
                save(finalFace);*/
        saveImage(getApplicationContext(), finalFace, fileName, "png");
        Log.d("pic", fileName+" saved!");
    }

    public void saveImage(Context context, Bitmap bitmap, String name, String extension){
        name = name + "." + extension;
        //FileOutputStream fileOutputStream;

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

            /* fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            fileOutputStream.close();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
