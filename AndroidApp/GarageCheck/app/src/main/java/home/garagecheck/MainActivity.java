package home.garagecheck;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.LinkedList;
import java.util.Queue;

import static home.garagecheck.Link.URL_STR;

public class MainActivity extends AppCompatActivity {

    final int CONSOLE_SIZE = 3;
    Button button;
    TextView text;
    TextView console;
    Queue<String> consoleLog = new LinkedList<>();
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        console = (TextView) findViewById(R.id.consoleView);
        text = (TextView) findViewById(R.id.statusText);
        button = (Button) findViewById(R.id.refreshButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStateFromAWS();
            }
        });
        // Update on start
        updateStateFromAWS();
    }

    public void setStateFromResponse(String res){
        String val = "";
        System.out.println(res);
        // TODO set text colors
        if(res.equals("\"closed\"")){
            val = "Closed";
            text.setTextColor(Color.parseColor("#4CBB17"));
        }
        else if(res.equals("\"open\"")){
            val = "Open";
            text.setTextColor(Color.parseColor("#E50000"));
        }
        else if(res.equals("\"none\"")){
            val = "Refresh Me";
            text.setTextColor(Color.parseColor("#696969"));
        }
        else{
            val = "Error";
            text.setTextColor(Color.parseColor("#696969"));
        }

        text.setText(val);
        addToConsole("Response received from server with value: " + val);
    }

    // Get state from AWS server
    private void updateStateFromAWS(){
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Link.URL_STR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setStateFromResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String val = "Can't Connect";
                String consoleError = console.getText() +
                        "Connection error: " + error.toString() + "\n";
                text.setText(val);
                text.setTextColor(Color.parseColor("#696969"));
                addToConsole(consoleError);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // Add message to the console
    private void addToConsole(String str){
        String updatedLog = "";
        consoleLog.add(str + "\n");

        if(consoleLog.size() > CONSOLE_SIZE){
            consoleLog.remove();
        }

        for(String line : consoleLog){
            updatedLog += line;
        }

        console.setText(updatedLog);
    }

}
