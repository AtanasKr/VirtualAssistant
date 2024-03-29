package com.example.voicetest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech myTTS;
    private SpeechRecognizer mySR;
    private Random rand = new Random();
    private static int choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                mySR.startListening(intent);

            }
        });
        initializeTextToSpeech();
        initializeSpeechRecogniser();
    }

    private void initializeSpeechRecogniser() {
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            mySR= SpeechRecognizer.createSpeechRecognizer(this);
            mySR.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(result.get(0));
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
        else{
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }
    }

    private void processResult(String command) {
        command = command.toLowerCase();
        if(command.indexOf("hi")!=-1){
            choice=rand.nextInt(3);
            if(choice==0){
                speak("Hello");
            }
            if(choice==1){
                speak("hi");
            }
            if(choice==2){
                speak("hi there");
            }
        }
        if(command.indexOf("what")!=-1){
            if(command.indexOf("your name")!=-1);
            speak("my name is VoiceTest 2.0");
            if(command.indexOf("time")!=-1){
                Date now =new Date();
                String time = DateUtils.formatDateTime(this,now.getTime(),DateUtils.FORMAT_SHOW_TIME);
                speak("The time is"+time);
            }
            if(command.indexOf("weather")!=-1){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://weather.com/bg-BG/weather/today/l/BUXX0005:1:BU"));
                startActivity(intent);
                finish();
            }
        }
        if(command.indexOf("open")!=-1){
            if(command.indexOf("browser")!=-1){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
                startActivity(intent);
                finish();
            }
            if(command.indexOf("google")!=-1){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
                startActivity(intent);
                finish();
            }
        }
        if(command.indexOf("shut")!=-1){
            if(command.indexOf("down")!=-1) {
                finish();
            }
        }
    }

    private void initializeTextToSpeech() {
        myTTS= new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(myTTS.getEngines().size()==0){
                    Toast.makeText(MainActivity.this,"There is no TTS engine installed",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    myTTS.setLanguage(Locale.US);
                    speak("Hello i'm ready");
                }
            }
        });
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT>=21){
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else{
            myTTS.speak(message,TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTTS.shutdown();
    }
}
