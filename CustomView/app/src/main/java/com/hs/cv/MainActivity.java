package com.hs.cv;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hs.customview.R;


public class MainActivity extends Activity
{
    private static int REF_TIME = 200;
    private CustomProgressBar mCustomBar = null;
    private boolean isStop = false;
    private Button cancelBtn = null;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0x123:

                    if (!isStop) {
                        mCustomBar.refreshView();
                        this.sendEmptyMessageDelayed(0x123, REF_TIME);
                    }
                    break;

                default:
                    break;
            }

        }

    };


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomBar = (CustomProgressBar) this.findViewById(R.id.custom_bar);
        mHandler.sendEmptyMessageDelayed(0x123, REF_TIME);

        cancelBtn = (Button)this.findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stopCustomProgress();
                cancelBtn.setClickable(false);
            }
        });

    }

    private void stopCustomProgress() {
        mCustomBar.setVisibility(View.GONE);
        isStop = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
