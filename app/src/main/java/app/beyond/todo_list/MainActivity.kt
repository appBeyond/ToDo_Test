package app.beyond.todo_list

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_edit.*

class MainActivity : AppCompatActivity(),
    EditFragment.OnFragmentInterFaceListener,
    DatePickerFragment.OnDateSetListener,
    MasterFragment.OnListFragmentInteractionListener,
    DetailFragment.OnDetailFragmentListener,
    CalendarFragment.OnCalendarListener {

    var isTwoPane: Boolean = false
    lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (container_detail != null) isTwoPane = true

        inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        supportFragmentManager.beginTransaction()
            .add(
                R.id.container_master,
                MasterFragment.newInstance(1),
                FragmentTag.MASTER.toString()
            ).commit()


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            if (isTwoPane) inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
            goEditScreen("", "", "", false,
                ModeIntent.NEW_ENTRY
            )
        }

        createNotification()
    }

    private fun createNotification() {
        
    }

    override fun onResume() {
        super.onResume()
        upDateTodoList()
    }

    private fun goEditScreen(
        title: String,
        deadline: String,
        task_Detail: String,
        is_Completed: Boolean,
        mode: ModeIntent
    ) {
        if (isTwoPane) {
            fab.visibility = View.INVISIBLE

            if (supportFragmentManager.findFragmentByTag(FragmentTag.EDIT.toString()) == null &&
                supportFragmentManager.findFragmentByTag(FragmentTag.DETAIL.toString()) == null &&
                supportFragmentManager.findFragmentByTag(FragmentTag.SETTINGS.toString()) == null
            ) {

                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.container_detail,
                        EditFragment.newInstance(
                            title, deadline, task_Detail, is_Completed, mode),
                        FragmentTag.EDIT.toString()
                    ).commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.container_detail,
                        EditFragment.newInstance(
                            title, deadline, task_Detail, is_Completed, mode
                        ),
                        FragmentTag.EDIT.toString()
                    ).commit()
            }

            return
        }
        val intent = Intent(this@MainActivity, EditActivity::class.java).apply {
            putExtra(IntentKey.TITLE.name, title)
            putExtra(IntentKey.DEADLINE.name, deadline)
            putExtra(IntentKey.TASK_DETAIL.name, task_Detail)
            putExtra(IntentKey.IS_COMPLETED.name, is_Completed)
            putExtra(IntentKey.MODE_IN_EDIT.name, mode)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.apply {
            findItem(R.id.menu_settings).isVisible = true
            findItem(R.id.menu_calendar).isVisible = true
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_register).isVisible = false
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == R.id.menu_settings) {
            if (isTwoPane) {
                if (supportFragmentManager.findFragmentByTag(FragmentTag.EDIT.name) == null &&
                    supportFragmentManager.findFragmentByTag(FragmentTag.DETAIL.name) == null &&
                    supportFragmentManager.findFragmentByTag(FragmentTag.SETTINGS.name) == null
                ) {
                    supportFragmentManager.beginTransaction().add(
                        R.id.container_detail,
                        SettingsFragment(), FragmentTag.SETTINGS.toString()
                    ).commit()
                } else {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.container_detail,
                        SettingsFragment(), FragmentTag.SETTINGS.toString()
                    ).commit()
                }
            } else {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }



    //    EditFragment.OnFragmentInterFaceListener, Fragment
    override fun onDatePickerLaunched() {
        DatePickerFragment()
            .show(supportFragmentManager, FragmentTag.DATE_PICKER.toString())
    }

    //    EditFragment.OnFragmentInterFaceListener
    override fun onDataEdited() {
        if (isTwoPane) inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        upDateTodoList()
        makeToast(
            this,
            getString(R.string.toast_register)
        )
    }

//    EditFragment.OnFragmentInterFaceListener
    override fun onDeleteFromRealm(title: String, deadline: String, taskDetail: String) {
        upDateTodoList()
    }

    //    DatePickerFragment.OnDateSetListener, DatePicker
    override fun onDateListener(dateString: String) {
        inputTextDeadline.setText(dateString)
    }



    //    MasterFragment.OnListFragmentInteractionListener
    override fun onListItemClicked(item: TodoModel) {
        goDetailScreen(item.title, item.deadLine, item.taskDetail, item.isCompleted)
    }

//    CalendarFragment.OnCalendarListener
    override fun onCalenderLaunched() {
        CalendarFragment()
            .show(supportFragmentManager, FragmentTag.CALENDAR.toString())
    }


    private fun goDetailScreen(
        title: String,
        deadLine: String,
        taskDetail: String,
        completed: Boolean
    ) {
        if (isTwoPane) {
            fab.visibility = View.INVISIBLE
            if (supportFragmentManager.findFragmentByTag(FragmentTag.EDIT.name) == null &&
                supportFragmentManager.findFragmentByTag(FragmentTag.DETAIL.name) == null &&
                supportFragmentManager.findFragmentByTag(FragmentTag.SETTINGS.name) == null
            ) {

                supportFragmentManager.beginTransaction().add(
                    R.id.container_detail,
                    DetailFragment.newInstance(
                        title,
                        deadLine,
                        taskDetail,
                        completed
                    ),
                    FragmentTag.DETAIL.toString()
                ).commit()
            } else {
                supportFragmentManager.beginTransaction().replace(
                    R.id.container_detail,
                    DetailFragment.newInstance(
                        title,
                        deadLine,
                        taskDetail,
                        completed
                    ),
                    FragmentTag.DETAIL.toString()
                ).commit()
            }
            return
        }

        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
            putExtra(IntentKey.TITLE.name, title)
            putExtra(IntentKey.DEADLINE.name, deadLine)
            putExtra(IntentKey.TASK_DETAIL.name, taskDetail)
            putExtra(IntentKey.IS_COMPLETED.name, completed)
        }
        startActivity(intent)
    }




    private fun upDateTodoList() {
        fab.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().replace(
            R.id.container_master,
            MasterFragment.newInstance(1), FragmentTag.MASTER.toString()
        ).commit()
    }

    //    DetailFragment.OnDetailFragmentListener
    override fun onInterfaceDeleteDB(title: String?, deadline: String?, taskDetail: String?) {
        upDateTodoList()
    }

    //    DetailFragment.OnDetailFragmentListener
    override fun onInterfaceEditDB(
        title: String?,
        deadline: String?,
        taskDetail: String?,
        isCompleted: Boolean,
        mode: ModeIntent
    ) {
        goEditScreen(title!!, deadline!!, taskDetail!!, isCompleted, mode)
    }


}
