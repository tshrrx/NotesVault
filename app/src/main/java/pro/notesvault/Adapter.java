package pro.notesvault;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Adapter extends FirestoreRecyclerAdapter<Notes, Adapter.NoteViewHolder> {
    Context context;
    public Adapter(@NonNull FirestoreRecyclerOptions<Notes> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull Notes notes) {
        noteViewHolder.TitleTextView.setText(notes.tittle);
        noteViewHolder.ContentTextView.setText(notes.content);
        noteViewHolder.TimeStampTextView.setText(Utility.TtoString(notes.timestamp));

        noteViewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context,NewNote.class);
            intent.putExtra("title",notes.tittle);
            intent.putExtra("content",notes.content);
            String docID = this.getSnapshots().getSnapshot(i).getId();
            intent.putExtra("docID",docID);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView TitleTextView, ContentTextView, TimeStampTextView , dummy;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleTextView = itemView.findViewById(R.id.NoteTitle);
            ContentTextView = itemView.findViewById(R.id.NoteContent);
            TimeStampTextView = itemView.findViewById(R.id.TimeStamp);
        }
    }
}
