package com.voice.calia;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ProgressBar chatloader;
    Intent intent;
    ImageButton iv_rec, editimg, ib_confrim, ib_cancel, imgbtn_clean, ib_go;
    TextView tvSearchResult, tvQuestion, tv_greetings;
    EditText tdQuestion;
    MainActivityPresenter mPresenter;
    String text = "", Slot_String="" , edittext = "" , currentIntent = "" , imageUrl;
    int start, index ;

    ArrayList<RegularData> regularData = new ArrayList<>();
    ArrayList<String> temp = new ArrayList<>();
    String [] new_slot_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillRegularData();
        // CALL getInternetStatus() function to check for internet and display error dialog
        if (new InternetDialog(this).getInternetStatus()) {

        }

        /**
         * here is ID Validation
         *
         *
         */
        chatloader= findViewById(R.id.pg_loader);
        imgbtn_clean = findViewById(R.id.imgbtn_trash);
        tv_greetings = findViewById(R.id.tv_greetings);
        iv_rec = findViewById(R.id.btn_record);
        tvSearchResult = findViewById(R.id.tv_search_result);
        tvQuestion = findViewById(R.id.tv_question);
        editimg = findViewById(R.id.btn_edit);
        ib_confrim = findViewById(R.id.btn_confirm);
        ib_cancel = findViewById(R.id.btn_cancel);
        tdQuestion = findViewById(R.id.et_question);
        ib_go = findViewById(R.id.ib_go);

        /*
        initialize google speech
         */

        mPresenter = new MainActivityPresenter();
        mPresenter.initGCPTTSSettings();
        mPresenter.initAndroidTTSSetting();
        /*
        Greetings timed message, first introduction to the user.
         */
        String greetings = "مَرحباً، كيف يمكنني مُساعدتك؟";
        mPresenter.startSpeak(greetings);
        tv_greetings.setText(greetings);
        tv_greetings.setVisibility(View.VISIBLE);
        /*
        timer for greeting message
         */
        tv_greetings.postDelayed(new Runnable() {
            public void run() {
                tv_greetings.setVisibility(View.GONE);
            }
        }, 4000);

        /**
         *
         * This function takes the speech of the user and converts in to text
         * @param byte of speech
         * @return text of the user's speech
         *
         *
         *
         *
         */
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
         * Edit message to the user, takes the edited message and send it to the CallWebService
         */

        editimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvQuestion.setVisibility(View.GONE);
                tdQuestion.setVisibility(View.VISIBLE);
                editimg.setVisibility(View.GONE);
                ib_confrim.setVisibility(View.VISIBLE);
                ib_cancel.setVisibility(View.VISIBLE);
                tdQuestion.setText(text);
            }
        });

        /**
         * Conformation for edit.
         *
         */
        ib_confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext = tdQuestion.getText().toString();
                tvQuestion.setText(edittext);
                tvQuestion.setVisibility(View.VISIBLE);
                tdQuestion.setVisibility(View.GONE);
               ib_confrim.setVisibility(View.GONE);
               ib_cancel.setVisibility(View.GONE);
               callWebService(edittext);
                Toast.makeText(MainActivity.this, "تم تعديل السؤال", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * Cancling for edit.
         *
         */
        ib_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tdQuestion.setVisibility(View.GONE);
                tvQuestion.setVisibility(View.VISIBLE);
                ib_confrim.setVisibility(View.GONE);
                ib_cancel.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "تم إلغاء تعديل السؤال", Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * Cleaning the chat
         *
         */
        imgbtn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvQuestion.setText("");
                tvQuestion.setVisibility(View.GONE);
                editimg.setVisibility(View.GONE);
                tvSearchResult.setVisibility(View.GONE);
                tvSearchResult.setText("");
                Toast.makeText(MainActivity.this, "تم حذف المحادثة", Toast.LENGTH_SHORT).show();

            }
        });

        /**
         * Loading map location image
         *
         */
        ib_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ImageLocation.class);
                i.putExtra("images",imageUrl);
                startActivity(i);
            }
        });
    }

    /**
     * @param menu
     * @return menu for user
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.aboutus:
                intent = new Intent(MainActivity.this, PolicyActivity.class);
                startActivity(intent);
                return true;
            case R.id.contactus:
                intent = new Intent(MainActivity.this, ContactUsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method converts speech to text and send it to the CallWebService
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String r = res.get(0);
                tvQuestion.setText(r);
                text = r;


                /**
                 * this method performs multiple greetings of the user
                 * @param user's greeting is String
                 * @return speech
                 */
                switch (r) {
                    case "اهلين":
                        tvQuestion.setText("اهلين");
                        tvQuestion.setVisibility(View.VISIBLE);
                        mPresenter.startSpeak("اهلين بك");
                        tvSearchResult.setText("اهلين بك");
                        tvSearchResult.setVisibility(View.VISIBLE);
                        break;
                    case "مرحبا":
                        tvQuestion.setText("مرحبا");
                        tvQuestion.setVisibility(View.VISIBLE);
                        mPresenter.startSpeak("مرحباً بك");
                        tvSearchResult.setText("مرحباً بك");
                        tvSearchResult.setVisibility(View.VISIBLE);
                        break;

                    case "هلا":
                        tvQuestion.setText("هلا");
                        tvQuestion.setVisibility(View.VISIBLE);
                        mPresenter.startSpeak("أهلا و سهلا");
                        tvSearchResult.setText("أهلا و سهلا");
                        tvSearchResult.setVisibility(View.VISIBLE);
                        break;
                    case "هاي":
                        tvQuestion.setText("هاي");
                        tvQuestion.setVisibility(View.VISIBLE);
                        mPresenter.startSpeak("أهلاً، لماذا لا نتحدث باللغة العربية ؟ ");
                        tvSearchResult.setText("أهلاً، لماذا لا نتحدث باللغة العربية ؟ ");
                        tvSearchResult.setVisibility(View.VISIBLE);
                        break;
                    default:callWebService(r);
                }
            }
        }
    }

    /**
     * This method reads from a regular expression json file and fill regularData
     * array.
     *
     */
    private void fillRegularData() {
        String fileContent = loadData("regulars.txt");
        //parse json
        try {
            JSONArray rootJson = new JSONArray(fileContent);
            for (int i = 0; i < rootJson.length(); i++) {

                JSONObject currentJSON = rootJson.getJSONObject(i);

                RegularData mRegularData = new RegularData();
                mRegularData.setRule(currentJSON.getString("rule"));
                mRegularData.setIntent(currentJSON.getInt("intent"));
                mRegularData.setSlot(currentJSON.getInt("slot"));


                regularData.add(mRegularData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Find matched pattern with user's question to extract intent and slot.
     * @param question
     * @return matched RegularData
     * else
     * @return null
     */
    private RegularData getMatchedRegular(String question) {

        for (int i = 0; i < regularData.size(); i++) {

            Pattern pattern = Pattern.compile(regularData.get(i).getRule());

            Matcher matcher = pattern.matcher(question);

            if (matcher.matches()) {
                return regularData.get(i);
            }

        }

        return null;
    }

    /**
     *This method analyze the user question by the following steps:
     * First: Remove the question mark to analyze.
     * Second: get matched Object from RegularData.
     *  if there isn't, the system shall response with error msg
     * Third: splitting the question to extract the slot
     * Third: calling webService by the requiring Intent and Slot.
     * if there isn't, the system shall response with error msg
     * @param question
     */

    private void callWebService(String question) {
        try {
            tvQuestion.setVisibility(View.VISIBLE);
            tvQuestion.setText(question);
            editimg.setVisibility(View.VISIBLE);
            question = question.replace("؟", "");
            RegularData selectedRegularData = getMatchedRegular(question);

            if (selectedRegularData == null) {

                Toast.makeText(MainActivity.this, "أرجوا إعادة صياغة السؤال", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] split_question = question.split("\\s+");
            String Slot = split_question[selectedRegularData.getSlot()];
            String [] new_slot_array;
            // <====counter to see the slot's position ====>
            for(int counter = 0; counter <= split_question.length-1;counter++){
                if(split_question[counter].equals(Slot)){
                    start = counter;
                    // loop to add the slots =====>
                    for(int j = start ; j <= split_question.length-1; j++){
                        temp.add(split_question[j]);
                        index++;
                    }// end for loop
                } // end if
            } // end for
            new_slot_array = temp.toArray(new String[temp.size()]);
            // make slot array into word
            for(int x = 0; x < new_slot_array.length;x++){
                Slot_String+= "%20"+new_slot_array[x];
            }// end for loop
            Slot_String = Slot_String.substring(3,Slot_String.length());
            if (selectedRegularData.getIntent() == -1) {

                GetPlacesByCategoryAsync getPlacesByCategoryAsync = new GetPlacesByCategoryAsync();
                getPlacesByCategoryAsync.execute(Slot);

            }
            else if (selectedRegularData.getIntent() == -2) {

                GetMultiOffersAsyc getMultiOffersAsyc = new GetMultiOffersAsyc();
                getMultiOffersAsyc.execute(Slot);

            }
            else if (selectedRegularData.getIntent() == -3) {

                GetMultiEventsAsyc getMultiEventsAsyc = new GetMultiEventsAsyc();
                getMultiEventsAsyc.execute(Slot);

            }else if (selectedRegularData.getIntent() == -4) {

                GetMultiFoodAsyc getMultiFoodAsyc = new GetMultiFoodAsyc();
                getMultiFoodAsyc.execute(Slot);

            }
            else {

                currentIntent = selectedRegularData.getIntent() + "";
                GetDataAsync getDataAsync = new GetDataAsync();
                getDataAsync.execute(Slot_String);
            }
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "هل يمكنك إعادة السؤال؟", Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * This method provide the execution of callWebService
     * Initiated by 3 methods
     */
    private class GetDataAsync extends AsyncTask<String, Void, String> {

        /**
         * This method is to provide the progress for the user
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            chatloader.setVisibility(View.VISIBLE);
        }

        /**
         * This method is to establish a http connection.
         * @param slot
         * @return result
         */

        @Override
        protected String doInBackground(String... slot) {

            String shopsUrl = "https://samall.000webhostapp.com/searchShops.php?name=" + slot[0] + "&intent=" + currentIntent;

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
        /**
         *This method provide json fetching data to the user's question by the currentIntent
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            chatloader.setVisibility(View.INVISIBLE);

            if (s == null) {

                Toast.makeText(MainActivity.this, "لا يمكن الاتصال بالخادم", Toast.LENGTH_LONG).show();
                return;
            }
            //parse json
            //parse json
            try {

                JSONObject rootJson = new JSONObject(s);

                int status = rootJson.getInt("status");

                if (status == 1) {

                    JSONArray shopsArray = rootJson.getJSONArray("shops");

                    String Answer = "";


                    if (currentIntent.trim().equals("1")) {

                        Answer = shopsArray.getJSONObject(0).getString("description");
                        imageUrl = shopsArray.getJSONObject(0).getString("location");
                        // Glide.with(Main3Activity.this).load(imageUrl).into(ivLocation);

                    } else if (currentIntent.trim().equals("2")) {

                        Answer = shopsArray.getJSONObject(0).getString("open_time");
                    }else if (currentIntent.trim().equals("3")){
                        Answer = "عبارة عن " + shopsArray.getJSONObject(0).getString("description") ;
                    }
                    else if (currentIntent.trim().equals("4")) {
                        for (int i = 0; i < shopsArray.length(); i++) {

                            JSONObject currentJSON = shopsArray.getJSONObject(i);

                            Answer += "تبدأ في" +  currentJSON.getString("start_date")  +"\n"+ "و تنتهي في"    +  currentJSON.getString("end_date")+ "\n" +currentJSON.getString("time");

                        }
                    }else if (currentIntent.trim().equals("5")){
                        Answer = "عبارة عن " + shopsArray.getJSONObject(0).getString("description") ;
                    }
                    else if (currentIntent.trim().equals("7")) {
                        for (int i = 0; i < shopsArray.length(); i++) {

                            JSONObject currentJSON = shopsArray.getJSONObject(i);

                            Answer +=  "تبدأ في" + "\n"+ currentJSON.getString("start_date") +"\n" + "و تنتهي في" + "\n" + currentJSON.getString("end_date");

                        }
                    }else if (currentIntent.trim().equals("9")) {

                        for (int i = 0; i < shopsArray.length(); i++) {

                            JSONObject currentJSON = shopsArray.getJSONObject(i);

                            Answer +=  "هو"    +  currentJSON.getString("category")+ "\n"+currentJSON.getString("outdoor");

                        }

                    }
                    tvSearchResult.setText(Answer);
                    tvSearchResult.setVisibility(View.VISIBLE);
                    mPresenter.startSpeak(Answer);
                    ib_go.setVisibility(View.VISIBLE);



                } else {

                    Toast.makeText(MainActivity.this, rootJson.getString("msg"), Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * This method provide the execution of callWebService for multiple data of the place's category
     * Initiated by 3 methods
     */

    private class GetPlacesByCategoryAsync extends AsyncTask<String, Void, String> {
        /**
         * This method is to provide the progress for the user
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            chatloader.setVisibility(View.VISIBLE);
        }

        /**
         * This method is to establish a http connection.
         * @param slot
         * @return result
         */

        @Override
        protected String doInBackground(String... slot) {

            String shopsUrl = "https://samall.000webhostapp.com/searchMultiShops.php?category=" + slot[0];

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

        /**
         *This method provide json fetching data to the user's question by the currentIntent
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            chatloader.setVisibility(View.INVISIBLE);

            if (s == null) {

                Toast.makeText(MainActivity.this, "لا يمكن الاتصال بالخادم", Toast.LENGTH_LONG).show();
                return;
            }

            //parse json
            try {

                JSONObject rootJson = new JSONObject(s);

                int status = rootJson.getInt("status");

                if (status == 1) {

                    JSONArray shopsArray = rootJson.getJSONArray("shops");

                    String result = "";

                    for (int i = 0; i < shopsArray.length(); i++) {

                        JSONObject currentJSON = shopsArray.getJSONObject(i);

                        result += "\n" + currentJSON.getString("name") + "\n ------------------";

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

    /**
     * This method provide the execution of callWebService for multiple data of the offers
     * Initiated by 3 methods
     */
    private class GetMultiOffersAsyc extends AsyncTask<String,Void, String> {
        /**
         * This method is to provide the progress for the user
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            chatloader.setVisibility(View.VISIBLE);
        }

        /**
         * This method is to establish a http connection.
         * @param slots
         * @return result
         */
        @Override
        protected String doInBackground(String... slots) {
            String shopsUrl = "https://samall.000webhostapp.com/searchMultiOffers.php?intent="+currentIntent;

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

        /**
         *This method provide json fetching data to the user's question by the currentIntent
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            chatloader.setVisibility(View.INVISIBLE);

            if (s == null) {

                Toast.makeText(MainActivity.this, "لا يمكن الاتصال بالخادم", Toast.LENGTH_LONG).show();
                return;
            }

            //parse json
            try {

                JSONObject rootJson = new JSONObject(s);

                int status = rootJson.getInt("status");

                if (status == 1) {

                    JSONArray shopsArray = rootJson.getJSONArray("offers");

                    String result = "";

                    for (int i = 0; i < shopsArray.length(); i++) {

                        JSONObject currentJSON = shopsArray.getJSONObject(i);

                        result += "\n" + currentJSON.getString("name") + "\n ------------------";

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

    /**
     * This method provide the execution of callWebService for multiple data of events
     * Initiated by 3 methods
     */

    private class GetMultiEventsAsyc extends AsyncTask<String,Void, String> {
        /**
         * This method is to establish a http connection.
         * @param slots
         * @return result
         */

        @Override
        protected String doInBackground(String... slots) {
            String shopsUrl = "https://samall.000webhostapp.com/searchMultiEvents.php?intent="+ currentIntent;

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
        /**
         * This method is to provide the progress for the user
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

             chatloader.setVisibility(View.VISIBLE);
        }

        /**
         *This method provide json fetching data to the user's question by the currentIntent
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            chatloader.setVisibility(View.INVISIBLE);

            if (s == null) {

                Toast.makeText(MainActivity.this, "لا يمكن الاتصال بالخادم", Toast.LENGTH_LONG).show();
                return;
            }

            //parse json
            try {

                JSONObject rootJson = new JSONObject(s);

                int status = rootJson.getInt("status");

                if (status == 1) {

                    JSONArray shopsArray = rootJson.getJSONArray("events");

                    String result = "";

                    for (int i = 0; i < shopsArray.length(); i++) {

                        JSONObject currentJSON = shopsArray.getJSONObject(i);

                        result += "\n" + currentJSON.getString("name") + "\n ------------------";

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

    /**
     * This method provide the execution of callWebService for multiple data of the offers
     * Initiated by 3 methods
     */
    private class GetMultiFoodAsyc extends AsyncTask<String,Void, String> {
        /**
         * This method is to provide the progress for the user
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            chatloader.setVisibility(View.VISIBLE);
        }

        /**
         * This method is to establish a http connection.
         * @param slots
         * @return result
         */
        @Override
        protected String doInBackground(String... slots) {
            String shopsUrl = "https://samall.000webhostapp.com/searchMultiFood.php?category=" + slots[0];

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

        /**
         *This method provide json fetching data to the user's question by the currentIntent
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            chatloader.setVisibility(View.VISIBLE);

            if (s == null) {

                Toast.makeText(MainActivity.this, "لا يمكن الاتصال بالخادم", Toast.LENGTH_LONG).show();
                return;
            }

            //parse json
            try {

                JSONObject rootJson = new JSONObject(s);

                int status = rootJson.getInt("status");

                if (status == 1) {

                    JSONArray shopsArray = rootJson.getJSONArray("food");

                    String result = "";

                    for (int i = 0; i < shopsArray.length(); i++) {

                        JSONObject currentJSON = shopsArray.getJSONObject(i);

                        result += "\n" + currentJSON.getString("place.name") + "\n ------------------";

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



    public String loadData(String inFile) {
        String tContents = "";

        try {
            InputStream stream = getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }
}