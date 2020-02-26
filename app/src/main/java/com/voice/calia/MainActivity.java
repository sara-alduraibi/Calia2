package com.voice.calia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    ImageButton iv_rec;
    TextView tvSearchResult , tv_greetings;
    TextView tvQuestion;

    String currentType = "";
    TabLayout tabLayout;
    MainActivityPresenter mPresenter;
    ImageView backimg, backgif;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        tv_greetings = findViewById(R.id.tv_greetings);

        mPresenter = new MainActivityPresenter();
        mPresenter.initGCPTTSSettings();
        mPresenter.initAndroidTTSSetting();
        String greetings =  "مَرحباً، كيف يمكنني مُساعدتك؟";
        mPresenter.startSpeak(greetings);
        tv_greetings.setText(greetings);
        tv_greetings.setVisibility(View.VISIBLE);

        /**
         *
         * This function takes the speech of the user and converts in to text
         * @return text of the user's speech
         *
         *
         *
         *
         */
        iv_rec = findViewById(R.id.btn_record);
        iv_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA");
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "إسألني");

                try {
                    startActivityForResult(i, 1000);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(MainActivity.this, "جهازك لا يدعم التطبيقات الصوتية", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /**
         * here is ID Validation
         *
         *
         */

        tvSearchResult = findViewById(R.id.tv_search_result);
        tvQuestion = findViewById(R.id.tv_question);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.home:
                intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.aboutus:
                intent = new Intent(MainActivity.this, PolicyActivity.class);
                startActivity(intent);
                return true;
            case R.id.contactus:
                Toast.makeText(getApplicationContext(),"Item 3 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {

            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String r = res.get(0);
                tvQuestion.setText(r);
                switch (r) {
                    case "اهلين" :
                        mPresenter.startSpeak("اهلين بك");
                        tvQuestion.setText("اهلين بك");
                        tvQuestion.setVisibility(View.VISIBLE);
                        tvSearchResult.setText("مرحباً!");
                        tvSearchResult.setVisibility(View.VISIBLE);
                        break;
                    case "مرحبا" :
                        mPresenter.startSpeak("مرحباً بك");
                        break;

                    case "هلا" :
                        mPresenter.startSpeak("أهلا و سهلا");
                        break;
                    case "هاي":
                        mPresenter.startSpeak("أهلاً، لماذا لا نتحدث باللغة العربية ؟ ");
                        default:callWebService(r);
                }


            }
        }
    }

    private void callWebService(String text) {

        try {

            tvQuestion.setVisibility(View.VISIBLE);
            tvQuestion.setText(text);

            text = text.replace("؟", "");

            String[] result = text.split("\\s+");

            String keyWord = "";

            if (result[0].trim().equals("اين") || result[0].trim().equals("وين")) {

                currentType = "1";

                if (result[1].trim().equals("محل") || result[1].trim().equals("مكان"))
                    keyWord = result[2];
                else
                    keyWord = result[1];

                GetDataAsync getDataAsync = new GetDataAsync();
                getDataAsync.execute(keyWord);


            } else if (result[0].trim().equals("متى") && result[1].trim().equals("يفتح")) {

                if (result[2].trim().equals("محل") || result[2].trim().equals("مكان"))
                    keyWord = result[3];
                else
                    keyWord = result[2];


                currentType = "2";

                GetDataAsync getDataAsync = new GetDataAsync();
                getDataAsync.execute(keyWord);

            }

        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Your Words not Valid", Toast.LENGTH_SHORT).show();
        }
    }


    class GetDataAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... data) {

            String shopsUrl = "https://samall.000webhostapp.com/searchShops.php?name=" + data[0] + "&type=" + currentType;

            try {

                URL url = new URL(shopsUrl);

                // Open url connection
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

                // set request method
                httpConnection.setRequestMethod("GET");

                // open input stream and read server response data
                InputStream inputStream = httpConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String result = bufferedReader.readLine();


                // close connection
                httpConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (s == null) {

                Toast.makeText(MainActivity.this, "Error in connect to Server", Toast.LENGTH_LONG).show();
                return;
            }

            //parse json
            try {

                JSONObject rootJson = new JSONObject(s);

                int status = rootJson.getInt("status");

                if (status == 1) {

                    JSONArray shopsArray = rootJson.getJSONArray("shops");

                    String result = "";

                    if (currentType.trim().equals("1")) {

                        result = shopsArray.getJSONObject(0).getString("description");
                    } else if (currentType.trim().equals("2")) {

                        result = shopsArray.getJSONObject(0).getString("open_time");
                    }

                    tvSearchResult.setText(result);
                    tvSearchResult.setVisibility(View.VISIBLE);

                    mPresenter.startSpeak(result);


                } else {

                    Toast.makeText(MainActivity.this, rootJson.getString("msg"), Toast.LENGTH_LONG).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}