package com.example.todo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todo.Database.MyDB;
import com.example.todo.Model.ToDoModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String tag = "AddNewTasks";
    private EditText newTasks;
    private Button btnSave;

    private MyDB db;
    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newTasks = view.findViewById(R.id.newTask);
        btnSave = view.findViewById(R.id.btnSave);
        db = new MyDB(getActivity());


        boolean isUpdate = false;
        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("tasks");
            if (task != null) {
                newTasks.setText(task);
                if (task.length() > 0) {
                    btnSave.setEnabled(false);
                }
            }
        }
        newTasks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.toString().equals("")) {
                    btnSave.setEnabled(false);
                    btnSave.setBackgroundResource(android.R.color.darker_gray);
                } else {
                    btnSave.setEnabled(true);
                    btnSave.setBackgroundResource(android.R.color.white);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newTasks.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(getContext(), "Please input task", Toast.LENGTH_SHORT).show();
                } else {
                    if(finalIsUpdate) {
                        db.updateTask(bundle.getInt("id"), text);
                        Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                    } else {
                        ToDoModel item = new ToDoModel();
                        item.setTask(text);
                        item.setStatus(0);
                        db.insertTask(item);
                    }
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof OnDialogCloseListner) {
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
