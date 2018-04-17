package solutions.masai.masai.android.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import solutions.masai.masai.android.R;
import solutions.masai.masai.android.core.MasaiController;
import solutions.masai.masai.android.core.helper.C;
import solutions.masai.masai.android.core.helper.PermissionHelper;

/**
 * Created by Semko on 2017-05-25.
 */

public class ChatFilePermissionController extends MasaiController {
    private ChatUtilities.ShareType shareType = ChatUtilities.ShareType.CAMERA_TYPE;
    private static String outputFileUri;
    private String imagePath;

    public void downloadOrOpenAttachment() {

    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case C.ACTION_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (shareType == ChatUtilities.ShareType.IMAGE_TYPE) {
                        ChatUtilities.selectImageFromGallery(this);
                    } else if (shareType == ChatUtilities.ShareType.FILE_TYPE) {
                        ChatUtilities.selectFile(this);
                    }
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        PermissionHelper.showPermissionRationaleDialog(this, R.string.please_give_us_access_to_your_files, new DialogInterface.OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    PermissionHelper.askForPermissionToReadExternalFiles(ChatFilePermissionController.this);
//                                }
                            }
                        });
                    } else {
                        PermissionHelper.showPermissionDialog(this, R.string.no_access_to_files_info);
                    }
                }
                break;
            }
            case C.ACTION_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (shareType == ChatUtilities.ShareType.CAMERA_TYPE) {
                        outputFileUri = ChatUtilities.takePhoto(this);
                    }
                    if (shareType == ChatUtilities.ShareType.FILE_TYPE) {
                        downloadOrOpenAttachment();
                    }
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        PermissionHelper.showPermissionRationaleDialog(this, R.string.please_give_us_access_to_write_your_files, new DialogInterface.OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    PermissionHelper.askForPermissionToWriteExternalFiles(ChatFilePermissionController.this);
//                                }
                            }
                        });
                    } else {
                        PermissionHelper.showPermissionDialog(this, R.string.no_access_to_write_files_info);
                    }
                }
                break;
            }
            case C.ACTION_PERMISSION_REQUEST_ACCESS_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (shareType == ChatUtilities.ShareType.POSITION_TYPE) {
                        locationAccessGranted();
                    }
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        PermissionHelper.showPermissionRationaleDialog(this, R.string.please_give_us_access_to_your_location, new DialogInterface.OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    PermissionHelper.askForPermissionToGetLocation(ChatFilePermissionController.this);
//                                }
                            }
                        });
                    } else {
                        PermissionHelper.showPermissionDialog(this, R.string.no_access_to_location);
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == C.ACTION_IMAGE_SELECTED) {
                if (outputFileUri != null) {
                    imagePath = outputFileUri;
                    outputFileUri = null;
                } else {
                    imagePath = null;
                    imagePath = ChatUtilities.getPathFromURI(this, data.getData());
                }
                fileSelected(imagePath);
            }
        }
    }

    public void takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shareType = ChatUtilities.ShareType.CAMERA_TYPE;
            PermissionHelper.askForPermissionToWriteExternalFiles(this);
        } else {
            outputFileUri = ChatUtilities.takePhoto(this);
        }
    }

    public void selectFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shareType = ChatUtilities.ShareType.FILE_TYPE;
            PermissionHelper.askForPermissionToReadExternalFiles(this);
        } else {
            ChatUtilities.selectFile(this);
        }
    }

    public void requestPermissionToDownloadOrOpenAttachment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shareType = ChatUtilities.ShareType.FILE_TYPE;
            PermissionHelper.askForPermissionToWriteExternalFiles(this);
        } else {
            downloadOrOpenAttachment();
        }
    }

    public void requestLocationAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shareType = ChatUtilities.ShareType.POSITION_TYPE;
            PermissionHelper.askForPermissionToGetLocation(this);
        } else {
            locationAccessGranted();
        }
    }

    public void selectPhotoFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shareType = ChatUtilities.ShareType.IMAGE_TYPE;
            PermissionHelper.askForPermissionToReadExternalFiles(this);
        } else {
            ChatUtilities.selectImageFromGallery(this);
        }
    }

    public void clearImagePath() {
        imagePath = null;
    }

    public void fileSelected(String imagePath) {

    }

    public void locationAccessGranted() {

    }
}
