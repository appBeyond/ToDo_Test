package app.beyond.todo_list

import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
//import com.example.beyond_todo_list.R
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity(),
    EditFragment.OnFragmentInterFaceListener
    , DatePickerFragment.OnDateSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(findViewById(R.id.toolbar))

        toolbar.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener {
                finish()
            }
        }

        val bundle = intent.extras!!
        val title = bundle.getString(IntentKey.TITLE.name)!!
        val deadline = bundle.getString(IntentKey.DEADLINE.name)!!
        val taskDetail = bundle.getString(IntentKey.TASK_DETAIL.name)!!
        val isCompleted = bundle.getBoolean(IntentKey.IS_COMPLETED.name)
        val mode = bundle.getSerializable(IntentKey.MODE_IN_EDIT.name)!! as ModeIntent

        supportFragmentManager.beginTransaction()
            .add(
                R.id.container_detail,
                EditFragment.newInstance(
                    title,
                    deadline,
                    taskDetail,
                    isCompleted,
                    mode
                ),
            FragmentTag.EDIT.toString()).commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu!!.apply {
            findItem(R.id.menu_settings).isVisible = false
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_register).isVisible = true
            findItem(R.id.menu_calendar).isVisible = false
        }
        return true
    }

    //    EditFragment.OnFragmentInterFaceListener, DatePicker
    override fun onDatePickerLaunched() {
        DatePickerFragment()
            .show(supportFragmentManager, FragmentTag.DATE_PICKER.toString())
    }


    //    DatePickerFragment.OnDateSetListener, DatePicker
    override fun onDateListener(dateString: String) {
        val inputDateText = findViewById<EditText>(R.id.inputTextDeadline)
        inputDateText.setText(dateString)
    }

//    EditFragment.OnFragmentInterFaceListener
    override fun onDataEdited() {
        finish()
    }


    override fun onDeleteFromRealm(title: String, deadline: String, taskDetail: String) {
        finish()
    }


}
