package app.beyond.todo_list

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.beyond_todo_list.R
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.lang.RuntimeException

/**
 * A fragment representing a list of Items.
 */
class MasterFragment : Fragment() {

    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null

    private var adapter: MyMasterRecyclerViewAdapter? = null
    lateinit var realm: Realm
    lateinit var results: RealmResults<TodoModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        setHasOptionsMenu(true)


        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                 realm = Realm.getDefaultInstance()
                 results = realm.where(TodoModel::class.java)
                    .equalTo(TodoModel::isCompleted.name, false)
                    .sort(TodoModel::deadLine.name, Sort.ASCENDING)
                    .findAll()

                  adapter = MyMasterRecyclerViewAdapter(results, listener)

            }
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.apply {
            findItem(R.id.menu_register).isVisible = false
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_delete).isVisible = false
            findItem(R.id.menu_settings).isVisible = true
            findItem(R.id.menu_calendar).isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_calendar) listener?.onCalenderLaunched()
        return super.onOptionsItemSelected(item)
    }


    interface OnListFragmentInteractionListener {
        fun onListItemClicked(item: TodoModel)
        fun onCalenderLaunched()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnArticleSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            MasterFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}

