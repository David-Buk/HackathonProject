package com.example.hackathonproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.*;
public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private EditText editTextMessage;
    private ChatAdapter chatAdapter;
    private List<String> messageList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        Button buttonSend = findViewById(R.id.buttonSend);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    analyzeAndSend(message);
                }
            }
        });
    }

    private void analyzeAndSend(String message) {
        ContentModeratorService service = ApiClient.createService();
        Call<ContentModeratorResponse> call = service.analyzeText(message);

        call.enqueue(new Callback<ContentModeratorResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ContentModeratorResponse> call, @NonNull Response<ContentModeratorResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ContentModeratorResponse result = response.body();
                    if (result.getTerms() == null || result.getTerms().isEmpty()) {
                        messageList.add(message);
                        chatAdapter.notifyDataSetChanged();
                        editTextMessage.setText("");
                    } else {
                        Toast.makeText(ChatActivity.this, "Inappropriate content detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChatActivity.this, "Content analysis failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContentModeratorResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
