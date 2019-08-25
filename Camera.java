package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class Camera {

    private int REQUEST_CAMERA_PERMISSION = 100;
    private String TAG = "CAMERA : ";

    private Context context;
    private TextureView textureView;
    private CameraDevice mCameraDevice;
    private Size previewSize;
    private Surface surface;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CameraCaptureSession mCaptureSession;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    public Camera(Context c, TextureView tv) {
        context = c;
        textureView = tv;
    }

    private boolean RequestPermission() {
        int permission_check = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (permission_check == PackageManager.PERMISSION_DENIED) {            //no Guaranty
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
        }

        if (permission_check == PackageManager.PERMISSION_DENIED) {
            Log.d(TAG, "Permission is denied");
            return false;
        } else {
            Log.d(TAG, "Permission is accepted");
            return true;
        }
    }
    //for resume
    private final TextureView.SurfaceTextureListener mSurfaceTextureListner = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };
    //Automatically working when the camera is open
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startCamera();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            stopBackgroundThread();
            cameraDevice.close();
            mCameraDevice = null;
            Log.d(TAG, " DISCONNECTED");
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            Log.d(TAG, error + " Error");
        }
    };

    String getFrontFacingCameraId(CameraManager cManager) {
        try{
            for(final String cameraId : cManager.getCameraIdList()){
                CameraCharacteristics characteristics = cManager.getCameraCharacteristics(cameraId);
                int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if(cOrientation == CameraCharacteristics.LENS_FACING_BACK)
                    return cameraId;
            }
        } catch (CameraAccessException e) {
            return null;
        }
        return null;
    }

    private void setUpDisplayer(){
        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(previewSize.getWidth(),previewSize.getHeight());
        surface = new Surface(surfaceTexture);
    }

    private void setUpPreviewRequest(){
        try{
        mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        mPreviewRequestBuilder.addTarget(surface);
        }catch (CameraAccessException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startBackgroundThread(){
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if(mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePreview(){
        try {
            //Auto focus
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            startBackgroundThread();
            CaptureRequest mPreviewBuilder = mPreviewRequestBuilder.build();
            mCaptureSession.setRepeatingRequest(mPreviewBuilder, null, mBackgroundHandler);
        }catch(CameraAccessException e){
            e.printStackTrace();
        }
    }
    //1
    public void openCamera() {
        Log.d(TAG, "setUpCamera");
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = getFrontFacingCameraId(cameraManager);
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            previewSize = map.getOutputSizes(SurfaceTexture.class)[0];

            int permissionCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if(permissionCamera == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                cameraManager.openCamera(cameraId, mStateCallback, null);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "setUpCamera Finish");
    }
    //2
    private void startCamera(){
        if(context == null || textureView == null || previewSize == null){
            Log.d(TAG, "context or texture view is null");
        }

        try{
            //Set the display area
            setUpDisplayer();
            //
            setUpPreviewRequest();
            //CameraCapturesSsssion
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    mCaptureSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            },null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void stopCamera(){
        if(mCameraDevice != null){
        stopBackgroundThread();
        mCameraDevice.close();
        mCameraDevice = null;
        }
        Log.d(TAG, "CameraDevice Close");
    }

    public void emptyCamera(){

    }
}
