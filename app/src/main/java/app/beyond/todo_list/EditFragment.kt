package app.beyond.todo_list

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
//import com.example.beyond_todo_list.R
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_edit.*
import java.text.ParseException
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER




/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var title: String? = ""
    private var deadline: String? = ""
    private var taskDetail: String? = ""
    private var isCompleted: Boolean? = false
    private var mode: ModeIntent? = null

    private var listener: OnFragmentInterFaceListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            deadline = it.getString(ARG_DEADLINE)
            taskDetail = it.getString(ARG_TASK_DETAIL)
            isCompleted = it.getBoolean(ARG_IS_COMPLETED)
            mode = it.getSerializable(ARG_MODE) as ModeIntent
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        setHasOptionsMenu(true)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        upDateUi(mode!!)
        imageButtonDeadline.setOnClickListener {
            listener!!.onDatePickerLaunched()
        }
    }

    private fun upDateUi(mode: ModeIntent) {
        if(mode == ModeIntent.EDIT) {
            inputTextTitle.setText(title)
            inputTextDeadline.setText(deadline)
            inputTextDetail.setText(taskDetail)
        }

        }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.apply {
            findItem(R.id.menu_settings).isVisible = false
            findItem(R.id.menu_delete).isVisible = mode == ModeIntent.EDIT
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_register).isVisible = true
            findItem(R.id.menu_calendar).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_register ->  recodedToRealm(mode)

            R.id.menu_delete -> deleteFromRealm(title!!, deadline!!, taskDetail!!)
        }
        
        return super.onOptionsItemSelected(item)
    }

    private fun deleteFromRealm(title: String, deadline: String, taskDetail: String) {
        val realm = Realm.getDefaultInstance()
        val selectedDB = realm.where(TodoModel::class.java)
            .equalTo(TodoModel::title.name, title)
            .equalTo(TodoModel::deadLine.name, deadline)
            .equalTo(TodoModel::taskDetail.name, taskDetail)
            .findFirst()!!


        val dialog = AlertDialog.Builder(requireActivity()).apply {
            setTitle(getString(R.string.alert_title))
            setMessage(getString(R.string.alert_message))
            setPositiveButton(getString(R.string.alert_positive)) { _, _  ->

                realm.beginTransaction()
                selectedDB.deleteFromRealm()
                realm.commitTransaction()

                makeToast(
                    requireActivity(),
                    getString(R.string.toast_delete)
                )
                parentFragmentManager.beginTransaction().remove(this@EditFragment).commit()
                listener?.onDeleteFromRealm(title, deadline, taskDetail)
                realm.close()
            }
            setNegativeButton(getString(R.string.alert_negative)) { _, _ ->
            }
            show()
        }
    }

    private fun recodedToRealm(mode: ModeIntent?) {
        val isCheckedErrorItem = isCheckedError()
        if(!isCheckedErrorItem) return

        when(mode) {
            ModeIntent.NEW_ENTRY -> addNewTodo()
            ModeIntent.EDIT -> changeTodo()
        }

        listener?.onDataEdited()
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    private fun addNewTodo() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        val newTodo = realm.createObject(TodoModel::class.java)
        newTodo.apply {
            title = inputTextTitle.text.toString()
            deadLine = inputTextDeadline.text.toString()
            taskDetail = inputTextDetail.text.toString()
        }

        realm.commitTransaction()

        realm.close()
    }
    private fun changeTodo() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val selectedDB = realm.where(TodoModel::class.java)
            .equalTo(TodoModel::title.name, title)
            .equalTo(TodoModel::deadLine.name, deadline)
            .equalTo(TodoModel::taskDetail.name, taskDetail)
            .findFirst()

        selectedDB!!.apply {
            title = inputTextTitle.text.toString()
            taskDetail = inputTextDetail.text.toString()
            deadLine = inputTextDeadline.text.toString()
        }
        realm.commitTransaction()


        realm.close()
    }


    private fun isCheckedError(): Boolean {
        if(inputTextTitle.text.toString() == "") {
            inputTitle.error = getString(R.string.error)
            return false
        }
        if(!inputChangeDeadline(inputTextDeadline.text.toString())) {
            inputDeadline.error = getString(R.string.error_deadline)
            return false
        }
        return true
    }

    private fun inputChangeDeadline(inputDeadline: String): Boolean {
        if(inputDeadline == "") return false

        try {
            val format = SimpleDateFormat("yyyy/MM/dd")
            format.isLenient = false
            format.parse(inputDeadline)
        } catch (e: ParseException) {
            return false
        }

        return true
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentInterFaceListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInterFaceListener {
        fun onDatePickerLaunched()
        fun onDataEdited()
        fun onDeleteFromRealm(title: String, deadline: String, taskDetail: String)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditFragment.
         */
        // TODO: Rename and change types and number of parameters
        private var ARG_TITLE = IntentKey.TITLE.name
        private var ARG_DEADLINE = IntentKey.DEADLINE.name
        private var ARG_TASK_DETAIL = IntentKey.TASK_DETAIL.name
        private var ARG_IS_COMPLETED = IntentKey.IS_COMPLETED.name
        private var ARG_MODE = IntentKey.MODE_IN_EDIT.name


        fun newInstance(title: String, deadline: String, taskDetail: String, IsCompleted: Boolean, mode: ModeIntent) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_DEADLINE, deadline)
                    putString(ARG_TASK_DETAIL, taskDetail)
                    putBoolean(ARG_IS_COMPLETED, IsCompleted)
                    putSerializable(ARG_MODE, mode)
                }
            }
    }
}