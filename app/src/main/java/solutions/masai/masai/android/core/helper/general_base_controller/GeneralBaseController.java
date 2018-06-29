package solutions.masai.masai.android.core.helper.general_base_controller;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * @author Sebastian Tanzer
 * @version 1.0
 *          created on 05/09/2017
 */

public class GeneralBaseController extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    public void onBackArrowPressed() {
        onBackPressed();
    }
}
