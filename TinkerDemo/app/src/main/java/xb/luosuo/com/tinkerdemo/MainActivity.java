package xb.luosuo.com.tinkerdemo;

import android.Manifest;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import xb.luosuo.com.tinkerdemo.util.Utils;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION_OTHER = 101;
    private static final int REQUEST_CODE_PERMISSION_CAMARA = 103;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //加载补丁包
        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip");
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "热修复qqqqqqqqqqqqq", Toast.LENGTH_SHORT).show();
            }
        });
        checkWriteStorageCameraAudioPermission();
    }
    public void checkWriteStorageCameraAudioPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            // 申请多个权限。
            AndPermission.with(MainActivity.this)
                    .requestCode(REQUEST_CODE_PERMISSION_OTHER)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .callback(permissionListener)
                    // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                    // 这样避免用户勾选不再提示，导致以后无法申请权限。
                    // 你也可以不设置。
                    .rationale(new RationaleListener() {
                                   @Override
                                   public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                       // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                       AndPermission.rationaleDialog(MainActivity.this, rationale).show();
                                   }
                               }
                    )
                    .start();
        } else {

        }
    }

    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_OTHER:

                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_CAMARA:
                case REQUEST_CODE_PERMISSION_OTHER:
                    Toast.makeText(MainActivity.this, "请求权限失败", Toast.LENGTH_SHORT).show();
                    break;

            }
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(MainActivity.this).show();
                // 第二种：用自定义的提示语。
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        Utils.setBackground(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.setBackground(true);
    }
}
