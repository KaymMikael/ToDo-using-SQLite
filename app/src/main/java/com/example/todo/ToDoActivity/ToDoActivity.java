package com.example.todo.ToDoActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.Adapter.ToDoAdapter;
import com.example.todo.AddNewTask;
import com.example.todo.Database.MyDB;
import com.example.todo.Model.ToDoModel;
import com.example.todo.OnDialogCloseListner;
import com.example.todo.R;
import com.example.todo.RecyclerViewTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class ToDoActivity extends AppCompatActivity implements OnDialogCloseListner {
    private RecyclerView mRecyclerview;

    private RecyclerViewTouchHelper touchHelper;
    private FloatingActionButton fab;
    private MyDB myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        mRecyclerview = findViewById(R.id.lists);
        fab = findViewById(R.id.fab);
        myDB = new MyDB(ToDoActivity.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(myDB, ToDoActivity.this);

        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(adapter);

        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.tag);
            }
        });
        touchHelper = new RecyclerViewTouchHelper(adapter, ToDoActivity.this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelper);
        itemTouchHelper.attachToRecyclerView(mRecyclerview);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }


    public void showWarningDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ToDoActivity.this, R.style.AlertDialogTheme); // Pass activity context instead of getApplicationContext()
        View view = LayoutInflater.from(ToDoActivity.this).inflate(
                R.layout.layout_warning_dailog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.warning_title));
        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.dummy_text));
        ((Button) view.findViewById(R.id.buttonYes)).setText(getResources().getString(R.string.yes));
        ((Button) view.findViewById(R.id.buttonNo)).setText(getResources().getString(R.string.no));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.warning);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);

        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.deleteTask(position);
                Toast.makeText(adapter.getContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyItemChanged(position);
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}