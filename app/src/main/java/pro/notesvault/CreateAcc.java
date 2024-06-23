package pro.notesvault;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAcc extends AppCompatActivity {

    EditText emailEditText,pwdEditText,confirmpwdEditText;
    Button createAccBtn;
    ProgressBar progressBar;
    TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        emailEditText = findViewById(R.id.email_text);
        pwdEditText = findViewById(R.id.pwd_text);
        confirmpwdEditText = findViewById(R.id.confirmpwd_text);
        createAccBtn = findViewById(R.id.create_acc_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginTextView = findViewById(R.id.login_redirect_btn);

        createAccBtn.setOnClickListener(v -> createAccount() );
        loginTextView.setOnClickListener(v -> finish() );
    }

    void createAccount() {
        String email = emailEditText.getText().toString();
        String pwd = pwdEditText.getText().toString();
        String confirmpwd = confirmpwdEditText.getText().toString();
        boolean check = CheckData(email, pwd, confirmpwd);
        if (!check){
            return;
        }

        createAccFirebase(email,pwd);

    }

    void createAccFirebase(String email, String pwd) {
        load_progress(true);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(CreateAcc.this,
            new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task ) {
                    load_progress(false);
                    if (task.isSuccessful()){
                        Toast.makeText(CreateAcc.this, "Account created successfully, check email to verify.",Toast.LENGTH_SHORT).show();
                        fAuth.getCurrentUser().sendEmailVerification();
                        fAuth.signOut();
                        finish();
                    }else {
                        //failed
                        Toast.makeText(CreateAcc.this, task.getException().getLocalizedMessage() ,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );


    }

    void load_progress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            createAccBtn.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            createAccBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean CheckData(String email, String pwd, String confirmpwd) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is Invalid.");
            return false;
        }
        if(pwd.length()<6) {
            pwdEditText.setError("Password should be minimum 6 Characters.");
            return false;
        }
        if (!pwd.equals(confirmpwd)) {
            confirmpwdEditText.setError("Password does not Match.");
            return false;
        }
        return true;
    }
}