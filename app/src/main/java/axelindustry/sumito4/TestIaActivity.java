package axelindustry.sumito4;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by axel on 19/04/15.
 */
public class TestIaActivity extends Activity {
    String niveaudifficulté;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        niveaudifficulté=intent.getExtras().getString("Niveaudifficulté");

        if(niveaudifficulté!=null) {
            if (niveaudifficulté.contentEquals("facile")) {
                Log.d("afficher", "facile");
                DrawViewTestIA drawViewTestIA = new DrawViewTestIA(this,false,0,1);
                drawViewTestIA.setBackgroundColor(Color.WHITE);
                setContentView(drawViewTestIA);
            }
            if (niveaudifficulté.contentEquals("moyen")) {
                Log.d("afficher", "moyen");
                DrawViewTestIA drawViewTestIA = new DrawViewTestIA(this,false,0,1);
                drawViewTestIA.setBackgroundColor(Color.WHITE);
                setContentView(drawViewTestIA);
            }
            if (niveaudifficulté.contentEquals("difficile")){
                Log.d("afficher", "difficile");
                DrawViewTestIA drawViewTestIA = new DrawViewTestIA(this,true,0,1);
                drawViewTestIA.setBackgroundColor(Color.WHITE);
                setContentView(drawViewTestIA);
            }
        }
        else {
            DrawViewTestIA drawViewTestIA = new DrawViewTestIA(this, true, 0, 1);
            drawViewTestIA.setBackgroundColor(Color.WHITE);
            setContentView(drawViewTestIA);
        }
    }


}