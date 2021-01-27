package app.beyond.todo_list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
//import com.example.beyond_todo_list.R

import io.realm.RealmResults
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMasterRecyclerViewAdapter(
    private val values: RealmResults<TodoModel>,
    private val listener: MasterFragment.OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyMasterRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item = values[position]!!
        holder.textViewTitle.text = values[position]!!.title
        holder.textViewDeadline.text = MyApplication.appContext.getString(
            R.string.deadline
        ) +
                " : " + values[position]!!.deadLine
        val changedDeadline = values[position]!!.deadLine
        val today = SimpleDateFormat("yyyy/MM/dd").format(Date())


        when {
            today > changedDeadline -> {
                holder.imageStatus.setImageResource(R.drawable.ic_ghost)
            }
            today == changedDeadline -> {
                holder.imageStatus.setImageResource(R.drawable.ic_baseline_warning_24)
            }
            else -> {
                holder.imageStatus.setImageResource(R.drawable.ic_dog)
            }
        }

        holder.view.setOnClickListener {
            listener?.onListItemClicked(holder.item!!)
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.textTitle)
        val textViewDeadline: TextView = view.findViewById(R.id.textDeadline)
        val imageStatus: ImageView = view.findViewById(R.id.imageViewStatus)

        var item: TodoModel? = null

    }
}