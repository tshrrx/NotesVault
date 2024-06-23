package pro.notesvault;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                if(User==null){
                    startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                } else {
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                }
                finish();
            }
        }, 1500);
    }
}