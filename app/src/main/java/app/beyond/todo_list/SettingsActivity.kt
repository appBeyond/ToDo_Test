package app.beyond.todo_list

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
//import com.example.beyond_todo_list.R
import kotlinx.android.synthetic.main.activity_contain_settings.*

class SettingsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contain_settings)
        setSupportActionBar(findViewById(R.id.toolbar))

        toolbar.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                finish()
            }
        }

        supportFragmentManager.beginTransaction()
            .add(
                R.id.container_detail,
                SettingsFragment(),
                FragmentTag.SETTINGS.toString()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.apply {
            findItem(R.id.menu_settings).isVisible = false
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_register).isVisible = false
        }
        return true
    }


}