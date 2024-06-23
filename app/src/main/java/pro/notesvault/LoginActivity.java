package pro.notesvault;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText,pwdEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_text);
        pwdEditText = findViewById(R.id.pwd_text);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccTextView = findViewById(R.id.create_acc_redirect_btn);

        loginBtn.setOnClickListener(v -> login());
        createAccTextView.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,CreateAcc.class)));
    }

    void login(){
        String email = emailEditText.getText().toString();
        String pwd = pwdEditText.getText().toString();
        boolean check = CheckData(email, pwd);
        if (!check){
            return;
        }

        loginAccFirebase(email,pwd);
    }

    void loginAccFirebase(String email, String pwd) {
        FirebaseAuth fAuth =FirebaseAuth.getInstance();
        load_progress(true);
        fAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                load_progress(false);
                if(task.isSuccessful()){
                    if(fAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class) );
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Email not verified, please verify your account." ,Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage() ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void load_progress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean CheckData(String email, String pwd) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is Invalid.");
            return false;
        }
        if(pwd.length()<6) {
            pwdEditText.setError("Password should be minimum 6 Characters.");
            return false;
        }
        return true;
    }
}