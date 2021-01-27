package app.beyond.todo_list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import app.beyond.todo_list.R
import kotlinx.android.synthetic.main.activity_edit.*

class DetailActivity : AppCompatActivity(),
    DetailFragment.OnDetailFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))


        toolbar.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                finish()
            }
        }

        val bundle = intent.extras!!
        val title = bundle.getString(IntentKey.TITLE.toString())!!
        val deadline = bundle.getString(IntentKey.DEADLINE.toString())!!
        val taskDetail = bundle.getString(IntentKey.TASK_DETAIL.toString())!!
        val isCompleted = bundle.getBoolean(IntentKey.IS_COMPLETED.toString())

        supportFragmentManager.beginTransaction().add(
            R.id.container_detail,
            DetailFragment.newInstance(
                title,
                deadline,
                taskDetail,
                isCompleted
            ), FragmentTag.DETAIL.toString()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu!!.apply {
            findItem(R.id.menu_settings).isVisible = false
            findItem(R.id.menu_register).isVisible = false
            findItem(R.id.menu_delete).isVisible = true
            findItem(R.id.menu_edit).isVisible = true
            findItem(R.id.menu_calendar).isVisible = false
        }
        return true
    }

    override fun onInterfaceDeleteDB(title: String?, deadline: String?, taskDetail: String?) {
        finish()
    }

    override fun onInterfaceEditDB(
        title: String?,
        deadline: String?,
        taskDetail: String?,
        isCompleted: Boolean,
        mode: ModeIntent
    ) {
        val intent = Intent(this@DetailActivity, EditActivity::class.java).apply {
            putExtra(IntentKey.TITLE.name, title)
            putExtra(IntentKey.DEADLINE.name, deadline)
            putExtra(IntentKey.TASK_DETAIL.name, taskDetail)
            putExtra(IntentKey.IS_COMPLETED.name, isCompleted)
            putExtra(IntentKey.MODE_IN_EDIT.name, mode)
        }
        startActivity(intent)
        finish()

    }
}