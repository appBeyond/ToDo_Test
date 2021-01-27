package app.beyond.todo_list

import io.realm.RealmObject

open class TodoModel: RealmObject() {

     var title: String = ""

     var deadLine: String = ""

     var taskDetail: String = ""

     var isCompleted: Boolean = false

}