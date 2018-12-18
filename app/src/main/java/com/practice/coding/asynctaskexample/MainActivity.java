package com.practice.coding.asynctaskexample;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ProgressBar progressBar;
    private MyTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBarHorizontal);

    }

    public void setData(String text) {
        textView.append(text);
    }

    public void runCode(View view) {
        setData("\nAsync Task Running ...");
        mTask = new MyTask();
        mTask.execute("Afreen Afreen", "Dil Dil Paksitan", "Jazba ha Janoon");

        //mTask.execute("Afreen Afreen", "Dil Dil Paksitan", "Jazba ha Janoon");
        // Generate Exception..with one obj call one time only
        /*
        --> Here i am created the inner class of AsyncTask in the Mainactivity so our Async task has the impliciltly
        referance of the MainActivity... Ok fine this is the basic example ..next example see how create async task in
        separate java class..
          --> AsyncTask<params, progress, result>
          -->Async task run on a separate background thread.
          -->Async task perform the tasks one by one in sequence not parrallel. like we create to objects of same Async Task
            Class then it execute the first one completely then go for second task execution..
          -->we cannot call two or more time execute() method with one object. if we call more time it gives exception.
          -->We use Async task because it provide easeness for updating the views on the main thread it provide the method
            for updating the views.
          -->onPreExecute() this method executed before the execution of doInBackground() method.. this method also run on the
              main thread so we can access the views here..like before executing the task we start the progress bar,..
          -->for update the progress we have publishProgress method in we pass the value like here that song is downloaded
                we pass it in that method.
         --> after execution of code doInBackground method return the same type of data that we pass generic paramater
            like here we pass string so it returns us a string...
         -->onProgressUpdate(String... values); we use this method for updating the views.
         --> This mehtod run on the main/UI Thread.so in this method we can acess our UI views
         --> we have the onPostExecute method in which we retrieve the value that return by doInBackground method...
            in the onPostExecute method also run on the main thread so we update our views in that method..
            like stop the progress bar..update the user that his task is completed.
         --> onPostExecute() method in which we retrieve the value that return by doInBackground method...
           in the onPostExecute method also run on the main thread so we update our views in that method..
           like stop the progress bar..update the user that his task is completed.
           -->if we cancel the task during running then onPostExecute mehtod cannot update the views
        -->if user cancel the task we have method onCancelled()...for updating the views like task has been
        cancelled we ovveride this method..

        --> If we want to get the value that doInBackground method return then we Override the Overloaded
         method onCancelled(String arg) mehtod with one argument ..if we override both zero argument and one
         arguement method then the one argument method executed only that holds the value that return
          by doInBackground method.

         */
    }

    public void cancelTask(View view) {
        if(mTask!=null)
        {
            //here we pass true boolean value for cancelling the task
            mTask.cancel(true);
        }
    }

    public void clearText(View view) {
        if(mTask.isCancelled())
        {
            textView.setText("");
        }else
        {
            Toast.makeText(this, "Background Thread is running...\nPlease Cancel the task", Toast.LENGTH_SHORT).show();
        }
    }

    class MyTask extends AsyncTask<String, String, String> {
        private static final String TAG = "TAG";

        /*
        onPreExecute() this method executed before the execution of doInBackground() method.. this method also run on the
        main thread so we can access the views here..like before executing the task we start the progress bar
         */
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            log("doInBackground executing...");
            for (String value : strings) {
                //check if the task id cancelled
                if(isCancelled())
                {
                    publishProgress("Task Cancelled!");
                    break;
                    /*
                    -->if we cancel the task during running then onProgress mehtod cannot update views the the last progress
                    like it cannot show msg to the user like task has been cancelled.
                     */
                }
                log("Start Downloading. . .");
                publishProgress("Start Downloading. . .");
                /*
                for update the progress we have publishProgress method in we pass the value like here that song is downloaded
                we pass it in that method.
                 -->if we cancel the task during running then onProgress mehtod cannot update views the the last progress
                 like it cannot show msg to the user like task has been cancelled.
                */
                try {
                    Thread.sleep(3000);

                    log("Download Complete :  " + value);
                    publishProgress("Song Downloaded :  " + value);
                } catch (InterruptedException e) {

                }
            }

            /*
            after execution of code doInBackground method return the same type of data that we pass generic paramater
            like here we pass string so it returns us a string...
            --> we have the onPostExecute method in which we retrieve the value that return by doInBackground method...
            in the onPostExecute method also run on the main thread so we update our views in that method..
            like stop the progress bar..update the user that his task is completed.
             */
            return "Execution Completed!!!";
        }

        /*
       --> onProgressUpdate(String... values); we use this method for updating the views.
       --> This mehtod run on the main/UI Thread.so in this method we can acess our UI views
       -->if we cancel the task during running then onProgressUpdate mehtod cannot update the views
         */

        @Override
        protected void onProgressUpdate(String... values) {
            //values[0] i am access only one value so pass values[0].
            setData("\n" + values[0]);
        }

        /*
           --> onPostExecute() method in which we retrieve the value that return by doInBackground method...
           in the onPostExecute method also run on the main thread so we update our views in that method..
           like stop the progress bar..update the user that his task is completed.

           -->if we cancel the task during running then onPostExecute mehtod cannot update the views
            */
        @Override
        protected void onPostExecute(String s) {
            setData("\n" + s);
            progressBar.setVisibility(View.GONE);
        }

        /*
        if user cancel the task we have method onCancelled()...for updating the views like task has been cancelled we ovveride
        this method..

        --> If we want to get the value that doInBackground method return then we Override the Overloaded
         method onCancelled(String arg) mehtod
        with one argument ..if we override both zero argument and one arguement method then the one argument method
        executed only that holds the value that return by doInBackground method.
         */

        @Override
        protected void onCancelled() {
            setData("\nTask has been cancelled.");
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled(String result) {
           setData("\nCacelled with return result by doInBackground : "+result);
           progressBar.setVisibility(View.GONE);
        }

        public void log(String msg) {
            Log.i(TAG, msg);
        }
    }

    /*
    for Suriving orientation changes and it maintains the views states
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){}
        else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){}
    }
}
