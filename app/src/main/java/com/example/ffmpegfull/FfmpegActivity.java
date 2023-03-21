package com.example.ffmpegfull;

import static com.example.ffmpegfull.FfmpegConv.ffmpegTask;
import static com.example.permission.PermissionsHandler.PERMISSIONS_REQUEST_WRITE_STORAGE_STATE;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.permission.IPermissionInterface;
import com.example.permission.PermissionListener;
import com.example.permission.PermissionsHandler;

import java.io.File;
import java.util.Map;

public class FfmpegActivity extends Activity implements View.OnClickListener, PermissionListener {
    public String location =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getPath() + "/ffmpeg"+"/";
    private File dir;
    private PermissionsHandler mPermissionsHandler;
    EditText inputFileEditText;
    Button runButton;
    LinearLayout output;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg);
        Uri downloadFolder;
        location = addNecessarySlashes(location);
        downloadFolder = Uri.parse(location);
        dir = new File(downloadFolder.getPath());
        if (!dir.exists())
            dir.mkdirs();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getApplicationContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        initUI();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.run_command:
                String inputFile = inputFileEditText.getText().toString().trim();

                String outfile = dir +"/" + "test.mp4";

                String cmd= "-version";
                if (!TextUtils.isEmpty(inputFile)){
                    cmd = inputFile;
                }
                else{
                    Toast.makeText(this, getString(R.string.empty_command_toast1), Toast.LENGTH_LONG).show();
                    break;
                }
                WebAddress webAddress;
                try {
                    webAddress = new WebAddress(inputFile);
                    webAddress.setPath(encodePath(webAddress.getPath()));
                    String urlString = inputFile;
                    ffmpegTask(urlString, outfile, new FfmpegListener(){

                        @Override
                        public void sendResult(String s) {
                            addTextViewToLayout(s);
                        }
                    });
                }
                catch (Exception e) {
                    // This only happens for very bad urls, we want to catch the
                    // exception here
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionsHandler != null)
            mPermissionsHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void OnPermissionResult(int requestCode, boolean hasPermission, IPermissionInterface iPermissionInterface, Map<String, Object> map) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == PERMISSIONS_REQUEST_WRITE_STORAGE_STATE) {
            iPermissionInterface.onPermissionServed(requestCode, hasPermission, map, this);
        }
    }

    @Override
    public void requestPermissions(int requestCode, IPermissionInterface permissionInterface, Map<String, Object> map) {
        mPermissionsHandler = new PermissionsHandler(this, requestCode, permissionInterface, map);
    }

    @NonNull
    public static String addNecessarySlashes(@Nullable String originalPath) {
        if (originalPath == null || originalPath.length() == 0) {
            return "/";
        }
        if (originalPath.charAt(originalPath.length() - 1) != '/') {
            originalPath = originalPath + '/';
        }
        if (originalPath.charAt(0) != '/') {
            originalPath = '/' + originalPath;
        }
        return originalPath;
    }

    private void initUI() {
        inputFileEditText = findViewById(R.id.input_file);
        runButton = findViewById(R.id.run_command);
        output = findViewById(R.id.command_output);

        runButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
    }

    @NonNull
    private static String encodePath(@NonNull String path) {
        char[] chars = path.toCharArray();

        boolean needed = false;
        for (char c : chars) {
            if (c == '[' || c == ']' || c == '|') {
                needed = true;
                break;
            }
        }
        if (!needed) {
            return path;
        }

        StringBuilder sb = new StringBuilder("");
        for (char c : chars) {
            if (c == '[' || c == ']' || c == '|') {
                sb.append('%');
                sb.append(Integer.toHexString(c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    private void addTextViewToLayout(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        output.addView(textView);
    }

}
