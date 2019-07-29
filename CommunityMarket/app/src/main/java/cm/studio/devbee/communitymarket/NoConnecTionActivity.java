package cm.studio.devbee.communitymarket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoConnecTionActivity extends AppCompatActivity {
    Button no_connextion_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connec_tion);
        no_connextion_button=findViewById(R.id.no_connextion_button);
        no_connextion_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
