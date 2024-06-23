package pro.notesvault;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NewNote extends AppCompatActivity {

    EditText titleText, contentText;
    ImageButton saveNoteBtn;
    TextView titleTextView;
    String title,content,docID;
    boolean mode =false;
    TextView deletebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        titleText = findViewById(R.id.note_titletext);
        contentText = findViewById(R.id.note_contenttext);
        saveNoteBtn = findViewById(R.id.savebtn);
        titleTextView = findViewById(R.id.title);
        deletebtn = findViewById(R.id.deleteTVbtn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docID = getIntent().getStringExtra("docID");

        if(docID!=null && !docID.isEmpty()){
            mode=true;
        }

        titleText.setText(title);
        contentText.setText(content);
        if(mode){
            titleTextView.setText("Edit your note.");
            deletebtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener((v) -> saveNote());

        deletebtn.setOnClickListener(v -> deleteFirebase());

    }

    void saveNote(){
        String noteTitle = titleText.getText().toString();
        String noteContent = contentText.getText().toString();
        if(noteTitle==null || noteTitle.isEmpty() ){
            titleText.setError("Title is required.");
            return;
        }
        Notes note = new Notes();
        note.setTittle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteFirebase(note);
    }

    void saveNoteFirebase(Notes note){
        DocumentReference documentReference;
        if(mode){
            //Update Mode
            documentReference = Utility.getReferenceNotes().document(docID);
        }else{
            //Create Mode
            documentReference = Utility.getReferenceNotes().document();
        }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewNote.this, "Note Added.",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NewNote.this, "Failed to add note.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void deleteFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getReferenceNotes().document(docID);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewNote.this, "Note Deleted.",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NewNote.this, "Failed to delete note.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}