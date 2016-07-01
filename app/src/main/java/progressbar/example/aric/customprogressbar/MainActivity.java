package progressbar.example.aric.customprogressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private  customedProgressBar customedProgressbar;
    private  RoundProgress roundProgress;

    private static final int MSG_UPDATE=0x11119999;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = customedProgressbar.getProgress();
            customedProgressbar.setProgress(++progress);
            roundProgress.setProgress(++progress);

            if(progress>=100){
                mHandler.removeMessages(MSG_UPDATE);
            }

            mHandler.sendEmptyMessageDelayed(MSG_UPDATE,100);


        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customedProgressbar= ((customedProgressBar) findViewById(R.id.cp));
        roundProgress = (RoundProgress)findViewById(R.id.rp);
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE,100);

    }
}
