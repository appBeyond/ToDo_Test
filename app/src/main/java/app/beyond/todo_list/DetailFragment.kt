package app.beyond.todo_list

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
//import com.example.beyond_todo_list.R
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_detail.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER




/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var title: String? = ""
    private var deadline: String? = ""
    private var taskDetail: String? = ""
    private var isCompleted: Boolean = false

    private var listener: OnDetailFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            deadline = it.getString(ARG_DEADLINE)
            taskDetail = it.getString(ARG_DETAIL)
            isCompleted = it.getBoolean(ARG_ISCOMPLETED)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnDetailFragmentListener
        if(listener == null) {
            throw ClassCastException("$context must implement OnArticleSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        setHasOptionsMenu(true)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        title_detail.text = title
        deadline_detail.text = deadline
        task_detail.text = taskDetail
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.apply {
            findItem(R.id.menu_settings).isVisible = false
            findItem(R.id.menu_register).isVisible = false
            findItem(R.id.menu_calendar).isVisible = false
            findItem(R.id.menu_edit).isVisible = true
            findItem(R.id.menu_delete).isVisible = true
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete -> {
                selectedDeleteDB(title, deadline, taskDetail)
            }

            R.id.menu_edit -> {
                listener?.onInterfaceEditDB(title!!, deadline!!, taskDetail!!, isCompleted,
                    ModeIntent.EDIT
                )
            }
        }

        return super.onOptionsItemSelected(item)
    }



    private fun selectedDeleteDB(title: String?, deadline: String?, taskDetail: String?) {

        val realm = Realm.getDefaultInstance()
        val deleteDB = realm.where(TodoModel::class.java)
            .equalTo(TodoModel::title.name, title)
            .equalTo(TodoModel::deadLine.name, deadline)
            .equalTo(TodoModel::taskDetail.name, taskDetail)
            .findFirst()


        val dialog = AlertDialog.Builder(requireActivity()).apply {
            setTitle(getString(R.string.alert_title))
            setMessage(getString(R.string.alert_message))
            setPositiveButton(getString(R.string.alert_positive)) { _, _  ->
                makeToast(
                    requireActivity(),
                    getString(R.string.toast_delete)
                )

                realm.beginTransaction()
                deleteDB?.deleteFromRealm()
                realm.commitTransaction()
                realm.close()

                 listener?.onInterfaceDeleteDB(title, deadline, taskDetail)
                parentFragmentManager.beginTransaction().remove(this@DetailFragment).commit()
            }
            setNegativeButton(getString(R.string.alert_negative)) { _, _ ->
            }
            show()
        }
    }

    interface OnDetailFragmentListener {
        fun onInterfaceDeleteDB(title: String?, deadline: String?, taskDetail: String?)
        fun onInterfaceEditDB(title: String?, deadline: String?, taskDetail: String?
                              ,isCompleted: Boolean, mode: ModeIntent
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        private  val ARG_TITLE = IntentKey.TITLE.name
        private  val ARG_DEADLINE = IntentKey.DEADLINE.name
        private  val ARG_DETAIL = IntentKey.TASK_DETAIL.name
        private  val ARG_ISCOMPLETED = IntentKey.IS_COMPLETED.name

        fun newInstance(
            title: String,
            deadline: String,
            taskDetail: String,
            isCompleted: Boolean
        ) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_DEADLINE, deadline)
                    putString(ARG_DETAIL, taskDetail)
                    putBoolean(ARG_ISCOMPLETED, isCompleted)
                }
            }
    }

}

