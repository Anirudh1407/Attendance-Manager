package com.example.attendance;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.SubjectHolder> {

     private Context ctx;
     private ArrayList<Subject> arrayList,deletedArrayList;
     SubjectListAdapter(Context ct, ArrayList<Subject> s,ArrayList<Subject> d){
        ctx = ct;
        arrayList = s;
        deletedArrayList = d;
    }

    @NonNull
    @Override
    public SubjectListAdapter.SubjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.subject_row,viewGroup,false);
        return new SubjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubjectListAdapter.SubjectHolder subjectHolder, final int i) {
         final Subject subject = arrayList.get(i);
         final String name = subject.getName();
        subjectHolder.subNameTextView.setText(subject.getName());
        String s = "Percentage:" + subject.perCalc();
        subjectHolder.percentTextView.setText(s);
        String t = subject.getCa() + "/" + subject.getCd();
        subjectHolder.cacdTextView.setText(t);
        if(subject.safe()){
            String safe = "Subject safe!!!";
            subjectHolder.attendTextView.setText(safe);
        }
        else{
            String safe = "Attend next " + subject.needToAttend() + " classes";
            subjectHolder.attendTextView.setText(safe);
        }
        subjectHolder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(ctx);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText caEditText = new EditText(ctx);
                caEditText.setHint("Classes Attended");
                caEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                caEditText.setSingleLine(true);
                layout.addView(caEditText);
                final EditText cdEditText = new EditText(ctx);
                cdEditText.setSingleLine(true);
                cdEditText.setHint("Classes Done");
                cdEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(cdEditText);
                AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                alert.setView(layout);
                alert.setMessage("Enter name of subject");
                alert.setTitle("Add subject");
                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(caEditText.getText().toString().equals("") || cdEditText.getText().toString().equals("")){
                            Toast.makeText(ctx, "Need all fields filled", Toast.LENGTH_SHORT).show();
                        }
                        else if(Integer.parseInt(caEditText.getText().toString())>Integer.parseInt(cdEditText.getText().toString())){
                            Toast.makeText(ctx, "Invalid Input", Toast.LENGTH_SHORT).show();
                        }
                        else if(caEditText.getText().toString().length()>3 || cdEditText.getText().toString().length()>3){
                            Toast.makeText(ctx, "Can't handle too many classes", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            subject.setCa(Integer.parseInt(caEditText.getText().toString()));
                            subject.setCd(Integer.parseInt(cdEditText.getText().toString()));
                            subject.setChanged(true);
                            String s = "Percentage:" + subject.perCalc();
                            subjectHolder.percentTextView.setText(s);
                            String t = subject.getCa() + "/" + subject.getCd();
                            subjectHolder.cacdTextView.setText(t);
                            String safe;
                            if(subject.safe()){
                                safe = "Subject safe!!!";
                                subjectHolder.attendTextView.setText(safe);
                            }
                            else{
                                if(subject.needToAttend()==1) {
                                     safe = "Attend the next class";
                                }
                                else{
                                     safe = "Attend next " + subject.needToAttend() + " classes";
                                }
                                subjectHolder.attendTextView.setText(safe);
                            }
                        }
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.setCancelable(false);
                alert.show();
            }
        });

        subjectHolder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
                alert.setTitle("Confirmation")
                        .setMessage("Sure you want to delete " + name + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletedArrayList.add(subject);
                                arrayList.remove(subject);
                                notifyItemRemoved(i);


                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class SubjectHolder extends  RecyclerView.ViewHolder{
        TextView subNameTextView,percentTextView,cacdTextView,attendTextView;
        RelativeLayout parentLayout;

        SubjectHolder(@NonNull View itemView) {
            super(itemView);
            subNameTextView = itemView.findViewById(R.id.subName);
            percentTextView = itemView.findViewById(R.id.curPercent);
            cacdTextView = itemView.findViewById(R.id.cacdTextView);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            attendTextView = itemView.findViewById(R.id.attendTextView);
        }
    }
}
