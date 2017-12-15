package simple.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SimpleNumFolderAct extends Activity implements View.OnClickListener{

    Context mContext;

    LinearLayout nfvLayout;
	private NumberFolderView nfv;
	
	private int x = 50;
	private int y = 0;
	private int w = 60;
	private int h = 60;
	private int tsize = 40;


    private final int downBtnTag  = 1;
    private final int upBtnTag    = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.main);

        nfv = new NumberFolderView(this,  tsize, x, y,  w, h);
        nfv.setTextColor(Color.WHITE);
        nfv.setTextSize(40);
        nfv.setBackgroundColor(Color.BLACK);


        nfvLayout = (LinearLayout)findViewById(R.id.nfvLayout);
        nfvLayout.addView(nfv);

        Button upBtn = (Button)findViewById(R.id.up);
        upBtn.setTag(upBtnTag);
        upBtn.setOnClickListener(this);
        Button downBtn = (Button)findViewById(R.id.down);
        downBtn.setTag(downBtnTag);
        downBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


       int tag =  Integer.parseInt(v.getTag().toString());

        switch (tag){
            case upBtnTag:
                nfv.setNumberText(nfv.getCurrentNumber()+1);
                break;
            case downBtnTag:
                nfv.setNumberText(nfv.getCurrentNumber()-1);
                break;
        }
    }
}