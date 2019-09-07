package com.cy.review.cameraalbumtest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 启动相机拍摄显示图片：
 * 创建File对象准备存放图片 -> 将File对象转换成Uri对象 -> 打开相机程序,同时设置图片的输出地址 -> 启动活动，显示图片
 *
 * 调用相册：
 * 运行时权限处理 -> 启动活动，调用相册 -> 选择图片后，对返回数据进行处理，需要对旧的系统的兼容性做出判断和处理 -> 获取图片真实路径 ->展示图片
 *
 * Uri类型。
 */

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO=1;
    private ImageView picture;
    private Uri imageUri;

    public static final int CHOOSE_PHOTO=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button takePhoto=findViewById(R.id.take_photo);
        picture=findViewById(R.id.picture);

        Button chooseFromAlbum=findViewById(R.id.choose_from_album);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                getExternalCacheDir()用于得到手机SD卡的应用关联缓存目录，就是专门用于存放当前应用缓存数据的位置。
                因为从6.0起，读写SD卡被列为了危险权限，如果要将图片存放在SD卡的其他任何目录，都要进行运行时权限处理才行，
                而使用应用关联缓存目录就可以跳过这一步。
                 */
                //创建File对象，用于存储拍照后的图片
                File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if (outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                /*
                从7.0开始，直接使用本地真实路径的Uri被认为是不安全的，会抛出FileUriExposedException异常，
                而FileProvider是一种特殊的内容提供器，使用了和内容提供器类似的机制，来对数据进行保护，可以选择性的将封装过的Uri共享给外部，
                从而提高了应用的安全性。
                 */
                if (Build.VERSION.SDK_INT>=24){
                    imageUri=FileProvider.getUriForFile(MainActivity.this,
                            "com.cy.review.cameraalbumtest.fileprovider",outputImage);
                    /*
                     内容提供器FileProvider需要在清单文件中注册！
                     */
                }else {
                    imageUri=Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });


        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                进行运行时权限处理，动态申请WRITE_EXTERNAL_STORAGE危险权限，
                要从SD卡中读取照片就要申请这个权限，这个权限表示同时授予程序对SD卡读和写的能力
                 */
                if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();//打开相册
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode==RESULT_OK){
                    try{
                        // 将拍摄的照片显示出来，
                        // decodeStream()从各种源创建Bitmap对象，包括文件，流和字节数组。
                        // 此时将输入流解码为位图
                        Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode==RESULT_OK){
                    /*
                    处理照片的过程，如果需要兼容老版本的手机则需要多进行一些判断，分别处理！
                    以4.4为分界点，
                    4.4版本开始，选择相册中的图片不再返回图片真实的Uri了，而是一个封装过的Uri，所以如果是4.4以上的版本的手机就需要对这个Uri进行解析。
                     */
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT>=19){
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else{
                        //4.4以下的系统使用这个方法处理照片
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");//选择媒体类型，还可以选择文本、音频、视频等等
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(this,"权限申请没有通过！",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //主要是如何解析这个封装过的Uri
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagepath=null;
        Uri uri=data.getData();
        /*
        几种判断情况：
         */
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的uri则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagepath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri=ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagepath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通的处理方式
            imagepath=getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径的方式就可以了
            imagepath=uri.getPath();
        }
        displayImage(imagepath);//根据图片路径显示图片
    }

    //Uri没有被封装过，不需要任何解析，直接将Uri拿去查询获取图片真实路径
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if (imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"图片获取失败",Toast.LENGTH_SHORT).show();
        }
    }
}
